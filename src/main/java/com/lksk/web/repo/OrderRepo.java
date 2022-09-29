package com.lksk.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.lksk.web.model.CustOrder;

public interface OrderRepo extends JpaRepository<CustOrder, Long>, JpaSpecificationExecutor<CustOrder>{
	
	List<CustOrder> findAllByOrderByIdDesc();
	
}
