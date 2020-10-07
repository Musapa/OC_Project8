package web.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gpsUtil.location.VisitedLocation;
import shared.user.User;
import web.controller.WebController;

@Service
public class WebService {

	public VisitedLocation getUserLocation(User user) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<VisitedLocation> response = restTemplate.getForEntity(WebController.GPSURL + "/getLocation?user={user}",
				VisitedLocation.class, user);
		return response.getBody();
	}

}
