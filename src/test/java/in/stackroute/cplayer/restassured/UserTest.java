package in.stackroute.cplayer.restassured;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.stackroute.cplayer.entity.User;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;

public class UserTest {

	private static int port = 8080;

	private static String uri = "http://localhost:" + port;
	private static String username;
	private static String password;
	private static String randomString;

	private static ObjectMapper mapper;

	private static PasswordEncoder passwordEncoder;

	@BeforeAll
	public static void setUp() {
		username = "user";
		password = "pass";
		randomString = RandomStringUtils.randomAlphabetic(10);

		mapper = new ObjectMapper();
		passwordEncoder = new BCryptPasswordEncoder();
	}

	ResponseSpecification checkStatusCodeAndContentType = new ResponseSpecBuilder().expectStatusCode(200)
			.expectContentType(ContentType.JSON).build();

	@Test
	public void getOwnDataGivenValidJwt() throws JsonProcessingException {

		/**
		 * Create a user object
		 */
		in.stackroute.cplayer.entity.User userTryingToSignIn = new in.stackroute.cplayer.entity.User();
		userTryingToSignIn.setUsername(username);
		userTryingToSignIn.setPassword(password);

		/**
		 * Convert the User object to JSON using ObjectMapper
		 */
		String requestBody = mapper.writeValueAsString(userTryingToSignIn);

		String endpoint = "/authenticate";

		/**
		 * Make the rest-assured request and get response
		 */
		RestAssured.baseURI = uri;
		Response response = given().header("Content-type", "application/json").and().body(requestBody).when()
				.post(endpoint).then().assertThat().spec(checkStatusCodeAndContentType).and().extract().response();

		/**
		 * Get the jwt from response body using JsonPathEvaluator
		 */
		JsonPath jsonPathEvaluator = response.jsonPath();
		String jwt = jsonPathEvaluator.get("jwt");

		endpoint = "/user/";
		RestAssured.baseURI = uri;
		response = given().header("Authorization", "Bearer " + jwt).when().get(endpoint).then().extract().response();

		jsonPathEvaluator = response.jsonPath();
		int userId = jsonPathEvaluator.get("userId");
		String name = jsonPathEvaluator.get("name");
		String email = jsonPathEvaluator.get("email");

		Assertions.assertEquals(200, response.statusCode(), "Should return all data of given user with HTTP 200");
		Assertions.assertNotNull(response.body());
		Assertions.assertEquals(1, userId);
		Assertions.assertTrue(name.contains("Name"));
		Assertions.assertEquals("test@example.com", email);

	}

	@Test
	public void getOwnDataNoJwt() throws JsonProcessingException {

		/**
		 * Create a user object
		 */
		in.stackroute.cplayer.entity.User userTryingToSignIn = new in.stackroute.cplayer.entity.User();
		userTryingToSignIn.setUsername(username);
		userTryingToSignIn.setPassword(password);
		userTryingToSignIn.setUserId(1L);
		userTryingToSignIn.setName("Test User");
		userTryingToSignIn.setEmail("test@example.com");

		String endpoint = uri + "/user/";
		Response response = given().when().get(endpoint).then().extract().response();
		Assertions.assertEquals(403, response.statusCode(),
				"Should not return any data when no JWT is passed in auth header.");

	}

	@Test
	public void getOwnDataGivenInvalidJwt() throws JsonProcessingException {

		/**
		 * Create a user object
		 */
		in.stackroute.cplayer.entity.User userTryingToSignIn = new in.stackroute.cplayer.entity.User();
		userTryingToSignIn.setUsername(username);
		userTryingToSignIn.setPassword(password);
		userTryingToSignIn.setUserId(1L);
		userTryingToSignIn.setName("Test User");
		userTryingToSignIn.setEmail("test@example.com");

		String endpoint = uri + "/user/";

		String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjE0NzA4Mzg3LCJpYXQiOjE2MTQ3MDQ3ODd9.t83aPorwoeVViVjgpTHH2BYx_0eUG1TF0HAowmY4-tY";
		Response response = given().header("Authorization", "Bearer " + jwt).when().get(endpoint).then().extract()
				.response();
		Assertions.assertEquals(403, response.statusCode(),
				"Should not return any data when invalid JWT is passed in auth header.");

	}

