package com.openclassrooms.ocproject8.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jsoniter.output.JsonStream;
import com.openclassrooms.ocproject8.shared.domain.VisitedLocationDTO;
import com.openclassrooms.ocproject8.web.service.WebService;

import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

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
	
	// http://localhost:8080/getLocation?userName=internalUser1
	@RequestMapping("/getLocation")
	public String getLocation(@RequestParam String userName) throws Exception {
		VisitedLocation visitedLocation = webService.getUserLocation(userName);
		return JsonStream.serialize(visitedLocation.location);
	}	

    @RequestMapping("/getAllCurrentLocations")
    public List<VisitedLocationDTO> getAllCurrentLocations() {
    	return webService.getAllCurrentLocations();
    }
    
    // http://localhost:8080/rewards?userName=internalUser1 - works
	/*@RequestMapping("/rewards")
	public String getRewards(@RequestParam String userName) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(REWARDSURL, String.class);
		return response.getBody();
	}*/
	
	// don't work
	@RequestMapping("/getRewards")
	public String getUserRewards(@RequestParam String userName) {
		return webService.getUserRewards(userName);
	}
	
	// don't work
    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
    	return webService.getTripDeals(userName);
    }
	

	// TODO: Change this method to no longer return a List of Attractions.
	// Instead: Get the closest five tourist attractions to the user - no matter how
	// far away they are.
	// Return a new JSON object that contains:
	// Name of Tourist attraction,
	// Tourist attractions lat/long,
	// The user's location lat/long,
	// The distance in miles between the user's location and each of the
	// attractions.
	// The reward points for visiting each Attraction.
	// Note: Attraction reward points can be gathered from RewardsCentral
	/*
	 * @RequestMapping("/getNearbyAttractions") public String
	 * getNearbyAttractions(@RequestParam String userName) { VisitedLocation
	 * visitedLocation = webService.getUserLocation(getUser(userName)); return
	 * JsonStream.serialize(webService.getNearByAttractions(visitedLocation)); }
	 * 
	 * @RequestMapping("/getRewards") public String getRewards(@RequestParam String
	 * userName) { return
	 * JsonStream.serialize(webService.getUserRewards(getUser(userName))); }
	 * 
	 * 
	 * @RequestMapping("/getTripDeals") public String getTripDeals(@RequestParam
	 * String userName) { List<Provider> providers =
	 * webService.getTripDeals(getUser(userName)); return
	 * JsonStream.serialize(providers); }
	 * 
	 */

}
