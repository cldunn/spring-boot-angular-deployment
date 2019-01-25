package com.cldbiz.userportal.unit.repository.data;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cldbiz.userportal.domain.Product;
import com.cldbiz.userportal.repository.product.ProductRepository;

@Component
public class ProductData {

	private static ProductRepository productRepository;

	@Autowired
	public ProductData(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	public static Product getAnotherProduct() {
		Product product = new Product();
		
		product.setUpc("71");
		product.setSku("91");
		product.setName("Note Cards");
		product.setPrice(5.42);
		product.setDescription("5X6 Note White Lined Cards");
		
		return product;
	}
	
	public static Product getExtraProduct() {
		Product product = new Product();
		
		product.setUpc("171");
		product.setSku("191");
		product.setName("Binders");
		product.setPrice(7.41);
		product.setDescription("Spiral Binder Note book");
		
		return product;
	}

	public static Product getAnotherExistingProduct() {
		Optional<Product> product = productRepository.findById(1L);
		return product.orElse(null);
	}

	public static Product getExtraExistingProduct() {
		Optional<Product> product = productRepository.findById(2L);
		return product.orElse(null);
	}
}
