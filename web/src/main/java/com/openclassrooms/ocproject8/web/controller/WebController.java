package com.openclassrooms.ocproject8.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jsoniter.output.JsonStream;
import com.openclassrooms.ocproject8.shared.domain.VisitedLocationDTO;
import com.openclassrooms.ocproject8.web.service.WebService;

import gpsUtil.location.VisitedLocation;

@RestController
public class WebController {

	static public String GPSURL = "http://localhost:8090/"; // constant on capital letters
	static public String REWARDSURL = "http://localhost:8095/"; // constant on capital letters

	@Autowired
	WebService webService;

	@RequestMapping("/")
	public String index() {
		return "Greetings from WebController!";
	}

	@RequestMapping("/gps")
	public String getGps() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(GPSURL, String.class);
		return response.getBody();
	}

	@RequestMapping("/getLocation")
	public String getLocation(@RequestParam String userName) throws Exception {
		VisitedLocation visitedLocation = webService.getUserLocation(userName);
		return JsonStream.serialize(visitedLocation.location);
	}

	@RequestMapping("/getAllCurrentLocations")
	public List<VisitedLocationDTO> getAllCurrentLocations() {
		return webService.getAllCurrentLocations();
	}

	@RequestMapping("/getRewards")
	public String getUserRewards(@RequestParam String userName) {
		return webService.getUserRewards(userName);
	}

	@RequestMapping("/getNearByAttractions")
	public String getNearByAttractions(@RequestParam String userName) {
		return webService.getNearByAttractions(userName);
	}

	@RequestMapping("/getTripDeals")
	public String getTripDeals(@RequestParam String userName) {
		return webService.getTripDeals(userName);
	}

}
