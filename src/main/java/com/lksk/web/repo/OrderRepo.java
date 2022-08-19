package com.lksk.web.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lksk.web.model.CustOrder;

public interface OrderRepo extends JpaRepository<CustOrder, Long>{
	
	List<CustOrder> findByDateBetween(Date  fromDate, Date  toDate);
	List<CustOrder> findByDateGreaterThanEqual(Date  fromDate);
	List<CustOrder> findByDateLessThanEqual(Date  toDate);
	
	List<CustOrder> findAllByOrderByIdDesc();
	
}
