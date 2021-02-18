package in.stackroute.cplayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import in.stackroute.cplayer.entity.User;
import in.stackroute.cplayer.repository.UserRepository;

@Component
public class DataLoader implements ApplicationRunner {

	@Value("${spring.security.user.name}")
	private String defaultUsername;

	@Value("${spring.security.user.password}")
	private String defaultPassword;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void run(ApplicationArguments args) {
		User fetchedUser = userRepository.findByUsername(defaultUsername);
		if (fetchedUser == null) {
			String password = passwordEncoder.encode(defaultPassword);
			User user = new User();
			user.setUsername(defaultUsername);
			user.setPassword(password);
			userRepository.save(user);
		}
	}
}