package com.openclassrooms.ocproject8.rewards.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;
import com.openclassrooms.ocproject8.rewards.service.RewardsService;
import com.openclassrooms.ocproject8.shared.domain.User;

@RestController
public class RewardsController {

	@Autowired
	RewardsService rewardsService;

	private User getUser(String userName) {
		return new User(rewardsService.getUser(userName).get());
	}

	@RequestMapping("/")
	public String index() {
		return "Greetings from RewardsController!";
	}

	@RequestMapping("/getRewards")
	public String getRewards(@RequestParam String userName) {
		return JsonStream.serialize(rewardsService.getUserRewards(getUser(userName)));
	}

	/*@RequestMapping("/getNearbyAttractions")
	public String getNearbyAttractions(@RequestParam String userName) {
		VisitedLocation visitedLocation = rewardsService.getUserLocation(getUser(userName));
		return JsonStream.serialize(rewardsService.getNearByAttractions(visitedLocation));
	}

	@RequestMapping("/getTripDeals")
	public String getTripDeals(@RequestParam String userName) {
		List<Provider> providers = rewardsService.getTripDeals(getUser(userName));
		return JsonStream.serialize(providers);
	}*/

}
