package com.lksk.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lksk.web.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long>{
	
}
