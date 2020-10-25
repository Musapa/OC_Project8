package shared.user;

import java.util.UUID;

public class UserDTO {

	private final UUID userId;
	private final String userName;
	private String phoneNumber;
	private String emailAddress;

	public UserDTO(UUID userId, String userName, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}

	public UserDTO() {
		this(UUID.randomUUID(), "", "", "");
	}
	
	public UserDTO(User user) {
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
