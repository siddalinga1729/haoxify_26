package com.hoaxify_1.hoaxify_1.user;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify_1.hoaxify_1.error.ApiError;

@RestController
public class LoginController {

	@PostMapping("/api/1.0/login")
	void handleLogin() {

	}

//	@ExceptionHandler({ AccessDeniedException.class })
//	@ResponseStatus(HttpStatus.UNAUTHORIZED)
//	ApiError handleAccessDeniedException() {
//		return new ApiError(404, "Access Error", "/api/1.0/login");
//	}
}
