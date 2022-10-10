package com.lksk.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lksk.web.model.Product;
import com.lksk.web.repo.ProductRepo;

@Service
public class ProductService {

	@Autowired
	private ProductRepo productRepo;

	public Product saveProductToDB(Product product) {
		return productRepo.save(product);
	}

	public Product findProductById(Long name) {
		return productRepo.findById(name).get();
	}
	
	public List<Product> getAllProducts() {
		return productRepo.findAllByOrderByIdDesc();
	}

	public void deleteProductById(Long id) {
		productRepo.deleteById(id);
	}

	public List<Product> findProductsByNameContaining(String product) {
		return productRepo.findByNameContainingIgnoreCase(product);
		
	}

	public Product findProductByName(String productName) {
		return productRepo.findByName(productName);
	}

}
