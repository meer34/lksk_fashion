package com.lksk.web.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lksk.web.model.StockOut;

public interface StockOutRepo extends JpaRepository<StockOut, Long>{
	
	List<StockOut> findByDateBetween(Date  fromDate, Date  toDate);
	
}
