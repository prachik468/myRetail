package com.myretail.service;

import com.myretail.exception.ProductNotFoundException;
import com.myretail.model.Product;

public interface ProductService {
	
	public Product getProduct(long id) throws ProductNotFoundException ;
	
	public Product updatePrice(long id, Product product) throws ProductNotFoundException ;

}
