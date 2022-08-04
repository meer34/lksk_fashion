package com.lksk.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lksk.web.model.TotalStock;

public interface TotalStockRepo extends JpaRepository<TotalStock, String>{

	List<TotalStock> findByItem(String item);
	
}
