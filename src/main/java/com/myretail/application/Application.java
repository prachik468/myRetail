package com.myretail.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.myretail.model.Price;
import com.myretail.model.Product;
import com.myretail.repository.ProductRepository;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = "com.*")
@EnableMongoRepositories(basePackageClasses = ProductRepository.class)
@EnableSwagger2
public class Application implements CommandLineRunner {

	@Autowired
	private ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	
	// pre-populate some price data in data store(mongodb)
	@Override
	public void run(String... args) throws Exception {
		Price price = new Price(13860428, 14.49, "USD");
		Product product = new Product(13860428, "The Big Lebowski (Blu-ray) (Widescreen)", price);
		productRepository.save(product);
	}
}
