package com.openclassrooms.ocproject8.rewards.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;
import com.openclassrooms.ocproject8.rewards.service.RewardsService;
import com.openclassrooms.ocproject8.shared.domain.User;
import com.openclassrooms.ocproject8.shared.domain.VisitedLocationDTO;

import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

@RestController
public class RewardsController {

	@Autowired
	RewardsService rewardsService;

	private User getUser(String userName) {
		return rewardsService.getUser(userName);
	}

	@RequestMapping("/")
	public String index() {
		return "Greetings from RewardsController!";
	}

	@RequestMapping("/getRewards")
	public String getRewards(@RequestParam String userName) {
		return JsonStream.serialize(rewardsService.getUserRewards(getUser(userName)));
	}
	
    //  TODO: Change this method to no longer return a List of Attractions.
 	//  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
 	//  Return a new JSON object that contains:
    	// Name of Tourist attraction, 
        // Tourist attractions lat/long, 
        // The user's location lat/long, 
        // The distance in miles between the user's location and each of the attractions.
        // The reward points for visiting each Attraction.
        //    Note: Attraction reward points can be gathered from RewardsCentral
	@RequestMapping(value = "/getNearbyAttractions", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<VisitedLocationDTO> getNearbyAttractions(@RequestParam(value = "userName") String userName) {
		VisitedLocation visitedLocation = rewardsService.getUserLocation(userName);
		if (visitedLocation == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(new VisitedLocationDTO(visitedLocation));
	}
/*
	@RequestMapping("/getNearbyAttractions")
	public String getNearbyAttractions(@RequestParam String userName) {
		VisitedLocation visitedLocation = rewardsService.getUserLocation(getUser(userName));
		return JsonStream.serialize(rewardsService.getNearByAttractions(visitedLocation));
	}
*/	

    @RequestMapping("/getTripDeals")
    public ResponseEntity<List<Provider>> getTripDeals(@RequestParam(value = "userName") String userName) {
    	List<Provider> visitedLocationDTO = rewardsService.getTripDeals(getUser(userName));	
    	return ResponseEntity.ok().body(visitedLocationDTO);
    }
}
