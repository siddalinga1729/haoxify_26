package com.hoaxify_1.hoaxify_1.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	UserRepository userRepository;
	//@Autowired(required = true)
	BCryptPasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	public User saveUser(User user) {
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		
		return userRepository.save(user);
	}
}
