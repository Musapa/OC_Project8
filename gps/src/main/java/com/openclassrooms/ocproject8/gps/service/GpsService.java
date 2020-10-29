package com.openclassrooms.ocproject8.gps.service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.ocproject8.shared.user.domain.User;
import com.openclassrooms.ocproject8.shared.user.service.UserService;

import gpsUtil.GpsUtil;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

@Service
public class GpsService {

	@Autowired
	private UserService userService;
	
	private final GpsUtil gpsUtil = new GpsUtil();

	public GpsService() {
		calculateAllUserLocations();
	}
	
	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
				: trackUserLocation(user);
		return visitedLocation;
	}

	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		//rewardsService.calculateRewards(user);
		return visitedLocation;
	}
	
	public Map<String, Location> getAllUsersLocations() {
		Map<String, Location> allUsersLocations = new HashMap<String, Location>();
		for (User user : userService.getAllUsers()) {
			allUsersLocations.put(user.getUserId().toString(),
					(user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation().location : null);
		}
		return allUsersLocations;
	}

	public void calculateAllUserLocations() {
		//TODO read users from database and calculate their locations
		//IntStream.range(0, 3).forEach(i -> {
		//	user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
		//			new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		//});
	}


}
