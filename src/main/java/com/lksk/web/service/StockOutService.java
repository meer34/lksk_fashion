package com.lksk.web.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lksk.web.model.StockOut;
import com.lksk.web.repo.StockOutRepo;
import com.lksk.web.specification.StockOutSearchSpecification;
import com.lksk.web.util.SearchSpecificationBuilder;

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

	public Page<StockOut> getAllStockOuts(int pageNo, int pageSize) {
		return stockOutRepo.findAll(PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id")));
	}
	
	public Page<StockOut> getAllStockOutsByItemAndUnit(Long itemId, String unit, int pageNo, int pageSize) {
		return stockOutRepo.findByItemAndUnit(itemId, unit, PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id")));
	}
	
	public Page<StockOut> searchStockOutsByDateAndKeyword(String keyword, 
			String fromDate, String toDate, int pageNo, Integer pageSize) throws ParseException {

		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
		StockOutSearchSpecification spec = (StockOutSearchSpecification) SearchSpecificationBuilder.build(fromDate, toDate, keyword, StockOut.class);
		return stockOutRepo.findAll(spec, pageRequest);

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
