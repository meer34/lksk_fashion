package com.lksk.web.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lksk.web.model.StockIn;

public interface StockInRepo extends JpaRepository<StockIn, Long>{
	
	List<StockIn> findByDateBetween(Date  fromDate, Date  toDate);
	
}
