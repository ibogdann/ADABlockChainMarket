package org.freesoftware.admin.soap.webservice.data.impl;

import java.util.ArrayList;
import java.util.List;

import org.freesoftware.admin.soap.webservice.data.WebServiceDataProvider;
import org.freesoftware.admin.soap.webservice.product.Product;
import org.freesoftware.admin.soap.webservice.user.User;

public class WebServiceDataProviderImpl implements WebServiceDataProvider{
	
	private List<User> users = new ArrayList<>();
	private List<Product> products = new ArrayList<>();
	
	@Override
	public boolean registerUser(String name, String password) {
		System.out.println("Register: "+ name + " " + password);
		for(User user :users)
		{
			if(user.getUsername().equals(name))
			{
				return false;
			}
		}
		User newUser = new User(name, password);
		users.add(newUser);
		return true;
	}

	@Override
	public boolean loginUser(String name, String password) {
		System.out.println("Login attempt: "+name);
		for(User user : users)
		{
			if(user.getUsername().equals(name))
			{
				if(user.passwordMatch(password)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public List<Product> getAllProducts() {
		return products;
	}

	@Override
	public void addQuantity(long productId, int quantity) {
		for(Product p : products)
		{
			if(p.getId() == productId)
			{
				p.setStock(p.getStock()+quantity);
				return;
			}
		}
	}

	@Override
	public void removeQuantity(long productId, int quantity) {
		for(Product p : products)
		{
			if(p.getId() == productId)
			{
				p.setStock(p.getStock()-quantity);
				return;
			}
		}
	}
	
	@Override
	public void addProduct(String name, String category, int stock, float price, String country)
	{
		long l = products.size();
		Product product = new Product(l, name, category, stock, price, country);
		products.add(product);
	}
	

	@Override
	public void removeProduct(long productId) {
		for(Product p : products)
		{
			if(p.getId() == productId)
			{
				products.remove(p);
				return;
			}
		}
	}

}
