package com.openclassrooms.ocproject8.gps.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.openclassrooms.ocproject8.shared.domain.User;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.domain.VisitedLocationDTO;
import com.openclassrooms.ocproject8.shared.service.UserService;

import gpsUtil.GpsUtil;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

@Service
public class GpsService {


	private UserService userService;

	private final GpsUtil gpsUtil = new GpsUtil();

	public GpsService(UserService userService) {
		this.userService = userService;
	}
	
	public VisitedLocation getUserLocation(String userName) {
		Optional<UserEntity> userEntity = userService.getUser(userName);
		if(userEntity.isPresent()) {
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
	
	public List<VisitedLocationDTO> getAllUsersLocations() {
		return calculateAllUserLocations();
	}

	public List<VisitedLocationDTO> calculateAllUserLocations() {
		//TODO read users from database and calculate their locations
		List<VisitedLocation> visitedLocations = new ArrayList<>();
		for (UserEntity userEntity : userService.getAllUsers()) {			
			VisitedLocation visitedLocation = new VisitedLocation(userEntity.getUserId(), generateRandomLongitude(),generateRandomLatitude(),getRandomTime());
			visitedLocations.add(new VisitedLocationDTO(visitedLocation));
		}
		return visitedLocations;
	}
	
	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}


}
