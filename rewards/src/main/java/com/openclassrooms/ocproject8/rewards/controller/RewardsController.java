package com.openclassrooms.ocproject8.rewards.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;
import com.openclassrooms.ocproject8.rewards.service.RewardsService;

@RestController
public class RewardsController {

	@Autowired
	RewardsService rewardsService;
	
	
	@RequestMapping("/")
	public String index() {
		return "Greetings from RewardsController!";
	}
	
	/*@RequestMapping(value = "/getRewards", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<VisitedLocationDTO> getRewards(@RequestParam(value = "userName") String userName) {
		VisitedLocation visitedLocation = gpsService.getUserLocation(userName);
		if(visitedLocation == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(new VisitedLocationDTO(visitedLocation));
	}*/
	
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(rewardsService.getUserRewards(getUser(userName)));
    }
	
    
    
    
}
