package com.openclassrooms.ocproject8.rewards.test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.openclassrooms.ocproject8.rewards.RewardsApp;
import com.openclassrooms.ocproject8.rewards.service.RewardsService;
import com.openclassrooms.ocproject8.rewards.tracker.Tracker;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RewardsApp.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class TestRewardsController {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webContext;

	@Autowired
	private UserService userService;

	@Autowired
	private RewardsService rewardsService;

	@Before
	public void initialise() {
		if (userService.getAllUsers().size() == 0) {
			userService.initializeUsers(100);
			rewardsService.initialiseUserMap();
			Tracker tracker = new Tracker(rewardsService, userService);
			tracker.creatingRewards();
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
			UserEntity user = userEntity.get();
			String uuid = user.getUserId().toString();
			String userName = user.getUserName();
			MvcResult result = mockMvc.perform(get("/getNearByAttractions").param("userName", userName)).andExpect(status().isOk()).andReturn();
			String json = result.getResponse().getContentAsString();
				
			assertNotEquals("There should be 1 user in userReward", 0, json.indexOf(uuid));
		}
		else {
			fail("Missing user");
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
