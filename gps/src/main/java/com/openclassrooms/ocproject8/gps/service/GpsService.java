package com.openclassrooms.ocproject8.gps.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.ocproject8.shared.domain.User;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.service.UserService;

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
	
	public VisitedLocation getUserLocation(String userName) {
		Optional<UserEntity> userEntity = userService.getUser(userName);
		if(userEntity != null) {
			User user = new User(userEntity.get());
			VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
					: trackUserLocation(user);
			return visitedLocation;
		}
		return null;
	}

	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		//rewardsService.calculateRewards(user);
		return visitedLocation;
	}
	
	public Map<String, Location> getAllUsersLocations() {
		Map<String, Location> allUsersLocations = new HashMap<String, Location>();
		/*for (User user : userService.getAllUsers()) {
			allUsersLocations.put(user.getUserId().toString(),
					(user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation().location : null);
		}*/
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
