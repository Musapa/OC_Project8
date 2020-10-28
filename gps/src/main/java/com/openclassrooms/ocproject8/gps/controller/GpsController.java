package com.openclassrooms.ocproject8.gps.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;
import com.openclassrooms.ocproject8.gps.service.GpsService;
import com.openclassrooms.ocproject8.shared.user.User;
import com.openclassrooms.ocproject8.shared.user.UserDTO;
import com.openclassrooms.ocproject8.shared.user.VisitedLocationDTO;

import gpsUtil.location.VisitedLocation;

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
	
    @RequestMapping("/all-current-locations")
    public String getAllCurrentLocations() {
        return JsonStream.serialize(gpsService.getAllUsersLocations());
    }
   
}
