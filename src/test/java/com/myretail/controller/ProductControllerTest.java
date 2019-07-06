package com.myretail.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.myretail.exception.ProductMisMatchException;
import com.myretail.exception.ProductNotFoundException;
import com.myretail.model.Price;
import com.myretail.model.Product;
import com.myretail.service.ProductService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ProductController.class)
@WebMvcTest(value = ProductController.class)
public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	@Qualifier(value = "productService")
	private ProductService productService;

	private static final String CONTEXT_ROOT = "/products/";

	private static final String PRODUCT_ID = "{\"productId\":";

	private static final String CONTENT = ",\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":19.99,\"currency_code\":\"USD\"}}";

	private long ITEM_ID = 13860428;

	private long INVALID_ITEM_ID = 888;

	private String INVALID_ARGUMENT = "XYZ";

	Product product = null;
	Price price = new Price();

	@Before
	public void setup() {
		price.setId(ITEM_ID);
		price.setCurrCode("USD");
		price.setValue(33.33);
		product = new Product(ITEM_ID, "The Big Lebowski (Blu-ray)", price);
	}

	@Test

	public void testGetRequest() throws Exception {
		Mockito.when(productService.getProduct(ITEM_ID)).thenReturn(product);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CONTEXT_ROOT + ITEM_ID)
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		String expected = "{\"productId\":13860428,\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":33.33,\"currency_code\":\"USD\"}}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test(expected = ProductNotFoundException.class)
	public void testGetProductNotFound() throws Exception {
		Mockito.when(productService.getProduct(INVALID_ITEM_ID)).thenThrow(ProductNotFoundException.class);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CONTEXT_ROOT + INVALID_ITEM_ID)
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
		throw result.getResolvedException();
	}

	@Test(expected = MethodArgumentTypeMismatchException.class)
	public void testInvalidGetRequest() throws Exception, MethodArgumentTypeMismatchException {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CONTEXT_ROOT + INVALID_ARGUMENT)
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
		throw result.getResolvedException();
	}

	@Test(expected = ProductMisMatchException.class)
	public void testProductMismatch() throws Exception, ProductMisMatchException {
		this.product.getPrice().setValue(19.99);
		Mockito.when(productService.updatePrice(ITEM_ID, product)).thenReturn(product);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(CONTEXT_ROOT + INVALID_ITEM_ID)
				.accept(MediaType.APPLICATION_JSON).content(PRODUCT_ID + ITEM_ID + CONTENT)
				.contentType(MediaType.APPLICATION_JSON_VALUE);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
		throw result.getResolvedException();
	}

	@Test

	public void testPutRequest() throws Exception {
		this.product.getPrice().setValue(19.99);
		Mockito.when(productService.updatePrice(ITEM_ID, product)).thenReturn(product);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(CONTEXT_ROOT + ITEM_ID)
				.accept(MediaType.APPLICATION_JSON).content(PRODUCT_ID + ITEM_ID + CONTENT)
				.contentType(MediaType.APPLICATION_JSON_VALUE);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}
}
