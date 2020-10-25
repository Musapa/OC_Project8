package gps.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gps.service.GpsService;
import gpsUtil.location.VisitedLocation;
import shared.user.User;
import shared.user.UserDTO;
import shared.user.VisitedLocationDTO;

@RestController
public class GpsController {

	@Autowired
	GpsService gpsService;

	@RequestMapping("/")
	public String index() {
		return "Greetings from GpsController!";
	}

	@RequestMapping(value = "/getLocation", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<VisitedLocationDTO> getLocation(@RequestBody UserDTO userDTO) {
		User user = new User(userDTO);
		VisitedLocation visitedLocation = gpsService.getUserLocation(user);
		return ResponseEntity.ok().body(new VisitedLocationDTO(visitedLocation));
	}
	
	// getAllLocations()
	// need userDatabase on gpsService
}
