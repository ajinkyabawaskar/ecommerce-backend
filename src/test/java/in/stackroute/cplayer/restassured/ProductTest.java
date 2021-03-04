package in.stackroute.cplayer.restassured;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;

public class ProductTest {
	private static int port = 8080;

	private static String uri = "http://localhost:" + port;
	private static String username;
	private static String password;

	private static ObjectMapper mapper;

	@BeforeAll
	public static void setUp() {
		username = "user";
		password = "pass";
		mapper = new ObjectMapper();
	}

	ResponseSpecification checkStatusCodeAndContentType = new ResponseSpecBuilder().expectStatusCode(200)
			.expectContentType(ContentType.JSON).build();

	@ParameterizedTest
	@ValueSource(strings = { "all/", "category/" })
	public void getProductsGivenValidJwt(String url) throws JsonProcessingException {

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

		endpoint = "/product/" + url;
		RestAssured.baseURI = uri;
		response = given().header("Authorization", "Bearer " + jwt).when().get(endpoint).then().extract().response();

		Assertions.assertEquals(200, response.statusCode(), "Should return all products with HTTP 200");
		Assertions.assertNotNull(response.body());

		jsonPathEvaluator = response.jsonPath();
		Assertions.assertTrue(jsonPathEvaluator.getList("$").size() > 1);

	}

	@ParameterizedTest
	@CsvSource(value = { "category/,Laptop Adapters", "category/,Laptops", "name/,Mac", "name/,Asus TUF" })
	public void getSearchedProductsGivenValidJwt(String url, String search) throws JsonProcessingException {

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

		endpoint = "/product/" + url + search;
		RestAssured.baseURI = uri;
		response = given().header("Authorization", "Bearer " + jwt).when().get(endpoint).then().extract().response();

		Assertions.assertEquals(200, response.statusCode(), "Should return all products with HTTP 200");
		Assertions.assertNotNull(response.body());

		jsonPathEvaluator = response.jsonPath();
		Assertions.assertTrue(jsonPathEvaluator.getList("$").size() > 0);

	}

	@ParameterizedTest
	@CsvSource(value = { "id/,2231", "id/,3414" })
	public void getProductByIdGivenValidJwt(String url, String search) throws JsonProcessingException {

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

		endpoint = "/product/" + url + search;
		RestAssured.baseURI = uri;
		response = given().header("Authorization", "Bearer " + jwt).when().get(endpoint).then().extract().response();

		Assertions.assertEquals(200, response.statusCode(), "Should return all products with HTTP 200");
		Assertions.assertNotNull(response.body());

		jsonPathEvaluator = response.jsonPath();

	}

}
