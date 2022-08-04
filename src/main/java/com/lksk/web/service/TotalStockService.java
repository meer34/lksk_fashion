package com.lksk.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lksk.web.model.TotalStock;
import com.lksk.web.repo.TotalStockRepo;

@Service
public class TotalStockService {

	@Autowired
	private TotalStockRepo totalStockRepo;
	
	public void saveTotalStockToDB(TotalStock currStock) {
		totalStockRepo.save(currStock);
	}

	public List<TotalStock> getTotalStocks() {
		return totalStockRepo.findAll();
	}
	
	public TotalStock findTotalStockByItem(String item) {
		System.out.println("Searching total stocks for item: " + item);
		return totalStockRepo.findById(item).get();
		
	}
	
	public void deleteTotalStocktById(String id) {
		totalStockRepo.deleteById(id);
	}

}
