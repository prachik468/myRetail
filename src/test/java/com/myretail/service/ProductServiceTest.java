package com.myretail.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.myretail.model.Price;
import com.myretail.model.Product;
import com.myretail.repository.ProductRepository;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ProductService.class)
@WebMvcTest(value = ProductService.class)
public class ProductServiceTest {

	@InjectMocks
	private ProductServiceImpl productService;
	@Mock
	private ProductRepository productRepository;

	Long productID = (long) 13860428;
	Price priceInfo = new Price("13860428",13860428,19.99,"USD");
	Product mockProduct = new Product(productID, "The Big Lebowski (Blu-ray)", priceInfo);

	@Test
	public void getProductInfo() throws Exception {

		Price price = new Price();
		price.setCurrCode(priceInfo.getCurrCode());
		price.setValue(priceInfo.getValue());
		price.setId(productID);

		Product product = new Product();
		product.setProductId(mockProduct.getProductId());
		product.setPrice(price);
		Mockito.when(productRepository.findByProductId(productID)).thenReturn(product);
		Mockito.when(productService.getProduct(productID)).thenReturn(product);
		

		Product result = productService.getProduct(productID);
		assertEquals(result.getPrice().getCurrCode(), mockProduct.getPrice().getCurrCode());
		assertEquals(result.getPrice().getValue(), mockProduct.getPrice().getValue());
		assertEquals(result.getProductId(), mockProduct.getProductId());
		assertEquals(result.getName(), mockProduct.getName());

	}

}