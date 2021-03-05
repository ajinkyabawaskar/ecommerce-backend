package in.stackroute.cplayer.controller;

import static io.restassured.RestAssured.given;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.stackroute.cplayer.entity.User;
import in.stackroute.cplayer.security.JwtUtil;
import in.stackroute.cplayer.service.UserServiceImpl;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@SpringBootTest
public class UserControllerTest {

//	@LocalServerPort
//	private int port;

	private String uri;
	private String username;
	private String password;

	private ObjectMapper mapper = new ObjectMapper();

	@MockBean
	private UserServiceImpl userServiceImpl;

	@MockBean
	private JwtUtil jwtUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

//	@Autowired
//	private MockBean mockMvc;

	private User user = new User();

	@PostConstruct
	public void init() {
		uri = "http://localhost:8080";
		username = "user";
		password = "pass";
	}

	@BeforeEach
	public void setup() {
		user.setUsername(username);
		user.setPassword(password);
		String rawPassword = user.getPassword();
		String encodedPassword = passwordEncoder.encode(rawPassword);
	}

	@AfterEach
	public void tearDown() {

	}

	@Test
	public void testCreateUserSuccess() throws JsonProcessingException {
		Mockito.when(userServiceImpl.createUser(user)).thenReturn(user);

		user.setUserId(1L);
		user.setEmail("test@example.com");

		String requestBody = mapper.writeValueAsString(user);

//		HashMap<String, String> authRequest = new HashMap<String, String>();
//		authRequest.put("username", "user");
//		authRequest.put("password", "pass");
//		authRequest.put("email", "test@gmail.com");

		String endpoint = "http://localhost:8080/user/";

		// Response response1 ;
		Response response = given().header("Content-type", "application/json").and().body(requestBody).when()
				.post(endpoint, requestBody).then().extract().response();
		JsonPath jsonPathEvaluator = response.jsonPath();
		String jwt = jsonPathEvaluator.get("jwt");

		// endpoint = uri + "/user/";

		response = given().header("Authorization", "Bearer " + jwt).when().get(endpoint).then().extract().response();

		System.out.println(response.asString());

		Assertions.assertEquals(200, response.statusCode(), "Should return all data of given user");

	}

}
