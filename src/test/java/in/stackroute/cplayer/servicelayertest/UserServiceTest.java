package in.stackroute.cplayer.servicelayertest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import in.stackroute.cplayer.entity.User;
import in.stackroute.cplayer.repository.UserRepository;
import in.stackroute.cplayer.security.JwtUtil;
import in.stackroute.cplayer.service.UserServiceImpl;

@SpringBootTest
public class UserServiceTest {

	@Test
	public void contextLoads() {
	}

	@InjectMocks
	@Autowired
	private UserServiceImpl userServiceImpl;

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private User user;

	@BeforeEach
	public void setUp() {
		user = new User();
		user.setUserId(101L);
		user.setUsername("user");
		user.setPassword("pass");
	}

	@AfterEach
	public void tearDown() throws Exception {

	}

	@Test
	public void testCreateUserSuccess() {
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		User userEntity = userServiceImpl.createUser(user);
		Assertions.assertEquals(user, userEntity);
		Mockito.verify(userRepository, Mockito.times(1)).save(user);
	}

	@Test
	public void testCreateUserFailure() {
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(null);
		User userEntity = userServiceImpl.createUser(user);
		Assertions.assertEquals(null, userEntity);
		Mockito.verify(userRepository, Mockito.times(1)).findByUsername(user.getUsername());
	}

	@Test
	public void testUpdateUserSuccess() {
		Mockito.when(userRepository.getOne(user.getUserId())).thenReturn(user);
		user.setEmail("user@gmail.com");
		Mockito.when(userRepository.save(user)).thenReturn(user);
		User userEntity = userServiceImpl.updateUser(user);
		Assertions.assertEquals("user@gmail.com", user.getEmail());
		Mockito.verify(userRepository, Mockito.times(1)).getOne(user.getUserId());
		Mockito.verify(userRepository, Mockito.times(1)).save(user);

	}

	@Test
	public void testUpdateUserFailure() {
		Mockito.when(userRepository.getOne(user.getUserId())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		User userEntity = userServiceImpl.updateUser(user);
	}

	@Test
	public void testReadUserSuccess() {
		Mockito.when(userRepository.getOne(user.getUserId())).thenReturn(user);
		User userEntity = userServiceImpl.readUser(user.getUsername());
		Assertions.assertEquals(user, userEntity);
	}

	@Test
	public void testReadUserFailure() {
		Mockito.when(userRepository.getOne(null)).thenReturn(null);
	}

//	@Test
//	public void testGetUserByUsername(){
//		Mockito.when(userRepository.findByUsername(user.getUsername()));
//		assertEquals(user,userServiceImpl.getUserByUsername("user"));
//	}
//	@Test
//	public void testCreateUser()  {
//		Mockito.when(userRepository.getOne(user.getUserId())).thenReturn(null);
//		Mockito.when(userRepository.save(user)).thenReturn(user);
//		User status = userServiceImpl.createUser(user);
//		assertEquals(user, status);
//		Mockito.verify(userRepository, Mockito.times(1)).save(user);		
//	}
//
//	@Test
//	public void testUpdateUser() {
//		//		userServiceImpl.saveUser(user);
//		Mockito.when(userRepository.getOne(user.getUserId())).thenReturn(user);
//		user.setUsername("deepika123");
//		Mockito.when(userRepository.save(user)).thenReturn(user);
//		User updatedUser = userServiceImpl.updateUser(user);
//		assertEquals(user , updatedUser);
//		//		verify(userRepository, times(1)).getUserByUserId(user.getUserId());
//		//		verify(userRepository, times(1)).save(user);
//
//	}

}
