package in.stackroute.projectone.service;

import java.util.List;

import in.stackroute.projectone.entity.User;

public interface UserService {

	public List<User> getAllUsers();

	public User createUser(User user);

	public User readUser(Long userId);

	public User updateUser(User user);

	public Boolean deleteUser(Long userId);
}
