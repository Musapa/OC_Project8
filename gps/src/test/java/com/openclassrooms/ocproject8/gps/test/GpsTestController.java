package com.openclassrooms.ocproject8.gps.test;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.ocproject8.shared.domain.VisitedLocationDTO;

import gpsUtil.location.VisitedLocation;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GpsTestController {
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webContext;

	@Autowired
	private ObjectMapper objectMapper;


	@Before
	public void setupMockmvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}
	
	
	@Test
	public void getValidLocation() throws Exception {
		MvcResult result = mockMvc.perform(get("/getLocation").param("userName", "internalUser1")).andExpect(status().isOk())
				.andReturn();
		String json = result.getResponse().getContentAsString();
		VisitedLocation visitedLocations = objectMapper.readValue(json, VisitedLocation.class);

		assertEquals("latitude is: 17.677591", "17.677591", visitedLocations.location.latitude);
		assertEquals("longitude is:", "3.447742", visitedLocations.location.longitude);
	}

	@Test
	public void getInvalidLocation() throws Exception {
		mockMvc.perform(get("/getLocation").param("userName", "internalUser100")).andExpect(status().isNotFound());
	}
	
	
	@Test
	public void getAllLocations() throws Exception {
		MvcResult result = mockMvc.perform(get("/getAllCurrentLocations")).andExpect(status().isOk())
				.andReturn();
		String json = result.getResponse().getContentAsString();
		List<VisitedLocationDTO> visitedLocationsDTO = objectMapper.readValue(json, new TypeReference<List<VisitedLocationDTO>>() {
		});

		assertEquals("There should be 102 locations", 102, visitedLocationsDTO.size());
	}
	
}
