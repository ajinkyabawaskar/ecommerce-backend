package in.stackroute.cplayer.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import in.stackroute.cplayer.entity.Product;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

	@Autowired
	private WebClient.Builder webClientBuilder;

	private HashMap<Long, Product> cachedProducts = new HashMap<Long, Product>();

	private final String M_PROTOCOL = "https";
	private final String M_HOST = "mock-products-api.herokuapp.com";
	private final String M_PRODUCT_BY_ID = "id";
	private final String M_PRODUCT_BY_NAME = "name";
	private final String M_PRODUCT_BY_CATEGORY = "category";
	private final String M_PRODUCT_ALL = "all";
	private final String M_CATEGORY_ALL = "category";

	@Bean
	public WebClient.Builder getWebClientBuilder() {
		return WebClient.builder();
	}

	@GetMapping("/id/{pid}")
	public ResponseEntity<?> getProductById(@PathVariable("pid") long productId) {

		Product product;

		// If product already exists in cache, then serve from cache
		if (cachedProducts.containsKey(productId)) {
			product = cachedProducts.get(productId);
		}

		// If product is not present in cache, then get the product from API and save it
		// to cache
		else {
			product = webClientBuilder
					.build().get().uri(builder -> builder.scheme(M_PROTOCOL).host(M_HOST)
							.path(M_PRODUCT_BY_ID + "/{productId}").build(productId))
					.retrieve().bodyToMono(Product.class).block();

			// to avoid cache overflowing memory, limit the number of products cached
			if (cachedProducts.size() < 100) {
				cachedProducts.put(productId, product);
			}
		}

		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<?> getProductsByName(@PathVariable("name") String name) {
		Product[] products;
		products = webClientBuilder.build().get()
				.uri(builder -> builder.scheme(M_PROTOCOL).host(M_HOST).path(M_PRODUCT_BY_NAME + "/{name}").build(name))
				.retrieve().bodyToMono(Product[].class).block();
		return new ResponseEntity<Product[]>(products, HttpStatus.OK);
	}

	@GetMapping("/category/{category}")
	public ResponseEntity<?> getProductsByCategory(@PathVariable("category") String category) {
		Product[] products;
		products = webClientBuilder
				.build().get().uri(builder -> builder.scheme(M_PROTOCOL).host(M_HOST)
						.path(M_PRODUCT_BY_CATEGORY + "/{category}").build(category))
				.retrieve().bodyToMono(Product[].class).block();
		return new ResponseEntity<Product[]>(products, HttpStatus.OK);
	}

	@GetMapping("/all/")
	public ResponseEntity<?> getAllProducts() {
		Product[] products;
		products = webClientBuilder.build().get()
				.uri(builder -> builder.scheme(M_PROTOCOL).host(M_HOST).path(M_PRODUCT_ALL).build()).retrieve()
				.bodyToMono(Product[].class).block();
		return new ResponseEntity<Product[]>(products, HttpStatus.OK);
	}

	@GetMapping("/category/")
	public ResponseEntity<?> getAllCategories() {
		String[] categories;
		categories = webClientBuilder.build().get()
				.uri(builder -> builder.scheme(M_PROTOCOL).host(M_HOST).path(M_CATEGORY_ALL).build()).retrieve()
				.bodyToMono(String[].class).block();
		return new ResponseEntity<String[]>(categories, HttpStatus.OK);
	}

}
