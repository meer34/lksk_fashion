package com.lksk.web.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.lksk.web.model.StockOut;

public interface StockOutRepo extends JpaRepository<StockOut, Long>, JpaSpecificationExecutor<StockOut>{
	
	List<StockOut> findByDateBetween(Date  fromDate, Date  toDate);
	List<StockOut> findByDateGreaterThanEqual(Date  fromDate);
	List<StockOut> findByDateLessThanEqual(Date  toDate);

	List<StockOut> findByItemNameAndDateBetween(String item, Date fromDate, Date toDate);
	List<StockOut> findByItemNameAndDateLessThanEqual(String item, Date toDate);
	List<StockOut> findByItemNameAndDateGreaterThanEqual(String item, Date fromDate);
	List<StockOut> findByItemName(String item);
	
	@Query("FROM StockOut so where so.item = (FROM Item item where item.id = :itemId)")
	Page<StockOut> findByItemId(Long itemId, PageRequest of);
	
}
