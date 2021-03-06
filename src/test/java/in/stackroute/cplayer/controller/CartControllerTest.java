package in.stackroute.cplayer.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

import in.stackroute.cplayer.entity.Product;
import in.stackroute.cplayer.entity.User;
import in.stackroute.cplayer.security.MyUserDetailsService;
import in.stackroute.cplayer.service.CartService;
import in.stackroute.cplayer.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private CartService cartService;

	@MockBean
	private MyUserDetailsService udService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static long randomId;
	private static String randomString;
	private static User user;
	private static Product product;
	private static List<Product> cart;

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

		product = new Product();
		product.setPid(randomId + 1);
		product.setName("product_" + randomString);
		product.setCategory("category_" + randomString);

		cart = new ArrayList<Product>();
		cart.add(product);
		user.setCart(cart);

	}

	@BeforeEach
	public void insert() throws IOException, Exception {
		when(userService.createUser(user)).thenReturn(user);

		MockMultipartFile uploadFile = new MockMultipartFile("profile.png", "content".getBytes());

		mockMvc.perform(MockMvcRequestBuilders.multipart("/user/register").file("file", uploadFile.getBytes())
				.queryParam("username", randomString).queryParam("password", randomString)
				.queryParam("name", randomString).queryParam("email", randomString).characterEncoding("UTF-8"))
				.andExpect(status().isOk());

		Mockito.when(cartService.addToCart(randomString, product)).thenReturn(product);
	}

	@Test
	public void testGetCart() throws Exception {

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
		Mockito.when(cartService.getCart(randomString)).thenReturn(cart);

		mockMvc.perform(MockMvcRequestBuilders.get("/cart/").header("Authorization", "Bearer " + jwt))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].name", Matchers.equalTo("product_" + randomString)));
	}

	@Test
	public void testAddToCart() throws Exception {

		String userJson = mapper.writeValueAsString(user);

		org.springframework.security.core.userdetails.User userT = new org.springframework.security.core.userdetails.User(
				randomString, passwordEncoder.encode(randomString), new ArrayList<>());

		Mockito.when(udService.loadUserByUsername(randomString)).thenReturn(userT);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/authenticate").contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		Product p = new Product();
		p.setName("Test add");

		Mockito.when(cartService.addToCart(randomString, p)).thenReturn(p);

		cart.add(p);
		Mockito.when(userService.getUserByUsername(randomString)).thenReturn(user);
		Mockito.when(cartService.getCart(randomString)).thenReturn(cart);

		String productJson = mapper.writeValueAsString(p);

		mockMvc.perform(MockMvcRequestBuilders.post("/cart/").header("Authorization", "Bearer " + jwt)
				.contentType(MediaType.APPLICATION_JSON).content(productJson)).andExpect(status().isOk()).andDo(res -> {
					System.out.println(res.getResponse().getContentAsString());
				});
	}

	@Test
	public void testRemoveFromCart() throws Exception {

		String userJson = mapper.writeValueAsString(user);

		org.springframework.security.core.userdetails.User userT = new org.springframework.security.core.userdetails.User(
				randomString, passwordEncoder.encode(randomString), new ArrayList<>());

		Mockito.when(udService.loadUserByUsername(randomString)).thenReturn(userT);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/authenticate").contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		String productJson = mapper.writeValueAsString(product);
		Mockito.when(cartService.removeFromCart(randomString, product)).thenReturn(product);

		Mockito.when(userService.getUserByUsername(randomString)).thenReturn(user);
		Mockito.when(cartService.getCart(randomString)).thenReturn(cart);
		mockMvc.perform(MockMvcRequestBuilders.put("/cart/").header("Authorization", "Bearer " + jwt)
				.contentType(MediaType.APPLICATION_JSON).content(productJson)).andExpect(status().isOk()).andDo(res -> {
					System.out.println(res.getResponse().getContentAsString());
				});
	}

	@Test
	public void testEmptyCart() throws Exception {

		String userJson = mapper.writeValueAsString(user);

		org.springframework.security.core.userdetails.User userT = new org.springframework.security.core.userdetails.User(
				randomString, passwordEncoder.encode(randomString), new ArrayList<>());

		Mockito.when(udService.loadUserByUsername(randomString)).thenReturn(userT);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/authenticate").contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andExpect(jsonPath("$.jwt", Matchers.notNullValue())).andExpect(status().isOk()).andReturn();

		String jwt = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");

		String productJson = mapper.writeValueAsString(product);
		Mockito.when(userService.getUserByUsername(randomString)).thenReturn(user);
		Mockito.when(cartService.getCart(randomString)).thenReturn(cart);

		mockMvc.perform(MockMvcRequestBuilders.delete("/cart/").header("Authorization", "Bearer " + jwt)
				.contentType(MediaType.APPLICATION_JSON).content(productJson)).andExpect(status().isOk()).andDo(res -> {
					String response = (res.getResponse().getContentAsString());
					Assertions.assertTrue(response.contains("Cart emptied successfully."));
				});
	}
}