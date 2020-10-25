package web.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import shared.user.User;
import shared.user.UserDTO;
import shared.user.UserDatabase;
import shared.user.VisitedLocationDTO;
import web.controller.WebController;

@Service
public class WebService {

	@Autowired
	UserDatabase userDatabase;

	public VisitedLocation getUserLocation(User user) throws Exception {
		UserDTO userDTO = new UserDTO(user);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<UserDTO> request = new HttpEntity<UserDTO>(userDTO, headers);
		ResponseEntity<VisitedLocationDTO> response = restTemplate.exchange(WebController.GPSURL + "/getLocation",
				HttpMethod.POST, request, VisitedLocationDTO.class);
		VisitedLocationDTO visitedLocationDTO = response.getBody();
		Location location = new Location(visitedLocationDTO.getLocationDTO().getLatitude(),
				visitedLocationDTO.getLocationDTO().getLongitude());
		return new VisitedLocation(visitedLocationDTO.getUserId(), location, visitedLocationDTO.getTimeVisited());
	}

	public User getUser(String userName) {
		return userDatabase.getUser(userName);
	}

	public List<User> getAllUsers() {
		return userDatabase.getAllUsers();
	}

	public void addUser(User user) {
		userDatabase.addUser(user);
	}

}
