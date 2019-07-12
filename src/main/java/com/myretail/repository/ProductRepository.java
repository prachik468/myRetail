package com.myretail.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myretail.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, Long> {
	Product findByProductId(long productId);
}
