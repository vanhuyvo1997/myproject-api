package com.myprojectapi.resource.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myprojectapi.entity.User.Role;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("not found user name: " + username));
	}

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
}
