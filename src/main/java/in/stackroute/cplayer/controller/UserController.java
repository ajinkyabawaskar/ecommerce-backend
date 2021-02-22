package in.stackroute.cplayer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping("/")
	public User getOwnData(@RequestHeader("Authorization") String authorizationHeader) {
		return userService.getUserByUsername(getUsername(authorizationHeader));
	}

	@PostMapping("/")
	public User createUser(@RequestBody User user) {
		return userService.createUser(user);
	}

	@PutMapping("/")
	public User updateUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody User user) {
		String username = getUsername(authorizationHeader);
		user.setUsername(username);
		return userService.updateUser(user);
	}

	@DeleteMapping("/")
	public Boolean deleteUser(@RequestHeader("Authorization") String authorizationHeader) {
		String username = getUsername(authorizationHeader);
		return userService.deleteUser(username);
	}

	public String getUsername(String authorizationHeader) {
		return jwtUtil.extractUsername(authorizationHeader.substring(7));
	}

}
