package com.openclassrooms.ocproject8.shared.user;

import java.util.Date;
import java.util.UUID;

import gpsUtil.location.VisitedLocation;

public class VisitedLocationDTO {

	private UUID userId;
	private Date timeVisited;
	private LocationDTO locationDTO;

	public VisitedLocationDTO() {

	}

	public VisitedLocationDTO(VisitedLocation visitedLocation) {
		this.userId = visitedLocation.userId;
		this.timeVisited = visitedLocation.timeVisited;
		this.locationDTO = new LocationDTO(visitedLocation.location.latitude, visitedLocation.location.longitude);
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public Date getTimeVisited() {
		return timeVisited;
	}

	public void setTimeVisited(Date timeVisited) {
		this.timeVisited = timeVisited;
	}

	public LocationDTO getLocationDTO() {
		return locationDTO;
	}

	public void setLocationDTO(LocationDTO locationDTO) {
		this.locationDTO = locationDTO;
	}

	
}
