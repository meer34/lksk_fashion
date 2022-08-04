package com.lksk.web.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lksk.web.model.StockOut;
import com.lksk.web.repo.StockOutRepo;

@Service
public class StockOutService {

	@Autowired
	private StockOutRepo stockOutRepo;
	
	public StockOut findStockOutById(Long id) {
		return stockOutRepo.findById(id).get();
	}
	
	public StockOut saveStockOutToDB(StockOut stockOut) {
		return stockOutRepo.save(stockOut);
	}

	public List<StockOut> getAllStockOuts() {
		return stockOutRepo.findAll();
	}
	
	public List<StockOut> searchStockOutByDate(String fromDate, String toDate) throws ParseException {

		System.out.println("From Date is - " + fromDate);
		System.out.println("To Date is - " + toDate);

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return stockOutRepo.findByDateBetween(formatter.parse(fromDate), formatter.parse(toDate));
		
	}
	
	public void deleteStockOutById(Long id) {
		stockOutRepo.deleteById(id);
	}

}
