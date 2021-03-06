package in.stackroute.cplayer.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
public class UserControllerTest {

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
	public void testGetOwnData() throws Exception {

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

		mockMvc.perform(MockMvcRequestBuilders.get("/user/").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isOk()).andExpect(jsonPath("$.username", Matchers.equalTo(randomString)));

	}

	@Test
	public void testUpdateUser() throws Exception {

		String userJson = mapper.writeValueAsString(user);

		org.springframework.security.core.userdetails.User userT = new org.springframework.security.core.userdetails.User(
				randomString, passwordEncoder.encode(randomString), new ArrayList<>());

		Mockito.when(udService.loadUserByUsername(randomString)).thenReturn(userT);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/authenticate").contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		user.setName("Updated Name");
		userJson = mapper.writeValueAsString(user);
		Mockito.when(userService.getUserByUsername(randomString)).thenReturn(user);
		Mockito.when(userService.updateUser(user)).thenReturn(user);
		Mockito.when(userService.getUserByUsername(randomString)).thenReturn(user);
		result = mockMvc
				.perform(MockMvcRequestBuilders.put("/user/").header("Authorization", "Bearer " + jwt)
						.contentType(MediaType.APPLICATION_JSON).content(userJson))
				.andExpect(status().isOk()).andDo(print()).andReturn();

	}

	@Test
	public void testDeleteUser() throws Exception {
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
		Mockito.when(userService.deleteUser(randomString)).thenReturn(true);

		mockMvc.perform(MockMvcRequestBuilders.delete("/user/").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isOk()).andDo(mvcResult -> {
					Assertions.assertEquals("true", mvcResult.getResponse().getContentAsString());
				});
	}
}