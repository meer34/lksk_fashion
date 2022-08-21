package com.lksk.web.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lksk.web.model.StockIn;
import com.lksk.web.repo.StockInRepo;

@Service
public class StockInService {

	@Autowired
	private StockInRepo stockInRepo;

	public StockIn saveStockInToDB(StockIn stockIn) {
		return stockInRepo.save(stockIn);
	}

	public List<StockIn> getAllStockIns() {
		return stockInRepo.findAllByOrderByIdDesc();
	}

	public StockIn findStockInById(long id) {
		return stockInRepo.findById(id).get();
	}

	public List<StockIn> searchStockInByDate(String fromDate, String toDate) throws ParseException {

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		if(fromDate != null && !fromDate.equalsIgnoreCase("") && toDate != null && !toDate.equalsIgnoreCase("")) {
			return stockInRepo.findByDateBetween(formatter.parse(fromDate), formatter.parse(toDate));
			
		} else if((fromDate == null || fromDate.equalsIgnoreCase("")) && toDate != null && !toDate.equalsIgnoreCase("")) {
			return stockInRepo.findByDateLessThanEqual(formatter.parse(toDate));
			
		} else if((toDate == null || toDate.equalsIgnoreCase("")) && fromDate != null && !fromDate.equalsIgnoreCase("")) {
			return stockInRepo.findByDateGreaterThanEqual(formatter.parse(fromDate));
			
		} else {
			return stockInRepo.findAll();
		}

	}

	public List<StockIn> searchStockInByItemAndDate(String item, String fromDate, String toDate) throws ParseException {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		if(fromDate != null && !"".equalsIgnoreCase(fromDate) && toDate != null && !"".equalsIgnoreCase(toDate)) {
			return stockInRepo.findByItemNameAndDateBetween(item, formatter.parse(fromDate), formatter.parse(toDate));
			
		} else if((fromDate == null || "".equalsIgnoreCase(fromDate)) && toDate != null && !"".equalsIgnoreCase(toDate)) {
			return stockInRepo.findByItemNameAndDateLessThanEqual(item, formatter.parse(toDate));
			
		} else if((toDate == null || "".equalsIgnoreCase(toDate)) && fromDate != null && !"".equalsIgnoreCase(fromDate)) {
			return stockInRepo.findByItemNameAndDateGreaterThanEqual(item, formatter.parse(fromDate));
			
		} else {
			return stockInRepo.findByItemName(item);
		}

	}

	public void deleteStockInById(Long id) {
		stockInRepo.deleteById(id);
	}

	public StockIn findStockInByScanCode(String scanCode) {
		return stockInRepo.findFirstByScanCodeOrderByIdDesc(scanCode);
	}

}
