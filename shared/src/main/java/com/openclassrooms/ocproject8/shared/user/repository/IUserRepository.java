package com.openclassrooms.ocproject8.shared.user.repository;

import org.springframework.stereotype.Repository;
import com.openclassrooms.ocproject8.shared.user.domain.User;

@Repository("userRepository")
public interface IUserRepository extends JpaRepository<User, Long> {

}
