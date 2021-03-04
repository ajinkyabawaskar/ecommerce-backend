package in.stackroute.cplayer.restassured;

import static io.restassured.RestAssured.given;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.stackroute.cplayer.entity.Product;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;

public class CartTest {
	private static int port = 8080;

	private static String uri = "http://localhost:" + port;
	private static String username;
	private static String password;
	private static String randomString;
	private static long randomId;

	private static ObjectMapper mapper;

	@BeforeAll
	public static void setUp() {
		username = "user";
		password = "pass";

		mapper = new ObjectMapper();
		randomId = new Random().nextLong();
	}

	@BeforeEach
	public void setRandoms() {
		randomString = RandomStringUtils.randomAlphabetic(10);
	}

	ResponseSpecification checkStatusCodeAndContentType = new ResponseSpecBuilder().expectStatusCode(200)
			.expectContentType(ContentType.JSON).build();

	@Test
	public void getCartGivenValidJwt() throws JsonProcessingException {

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

		endpoint = "/cart/";
		RestAssured.baseURI = uri;
		response = given().header("Authorization", "Bearer " + jwt).when().get(endpoint).then().extract().response();

		Assertions.assertEquals(200, response.statusCode(), "Should return all products with HTTP 200");
		Assertions.assertNotNull(response.body());

		jsonPathEvaluator = response.jsonPath();

	}

	@Test
	public void addToCartGivenValidJwt() throws JsonProcessingException {

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

		endpoint = "/cart/";
		RestAssured.baseURI = uri;

		Product product = new Product();
		product.setPid(randomId);
		product.setName("Test Product");
		product.setCategory("Test Category");
		product.setOfferedPrice("3423");
		product.setActualPrice("4323");

		requestBody = mapper.writeValueAsString(product);

		response = given().header("Content-type", "application/json").header("Authorization", "Bearer " + jwt)
				.body(requestBody).when().post(endpoint).then().extract().response();

		Assertions.assertEquals(200, response.statusCode(), "Should add the product to cart with HTTP 200");
		Assertions.assertNotNull(response.body());

		jsonPathEvaluator = response.jsonPath();

		String name = jsonPathEvaluator.get("name");
		Assertions.assertEquals("Test Product", name);

		String category = jsonPathEvaluator.get("category");
		Assertions.assertEquals("Test Category", category);

		String offeredPrice = jsonPathEvaluator.get("offeredPrice");
		Assertions.assertEquals("3423", offeredPrice);

		String actualPrice = jsonPathEvaluator.get("actualPrice");
		Assertions.assertEquals("4323", actualPrice);

	}

	@Test
	public void removeToCartGivenValidJwt() throws JsonProcessingException {

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

		endpoint = "/cart/";
		RestAssured.baseURI = uri;

		Product product = new Product();
		product.setPid(randomId);
		product.setName("Test Product");
		product.setCategory("Test Category");
		product.setOfferedPrice("3423");
		product.setActualPrice("4323");

		requestBody = mapper.writeValueAsString(product);

		response = given().header("Content-type", "application/json").header("Authorization", "Bearer " + jwt)
				.body(requestBody).when().put(endpoint).then().extract().response();

		Assertions.assertEquals(200, response.statusCode(), "Product should be removed from cart with HTTP 200");
		Assertions.assertNotNull(response.body());

		jsonPathEvaluator = response.jsonPath();

		String name = jsonPathEvaluator.get("name");
		Assertions.assertEquals("Test Product", name);

		String category = jsonPathEvaluator.get("category");
		Assertions.assertEquals("Test Category", category);

		String offeredPrice = jsonPathEvaluator.get("offeredPrice");
		Assertions.assertEquals("3423", offeredPrice);

		String actualPrice = jsonPathEvaluator.get("actualPrice");
		Assertions.assertEquals("4323", actualPrice);

	}

	@Test
	public void emptyCartGivenValidJwt() throws JsonProcessingException {

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

		endpoint = "/cart/";
		RestAssured.baseURI = uri;

		response = given().header("Authorization", "Bearer " + jwt).when().delete(endpoint).then().extract().response();

		Assertions.assertEquals(200, response.statusCode(), "Should empty cart with HTTP 200");

	}

}
