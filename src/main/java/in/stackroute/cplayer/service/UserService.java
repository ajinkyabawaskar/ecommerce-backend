package in.stackroute.cplayer.service;

import java.util.List;

import in.stackroute.cplayer.entity.User;

public interface UserService {

	public List<User> getAllUsers();

	public User createUser(User user);

	public User readUser(Long userId);

	public User updateUser(User user);

	public Boolean deleteUser(Long userId);
}
