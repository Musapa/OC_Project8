package com.openclassrooms.ocproject8.rewards.test;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
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
import com.openclassrooms.ocproject8.rewards.RewardsApp;
import com.openclassrooms.ocproject8.rewards.service.RewardsService;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.domain.UserReward;
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

	private static boolean initialised = true;

	@Before
	public void initialise() {
		if (!initialised) {
			userService.initializeUsers(100);
			initialised = true;
		}
	}

	@Before
	public void setupMockmvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}
	
	@Test
	public void getRewards() throws Exception {
		MvcResult result = mockMvc.perform(get("/getRewards")).andExpect(status().isOk()).andReturn();
		String json = result.getResponse().getContentAsString();
		
		List<UserReward> userRewards = objectMapper.readValue(json, new TypeReference<List<UserReward>>() {});
		
		assertEquals("There should be 100 userRewards", 100, userRewards.size());
	}

	
	@Test
	public void getNearbyAttractions() throws Exception {
		Optional<UserEntity> userEntity = userService.getUser("internalUser1");
		if (userEntity.isPresent()) {
			MvcResult result = mockMvc.perform(get("/getNearByAttractions").param("userName", "internalUser1"))
					.andExpect(status().isOk()).andReturn();
			String json = result.getResponse().getContentAsString();
			VisitedLocationDTO visitedLocation = objectMapper.readValue(json, VisitedLocationDTO.class);

			assertEquals("Incorrect user", userEntity.get().getUserId(), visitedLocation.getUserId().toString());
		}
	}

	@Test
	public void getTripDeals() throws Exception {
		MvcResult result = mockMvc.perform(get("/getTripDeals")).andExpect(status().isOk()).andReturn();
		String json = result.getResponse().getContentAsString();
		
		List<Provider> provider = objectMapper.readValue(json, new TypeReference<List<Provider>>() {});
		
		assertEquals("There should be 100 providers", 100, provider.size());	
	}
	
}
