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

	public List<Product> getAllProducts() {
		return productRepo.findAll();
	}

	public void deleteProductById(String id) {
		productRepo.deleteById(id);
	}

}
