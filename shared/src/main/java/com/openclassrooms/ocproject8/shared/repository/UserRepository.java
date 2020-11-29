package com.openclassrooms.ocproject8.shared.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.ocproject8.shared.domain.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
	Optional<UserEntity> findByUserName(String userName);
}
