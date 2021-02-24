package com.openclassrooms.ocproject8.rewards.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

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
import com.openclassrooms.ocproject8.rewards.tracker.Tracker;
import com.openclassrooms.ocproject8.shared.domain.User;
import com.openclassrooms.ocproject8.shared.domain.UserReward;
import com.openclassrooms.ocproject8.shared.service.UserService;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RewardsApp.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class TestRewardsService {
	
	@Autowired
	UserService userService;
	
	@Autowired
	RewardsService rewardsService;
	
	private static boolean initialised = false;

	@Before
	public void initialise() {
		if (!initialised) {
			userService.initializeUsers(100);
			rewardsService.initialiseUserMap();
			Tracker tracker = new Tracker(rewardsService, userService);
			tracker.creatingRewards();
			//userService.deleteAll();
		}
	}
	
	@Test
	public void userGetRewards() {
		GpsUtil gpsUtil = new GpsUtil();
		
		User user = rewardsService.getUser("internalUser1");
		
		Attraction attraction = gpsUtil.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		rewardsService.trackUserLocation(user);
		List<UserReward> userRewards = user.getUserRewards();
		assertTrue(userRewards.size() > 0);
	}
	
	@Test
	public void isWithinAttractionProximity() {
		GpsUtil gpsUtil = new GpsUtil();
		Attraction attraction = gpsUtil.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}
	
	@Test
	public void nearAllAttractions() {
		GpsUtil gpsUtil = new GpsUtil();
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);
			
		User user = rewardsService.getUser("internalUser1");
		rewardsService.calculateRewards(user);
		List<UserReward> userRewards = rewardsService.getUserRewards(user);
		assertEquals("Number of attractions does not match user rewards ", gpsUtil.getAttractions().size(), userRewards.size());
	}
	
}
