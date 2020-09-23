import org.springframework.stereotype.Service;

import gpsUtil.location.VisitedLocation;
import tourGuide.user.User;

@Service
public class GpsService {

	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
			user.getLastVisitedLocation() :
			trackUserLocation(user);
		return visitedLocation;
	}
	
}
