package com.openclassrooms.ocproject8.shared.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.ocproject8.shared.domain.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
	
}
