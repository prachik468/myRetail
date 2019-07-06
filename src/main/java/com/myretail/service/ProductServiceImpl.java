package com.myretail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.myretail.exception.ProductNotFoundException;
import com.myretail.model.Price;
import com.myretail.model.Product;
import com.myretail.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private static final String EXCLUDE_PATH = "?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";
	private static final String HOST = "http://redsky.target.com/v2/pdp/tcin/";
	
	@Autowired
	private ProductRepository productRepository;

	public Product getProduct(long id) {
		String name = this.getProductInfo(id);
		return new Product(id, name, getPrice(id));
	}

	private Price getPrice(long id) {
		return productRepository.findByProductId(id).getPrice();
	}

	private String getProductInfo(long id) {
		RestTemplate restTemplate = new RestTemplate();

		StringBuffer URL = new StringBuffer(HOST);
		URL.append(id);
		URL.append(EXCLUDE_PATH);
		try {
			JsonNode root = restTemplate.getForObject(URL.toString(), JsonNode.class);
			String name = root.at("/product/item/product_description/title").asText();
			return name;

		} catch (Exception e) {
			throw new ProductNotFoundException();
		}
	}

	public Product updatePrice(long id, Product product) {
		Product productFromDB = productRepository.findByProductId(id);
		if (productFromDB == null) {
			throw new ProductNotFoundException();
		}
		productFromDB.setPrice(product.getPrice());
		return productRepository.save(productFromDB);
	}
}
