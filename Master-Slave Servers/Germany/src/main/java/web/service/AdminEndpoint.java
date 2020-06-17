package web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import block.chain.market.products.Product;
import block.chain.market.products.ProductRepository;
import io.spring.guides.gs_producing_web_service.AddProduct;
import io.spring.guides.gs_producing_web_service.AddQuantity;
import io.spring.guides.gs_producing_web_service.GetAllProducts;
import io.spring.guides.gs_producing_web_service.GetAllProductsResponse;
import io.spring.guides.gs_producing_web_service.LoginUser;
import io.spring.guides.gs_producing_web_service.LoginUserResponse;
import io.spring.guides.gs_producing_web_service.RegisterUser;
import io.spring.guides.gs_producing_web_service.RegisterUserResponse;
import io.spring.guides.gs_producing_web_service.RemoveProduct;
import io.spring.guides.gs_producing_web_service.RemoveQuantity;

@Endpoint
public class AdminEndpoint {
	private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

	private ProductRepository productRepository;
	
	private List<WebUser> users = new ArrayList<>();

	@Autowired
	public AdminEndpoint(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "loginUser")
	@ResponsePayload
	public LoginUserResponse loginUser(@RequestPayload LoginUser request) {
		String username = request.getName();
		LoginUserResponse response = new LoginUserResponse();
		for(WebUser user : users)
		{
			if(user.getUsername().equals(username))
			{
				if(user.passwordMatch(request.getPassword())) {
					response.setLogin(true);
					return response;
				}
			}
		}
		response.setLogin(false);
		return response;
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "registerUser")
	@ResponsePayload
	public RegisterUserResponse registerUser(@RequestPayload RegisterUser request) {
		String username = request.getName();
		RegisterUserResponse response = new RegisterUserResponse();
		for(WebUser user :users)
		{
			if(user.getUsername().equals(username))
			{
				response.setRegister(false);
				return response;
			}
		}
		WebUser newUser = new WebUser(username, request.getPassword());
		users.add(newUser);
		response.setRegister(true);
		return response;
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllProducts")
	@ResponsePayload
	public GetAllProductsResponse getAllProducts(@RequestPayload GetAllProducts request) {
		GetAllProductsResponse response = new GetAllProductsResponse();
		List<Product> repoProducts = productRepository.findAll();
		io.spring.guides.gs_producing_web_service.Product webProduct;
		for(Product product : repoProducts)
		{
			webProduct = new io.spring.guides.gs_producing_web_service.Product();
			webProduct.setId(product.getId());
			webProduct.setName(product.getName());
			webProduct.setCategory(product.getCategory());
			webProduct.setPrice(product.getPrice());
			webProduct.setStock(product.getStock());
			webProduct.setCountry(product.getCountry());
			response.getProducts().add(webProduct);
		}
		return response;
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "addQuantity")
	@ResponsePayload
	public void addQuantity(@RequestPayload AddQuantity request) {
		Optional<Product> product = productRepository.findById(request.getId());
		if(product.isPresent())
		{
			Product p = product.get();
			p.setStock(p.getStock()+request.getQuantity());
			productRepository.save(p);
		}
		return;
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "removeQuantity")
	@ResponsePayload
	public void removeQuantity(@RequestPayload RemoveQuantity request) {
		Optional<Product> product = productRepository.findById(request.getId());
		if(product.isPresent())
		{
			Product p = product.get();
			p.setStock(p.getStock()-request.getQuantity());
			productRepository.save(p);
		}
		return;
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "addProduct")
	@ResponsePayload
	public void addProduct(@RequestPayload AddProduct request) {
		Product p = new Product();
		p.setName(request.getName());
		p.setCategory(request.getCategory());
		p.setPrice(request.getPrice());
		p.setStock(request.getStock());
		p.setCountry(request.getCountry());
		productRepository.save(p);
		return;
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "removeProduct")
	@ResponsePayload
	public void removeProduct(@RequestPayload RemoveProduct request) {
		productRepository.deleteById(request.getId());
		return;
	}
}
