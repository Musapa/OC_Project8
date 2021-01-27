package com.openclassrooms.ocproject8.rewards.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.openclassrooms.ocproject8.rewards.RewardsApp;
import com.openclassrooms.ocproject8.rewards.service.RewardsService;
import com.openclassrooms.ocproject8.shared.domain.User;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.helper.InternalTestHelper;
import com.openclassrooms.ocproject8.shared.repository.UserRepository;
import com.openclassrooms.ocproject8.shared.service.UserService;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.service.TourGuideService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RewardsApp.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class TestPerformance {

	@Autowired
	RewardsService rewardsService;

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Before
	public void initializeUsers() {
		userService.initializeUsers(100000);
	}

	@Test
	public void highVolumeGetRewards() {
		GpsUtil gpsUtil = new GpsUtil();

		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(100);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		RewardsService rewardsService = new RewardsService(userService);
		
	    Attraction attraction = gpsUtil.getAttractions().get(0);
		List<User> allUsers = new ArrayList<>();
		allUsers = rewardsService.getUser(userEntity.getUserName());
		allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));
	     
	    allUsers.forEach(u -> rewardsService.calculateRewards(u));
	    
		for(User user : allUsers) {
			assertTrue(user.getUserRewards().size() > 0);
		}
		stopWatch.stop();
		rewardsService.tracker.stopTracking();

		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

}