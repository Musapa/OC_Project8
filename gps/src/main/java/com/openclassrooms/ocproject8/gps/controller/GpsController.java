package com.openclassrooms.ocproject8.gps.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;
import com.openclassrooms.ocproject8.gps.service.GpsService;
import com.openclassrooms.ocproject8.shared.user.domain.User;
import com.openclassrooms.ocproject8.shared.user.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.user.domain.VisitedLocationEntity;

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
	public ResponseEntity<VisitedLocationEntity> getLocation(@RequestBody UserEntity userDTO) {
		User user = new User(userDTO);
		VisitedLocation visitedLocation = gpsService.getUserLocation(user);
		return ResponseEntity.ok().body(new VisitedLocationEntity(visitedLocation));
	}
	
    @RequestMapping("/all-current-locations")
    public String getAllCurrentLocations() {
        return JsonStream.serialize(gpsService.getAllUsersLocations());
    }
   
}
