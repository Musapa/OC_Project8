package com.openclassrooms.ocproject8.web.service;

import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.domain.VisitedLocationDTO;
import com.openclassrooms.ocproject8.shared.helper.InternalTestHelper;
import com.openclassrooms.ocproject8.shared.service.UserService;
import com.openclassrooms.ocproject8.web.controller.WebController;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

@Service
public class WebService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private Environment env;

	private UserService userService;

	public WebService(UserService userService) {
		this.userService = userService;
		if (userService.getAllUsers().size() == 0) {
			userService.initializeUsers(InternalTestHelper.getInternalUserNumber());
		}
	}

	public List<UserEntity> getAllUsers() {
		return userService.getAllUsers();
	}


	public VisitedLocation getUserLocation(String userName) throws Exception {
		ResponseEntity<VisitedLocationDTO> response = restTemplate
				.getForEntity(getGPSUrl() + "/getLocation?userName=" + userName, VisitedLocationDTO.class);
		// TODO check response entity is not found
		VisitedLocationDTO visitedLocationDTO = response.getBody();
		Location location = new Location(visitedLocationDTO.getLocationDTO().getLatitude(),
				visitedLocationDTO.getLocationDTO().getLongitude());
		return new VisitedLocation(visitedLocationDTO.getUserId(), location, visitedLocationDTO.getTimeVisited());
	}	
	
	public List<VisitedLocationDTO> getAllCurrentLocations() {
		ResponseEntity<List<VisitedLocationDTO>> response = restTemplate
				.exchange(getGPSUrl() + "/getAllCurrentLocations", HttpMethod.GET, null,new ParameterizedTypeReference<List<VisitedLocationDTO>>() {});
		return response.getBody();
	}
	
	public String getUserRewards(String userName) {
		ResponseEntity<String> response = restTemplate
				.exchange(getRewardsUrl() + "/getRewards?userName=" + userName, HttpMethod.GET, null,new ParameterizedTypeReference<String>() {});
		return response.getBody();
	}
	
	public String getTripDeals(String userName) {
		ResponseEntity<String> response = restTemplate
				.exchange(getRewardsUrl() + "/getTripDeals?userName=" + userName, HttpMethod.GET, null,new ParameterizedTypeReference<String>() {});
		return response.getBody();
	}
	
	public String getNearByAttractions(String userName) {
		ResponseEntity<String> response = restTemplate
				.exchange(getRewardsUrl() + "/getNearByAttractions?userName=" + userName, HttpMethod.GET, null,new ParameterizedTypeReference<String>() {});
		return response.getBody();
	}
	
	private String getGPSUrl() {
		String url = env.getProperty("GPS_URL");
		if (url == null) {
			url = WebController.GPSURL;
		}
		return url;
	}
	
	private String getRewardsUrl() {
		String url = env.getProperty("REWARDS_URL");
		if (url == null) {
			url = WebController.REWARDSURL;
		}
		return url;
	}

}
