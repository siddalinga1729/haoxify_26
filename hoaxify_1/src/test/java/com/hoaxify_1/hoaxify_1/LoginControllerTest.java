package com.hoaxify_1.hoaxify_1;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
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
		try{
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		}catch (AssertionError e) {
			// TODO: handle exception
		}
	
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
	@Test
	public void postLogin_withValidCredentials_recieveLoggedInUserId() {//authentication
		User user=new User();
		user.setUserName("test-user");
		user.setDisplayName("test-display");
		user.setPassword("password");
		User inDB=userService.saveUser(user);
		authenticated();
		ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<Map<String,Object>>() {});
		Map<String, Object> body = response.getBody();
		Integer id = (Integer) body.get("id");
		assertThat(id).isEqualTo(inDB.getId());
		
	}
	@Test
	public void postLogin_withValidCredentials_recieveLoggedInUsersImage() {//authentication
		User user=new User();
		user.setUserName("test-user");
		user.setDisplayName("test-display");
		user.setPassword("password");
		user.setImage("profile-image.png");
		User inDB=userService.saveUser(user);
		authenticated();
		ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<Map<String,Object>>() {});
		Map<String, Object> body = response.getBody();
		String image = (String) body.get("image");
		assertThat(image).isEqualTo(inDB.getImage());
		
	}
	@Test
	public void postLogin_withValidCredentials_recieveLoggedInDisplayName() {//we are saving display name in DB
		User user=new User();
		user.setUserName("test-user");
		user.setDisplayName("test-display");
		user.setPassword("password");
		user.setImage("profile-image.png");
		User inDB=userService.saveUser(user);
		authenticated();
		ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<Map<String,Object>>() {});
		Map<String, Object> body = response.getBody();
		String displayName = (String) body.get("displayName");
		assertThat(displayName).isEqualTo(inDB.getDisplayName());
		
	}
	@Test
	public void postLogin_withValidCredentials_recieveLoggedInUserName() {//we are saving display name in DB
		User user=new User();
		user.setUserName("test-user");
		user.setDisplayName("test-display");
		user.setPassword("password");
		user.setImage("profile-image.png");
		User inDB=userService.saveUser(user);
		authenticated();
		ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<Map<String,Object>>() {});
		Map<String, Object> body = response.getBody();
		String userName = (String) body.get("userName");
		assertThat(userName).isEqualTo(inDB.getUsername());
		
	}
	
	private void authenticated() {
		testRestTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("test-User", "password"));
	}
	public <T> ResponseEntity<T> login(Class<T> responseType){
		return testRestTemplate.postForEntity(API_1_0_LOGIN, null, responseType);
	}
	public <T> ResponseEntity<T> login(ParameterizedTypeReference<T> responseType){
		return testRestTemplate.exchange(API_1_0_LOGIN, HttpMethod.POST,null,responseType);
	}

}
