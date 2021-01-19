package com.openclassrooms.ocproject8.shared.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public void initializeUsers(int count) {
		IntStream.range(0, count).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			UserEntity userEntity = new UserEntity(UUID.randomUUID(), userName, phone, email);
			userRepository.save(userEntity);
		});
	}
	
	public Optional<UserEntity> getUser(String userName) {
		return userRepository.findByUserName(userName);
	}

	public void addUser(UserEntity userEntity) {
		userRepository.save(userEntity);
	}

	public List<UserEntity> getAllUsers() {
		return userRepository.findAll();
	}
	
	public void deleteAll() {
		userRepository.deleteAll();
	}
	
}
