package com.openclassrooms.ocproject8.web.service;

import java.util.List;

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
import tripPricer.Provider;

@Service
public class WebService {

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
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<VisitedLocationDTO> response = restTemplate
				.getForEntity(WebController.GPSURL + "/getLocation?userName=" + userName, VisitedLocationDTO.class);
		// TODO check response entity is not found
		VisitedLocationDTO visitedLocationDTO = response.getBody();
		Location location = new Location(visitedLocationDTO.getLocationDTO().getLatitude(),
				visitedLocationDTO.getLocationDTO().getLongitude());
		return new VisitedLocation(visitedLocationDTO.getUserId(), location, visitedLocationDTO.getTimeVisited());
	}
	
	public List<VisitedLocationDTO> getAllCurrentLocations() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<VisitedLocationDTO>> response = restTemplate
				.exchange(WebController.GPSURL + "/getAllCurrentLocations", HttpMethod.GET, null,new ParameterizedTypeReference<List<VisitedLocationDTO>>() {});
		return response.getBody();
	}
	
	public List<Provider> getTripDeals(String userName) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<Provider>> response = restTemplate
				.exchange(WebController.REWARDSURL + "/getTripDeals", HttpMethod.GET, null,new ParameterizedTypeReference<List<Provider>>() {});
		return response.getBody();
	}

}
