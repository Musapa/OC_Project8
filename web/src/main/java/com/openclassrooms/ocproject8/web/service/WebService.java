package com.openclassrooms.ocproject8.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.openclassrooms.ocproject8.shared.domain.User;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.domain.VisitedLocationDTO;
import com.openclassrooms.ocproject8.shared.helper.InternalTestHelper;
import com.openclassrooms.ocproject8.shared.service.UserService;
import com.openclassrooms.ocproject8.web.controller.WebController;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

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
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/* TODO: Get a list of every user's most recent
	 * location as JSON //- Note: does not use gpsUtil to query for their current
	 * location, // but rather gathers the user's current location from their stored
	 * location history. // // Return object should be the just a JSON mapping of
	 * userId to Locations similar to: // { //
	 * "019b04a9-067a-4c76-8817-ee75088c3822":
	 * {"longitude":-48.188821,"latitude":74.84371} // ... // }
	 * 
	 * return JsonStream.serialize(""); }
	 */

	// TODO inistialise database (private)

	// TODO this will be instanciated by in web service

}
