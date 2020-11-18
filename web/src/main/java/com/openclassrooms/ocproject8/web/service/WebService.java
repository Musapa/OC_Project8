package com.openclassrooms.ocproject8.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.openclassrooms.ocproject8.shared.domain.User;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.domain.VisitedLocationDTO;
import com.openclassrooms.ocproject8.shared.service.UserService;
import com.openclassrooms.ocproject8.web.controller.WebController;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

@Service
public class WebService {

	@Autowired
	private UserService userService;

	public WebService() {
		// TODO if there no users initialise them
		UserEntity user = new UserEntity();
		if (user == null) {
			return userService.initialise();
		}
	}

	//do I need this?
	public List<User> getAllUsers() {
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

	// TODO inistialise database (private)

	// TODO this will be instanciated by in web service

}
