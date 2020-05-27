package org.freesoftware.admin.soap.webservice.data;

import java.util.List;

import org.freesoftware.admin.soap.webservice.product.Product;

public interface WebServiceDataProvider {
	
	public boolean registerUser(String name, String password);
	
	public boolean loginUser(String name, String password);
	
	public List<Product> getAllProducts();
	
	public void addQuantity(long productId, int quantity);
	
	public void removeQuantity(long productId, int quantity);

	public void addProduct(String name, String category, int stock, float price, String country);
	
	public void removeProduct(long productId);
	
}
