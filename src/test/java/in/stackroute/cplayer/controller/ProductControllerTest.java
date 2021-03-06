package in.stackroute.cplayer.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import in.stackroute.cplayer.entity.User;
import in.stackroute.cplayer.security.MyUserDetailsService;
import in.stackroute.cplayer.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private MyUserDetailsService udService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static long randomId;
	private static String randomString;
	private static User user;

	private ObjectMapper mapper = new ObjectMapper();

	@BeforeAll
	public static void setUp() {

		randomId = new Random().nextLong();
		randomString = RandomStringUtils.randomAlphabetic(10);

		user = new User();
		user.setUserId(randomId);
		user.setEmail(randomString + "@example.com");
		user.setUsername(randomString);
		user.setPassword(randomString);
		user.setName(randomString);
	}

	@BeforeEach
	public void insert() throws IOException, Exception {
		when(userService.createUser(user)).thenReturn(user);

		MockMultipartFile uploadFile = new MockMultipartFile("profile.png", "content".getBytes());

		mockMvc.perform(MockMvcRequestBuilders.multipart("/user/register").file("file", uploadFile.getBytes())
				.queryParam("username", randomString).queryParam("password", randomString)
				.queryParam("name", randomString).queryParam("email", randomString).characterEncoding("UTF-8"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetAllProducts() throws Exception {

		String userJson = mapper.writeValueAsString(user);

		org.springframework.security.core.userdetails.User userT = new org.springframework.security.core.userdetails.User(
				randomString, passwordEncoder.encode(randomString), new ArrayList<>());

		Mockito.when(udService.loadUserByUsername(randomString)).thenReturn(userT);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/authenticate").contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		Mockito.when(userService.getUserByUsername(randomString)).thenReturn(user);

		mockMvc.perform(MockMvcRequestBuilders.get("/product/all/").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isOk()).andDo(mvcResult -> {
					String jsonString = (mvcResult.getResponse().getContentAsString());
					int length = JsonPath.parse(jsonString).read("$.length()");
					Assertions.assertTrue(length > 10);
					Assertions.assertTrue(jsonString.contains("Mac"));
				});
	}

	@Test
	public void testGetAllCategories() throws Exception {

		String userJson = mapper.writeValueAsString(user);

		org.springframework.security.core.userdetails.User userT = new org.springframework.security.core.userdetails.User(
				randomString, passwordEncoder.encode(randomString), new ArrayList<>());

		Mockito.when(udService.loadUserByUsername(randomString)).thenReturn(userT);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/authenticate").contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		Mockito.when(userService.getUserByUsername(randomString)).thenReturn(user);

		mockMvc.perform(MockMvcRequestBuilders.get("/product/category/").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isOk()).andDo(mvcResult -> {
					String jsonString = (mvcResult.getResponse().getContentAsString());
					int length = JsonPath.parse(jsonString).read("$.length()");
					Assertions.assertTrue(length > 1);
					Assertions.assertTrue(jsonString.contains("laptop"));
				});

	}

	@ParameterizedTest
	@ValueSource(strings = { "2231", "7186" })
	public void testGetById(String id) throws Exception {

		String userJson = mapper.writeValueAsString(user);

		org.springframework.security.core.userdetails.User userT = new org.springframework.security.core.userdetails.User(
				randomString, passwordEncoder.encode(randomString), new ArrayList<>());

		Mockito.when(udService.loadUserByUsername(randomString)).thenReturn(userT);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/authenticate").contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		Mockito.when(userService.getUserByUsername(randomString)).thenReturn(user);

		mockMvc.perform(MockMvcRequestBuilders.get("/product/id/" + id).header("Authorization", "Bearer " + jwt))
				.andExpect(status().isOk()).andDo(mvcResult -> {
					String jsonString = (mvcResult.getResponse().getContentAsString());
					Assertions.assertTrue(jsonString.contains("pid"));
				});

	}

	@ParameterizedTest
	@ValueSource(strings = { "lenovo", "asus", "apple", "hp" })
	public void testGetByName(String name) throws Exception {

		String userJson = mapper.writeValueAsString(user);

		org.springframework.security.core.userdetails.User userT = new org.springframework.security.core.userdetails.User(
				randomString, passwordEncoder.encode(randomString), new ArrayList<>());

		Mockito.when(udService.loadUserByUsername(randomString)).thenReturn(userT);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/authenticate").contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		Mockito.when(userService.getUserByUsername(randomString)).thenReturn(user);

		mockMvc.perform(MockMvcRequestBuilders.get("/product/name/" + name).header("Authorization", "Bearer " + jwt))
				.andExpect(status().isOk()).andDo(mvcResult -> {
					String jsonString = (mvcResult.getResponse().getContentAsString());
					int length = JsonPath.parse(jsonString).read("$.length()");
					Assertions.assertTrue(length > 1);
				});

	}

	@ParameterizedTest
	@ValueSource(strings = { "laptops", "Data Cards", "Routers" })
	public void testGetByCategory(String category) throws Exception {

		String userJson = mapper.writeValueAsString(user);

		org.springframework.security.core.userdetails.User userT = new org.springframework.security.core.userdetails.User(
				randomString, passwordEncoder.encode(randomString), new ArrayList<>());

		Mockito.when(udService.loadUserByUsername(randomString)).thenReturn(userT);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/authenticate").contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		Mockito.when(userService.getUserByUsername(randomString)).thenReturn(user);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/product/category/" + category).header("Authorization", "Bearer " + jwt))
				.andExpect(status().isOk()).andDo(mvcResult -> {
					String jsonString = (mvcResult.getResponse().getContentAsString());
					int length = JsonPath.parse(jsonString).read("$.length()");
					Assertions.assertTrue(length > 1);
				});

	}

}