package com.myretail.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.myretail.model.Price;
import com.myretail.model.Product;
import com.myretail.repository.ProductRepository;


@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = ProductRepository.class)
public class Application  implements CommandLineRunner {

	@Autowired
	private ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// pre-populate some price data in data store(mongodb)
	@Override
	public void run(String... args) throws Exception {
		Price price = new Price();
		price.setId(13860428);
		price.setCurrCode("USD");
		price.setValue(13.49);
		Product product = new Product();
		product.setProductId(13860428);
		product.setName("he Big Lebowski (Blu-ray) (Widescreen");
		product.setPrice(price);
		productRepository.save(product);
	}
	
}
