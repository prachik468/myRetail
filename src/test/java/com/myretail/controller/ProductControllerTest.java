package com.myretail.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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

import com.myretail.exception.ProductMisMatchException;
import com.myretail.exception.ProductNotFoundException;
import com.myretail.model.Price;
import com.myretail.model.Product;
import com.myretail.service.ProductService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ProductController.class)
@WebMvcTest(value = ProductController.class, secure = false)
public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	@Qualifier(value = "productService")
	private ProductService productService;

	private long ITEM_ID = 13860428;

	Product product = null;
	Price price = null;

	@Before
	public void setup() {
		price.setId(ITEM_ID);
		price.setCurrCode("USD");
		price.setValue(33.33);
		product = new Product(ITEM_ID, "The Big Lebowski (Blu-ray)", price);
	}

	@Test
	@Ignore
	public void testGetRequest() throws Exception {
		Mockito.when(productService.getProduct(ITEM_ID)).thenReturn(product);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/" + ITEM_ID).accept(MediaType.APPLICATION_JSON);
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		String expected = "{\"productId\":13860428,\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":33.33,\"currency_code\":\"USD\"}}";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	@Ignore
	public void testGetProductNotFound() throws Exception {
		Mockito.when(productService.getProduct(888)).thenThrow(ProductNotFoundException.class);
		mockMvc.perform(get("/products/888")).andExpect(status().isNotFound())
				.andExpect(content().json("{message:'product not found'}"));

	}

	@Test(expected = ProductMisMatchException.class)
	@Ignore
	public void testBadGetRequest() throws Exception, ProductMisMatchException {
		String var = "XYZ";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/product/" + var)
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
		throw result.getResolvedException();
	}

	@Test
	@Ignore
	public void testPutRequest() throws Exception {
		this.product.getPrice().setValue(19.99);
		Mockito.when(productService.updatePrice(ITEM_ID, product)).thenReturn(product);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/products/" + ITEM_ID)
				.accept(MediaType.APPLICATION_JSON)
				.content("{\"productId\":" + ITEM_ID
						+ ",\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":19.99,\"currency_code\":\"USD\"}}")
				.contentType(MediaType.APPLICATION_JSON_VALUE);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}
}
