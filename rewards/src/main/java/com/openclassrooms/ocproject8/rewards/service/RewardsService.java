package com.openclassrooms.ocproject8.rewards.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.openclassrooms.ocproject8.rewards.tracker.Tracker;
import com.openclassrooms.ocproject8.shared.domain.User;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.domain.UserReward;
import com.openclassrooms.ocproject8.shared.service.UserService;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tripPricer.Provider;
import tripPricer.TripPricer;

class TrackerThread extends Thread {

	private Tracker tracker;

	public TrackerThread(Tracker tracker) {
		this.tracker = tracker;
	}


	public void run() {
		try {
			Thread.currentThread();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
		}
		this.tracker.creatingRewards();
	}

}

@Service
public class RewardsService {

	private Logger logger = LoggerFactory.getLogger(RewardsService.class);
	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
	private int defaultProximityBuffer = 200;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 20000;
	private HashMap<String, User> userMap = new HashMap<>();
	private final GpsUtil gpsUtil;
	private final RewardCentral rewardsCentral;
	public final Tracker tracker;
	private final TripPricer tripPricer = new TripPricer();

	private UserService userService;

	public RewardsService(UserService userService, Environment env) {
		this.userService = userService;
		this.gpsUtil = new GpsUtil();
		this.rewardsCentral = new RewardCentral();
		this.tracker = new Tracker(this, userService);
		if (env.getProperty("start.tracker").equals("true")) {
			new TrackerThread(this.tracker).start();
		}
	}

	public User getUser(String userName) {
		User user = userMap.get(userName);
		if (user == null) {
			Optional<UserEntity> userEntity = userService.getUser(userName);
			if (userEntity.isPresent()) {
				user = new User(userEntity.get());
				userMap.put(userName, user);
			}
		}
		return user;
	}

	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	public VisitedLocation getUserLocation(String userName) {
		Optional<UserEntity> userEntity = userService.getUser(userName);
		if (userEntity.isPresent()) {
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
		calculateRewards(user);
		return visitedLocation;
	}

	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	public void calculateRewards(User user) {
		List<VisitedLocation> userLocations = user.getVisitedLocations();
		List<Attraction> attractions = gpsUtil.getAttractions();
		logger.debug("Number of attractions " + attractions.size());
		for (VisitedLocation visitedLocation : userLocations) {
			for (Attraction attraction : attractions) {
				if (user.getUserRewards().stream()
						.filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {

					if (nearAttraction(user.getUserPreferences().getAttractionProximity(), visitedLocation,
							attraction)) {
						logger.debug("Add reward for user " + user.getUserName());
						user.addUserReward(
								new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			}
		}
	}

	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}

	private boolean nearAttraction(int proximity, VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximity ? false : true;
	}

	private int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}

	public double getDistance(Location loc1, Location loc2) {
		double lat1 = Math.toRadians(loc1.latitude);
		double lon1 = Math.toRadians(loc1.longitude);
		double lat2 = Math.toRadians(loc2.latitude);
		double lon2 = Math.toRadians(loc2.longitude);

		double angle = Math
				.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

		double nauticalMiles = 60 * Math.toDegrees(angle);
		double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
		return statuteMiles;
	}

	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		for (Attraction attraction : gpsUtil.getAttractions()) {
			if (isWithinAttractionProximity(attraction, visitedLocation.location)) {
				nearbyAttractions.add(attraction);
			}
		}
		return nearbyAttractions;
	}

	private static final String tripPricerApiKey = "test-server-api-key";

	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfChildren(),
				user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

}
