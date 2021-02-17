package in.stackroute.projectone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.stackroute.projectone.entity.User;
import in.stackroute.projectone.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User createUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User readUser(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}

	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public Boolean deleteUser(Long userId) {
		Boolean response = Boolean.FALSE;
		try {
			userRepository.deleteById(userId);
			response = Boolean.TRUE;
		} catch (Exception e) {

		}
		return response;
	}

}
