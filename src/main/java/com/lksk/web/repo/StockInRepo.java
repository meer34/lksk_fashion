package com.lksk.web.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.lksk.web.model.StockIn;

public interface StockInRepo extends JpaRepository<StockIn, Long>, JpaSpecificationExecutor<StockIn>{
	
	List<StockIn> findByDateBetween(Date  fromDate, Date  toDate);
	List<StockIn> findByDateGreaterThanEqual(Date  fromDate);
	List<StockIn> findByDateLessThanEqual(Date  toDate);
	
	List<StockIn> findByItemNameAndDateBetween(String item, Date fromDate, Date toDate);
	List<StockIn> findByItemNameAndDateLessThanEqual(String item, Date toDate);
	List<StockIn> findByItemNameAndDateGreaterThanEqual(String item, Date fromDate);
	List<StockIn> findByItemName(String item);
	
	StockIn findFirstByScanCodeOrderByIdDesc(String scanCode);
	
	@Query("FROM StockIn si where si.item = (FROM Item item where item.id = :itemId)")
	Page<StockIn> findByItemId(Long itemId, PageRequest of);
	
}
