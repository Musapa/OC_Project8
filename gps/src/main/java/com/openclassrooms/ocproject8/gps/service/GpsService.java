package com.openclassrooms.ocproject8.gps.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import com.openclassrooms.ocproject8.shared.domain.User;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.domain.VisitedLocationDTO;
import com.openclassrooms.ocproject8.shared.service.UserService;

import gpsUtil.GpsUtil;
import gpsUtil.location.VisitedLocation;

@Service
public class GpsService {

	private UserService userService;
	
	private final GpsUtil gpsUtil = new GpsUtil();
	
	public GpsService(UserService userService) {
		this.userService = userService;
	}

	public VisitedLocation getUserLocation(String userName) {
		Optional<UserEntity> userEntity = userService.getUser(userName);
		if (userEntity.isPresent()) {
			User user = new User(userEntity.get());
			VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
					: trackUserLocation(user);
			return visitedLocation;
		}
		return null;
	}

	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		// rewardsService.calculateRewards(user);
		return visitedLocation;
	}

	public List<VisitedLocationDTO> getAllUsersLocations() {
		return calculateAllUserLocations();
	}

	public List<VisitedLocationDTO> calculateAllUserLocations() {

		List<VisitedLocationDTO> visitedLocations = new ArrayList<>();

		ExecutorService executor = Executors.newFixedThreadPool(1000);
		// create a list to hold the Future object associated with Callable
		List<Future<VisitedLocation>> list = new ArrayList<Future<VisitedLocation>>();
		// Create Callable instance
		Callable<VisitedLocation> callable = new GpsTask(gpsUtil);
		for (int i = 0; i < 10000; i++) {
			// submit Callable tasks to be executed by thread pool
			Future<VisitedLocation> future = executor.submit(callable);
			// add Future to the list, we can get return value using Future
			list.add(future);
		}
		for (Future<VisitedLocation> future : list) {
			try {
				// print the return value of future, output delay in console because
				// Future.get() waits for a task to be a complited

				// add location to list
				visitedLocations.add(future.get());
				
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		executor.shutdown();

		/*
		 * List<VisitedLocationDTO> visitedLocations = new ArrayList<>(); for
		 * (UserEntity userEntity : userService.getAllUsers()) { LocationDTO locationDTO
		 * = new LocationDTO(generateRandomLatitude(), generateRandomLongitude());
		 * visitedLocations.add(new
		 * VisitedLocationDTO(UUID.fromString(userEntity.getUserId()),getRandomTime(),
		 * locationDTO)); } return visitedLocations;
		 */

	}

	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

}
