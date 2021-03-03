package in.stackroute.cplayer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.stackroute.cplayer.entity.Product;
import in.stackroute.cplayer.entity.User;
import in.stackroute.cplayer.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Product addToCart(String username, Product product) {
		User user = userRepository.findByUsername(username);
		List<Product> cart = new ArrayList<Product>(user.getCart());
		cart.add(product);
		user.setCart(cart);
		userRepository.save(user);
		return product;
	}

	@Override
	public Product removeFromCart(String username, Product product) {
		User user = userRepository.findByUsername(username);
		List<Product> cart = new ArrayList<Product>(user.getCart());
		for (Product cartProduct : cart) {
			long given = cartProduct.getPid();
			long wanted = product.getPid();
			if (wanted == given) {
				cart.remove(cartProduct);
				user.setCart(cart);
				userRepository.save(user);
				break;
			}
		}
		return product;
	}

	@Override
	public void emptyCart(String username) {
		User user = userRepository.findByUsername(username);
		user.setCart(new ArrayList<Product>());
	}

	@Override
	public List<Product> getCart(String username) {
		User user = userRepository.findByUsername(username);
		return user.getCart();
	}

}
