package in.stackroute.cplayer.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.stackroute.cplayer.entity.User;
import in.stackroute.cplayer.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User createUser(User user) {
		String rawPassword = user.getPassword();
		String encodedPassword = passwordEncoder.encode(rawPassword);
		user.setPassword(encodedPassword);
		user.setCreatedAt(LocalDateTime.now());
		return userRepository.save(user);
	}

	@Override
	public User readUser(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public Boolean deleteUser(String username) {
		User u = userRepository.findByUsername(username);
		long userId = u.getUserId();
		Boolean response = Boolean.FALSE;
		try {
			userRepository.deleteById(userId);
			response = Boolean.TRUE;
		} catch (Exception e) {

		}
		return response;
	}

}
