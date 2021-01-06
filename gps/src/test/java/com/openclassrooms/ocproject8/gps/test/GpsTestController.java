package com.openclassrooms.ocproject8.gps.test;

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
import com.openclassrooms.ocproject8.gps.GpsApp;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.domain.VisitedLocationDTO;
import com.openclassrooms.ocproject8.shared.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GpsApp.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class GpsTestController {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webContext;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserService userService;

	@Before
	public void setupMockmvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}
	
	@Test
	public void getValidLocation() throws Exception {
		Optional<UserEntity> userEntity = userService.getUser("internalUser1");
		if (userEntity.isPresent()) {
			MvcResult result = mockMvc.perform(get("/getLocation").param("userName", "internalUser1"))
					.andExpect(status().isOk()).andReturn();
			String json = result.getResponse().getContentAsString();
			VisitedLocationDTO visitedLocation = objectMapper.readValue(json, VisitedLocationDTO.class);

			assertEquals("Incorrect user", userEntity.get().getUserId(), visitedLocation.getUserId());
		}
	}

	@Test
	public void getInvalidLocation() throws Exception {
		mockMvc.perform(get("/getLocation").param("userName", "internalUser")).andExpect(status().isNotFound());
	}

	@Test
	public void getAllLocations() throws Exception {
		userService.initializeUsers(100);
		
		MvcResult result = mockMvc.perform(get("/getAllCurrentLocations")).andExpect(status().isOk()).andReturn();
		String json = result.getResponse().getContentAsString();
		List<VisitedLocationDTO> visitedLocationsDTO = objectMapper.readValue(json,
				new TypeReference<List<VisitedLocationDTO>>() {
				});

		// zasto 300?
		assertEquals("There should be 100 locations", 100, visitedLocationsDTO.size());
	}

	// kopirati sa tour guida RewardsService i RewardsController i onda ukomponirati
	// to sve u svoj kod
}
