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

	public void saveOrderToDB(CustOrder order) {
		orderRepo.save(order);
	}

	public List<CustOrder> getAllOrders() {
		return orderRepo.findAll();
	}

	public List<CustOrder> searchOrderByDate(String fromDate, String toDate) throws ParseException {

		System.out.println("From Date is - " + fromDate);
		System.out.println("To Date is - " + toDate);

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return orderRepo.findByDateBetween(formatter.parse(fromDate), formatter.parse(toDate));
		
	}

	public void deleteOrderById(Long id) {
		orderRepo.deleteById(id);
	}

}
