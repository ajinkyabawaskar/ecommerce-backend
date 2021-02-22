package in.stackroute.cplayer.service;

import in.stackroute.cplayer.entity.User;

public interface UserService {

	public User createUser(User user);

	public User readUser(String username);

	public User updateUser(User user);

	public Boolean deleteUser(String username);

	public User getUserByUsername(String username);

}
