package in.stackroute.cplayer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.stackroute.cplayer.entity.User;
import in.stackroute.cplayer.security.JwtUtil;
import in.stackroute.cplayer.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/")
	public User getOwnData(@RequestHeader("Authorization") String authorizationHeader) {
		return userService.getUserByUsername(getUsername(authorizationHeader));
	}

	@PostMapping("/register")
	public User createUser(@RequestBody User user) {
		String rawPassword = user.getPassword();
		String encodedPassword = passwordEncoder.encode(rawPassword);
		user.setPassword(encodedPassword);
		return userService.createUser(user);
	}

	@PutMapping("/{userId}")
	public User updateUser(@PathVariable Long userId, @RequestBody User user) {
		user.setUserId(userId);
		return userService.updateUser(user);
	}

	@DeleteMapping("/{userId}")
	public Boolean deleteUser(@PathVariable Long userId) {
		return userService.deleteUser(userId);
	}

	public String getUsername(String authorizationHeader) {
		return jwtUtil.extractUsername(authorizationHeader.substring(7));
	}

}
