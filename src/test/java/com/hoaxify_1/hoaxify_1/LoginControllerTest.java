package com.hoaxify_1.hoaxify_1;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.hoaxify_1.hoaxify_1.error.ApiError;
import com.hoaxify_1.hoaxify_1.user.User;
import com.hoaxify_1.hoaxify_1.user.UserRepository;
import com.hoaxify_1.hoaxify_1.user.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {
	
	private static final String API_1_0_LOGIN = "/api/1.0/login";
	
	@Autowired
	TestRestTemplate testRestTemplate;
	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepo;
	
	@Test
	public void postLogin_withoutUserCredentials_recieveUnauthorized() {
		ResponseEntity<Object> response = login(Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	
	}
	@Test
	public void postLogin_withInCorrectCredentials_recieveUnauthorized() {
		ResponseEntity<Object> response = login(Object.class);
		authenticated();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	
	}
	@Test
	public void postLogin_withoutUserCredentials_recieveApiError() {
		ResponseEntity<ApiError> response = login(ApiError.class);
		assertThat(response.getBody().getUrl()).isEqualTo(API_1_0_LOGIN);
	
	}
	@Test
	public void postLogin_withoutUserCredentials_recieveApiError_withoutVlidationErrors() {
		ResponseEntity<String> response = login(String.class);
		assertThat(response.getBody().contains("validationErrors")).isFalse();
	
	}
	@Test
	public void postLogin_withInCorrectCredentials_recieveUnauthorizedWithoutWWWAuthenticationHeader() {
		ResponseEntity<Object> response = login(Object.class);
		authenticated();
		assertThat(response.getHeaders().containsKey("WWW-Authenticate")).isFalse();
	
	}
	@Test
	public void postLogin_withValidCredentials_recieveOk() {
		User user=new User();
		user.setUserName("test-user");
		user.setDisplayName("test-display");
		user.setPassword("password");
		userService.saveUser(user);
		authenticated();
		ResponseEntity<Object> response = login(Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		
	}
//	@Test
//	public void postLogin_withValidCredentials_recieveLoggedInUserId() {
//		User user=new User();
//		user.setUserName("test-user");
//		user.setDisplayName("test-display");
//		user.setPassword("password");
//		User inDB=userService.saveUser(user);
//		authenticated();
//		ResponseEntity<Map<String, Object>> response = login(Object.class);
//		
//		
//	}
	private void authenticated() {
		testRestTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("test-User", "password"));
	}
	public <T> ResponseEntity<T> login(Class<T> responseType){
		return testRestTemplate.postForEntity(API_1_0_LOGIN, null, responseType);
	}

}
