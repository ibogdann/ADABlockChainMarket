package org.freesoftware.admin.soap.webservice;


import javax.jws.WebMethod;
import javax.jws.soap.SOAPBinding;

import org.freesoftware.admin.soap.webservice.product.Product;

@javax.jws.WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface WebService {

	@WebMethod
	public boolean registerUser(String name, String password);
	
	@WebMethod
	public boolean loginUser(String name, String password);
	
	@WebMethod
	public Product[] getAllProducts();
	
	@WebMethod
	public void addQuantity(long productId, int quantity);
	
	@WebMethod
	public void removeQuantity(long productId, int quantity);
	
	@WebMethod
	public void addProduct(String name, String category, int stock, float price, String country);
	
	@WebMethod
	public void removeProduct(long productId);
}
