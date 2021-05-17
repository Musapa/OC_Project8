package com.openclassrooms.ocproject8.rewards.test;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.openclassrooms.ocproject8.rewards.RewardsApp;
import com.openclassrooms.ocproject8.rewards.service.RewardsService;
import com.openclassrooms.ocproject8.rewards.tracker.Tracker;
import com.openclassrooms.ocproject8.shared.repository.UserRepository;
import com.openclassrooms.ocproject8.shared.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RewardsApp.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
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
		userService.initializeUsers(10000);
	}	
	
	@Test
	public void highVolumeGetRewards() {
		StopWatch stopWatch = new StopWatch();
		Tracker tracker = new Tracker(rewardsService, userService);
		stopWatch.start();
		
		tracker.creatingRewards();
		stopWatch.stop();
		
		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

}