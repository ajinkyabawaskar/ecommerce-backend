package in.stackroute.cplayer.service;

import in.stackroute.cplayer.entity.User;

public interface UserService {

	public User createUser(User user);

	public User readUser(Long userId);

	public User updateUser(User user);

	public Boolean deleteUser(Long userId);

	public User getUserByUsername(String username);
	
}
