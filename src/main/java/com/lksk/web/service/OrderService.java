package com.lksk.web.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lksk.web.model.CustOrder;
import com.lksk.web.repo.OrderRepo;

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

	public List<CustOrder> getAllOrders() {
		return orderRepo.findAllByOrderByIdDesc();
	}

	public List<CustOrder> searchOrderByDate(String fromDate, String toDate) throws ParseException {

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		if(fromDate != null && !fromDate.equalsIgnoreCase("") && toDate != null && !toDate.equalsIgnoreCase("")) {
			return orderRepo.findByDateBetween(formatter.parse(fromDate), formatter.parse(toDate));
		} else if((fromDate == null || fromDate.equalsIgnoreCase("")) && toDate != null && !toDate.equalsIgnoreCase("")) {
			return orderRepo.findByDateLessThanEqual(formatter.parse(toDate));
			
		} else if((toDate == null || toDate.equalsIgnoreCase("")) && fromDate != null && !fromDate.equalsIgnoreCase("")) {
			return orderRepo.findByDateGreaterThanEqual(formatter.parse(fromDate));
			
		} else {
			return orderRepo.findAll();
		}
		
	}

	public void deleteOrderById(Long id) {
		orderRepo.deleteById(id);
	}

}
