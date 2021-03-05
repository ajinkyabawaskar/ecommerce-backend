package in.stackroute.cplayer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import in.stackroute.cplayer.entity.Product;
import in.stackroute.cplayer.entity.User;
import in.stackroute.cplayer.repository.UserRepository;
import in.stackroute.cplayer.security.JwtUtil;

@SpringBootTest
class CartServiceImplTest {

	// @InjectMocks
	@Autowired
	private CartServiceImpl cartServiceImpl;

	@InjectMocks
	@Autowired
	private UserServiceImpl userServiceImpl;

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private User user;

	private Product product1;

	@BeforeEach
	public void setUp() {
		user = new User();
		user.setUserId(101L);
		user.setUsername("user");
		user.setPassword("pass");
		String rawPassword = user.getPassword();
		String encodedPassword = passwordEncoder.encode(rawPassword);
		user.setPassword(encodedPassword);

		product1 = new Product();
		product1.setPid(1L);
		product1.setName("Probook");
		product1.setCategory("Laptop");

		List<Product> cart = new ArrayList<Product>();

		cart.add(product1);

		user.setCart(cart);
	}

	@AfterEach
	public void tearDown() throws Exception {

	}

	@Test
	public void testAddToCartSuccess() {
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		Product cartEntity = cartServiceImpl.addToCart(user.getUsername(), product1);
		User userEntity = userServiceImpl.createUser(user);
		assertEquals(user, userEntity);
		Mockito.verify(userRepository, Mockito.times(2)).save(userEntity);
		// System.out.println(userEntity);
	}

	@Test
	public void testAddToCartFailure() {
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(null);
		Product cartEntity = cartServiceImpl.addToCart(user.getUsername(), product1);
		User userEntity = userServiceImpl.createUser(user);
		assertEquals(null, userEntity);
		Mockito.verify(userRepository, Mockito.times(2)).save(user);
		// System.out.println(userEntity);
	}

	@Test
	public void testremoveFromCartSuccess() {
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		Product cartEntity = user.getCart().get(0);
		User userEntity = userServiceImpl.createUser(user);
		System.out.println(userEntity);
		assertEquals(user, userEntity);
		Mockito.verify(userRepository, Mockito.times(1)).save(userEntity);
		// Mockito.when(cartServiceImpl)
		Product product = cartServiceImpl.removeFromCart(userEntity.getUsername(), cartEntity);
		System.out.println(userEntity.getCart());
		System.out.println(userEntity);
		assertEquals(new ArrayList<Product>(), userEntity.getCart());
	}

	@Test
	public void testremoveFromCartFailure() {
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		// Product cartEntity = cartServiceImpl.addToCart(user.getUsername(), product1);
		User userEntity = userServiceImpl.createUser(user);
		// System.out.println(userEntity);
		assertEquals(user, userEntity);
		Mockito.verify(userRepository, Mockito.times(1)).save(userEntity);
		Product product = cartServiceImpl.removeFromCart(userEntity.getUsername(), user.getCart().get(0));
		// System.out.println(userEntity.getCart());
		// System.out.println(userEntity);
		assertEquals(new ArrayList<Product>(), userEntity.getCart());
	}

	@Test
	public void testEmptyCartSuccess() {
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		Product cartEntity = cartServiceImpl.addToCart(user.getUsername(), product1);
		User userEntity = userServiceImpl.createUser(user);
		// System.out.println("empty cart success");
		// System.out.println(userEntity);
		assertEquals(user, userEntity);
		Mockito.verify(userRepository, Mockito.times(2)).save(userEntity);
		cartServiceImpl.emptyCart(userEntity.getUsername());
		// System.out.println(userEntity.getCart());
		// System.out.println(userEntity);
		assertEquals(new ArrayList<Product>(), userEntity.getCart());
	}

	@Test
	public void testEmptyCartFailure() {
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		Product cartEntity = cartServiceImpl.addToCart(user.getUsername(), product1);
		User userEntity = userServiceImpl.createUser(user);
		// System.out.println("empty cart failure");
		// System.out.println(userEntity);
		assertEquals(user, userEntity);
		Mockito.verify(userRepository, Mockito.times(2)).save(userEntity);
		// cartServiceImpl.emptyCart(userEntity.getUsername());
		// System.out.println(userEntity.getCart());
		// System.out.println(userEntity);
		assertEquals(user.getCart(), userEntity.getCart());
	}

	@Test
	public void testGetCartSuccess() {
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		Product cartEntity = cartServiceImpl.addToCart(user.getUsername(), product1);
		User userEntity = userServiceImpl.createUser(user);
		// System.out.println("get cart success");
		// System.out.println(userEntity);
		assertEquals(user, userEntity);
		Mockito.verify(userRepository, Mockito.times(2)).save(userEntity);
		List<Product> productList = cartServiceImpl.getCart(userEntity.getUsername());
		assertEquals(user.getCart(), productList);
	}

	@Test
	public void testGetCartFailure() {
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		Product cartEntity = cartServiceImpl.addToCart(user.getUsername(), product1);
		User userEntity = userServiceImpl.createUser(user);
		// System.out.println("get cart failure");
		// System.out.println(userEntity);
		assertEquals(user, userEntity);
		Mockito.verify(userRepository, Mockito.times(2)).save(userEntity);
		cartServiceImpl.emptyCart(userEntity.getUsername());
		List<Product> productList = cartServiceImpl.getCart(userEntity.getUsername());
		assertEquals(new ArrayList<Product>(), productList);
	}

}
