package com.openclassrooms.ocproject8.gps.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.ocproject8.gps.service.GpsService;
import com.openclassrooms.ocproject8.shared.domain.VisitedLocationDTO;

import gpsUtil.location.VisitedLocation;

@RestController
public class GpsController {

	@Autowired
	GpsService gpsService;

	@RequestMapping("/")
	public String index() {
		return "Greetings from GpsController!";
	}
	
	@RequestMapping(value = "/getLocation", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<VisitedLocationDTO> getLocation(@RequestParam(value = "userName") String userName) {
		VisitedLocation visitedLocation = gpsService.getUserLocation(userName);
		if(visitedLocation == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(new VisitedLocationDTO(visitedLocation));
	}
	
    @RequestMapping("/all-current-locations")
    public ResponseEntity<List<VisitedLocationDTO>> getAllCurrentLocations() {
    	List<VisitedLocationDTO> visitedLocationDTO = gpsService.getAllUsersLocations();	
    	return ResponseEntity.ok().body(visitedLocationDTO);
    }
   
   
}
