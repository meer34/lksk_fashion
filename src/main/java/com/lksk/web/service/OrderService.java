package com.lksk.web.service;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lksk.web.model.CustOrder;
import com.lksk.web.repo.OrderRepo;
import com.lksk.web.specification.CustOrderSearchSpecification;
import com.lksk.web.util.SearchSpecificationBuilder;

@Service
public class OrderService {

	@Autowired
	private OrderRepo orderRepo;

	public CustOrder saveOrderToDB(CustOrder order) {
		return orderRepo.save(order);
	}
	
	public CustOrder findOrderById(Long id) {
		return orderRepo.findById(id).get();
	}

	public Page<CustOrder> getAllOrders(int pageNo, int pageSize) {
		return orderRepo.findAll(PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id")));
	}

	public Page<CustOrder> searchOrdersByDateAndKeyword(String keyword, 
			String fromDate, String toDate, int pageNo, Integer pageSize) throws ParseException {

		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
		CustOrderSearchSpecification spec = (CustOrderSearchSpecification) SearchSpecificationBuilder.build(fromDate, toDate, keyword, CustOrder.class);
		return orderRepo.findAll(spec, pageRequest);

	}

	public void deleteOrderById(Long id) {
		orderRepo.deleteById(id);
	}

}
