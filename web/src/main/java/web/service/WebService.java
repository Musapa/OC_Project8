package web.service;

import java.util.List;

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
import shared.user.UserRepository;
import shared.user.VisitedLocationDTO;
import web.controller.WebController;

@Service
public class WebService {

	private UserRepository userRepository;

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
		return userRepository.getUser(userName);
	}

	public List<User> getAllUsers() {
		return userRepository.getAllUsers();
	}

	public void addUser(User user) {
		userRepository.addUser(user);
	}



}
