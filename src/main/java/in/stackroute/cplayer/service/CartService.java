package in.stackroute.cplayer.service;

import java.util.List;

import in.stackroute.cplayer.entity.Product;

public interface CartService {

	Product addToCart(String username, Product product);

	Product removeFromCart(String username, Product product);

	void emptyCart(String username);

	List<Product> getCart(String username);

}
