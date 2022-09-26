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

import com.lksk.web.model.StockIn;
import com.lksk.web.repo.StockInRepo;
import com.lksk.web.specification.StockInSearchSpecification;
import com.lksk.web.util.SearchSpecificationBuilder;

@Service
public class StockInService {

	@Autowired
	private StockInRepo stockInRepo;

	public StockIn saveStockInToDB(StockIn stockIn) {
		return stockInRepo.save(stockIn);
	}

	public Page<StockIn> getAllStockIns(int pageNo, int pageSize) {
		return stockInRepo.findAll(PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id")));
	}
	
	public Page<StockIn> getAllStockInsByItemId(Long itemId, int pageNo, int pageSize) {
		return stockInRepo.findByItemId(itemId, PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id")));
	}

	public StockIn findStockInById(long id) {
		return stockInRepo.findById(id).get();
	}

	public Page<StockIn> searchStocksInByDateAndKeyword(String keyword, 
			String fromDate, String toDate, int pageNo, Integer pageSize) throws ParseException {

		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
		StockInSearchSpecification spec = (StockInSearchSpecification) SearchSpecificationBuilder.build(fromDate, toDate, keyword, StockIn.class);
		return stockInRepo.findAll(spec, pageRequest);

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
