package com.myretail.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myretail.model.Product;

public interface ProductRepository extends MongoRepository<Product, Double> {
	Product findByProductId(long productId);
}