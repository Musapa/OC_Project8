package com.openclassrooms.ocproject8.shared.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.ocproject8.shared.domain.User;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.helper.InternalTestHelper;
import com.openclassrooms.ocproject8.shared.repository.UserRepository;
import com.openclassrooms.paymybuddy.domain.Account;
import com.openclassrooms.paymybuddy.domain.Role;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public void initializeUsers(User user) {
		userRepository.save(new UserEntity(user));
	}
	
	public Optional<UserEntity> getUser(String userName) {
		return userRepository.findById(userName);
	}

	public void addUser(UserEntity userEntity) {
		userRepository.save(userEntity);
	}

	public List<UserEntity> getAllUsers() {
		return userRepository.findAll();
	}

	public void initialise() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			userRepository.save(new UserEntity(user));
		});
	}
}
