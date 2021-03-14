package com.openclassrooms.ocproject8.web.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jsoniter.output.JsonStream;
import com.openclassrooms.ocproject8.shared.domain.LocationDTO;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.domain.UserReward;
import com.openclassrooms.ocproject8.shared.domain.VisitedLocationDTO;
import com.openclassrooms.ocproject8.shared.service.UserService;
import com.openclassrooms.ocproject8.web.WebApp;
import com.openclassrooms.ocproject8.web.controller.WebController;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

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
	
	@Autowired
	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;
	
	@Before
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

	@Before
	public void setupMockmvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}
	
	@Test
	public void getLocation() throws Exception {
		GpsUtil gpsUtil = new GpsUtil();
		Attraction attraction = gpsUtil.getAttractions().get(0);
		String userName = "internalUser1";
		Optional<UserEntity> userEntity = userService.getUser(userName);
		
		if (userEntity.isPresent()) {
			UserEntity user = userEntity.get();
			UUID uuid = UUID.fromString(user.getUserId());

			Location location = new Location(15, 25);
		    VisitedLocation visitedLocation = new VisitedLocation(uuid, location, new Date());
			
			String inputJson = JsonStream.serialize(visitedLocation);
			mockServer
					.expect(ExpectedCount.once(), requestTo(new URI(WebController.GPSURL + "getLocation?userName=" + userName)))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(inputJson));

			MvcResult result = mockMvc.perform(get("/getLocation").param("userName", userName))
					.andExpect(status().isOk()).andReturn();
			String json = result.getResponse().getContentAsString();

			mockServer.verify();
			assertEquals(inputJson, json);
		} else {
			fail("Missing user");
		}
	}
	
	@Test
	public void getAllCurrentLocations() throws Exception {
		GpsUtil gpsUtil = new GpsUtil();
		Attraction attraction = gpsUtil.getAttractions().get(0);
		String userName = "internalUser1";
		Optional<UserEntity> userEntity = userService.getUser(userName);
		
		if (userEntity.isPresent()) {
			UserEntity user = userEntity.get();
			UUID uuid = UUID.fromString(user.getUserId());
			//TODO this return list of VisitedLoacaitonDTO
			List<VisitedLocationDTO> visitedLocations = new ArrayList<VisitedLocationDTO>();
			LocationDTO locationDTO = new LocationDTO(106, 15);
			visitedLocations.add(new VisitedLocationDTO(uuid, new Date(), locationDTO));
			
			String inputJson = JsonStream.serialize(visitedLocations);
			mockServer
					.expect(ExpectedCount.once(), requestTo(new URI(WebController.GPSURL + "getAllCurrentLocations")))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(inputJson));

			MvcResult result = mockMvc.perform(get("/getAllCurrentLocations"))
					.andExpect(status().isOk()).andReturn();
			String json = result.getResponse().getContentAsString();

			mockServer.verify();
			assertEquals(inputJson, json);
		} else {
			fail("Missing user");
		}
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
					.expect(ExpectedCount.once(), requestTo(new URI(WebController.REWARDSURL + "getRewards?userName=" + userName)))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(inputJson));

			MvcResult result = mockMvc.perform(get("/getRewards").param("userName", userName))
					.andExpect(status().isOk()).andReturn();
			String json = result.getResponse().getContentAsString();

			mockServer.verify();
			assertEquals(inputJson, json);
			assertNotEquals("There should be 1 user in userReward", 0, json.indexOf(uuid));
		} else {
			fail("Missing user");
		}
	}
	
	@Test
	public void getNearByAttractions() throws Exception {
		
		GpsUtil gpsUtil = new GpsUtil();
		Attraction attraction = gpsUtil.getAttractions().get(0);
		String userName = "internalUser1";
		Optional<UserEntity> userEntity = userService.getUser(userName);
		
		if (userEntity.isPresent()) {
			List<Attraction> nearbyAttractions = new ArrayList<Attraction>();
			nearbyAttractions.add(attraction);
		
			String inputJson = JsonStream.serialize(nearbyAttractions);
			mockServer
					.expect(ExpectedCount.once(), requestTo(new URI(WebController.REWARDSURL + "getNearByAttractions?userName=" + userName)))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(inputJson));
			
			MvcResult result = mockMvc.perform(get("/getNearByAttractions").param("userName", userName))
					.andExpect(status().isOk()).andReturn();
			String json = result.getResponse().getContentAsString();

			mockServer.verify();
			assertEquals(inputJson, json);

		} else {
			fail("Missing user");
		}
	}
	
	@Test
	public void getTripDeals() throws Exception {
		String userName = "internalUser1";
		Optional<UserEntity> userEntity = userService.getUser(userName);
		
		if (userEntity.isPresent()) {
			List<Provider> providers = new ArrayList<Provider>();
			providers.addAll(providers);

			String inputJson = JsonStream.serialize(providers);
			mockServer
					.expect(ExpectedCount.once(), requestTo(new URI(WebController.REWARDSURL + "getTripDeals?userName=" + userName)))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(inputJson));

			MvcResult result = mockMvc.perform(get("/getTripDeals").param("userName", userName))
					.andExpect(status().isOk()).andReturn();
			String json = result.getResponse().getContentAsString();

			mockServer.verify();
			assertEquals(inputJson, json);

		} else {
			fail("Missing user");
		}
	}
	


}
