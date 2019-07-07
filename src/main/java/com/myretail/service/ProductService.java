package com.myretail.service;

import com.myretail.exception.ProductNotFoundException;
import com.myretail.model.Product;

public interface ProductService {
	
	Product getProduct(long id) throws ProductNotFoundException ;
	
	Product updatePrice(long id, Product product) throws ProductNotFoundException ;

}
