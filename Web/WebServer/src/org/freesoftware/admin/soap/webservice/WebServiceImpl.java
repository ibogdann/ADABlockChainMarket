package org.freesoftware.admin.soap.webservice;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.freesoftware.admin.soap.webservice.data.WebServiceDataProvider;
import org.freesoftware.admin.soap.webservice.product.Product;

@WebService(endpointInterface = "org.freesoftware.admin.soap.webservice.WebService")  
public class WebServiceImpl implements org.freesoftware.admin.soap.webservice.WebService {

	private WebServiceDataProvider dataProvider;
	
	public WebServiceImpl(WebServiceDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}
	
	@WebMethod
	public boolean registerUser(String name, String password) {
		return dataProvider.registerUser(name, password);
	}
	
	@WebMethod
	public boolean loginUser(String name, String password) {
		return dataProvider.loginUser(name, password);
	}
	
	@WebMethod
	public Product[] getAllProducts() {
		List<Product> products = dataProvider.getAllProducts();
		Product[] prdcts = new Product[products.size()+1];
		int i=0;
		for(Product product : products)
		{
			prdcts[i]=product;
			i++;
		}
		return prdcts;
	}
	
	@WebMethod
	public void addQuantity(long productId, int quantity)
	{
		dataProvider.addQuantity(productId, quantity);
	}
	
	@WebMethod
	public void removeQuantity(long productId, int quantity)
	{
		dataProvider.removeQuantity(productId, quantity);
	}
	
	@WebMethod
	public void addProduct(String name, String category, int stock, float price, String country)
	{
		dataProvider.addProduct(name, category, stock, price, country);
	}
	
	@WebMethod
	public void removeProduct(long productId)
	{
		dataProvider.removeProduct(productId);
	}
	
}
