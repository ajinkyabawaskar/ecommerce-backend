package in.stackroute.cplayer.restassured;

import static io.restassured.RestAssured.given;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class AuthenticationTest {

	private int port = 8080;

	private String uri = "http://localhost:" + port;;
	private String username = "user";
	private String password = "pass";

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void shouldSigninGivenCorrectCredentials() throws JsonProcessingException {

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
		String endpoint = uri + "/authenticate";

		/**
		 * Make the rest-assured request and get response
		 */
		Response response = given().header("Content-type", "application/json").and().body(requestBody).when()
				.post(endpoint).then().extract().response();

		/**
		 * Get the jwt from response body using JsonPathEvaluator
		 */
		JsonPath jsonPathEvaluator = response.jsonPath();
		String jwt = jsonPathEvaluator.get("jwt");

		Assertions.assertEquals(200, response.statusCode(), "User should be able to sign in with correct credentials.");
		Assertions.assertNotNull(jwt,
				"A jwt field must be present in the response JSON, containing the generated JWT for given user.");

	}

	@Test
	public void shouldNotSigninGivenIncorrectCredentials() throws JsonProcessingException {
		in.stackroute.cplayer.entity.User userTryingToSignIn = new in.stackroute.cplayer.entity.User();
		userTryingToSignIn.setUsername(username);
		userTryingToSignIn.setPassword("wrong" + password);

		String requestBody = mapper.writeValueAsString(userTryingToSignIn);

		String endpoint = uri + "/authenticate";

		Response response = given().header("Content-type", "application/json").and().body(requestBody).when()
				.post(endpoint).then().extract().response();

		JsonPath jsonPathEvaluator = response.jsonPath();
		String jwt = jsonPathEvaluator.get("jwt");

		Assertions.assertEquals(403, response.statusCode(),
				"JWT should not be generated if credentials are incorrect.");
		Assertions.assertNull(jwt);
	}

	@Test
	public void shouldNotSigninGivenNoCredentials() throws JsonProcessingException {
		in.stackroute.cplayer.entity.User userTryingToSignIn = new in.stackroute.cplayer.entity.User();
		userTryingToSignIn.setUsername(username);
		userTryingToSignIn.setPassword(password);

		String requestBody = "";

		String endpoint = uri + "/authenticate";

		Response response = given().header("Content-type", "application/json").and().body(requestBody).when()
				.post(endpoint).then().extract().response();

		JsonPath jsonPathEvaluator = response.jsonPath();
		String jwt = jsonPathEvaluator.get("jwt");

		Assertions.assertEquals(400, response.statusCode(), "Should not process requests without credentials.");
		Assertions.assertNull(jwt);
	}

	@Test
	public void shouldNotSigninGivenNonExistantCredentials() throws JsonProcessingException {
		in.stackroute.cplayer.entity.User userTryingToSignIn = new in.stackroute.cplayer.entity.User();
		userTryingToSignIn.setUsername(username + RandomStringUtils.randomAlphabetic(10));
		userTryingToSignIn.setPassword(password);

		String requestBody = mapper.writeValueAsString(userTryingToSignIn);

		String endpoint = uri + "/authenticate";

		Response response = given().header("Content-type", "application/json").and().body(requestBody).when()
				.post(endpoint).then().extract().response();

		JsonPath jsonPathEvaluator = response.jsonPath();
		String jwt = jsonPathEvaluator.get("jwt");

		Assertions.assertEquals(403, response.statusCode(),
				"JWT should not be generated if given user does not exist.");
		Assertions.assertNull(jwt);
	}

}
