package com.openclassrooms.ocproject8.rewards.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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

	@Before
	public void initialise() {
		userService.deleteAll();
	}
	
	@Test
	public void userGetRewards() {
		GpsUtil gpsUtil = new GpsUtil();	
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		
		userService.addUser(new UserEntity(user));
		Attraction attraction = gpsUtil.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		rewardsService.trackUserLocation(user);
		List<UserReward> userRewards = user.getUserRewards();
		rewardsService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}
	
	@Test
	public void isWithinAttractionProximity() {
		GpsUtil gpsUtil = new GpsUtil();
		Attraction attraction = gpsUtil.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}
	
	//fix test
	@Test
	public void nearAllAttractions() {
		GpsUtil gpsUtil = new GpsUtil();
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);
			
		User user = new User(userService.getAllUsers().get(0));
		rewardsService.calculateRewards(user);
		List<UserReward> userRewards = rewardsService.getUserRewards(user);
		rewardsService.tracker.stopTracking();

		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}
	
}
