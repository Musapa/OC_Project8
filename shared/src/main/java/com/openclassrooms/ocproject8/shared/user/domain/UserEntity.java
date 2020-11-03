package com.openclassrooms.ocproject8.shared.user.domain;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private final UUID userId;
	private final String userName;
	private String phoneNumber;
	private String emailAddress;

	public UserEntity(UUID userId, String userName, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}

	public UserEntity() {
		this(UUID.randomUUID(), "", "", "");
	}
	
	public UserEntity(User user) {
		this(user.getUserId(), user.getUserName(), user.getPhoneNumber(), user.getEmailAddress());
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public UUID getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}
	
}
