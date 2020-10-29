package com.openclassrooms.ocproject8.web.service;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.openclassrooms.ocproject8.shared.user.domain.User;
import com.openclassrooms.ocproject8.shared.user.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.user.domain.VisitedLocationEntity;
import com.openclassrooms.ocproject8.shared.user.repository.UserRepository;
import com.openclassrooms.ocproject8.web.controller.WebController;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

@Service
public class WebService {

	private UserRepository userRepository;

	public VisitedLocation getUserLocation(User user) throws Exception {
		UserEntity userDTO = new UserEntity(user);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<UserEntity> request = new HttpEntity<UserEntity>(userDTO, headers);
		ResponseEntity<VisitedLocationEntity> response = restTemplate.exchange(WebController.GPSURL + "/getLocation",
				HttpMethod.POST, request, VisitedLocationEntity.class);
		VisitedLocationEntity visitedLocationDTO = response.getBody();
		Location location = new Location(visitedLocationDTO.getLocationDTO().getLatitude(),
				visitedLocationDTO.getLocationDTO().getLongitude());
		return new VisitedLocation(visitedLocationDTO.getUserId(), location, visitedLocationDTO.getTimeVisited());
	}

	public User getUser(String userName) {
		return userRepository.getUser(userName);
	}

	public List<User> getAllUsers() {
		return userRepository.getAllUsers();
	}

	public void addUser(User user) {
		userRepository.addUser(user);
	}

	//TODO inistialise database (private) 
	
	
	//TODO this will be instanciated by in web service
	 

}