	@Test
	public void createUser() throws JsonProcessingException {
		User newUser = new User();

		newUser.setName(randomString);
		newUser.setPassword(randomString);
		newUser.setUsername(randomString);

		String requestBody = mapper.writeValueAsString(newUser);
		String endpoint = uri + "/user/register";

		Response response = given().contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.queryParam("username", randomString).queryParam("password", randomString)
				.queryParam("name", randomString).queryParam("email", randomString + "@example.com")
				.multiPart("type", "image/png")
				.multiPart("file", new File("C:\\Users\\Ajinkya\\Documents\\profile.png")).when().post(endpoint).then()
				.extract().response();

		JsonPath jsonPathEvaluator = response.jsonPath();
		String name = jsonPathEvaluator.get("name");
		String username = jsonPathEvaluator.get("username");
		String email = jsonPathEvaluator.get("email");

		Assertions.assertEquals(200, response.statusCode(), "Should return data of created user with HTTP 200");
		Assertions.assertNotNull(response.body());

		Assertions.assertEquals(randomString, name);
		Assertions.assertEquals(randomString, username);
		Assertions.assertEquals(randomString + "@example.com", email);
	}

	@Test
	public void updateUserGivenValidJwt() throws JsonProcessingException {

		/**
		 * Create a user object
		 */
		User userTryingToSignIn = new User();
		userTryingToSignIn.setUsername(username);
		userTryingToSignIn.setPassword(password);

		/**
		 * Convert the User object to JSON using ObjectMapper
		 */
		String requestBody = mapper.writeValueAsString(userTryingToSignIn);

		String endpoint = "/authenticate";

		/**
		 * Make the rest-assured request and get response
		 */
		RestAssured.baseURI = uri;
		Response response = given().header("Content-type", "application/json").and().body(requestBody).when()
				.post(endpoint).then().assertThat().spec(checkStatusCodeAndContentType).and().extract().response();

		/**
		 * Get the jwt from response body using JsonPathEvaluator
		 */
		JsonPath jsonPathEvaluator = response.jsonPath();
		String jwt = jsonPathEvaluator.get("jwt");

		endpoint = "/user/";
		RestAssured.baseURI = uri;

		userTryingToSignIn.setName("Updated Name " + RandomStringUtils.randomAlphabetic(10));
		userTryingToSignIn.setUserId((long) 1);
		userTryingToSignIn.setEmail("test@example.com");
		userTryingToSignIn.setPassword(passwordEncoder.encode(password));

		requestBody = mapper.writeValueAsString(userTryingToSignIn);

		response = given().header("Content-type", "application/json").header("Authorization", "Bearer " + jwt)
				.body(requestBody).when().put(endpoint).then().extract().response();

		jsonPathEvaluator = response.jsonPath();
		String name = jsonPathEvaluator.get("name");
		String email = jsonPathEvaluator.get("email");

		Assertions.assertEquals(200, response.statusCode(), "Should return all data of given user with HTTP 200");
		Assertions.assertNotNull(response.body());

		Assertions.assertTrue(name.contains("Updated Name"));
		Assertions.assertEquals("test@example.com", email);

	}

	@Test
	public void updateUserGivenInvalidJwt() throws JsonProcessingException {

		/**
		 * Create a user object
		 */
		User userTryingToSignIn = new User();
		userTryingToSignIn.setUsername(username);
		userTryingToSignIn.setPassword(password);

		/**
		 * Convert the User object to JSON using ObjectMapper
		 */
		String requestBody = mapper.writeValueAsString(userTryingToSignIn);

		String endpoint = "/authenticate";

		/**
		 * Make the rest-assured request and get response
		 */
		RestAssured.baseURI = uri;
		Response response = given().header("Content-type", "application/json").and().body(requestBody).when()
				.post(endpoint).then().assertThat().spec(checkStatusCodeAndContentType).and().extract().response();

		/**
		 * Get the jwt from response body using JsonPathEvaluator
		 */
		JsonPath jsonPathEvaluator = response.jsonPath();
		String jwt = jsonPathEvaluator.get("jwt");

		endpoint = "/user/";
		RestAssured.baseURI = uri;

		userTryingToSignIn.setName("Updated Name " + RandomStringUtils.randomAlphabetic(10));
		userTryingToSignIn.setUserId((long) 1);
		userTryingToSignIn.setEmail("test@example.com");
		userTryingToSignIn.setPassword(passwordEncoder.encode(password));

		requestBody = mapper.writeValueAsString(userTryingToSignIn);

		response = given().header("Content-type", "application/json").header("Authorization", "Bearer 1" + jwt)
				.body(requestBody).when().put(endpoint).then().extract().response();

		Assertions.assertEquals(403, response.statusCode(), "Should return 403 forbidden for invalid jwt");

	}

