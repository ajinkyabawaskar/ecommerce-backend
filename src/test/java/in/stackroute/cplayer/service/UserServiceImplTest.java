package in.stackroute.cplayer.service;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import in.stackroute.cplayer.ProjectOneApplication;
import in.stackroute.cplayer.entity.User;
import in.stackroute.cplayer.repository.UserRepository;
import in.stackroute.cplayer.security.JwtUtil;
import in.stackroute.cplayer.service.UserServiceImpl;


import org.junit.jupiter.api.Test;

@SpringBootTest
@RunWith(SpringRunner.class)
//@WebMvcTest
@ContextConfiguration(classes = {ProjectOneApplication.class})
class UserServiceImplTest {

	@InjectMocks
	@Autowired
	private UserServiceImpl userServiceImpl;

	@MockBean
	private UserRepository userRepository;	
	
//	@Autowired
//	private JwtUtil jwtUtil;	
//	@Autowired
//	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();	
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}

	private User user;

	@BeforeEach
	public void setUp() {
		user = new User();
		user.setUserId(101L);
		user.setUsername("user");
		user.setPassword("pass");
//		String rawPassword = user.getPassword();
//		String encodedPassword = passwordEncoder.encode(rawPassword);
	}

	@AfterEach
	public void tearDown() throws Exception {

	}

	@Test
	public void testCreateUserSuccess() {
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		User userEntity = userServiceImpl.createUser(user);
		assertEquals(user,userEntity);
		Mockito.verify(userRepository,Mockito.times(1)).save(user);
	}
	
	@Test
	public void testCreateUserFailure() {
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(null);
		User userEntity = userServiceImpl.createUser(user);
		assertEquals(null,userEntity);
		Mockito.verify(userRepository,Mockito.times(1)).save(user);
	}
	
	@Test
	public void testUpdateUserSuccess() {
		Mockito.when(userRepository.getOne(user.getUserId())).thenReturn(user);
		user.setEmail("user@gmail.com");
		Mockito.when(userRepository.save(user)).thenReturn(user);
		Mockito.when(userServiceImpl.updateUser(user)).thenReturn(user);
		User userEntity = userServiceImpl.updateUser(user);
		assertEquals("user@gmail.com",user.getEmail());
		Mockito.verify(userRepository,Mockito.times(1)).save(user);
		
	}
	
	@Test
	public void testUpdateUserFailure() {
		Mockito.when(userRepository.getOne(user.getUserId())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		User userEntity = userServiceImpl.updateUser(user);		
	}

	@Test
	public void testReadUserSuccess() {
		Mockito.when(userRepository.save(user)).thenReturn(user);
		Mockito.when(userServiceImpl.createUser(user)).thenReturn(user);
		User userEntity = userServiceImpl.readUser(user.getUsername());
		assertEquals(user,userEntity);
	}
	
	@Test
	public void testReadUserFailure() {
		Mockito.when(userRepository.save(user)).thenReturn(null);
		Mockito.when(userServiceImpl.createUser(user)).thenReturn(null);
		User userEntity = userServiceImpl.readUser(user.getUsername());
		assertEquals(null,userEntity);
	}
	
	@Test
	public void testDeleteUserSuccess() {
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		User userEntity = userServiceImpl.createUser(user);
		assertEquals(user,userEntity);
		Mockito.verify(userRepository,Mockito.times(1)).save(user);
		boolean status = userServiceImpl.deleteUser(user.getUsername());
		assertTrue(status);
	}
	
	@Test
	public void testDeleteUserFailure() {		
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(null);
		User userEntity = userServiceImpl.createUser(user);
		boolean status = userServiceImpl.deleteUser(user.getUsername());
		assertTrue(status);		
	}

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
