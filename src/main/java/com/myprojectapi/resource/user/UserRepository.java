package com.myprojectapi.resource.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myprojectapi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

}