	@Test
	public void updateUserGivenNoJwt() throws JsonProcessingException {

		/**
		 * Create a user object
		 */
		User userTryingToSignIn = new User();
		userTryingToSignIn.setUsername(username);
		userTryingToSignIn.setPassword(password);

		/**
		 * Convert the User object to JSON using ObjectMapper
		 */
		String requestBody = mapper.writeValueAsString(userTryingToSignIn);

		/**
		 * Get the jwt from response body using JsonPathEvaluator
		 */

		String endpoint = "/user/";
		RestAssured.baseURI = uri;

		requestBody = mapper.writeValueAsString(userTryingToSignIn);

		Response response = given().header("Content-type", "application/json").body(requestBody).when().put(endpoint)
				.then().extract().response();

		Assertions.assertEquals(403, response.statusCode(), "Should return 403 forbidden for invalid jwt");

	}

	@Test
	public void deleteUserGivenValidJwt() throws JsonProcessingException {

		/**
		 * Create a user object
		 */
		User userTryingToSignIn = new User();
		userTryingToSignIn.setUsername(randomString);
		userTryingToSignIn.setPassword(randomString);

		/**
		 * Convert the User object to JSON using ObjectMapper
		 */
		String requestBody = mapper.writeValueAsString(userTryingToSignIn);

		String endpoint = "/authenticate";

		/**
		 * Make the rest-assured request and get response
		 */
		RestAssured.baseURI = uri;
		Response response = given().header("Content-type", "application/json").and().body(requestBody).when()
				.post(endpoint).then().assertThat().spec(checkStatusCodeAndContentType).and().extract().response();

		/**
		 * Get the jwt from response body using JsonPathEvaluator
		 */
		JsonPath jsonPathEvaluator = response.jsonPath();
		String jwt = jsonPathEvaluator.get("jwt");

		endpoint = "/user/";
		RestAssured.baseURI = uri;
		response = given().header("Content-type", "application/json").header("Authorization", "Bearer " + jwt).when()
				.delete(endpoint).then().extract().response();

		Assertions.assertEquals(200, response.statusCode(), "Should return all data of given user with HTTP 200");
		Assertions.assertNotNull(response.body());
		Assertions.assertTrue(response.asString().contains("true"));
	}

	@Test
	public void deleteUserGivenInvalidJwt() throws JsonProcessingException {

		/**
		 * Create a user object
		 */
		User userTryingToSignIn = new User();
		userTryingToSignIn.setUsername(username);
		userTryingToSignIn.setPassword(password);

		/**
		 * Convert the User object to JSON using ObjectMapper
		 */
		String requestBody = mapper.writeValueAsString(userTryingToSignIn);

		String endpoint = "/authenticate";

		/**
		 * Make the rest-assured request and get response
		 */
		RestAssured.baseURI = uri;
		Response response = given().header("Content-type", "application/json").and().body(requestBody).when()
				.post(endpoint).then().assertThat().spec(checkStatusCodeAndContentType).and().extract().response();

		/**
		 * Get the jwt from response body using JsonPathEvaluator
		 */
		JsonPath jsonPathEvaluator = response.jsonPath();
		String jwt = jsonPathEvaluator.get("jwt");

		endpoint = "/user/";
		RestAssured.baseURI = uri;

		requestBody = mapper.writeValueAsString(userTryingToSignIn);

		response = given().header("Authorization", "Bearer 1" + jwt).when().delete(endpoint).then().extract()
				.response();

		Assertions.assertEquals(403, response.statusCode(), "Should return 403 forbidden for invalid jwt");

	}

	@Test
	public void deleteUserGivenNoJwt() throws JsonProcessingException {

		String endpoint = "/user/";
		RestAssured.baseURI = uri;

		Response response = given().when().delete(endpoint).then().extract().response();

		Assertions.assertEquals(403, response.statusCode(), "Should return 403 forbidden for absent jwt");

	}
}
