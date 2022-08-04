package com.lksk.web.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
		return stockInRepo.findAll();
	}
	
	public StockIn findStockInById(long id) {
		return stockInRepo.findById(id).get();
	}
	
	public List<StockIn> searchStockInByDate(String fromDate, String toDate) throws ParseException {

		System.out.println("From Date is - " + fromDate);
		System.out.println("To Date is - " + toDate);

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return stockInRepo.findByDateBetween(formatter.parse(fromDate), formatter.parse(toDate));
		
	}

	public void deleteStockInById(Long id) {
		stockInRepo.deleteById(id);
	}
	
	
	
	
	
	
	public StockIn  saveStockInToDB(MultipartFile file,String sCode, String product, String party, 
			String quantity, String price, String amount, String rBy, String rFrom, String remarks) {

		StockIn stockIn = new StockIn();
		
		try {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			if(fileName.contains("..")) System.out.println("not a a valid file");

			stockIn.setSPhotoBlob(file.getBytes());
			stockIn.setSCode(sCode);
			stockIn.setProduct(product);
			stockIn.setParty(party);
			stockIn.setQuantity(Integer.parseInt(quantity));
			stockIn.setPrice(Double.parseDouble(price));
			stockIn.setAmount(Double.parseDouble(amount));
			stockIn.setRBy(rBy);
			stockIn.setRFrom(rFrom);
			stockIn.setRemakrs(remarks);

			stockInRepo.save(stockIn);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stockIn;
	}

}
