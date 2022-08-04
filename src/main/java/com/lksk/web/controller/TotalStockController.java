package com.lksk.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lksk.web.model.TotalStock;
import com.lksk.web.service.TotalStockService;

@Controller
public class TotalStockController {
	
	@Autowired
	TotalStockService totalStockService;
	
	@GetMapping("/stock")
	public String showTotalStockPage(Model model) {
		model.addAttribute("totalStockList", totalStockService.getTotalStocks());
		return "stock";
	}
	
	@RequestMapping(value = "/searchTotalStock",
			method = RequestMethod.GET)
	public String searchTotalStock(Model model, @RequestParam("item") String item ) throws Exception{

		TotalStock totalStockList= totalStockService.findTotalStockByItem(item);
		System.out.println("Search found total stock for item " + item);
		
		model.addAttribute("totalStockList", totalStockList);
		return "stock";

	}
	
}
