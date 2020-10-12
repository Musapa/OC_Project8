package gps.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gps.service.GpsService;
import gpsUtil.location.VisitedLocation;
import shared.user.User;

@RestController
public class GpsController {

	@Autowired
	GpsService gpsService;

	@RequestMapping("/")
	public String index() {
		return "Greetings from GpsController!";
	}

	@RequestMapping("/getLocation")
	public String getLocation(@RequestParam User user) {
		VisitedLocation visitedLocation = gpsService.getUserLocation(user);
		return JsonStream.serialize(visitedLocation.location);
	}
}
