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
	
	public StockOut saveStockOutToDB(StockOut stockOut) {
		return stockOutRepo.save(stockOut);
	}
	
	public StockOut findStockOutById(Long id) {
		return stockOutRepo.findById(id).get();
	}

	public List<StockOut> getAllStockOuts() {
		return stockOutRepo.findAllByOrderByIdDesc();
	}
	
	public List<StockOut> searchStockOutByDate(String fromDate, String toDate) throws ParseException {

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		if(fromDate != null && !fromDate.equalsIgnoreCase("") && toDate != null && !toDate.equalsIgnoreCase("")) {
			return stockOutRepo.findByDateBetween(formatter.parse(fromDate), formatter.parse(toDate));
			
		} else if((fromDate == null || fromDate.equalsIgnoreCase("")) && toDate != null && !toDate.equalsIgnoreCase("")) {
			return stockOutRepo.findByDateLessThanEqual(formatter.parse(toDate));
			
		} else if((toDate == null || toDate.equalsIgnoreCase("")) && fromDate != null && !fromDate.equalsIgnoreCase("")) {
			return stockOutRepo.findByDateGreaterThanEqual(formatter.parse(fromDate));
			
		} else {
			return stockOutRepo.findAll();
		}

	}
	
	public List<StockOut> searchStockOutByItemAndDate(String item, String fromDate, String toDate) throws ParseException {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		if(fromDate != null && !"".equalsIgnoreCase(fromDate) && toDate != null && !"".equalsIgnoreCase(toDate)) {
			return stockOutRepo.findByItemNameAndDateBetween(item, formatter.parse(fromDate), formatter.parse(toDate));
			
		} else if((fromDate == null || "".equalsIgnoreCase(fromDate)) && toDate != null && !"".equalsIgnoreCase(toDate)) {
			return stockOutRepo.findByItemNameAndDateLessThanEqual(item, formatter.parse(toDate));
			
		} else if((toDate == null || "".equalsIgnoreCase(toDate)) && fromDate != null && !"".equalsIgnoreCase(fromDate)) {
			return stockOutRepo.findByItemNameAndDateGreaterThanEqual(item, formatter.parse(fromDate));
			
		} else {
			return stockOutRepo.findByItemName(item);
		}
		
	}
	
	public void deleteStockOutById(Long id) {
		stockOutRepo.deleteById(id);
	}

}
