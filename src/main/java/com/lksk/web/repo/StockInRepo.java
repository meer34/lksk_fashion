package com.lksk.web.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lksk.web.model.StockIn;

public interface StockInRepo extends JpaRepository<StockIn, Long>{
	
	List<StockIn> findByDateBetween(Date  fromDate, Date  toDate);
	List<StockIn> findByDateGreaterThanEqual(Date  fromDate);
	List<StockIn> findByDateLessThanEqual(Date  toDate);
	
	List<StockIn> findByItemNameAndDateBetween(String item, Date fromDate, Date toDate);
	List<StockIn> findByItemNameAndDateLessThanEqual(String item, Date toDate);
	List<StockIn> findByItemNameAndDateGreaterThanEqual(String item, Date fromDate);
	List<StockIn> findByItemName(String item);
	
	List<StockIn> findAllByOrderByIdDesc();
	
	StockIn findFirstByScanCodeOrderByIdDesc(String scanCode);
	
}
