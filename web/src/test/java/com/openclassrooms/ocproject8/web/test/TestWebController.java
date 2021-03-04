package com.openclassrooms.ocproject8.web.test;

import static org.junit.Assert.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jsoniter.output.JsonStream;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.domain.UserReward;
import com.openclassrooms.ocproject8.shared.service.UserService;
import com.openclassrooms.ocproject8.web.WebApp;
import com.openclassrooms.ocproject8.web.controller.WebController;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApp.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class TestWebController {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webContext;

	@Autowired
	private UserService userService;

	private MockRestServiceServer mockServer;

	private static boolean initialised = false;

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

		GpsUtil gpsUtil = new GpsUtil();
		Attraction attraction = gpsUtil.getAttractions().get(0);

		String userName = "internalUser1";
		Optional<UserEntity> userEntity = userService.getUser(userName);
		
		if (userEntity.isPresent()) {
			UserEntity user = userEntity.get();
			String uuid = user.getUserId().toString();
			VisitedLocation visitedLocation = new VisitedLocation(UUID.fromString(uuid), attraction, new Date());
			UserReward userRewards = new UserReward(visitedLocation, attraction, 10);
			String inputJson = JsonStream.serialize(userRewards);
			mockServer
					.expect(ExpectedCount.once(), requestTo(new URI(WebController.REWARDSURL + "/getRewards?userName=" + userName)))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(inputJson));

			MvcResult result = mockMvc.perform(get("/getRewards").param("userName", userName))
					.andExpect(status().isOk()).andReturn();
			String json = result.getResponse().getContentAsString();

			mockServer.verify();
			Assert.assertEquals(inputJson, json);
			// TODO check username is in the json
		} else {
			fail("Missing user");
		}
	}
	// don't serialize tests
	// same as testRewardsController
}
