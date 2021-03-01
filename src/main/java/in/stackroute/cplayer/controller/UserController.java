package in.stackroute.cplayer.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import in.stackroute.cplayer.entity.User;
import in.stackroute.cplayer.security.JwtUtil;
import in.stackroute.cplayer.service.FileStorageService;
import in.stackroute.cplayer.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping("/")
	public User getOwnData(@RequestHeader("Authorization") String authorizationHeader) {
		return userService.getUserByUsername(getUsername(authorizationHeader));
	}

	@PostMapping("/register")
	public User createUser(@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("file") MultipartFile file) {

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.setName(name);

		String fileName = fileStorageService.storeFile(file);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/images/")
				.path(fileName).toUriString();
		user.setImagePath(fileDownloadUri);

		User createdUser = userService.createUser(user);
		System.out.println(createdUser);
		return createdUser;
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

	@GetMapping("/images/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		System.out.println(fileName);
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			System.out.println("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	public String getUsername(String authorizationHeader) {
		return jwtUtil.extractUsername(authorizationHeader.substring(7));
	}

}
