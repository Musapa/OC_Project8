package com.openclassrooms.ocproject8.rewards.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openclassrooms.ocproject8.gps.service.GpsTask;
import com.openclassrooms.ocproject8.rewards.service.RewardsService;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.domain.VisitedLocationDTO;
import com.openclassrooms.ocproject8.shared.service.UserService;

import gpsUtil.location.VisitedLocation;

public class Tracker extends Thread {
	private Logger logger = LoggerFactory.getLogger(Tracker.class);
	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final RewardsService rewardsService;
	private final UserService userService;
	private boolean stop = false;

	public Tracker(RewardsService rewardsService, UserService userService) {
		this.rewardsService = rewardsService;
		this.userService = userService;

		executorService.submit(this);
	}

	/**
	 * Assures to shut down the Tracker thread
	 */
	public void stopTracking() {
		stop = true;
		executorService.shutdownNow();
	}

	@Override
	public void run() {
		StopWatch stopWatch = new StopWatch();
		while (true) {
			if (Thread.currentThread().isInterrupted() || stop) {
				logger.debug("Tracker stopping");
				break;
			}

			List<UserEntity> users = userService.getAllUsers();
			logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
			stopWatch.start();
			users.forEach(u -> rewardsService.trackUserLocation(rewardsService.getUser(u.getUserName())));
			stopWatch.stop();
			logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
			stopWatch.reset();
			try {
				logger.debug("Tracker sleeping");
				TimeUnit.SECONDS.sleep(trackingPollingInterval);
			} catch (InterruptedException e) {
				break;
			}
		}

	}
		
	public void start() {
		List<UserEntity> users = new ArrayList<>();

		ExecutorService executor = Executors.newFixedThreadPool(1000);
		// create a list to hold the Future object associated with Callable
		List<Future<UserEntity>> list = new ArrayList<Future<UserEntity>>();

		for (UserEntity userEntity : userService.getAllUsers()) {
			// Create Callable instance
			Callable<UserEntity> callable = new RewardsTask(userEntity);
			// submit Callable tasks to be executed by thread pool
			Future<UserEntity> future = executor.submit(callable);
			// add Future to the list, we can get return value using Future
			list.add(future);
		}
		for (Future<UserEntity> future : list) {
			try {
				// print the return value of future, output delay in console because
				// Future.get() waits for a task to be a complited
				users.add(new VisitedLocationDTO(future.get()));

			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		executor.shutdown();
	}
	
	
	
	public void isFinished() {

	}
}
