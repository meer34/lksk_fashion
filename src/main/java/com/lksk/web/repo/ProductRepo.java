package com.lksk.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lksk.web.model.Product;

public interface ProductRepo extends JpaRepository<Product, String>{
	
}
