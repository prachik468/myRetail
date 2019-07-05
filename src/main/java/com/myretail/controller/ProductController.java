package com.myretail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myretail.exception.ProductMisMatchException;
import com.myretail.exception.ProductNotFoundException;
import com.myretail.model.Product;
import com.myretail.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "My Retail API")
@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@ApiOperation(value = "Retrieve product from catalog", nickname = "getProduct", response = Product.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved product from catalog"),
			@ApiResponse(code = 404, message = "Unable to find product within catalog"),
			@ApiResponse(code = 500, message = "There has been an unexpected error") })
	@GetMapping("/{id}")
	public Product getById(@PathVariable long id) throws ProductNotFoundException {
		return productService.getProduct(id);
	}

	@ApiOperation(value = "Update product from catalog", nickname = "updateProduct", response = Product.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved product from catalog"),
			@ApiResponse(code = 404, message = "Unable to find product within catalog"),
			@ApiResponse(code = 500, message = "There has been an unexpected error") })
	@PutMapping("/{id}")
	public Product update(@PathVariable("id") long productId, @RequestBody Product product)
			throws ProductMisMatchException {
		if (product.getProductId() != productId) {
			throw new ProductMisMatchException();
		}

		try {
			return productService.updatePrice(productId, product);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
