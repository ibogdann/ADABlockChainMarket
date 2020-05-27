package org.freesoftware.admin.soap.webservice.product;

import java.io.Serializable;

public class Product implements Serializable{

	private static final long serialVersionUID = -6736076415436587880L;
	private Long id;
	private String name;
	private String cat;
	private int stock;
	private float price;
	private String country;
	
	public Product(Long id, String name, String cat, int stock, float price, String country) {
		super();
		this.id = id;
		this.name = name;
		this.cat = cat;
		this.stock = stock;
		this.price = price;
		this.country = country;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCat() {
		return cat;
	}

	public int getStock() {
		return stock;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public float getPrice() {
		return price;
	}

	public String getCountry() {
		return country;
	}
	
}
