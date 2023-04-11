package com.myprojectapi.resource.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myprojectapi.entity.User.Role;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;

	public UserDTO createNew(UserRequest request) {
		var isExisted = userRepo.findByUsername(request.username()).isPresent();
		if (isExisted) {
			throw new UserAlreadyExistedException(request.username() + " was in use.");
		} else {
			var user = request.toUser();
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRole(Role.USER);

			return UserDTO.form(userRepo.save(user));
		}
	}

	public List<UserDTO> getAll() {
		return userRepo.findAll().stream().map(UserDTO::form).collect(Collectors.toList());
	}
}
