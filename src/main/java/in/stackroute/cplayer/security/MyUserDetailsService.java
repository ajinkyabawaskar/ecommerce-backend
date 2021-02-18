package in.stackroute.cplayer.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.stackroute.cplayer.service.UserService;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		in.stackroute.cplayer.entity.User fetchedUser = userService.getUserByUsername(username);
		User user = new User(username, fetchedUser.getPassword(), new ArrayList<>());
		return user;
	}

}
