package com.hoaxify_1.hoaxify_1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.hoaxify_1.hoaxify_1.shared.GenericResponce;
import com.hoaxify_1.hoaxify_1.user.User;
import com.hoaxify_1.hoaxify_1.user.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {
	private static final String API_1_0_USER = "/api/1.0/users";
	@Autowired
	TestRestTemplate testRestTemplate;// it is used for web services testing
	@Autowired
	UserRepository userRepository;

	@Before
	public void cleanUp() {
		userRepository.deleteAll();
	}

	@Test
	public void postUser_whenUserIsValid_recieveOk() {
		User user = createVaidUser();

		ResponseEntity<Object> response = postSignup(user, Object.class);
		try {
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		} catch (AssertionError e) {
			e.printStackTrace();
		}

	}

	private User createVaidUser() {// extracted into method
		User user = new User();
		user.setUserName("test-user");
		user.setDisplayName("test-display");
		user.setPassword("Password");
		return user;
	}

	@Test
	public void postUser_whenUserIsValid_userStoredtoDatabase() {
		User user = createVaidUser();
		ResponseEntity<Object> response = postSignup(user, Object.class);
		try {
			assertThat(userRepository.count()).isEqualTo(1);
		} catch (AssertionError e) {
			e.printStackTrace();
		}

	}

	@Test
	public void postUser_whenUserIsValid_recieveSuccessMessage() {
		User user = createVaidUser();

		ResponseEntity<GenericResponce> response = testRestTemplate.postForEntity(API_1_0_USER, user,
				GenericResponce.class);
		try {
			assertThat(response.getBody().getMessage()).isNotNull();
		} catch (AssertionError e) {
			e.printStackTrace();
		}
	}

	@Test
	public void postUser_whenserValid_passwordIsHasheedInDatabase() {
		User user = createVaidUser();
		testRestTemplate.postForEntity(API_1_0_USER, user, Object.class);
		List<User> users = userRepository.findAll();
		User inDB = users.get(1);
		try {
			assertThat(inDB.getPassword()).isNotEqualTo(user.getPassword());
		} catch (AssertionError e) {
			e.printStackTrace();
		}
	}

	@Test
	public void postUser_whenUserHasNullUserName_recieveBadRequest() {
		User user = createVaidUser();
		user.setUserName(null);
		ResponseEntity<Object> response = postSignup(user, Object.class);

		try {
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void postUser_whenUserHasNullPassword_recieveBadRequest() {
		User user = createVaidUser();
		user.setPassword(null);
		ResponseEntity<Object> response = postSignup(user, Object.class);

		try {
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void postUser_whenUserHasNullDisplayName_recieveBadRequest() {
		User user = createVaidUser();
		user.setDisplayName(null);
		ResponseEntity<Object> response = postSignup(user, Object.class);

		try {
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void postUser_whenUserHasUerNameWithLessThanRequired_recieveBadRequest() {
		User user = createVaidUser();
		user.setUserName("abc");
		ResponseEntity<Object> response = postSignup(user, Object.class);

		try {
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void postUser_whenUserHasPasswordWithLessThanRequired_recieveBadRequest() {
		User user = createVaidUser();
		user.setPassword("abc");
		ResponseEntity<Object> response = postSignup(user, Object.class);

		try {
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void postUser_whenUserHasDisplayNameWithLessThanRequired_recieveBadRequest() {
		User user = createVaidUser();
		user.setDisplayName("abc");
		ResponseEntity<Object> response = postSignup(user, Object.class);

		try {
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void postUser_whenUserHasUserNameExceedLength_recieveBadRequest() {
		User user = createVaidUser();
		String valueOf256Chars = IntStream.rangeClosed(1,256).mapToObj(x -> "a").collect(Collectors.joining());
		user.setUserName(valueOf256Chars);
		ResponseEntity<Object> response = postSignup(user, Object.class);

		try {
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void postUser_whenUserHasDisplayNameExceedLength_recieveBadRequest() {
		User user = createVaidUser();
		String valueOf256Chars = IntStream.rangeClosed(1,256).mapToObj(x -> "a").collect(Collectors.joining());
		user.setDisplayName(valueOf256Chars);
		ResponseEntity<Object> response = postSignup(user, Object.class);

		try {
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void postUser_whenUserHasPasswordExceedLength_recieveBadRequest() {
		User user = createVaidUser();
		String valueOf256Chars = IntStream.rangeClosed(1,256).mapToObj(x -> "a").collect(Collectors.joining());
		user.setPassword(valueOf256Chars);
		ResponseEntity<Object> response = postSignup(user, Object.class);

		try {
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void postUser_whenUserHasPasswordWithAllLowerCase_recieveBadRequest() {
		User user = createVaidUser();
		user.setPassword("abceerggf");
		ResponseEntity<Object> response = postSignup(user, Object.class);

		try {
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void postUser_whenUserHasPasswordWithAllUpperCase_recieveBadRequest() {
		User user = createVaidUser();
		user.setPassword("ABVGCFS");
		ResponseEntity<Object> response = postSignup(user, Object.class);

		try {
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void postUser_whenUserHasPasswordWithAllNumbers_recieveBadRequest() {
		User user = createVaidUser();
		user.setPassword("1546878");
		ResponseEntity<Object> response = postSignup(user, Object.class);

		try {
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	public <T> ResponseEntity<T> postSignup(Object request, Class<T> response) {
		return testRestTemplate.postForEntity(API_1_0_USER, request, response);
	}
}
