package com.myretail.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {

	public long productId;

	public String name;

	@JsonProperty("current_price")
	private Price price;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public Product() {
	}

	public Product(long productId, String name, Price price) {
		this.productId = productId;
		this.name = name;
		this.price = price;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", name=" + name + ", price=" + price + "]";
	}

}
