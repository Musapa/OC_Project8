package com.openclassrooms.ocproject8.shared.user.domain;

import java.util.Date;
import java.util.UUID;

import gpsUtil.location.VisitedLocation;

public class VisitedLocationEntity {

	private UUID userId;
	private Date timeVisited;
	private LocationEntity locationDTO;

	public VisitedLocationEntity() {

	}

	public VisitedLocationEntity(VisitedLocation visitedLocation) {
		this.userId = visitedLocation.userId;
		this.timeVisited = visitedLocation.timeVisited;
		this.locationDTO = new LocationEntity(visitedLocation.location.latitude, visitedLocation.location.longitude);
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

	public LocationEntity getLocationDTO() {
		return locationDTO;
	}

	public void setLocationDTO(LocationEntity locationDTO) {
		this.locationDTO = locationDTO;
	}

	
}
