package com.openclassrooms.ocproject8.rewards.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsoniter.output.JsonStream;
import com.openclassrooms.ocproject8.rewards.RewardsApp;
import com.openclassrooms.ocproject8.rewards.service.RewardsService;
import com.openclassrooms.ocproject8.rewards.tracker.Tracker;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.domain.VisitedLocationDTO;
import com.openclassrooms.ocproject8.shared.service.UserService;

import tripPricer.Provider;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RewardsApp.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class TestRewardsController {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webContext;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private RewardsService rewardsService;

	private static boolean initialised = false;

	@Before
	public void initialise() {
		if (!initialised) {
			userService.initializeUsers(100);
			rewardsService.initialiseUserMap();
			Tracker tracker = new Tracker(rewardsService, userService);
			tracker.creatingRewards();
			initialised = true;
		}
	}

	@Before
	public void setupMockmvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}
	
	@Test
	public void getRewards() throws Exception {
		Optional<UserEntity> userEntity = userService.getUser("internalUser1");
		if (userEntity.isPresent()) {
			UserEntity user = userEntity.get();
			String uuid = user.getUserId().toString();
			String userName = user.getUserName();
			MvcResult result = mockMvc.perform(get("/getRewards").param("userName", userName)).andExpect(status().isOk()).andReturn();
			String json = result.getResponse().getContentAsString();	

			assertNotEquals("There should be 1 user in userReward", 0, json.indexOf(uuid));
		}
		else {
			fail("Missing user");
		}
	}

	@Test
	public void getNearbyAttractions() throws Exception {
		Optional<UserEntity> userEntity = userService.getUser("internalUser1");
		if (userEntity.isPresent()) {
			MvcResult result = mockMvc.perform(get("/getNearByAttractions").param("userName", "internalUser1")).andExpect(status().isOk()).andReturn();
			String json = result.getResponse().getContentAsString();
			VisitedLocationDTO visitedLocation = objectMapper.readValue(json, VisitedLocationDTO.class);
				
			assertEquals("Incorrect user", userEntity.get().getUserId(), visitedLocation.getUserId().toString());
		}
	}
	
	@Test
	public void getTripDeals() throws Exception {
		Optional<UserEntity> userEntity = userService.getUser("internalUser1");
		if (userEntity.isPresent()) {
			UserEntity user = userEntity.get();
			String uuid = user.getUserId().toString();
			String userName = user.getUserName();
			MvcResult result = mockMvc.perform(get("/getTripDeals").param("userName", userName)).andExpect(status().isOk()).andReturn();
			String json = result.getResponse().getContentAsString();	

			assertNotEquals("There should be 1 user in provider", 0, json.indexOf(uuid));
		}
		else {
			fail("Missing user");
		}
	}

}
