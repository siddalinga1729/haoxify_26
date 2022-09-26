package com.hoaxify_1.hoaxify_1.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify_1.hoaxify_1.shared.GenericResponce;

@RestController
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/api/1.0/users")
	GenericResponce createUser(@Validated @RequestBody User user) {
		try {
			userService.saveUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}

		GenericResponce body = new GenericResponce();
		body.setMessage("user Seved");
		return body;
	}
}
