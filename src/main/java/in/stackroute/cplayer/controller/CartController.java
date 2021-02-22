package in.stackroute.cplayer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.stackroute.cplayer.entity.Product;
import in.stackroute.cplayer.security.JwtUtil;
import in.stackroute.cplayer.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CartService productService;

	@PostMapping("/")
	public ResponseEntity<?> addToCart(@RequestBody Product product,
			@RequestHeader("Authorization") String authorizationHeader) {
		String username = getUsername(authorizationHeader);
		return new ResponseEntity<Product>(productService.addToCart(username, product), HttpStatus.OK);
	}

	@PutMapping("/")
	public ResponseEntity<?> removeFromCart(@RequestBody Product product,
			@RequestHeader("Authorization") String authorizationHeader) {
		String username = getUsername(authorizationHeader);
		return new ResponseEntity<Product>(productService.removeFromCart(username, product), HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<?> getCart(@RequestHeader("Authorization") String authorizationHeader) {
		String username = getUsername(authorizationHeader);
		List<Product> products = productService.getCart(username);
		return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
	}

	@DeleteMapping("/")
	public ResponseEntity<?> emptyCart(@RequestBody Product product,
			@RequestHeader("Authorization") String authorizationHeader) {
		ResponseEntity<String> response;
		try {
			String username = getUsername(authorizationHeader);
			productService.emptyCart(username);
			response = new ResponseEntity<String>("Cart emptied successfully.", HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<String>("Error occured while emptying the cart", HttpStatus.BAD_REQUEST);
		}
		return response;
	}

	public String getUsername(String authorizationHeader) {
		return jwtUtil.extractUsername(authorizationHeader.substring(7));
	}

}
