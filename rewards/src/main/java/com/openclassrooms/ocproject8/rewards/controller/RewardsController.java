package com.openclassrooms.ocproject8.rewards.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;
import com.openclassrooms.ocproject8.rewards.service.RewardsService;
import com.openclassrooms.ocproject8.shared.domain.User;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;

import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

@RestController
public class RewardsController {

	@Autowired
	RewardsService rewardsService;

	private Optional<UserEntity> getUser(String userName) {
		return rewardsService.getUser(userName);
	}

	@RequestMapping("/")
	public String index() {
		return "Greetings from RewardsController!";
	}

	/*
	 * @RequestMapping(value = "/getRewards", method = RequestMethod.GET, produces =
	 * "application/json") public ResponseEntity<VisitedLocationDTO>
	 * getRewards(@RequestParam(value = "userName") String userName) {
	 * VisitedLocation visitedLocation = gpsService.getUserLocation(userName);
	 * if(visitedLocation == null) { return ResponseEntity.notFound().build(); }
	 * return ResponseEntity.ok().body(new VisitedLocationDTO(visitedLocation)); }
	 */

	@RequestMapping("/getRewards")
	public String getRewards(@RequestParam String userName) {
		return JsonStream.serialize(rewardsService.getUserRewards(getUser(userName)));
	}

	@RequestMapping("/getNearbyAttractions")
	public String getNearbyAttractions(@RequestParam String userName) {
		VisitedLocation visitedLocation = rewardsService.getUserLocation(getUser(userName));
		return JsonStream.serialize(tourGuideService.getNearByAttractions(visitedLocation));
	}

	@RequestMapping("/getTripDeals")
	public String getTripDeals(@RequestParam String userName) {
		List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
		return JsonStream.serialize(providers);
	}

}
