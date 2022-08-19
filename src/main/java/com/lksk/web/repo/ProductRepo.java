package com.lksk.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lksk.web.model.Product;

public interface ProductRepo extends JpaRepository<Product, Long>{

	List<Product> findByNameContainingIgnoreCase(String product);

	List<Product> findAllByOrderByIdDesc();

	Product findByName(String productName);
	
}
