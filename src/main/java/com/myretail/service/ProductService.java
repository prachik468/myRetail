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
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Product getProduct(long id) {
		String name = this.getProductInfo(id);
		return new Product(id, name, getPrice(id));
	}

	private Price getPrice(long id) {
		return productRepository.findByProductId(id).getPrice();
	}

	private String getProductInfo(long id) throws ProductNotFoundException {
		// URL of external api
		StringBuffer URL = new StringBuffer("http://redsky.target.com/v2/pdp/tcin/");
		URL.append(id);
		URL.append(
				"?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics");

		String urlString = String.valueOf(URL);

		// Use RestTemplate to make the HTTP get request and JSONNode to read the name
		// from the retrieved response
		RestTemplate restTemplate = new RestTemplate();
		try {
			JsonNode root = restTemplate.getForObject(urlString, JsonNode.class);
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
