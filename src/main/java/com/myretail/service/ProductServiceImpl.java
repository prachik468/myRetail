package com.myretail.service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.myretail.exception.ProductNotFoundException;
import com.myretail.model.Price;
import com.myretail.model.Product;
import com.myretail.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private static final String HOST = "https://redsky.target.com/v2/pdp/tcin/{id}";

	@Autowired
	private ProductRepository productRepository;

	public Product getProduct(long id) {
		String name = this.getProductInfo(id);
		return new Product(id, name, getPrice(id));
	}

	private Price getPrice(long id) {
		Product product = productRepository.findByProductId(id);
		if (product == null) {
			throw new ProductNotFoundException();
		}
		return product.getPrice();
	}

	private String getProductInfo(long id) {
		RestTemplate restTemplate = new RestTemplate();

		Map<String, String> params = new HashMap<String, String>();
		params.put("id", Long.toString(id));
		URI uri = UriComponentsBuilder.fromUriString(HOST).buildAndExpand(params).toUri();
		uri = UriComponentsBuilder.fromUri(uri).queryParam("excludes",
				"taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics")
				.build().toUri();
		try {
			JsonNode apiResponse = restTemplate.getForObject(uri, JsonNode.class);

			return apiResponse.at("/product/item/product_description/title").asText();
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

