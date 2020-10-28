package com.openclassrooms.ocproject8.shared.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public User getUser(String userName) {
		return userRepository.getUser(userName);
	}

	public void addUser(User user) {
		userRepository.addUser(user);
	}

	public List<User> getAllUsers() {
		return userRepository.getInternalUserMap().values().stream().collect(Collectors.toList());
	}
}
