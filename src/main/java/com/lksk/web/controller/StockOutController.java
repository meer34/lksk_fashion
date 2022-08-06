package com.lksk.web.controller;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lksk.web.model.StockOut;
import com.lksk.web.model.TotalStock;
import com.lksk.web.service.ItemService;
import com.lksk.web.service.ModeratorService;
import com.lksk.web.service.PartyService;
import com.lksk.web.service.StockOutService;
import com.lksk.web.service.TotalStockService;

@Controller
public class StockOutController {
	
	@Autowired StockOutService stockOutService;
	@Autowired TotalStockService totalStockService;
	@Autowired ItemService itemService;
	@Autowired PartyService partyService;
	@Autowired ModeratorService moderatorService;
	
	@GetMapping("/stock-out")
	public String showStockIn(Model model) {
		System.out.println("Inside stock-out");
		model.addAttribute("stockOutList", stockOutService.getAllStockOuts());
		return "stock-out";
	}
	
	@GetMapping("/addStockOutPage")
	public String showAddStockOutPage(Model model) {
		model.addAttribute("products", itemService.getAllItems());
		model.addAttribute("parties", partyService.getAllUsers());
		model.addAttribute("moderators", moderatorService.getAllUsers());
		model.addAttribute("availableQuantity", "10");
		return "stock-out-popup";
	}
	
	@RequestMapping(value = "/addStockOut",
			method = RequestMethod.POST)
	public String addStockOut(Model model, StockOut stockOut, 
			RedirectAttributes redirectAttributes) throws Exception{
		
		String fileName = StringUtils.cleanPath(stockOut.getSPhoto().getOriginalFilename());
		if(fileName.contains("..")) System.out.println("not a a valid file");
		stockOut.setSPhotoBlob(Base64.getEncoder().encodeToString(stockOut.getSPhoto().getBytes()));
		
		stockOut = stockOutService.saveStockOutToDB(stockOut);
		
		if(stockOut != null) {
			TotalStock ts = totalStockService.findTotalStockByItem(stockOut.getProduct());
			ts.setQuantity(ts.getQuantity() - stockOut.getQuantity());
			totalStockService.saveTotalStockToDB(ts);
			System.out.println("Total Stock updated for product: " + stockOut.getProduct());
		}
		
		redirectAttributes.addFlashAttribute("successMessage", "Stock Out with code " + stockOut.getSCode() + " added successfully!");
		
		return "redirect:/stock-out";
		
	}
	
	@RequestMapping(value = "/searchStockOut",
			method = RequestMethod.GET)
	public String searchStockOut(Model model, 
			@RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate ) throws Exception{

		List<StockOut> stockOutList= stockOutService.searchStockOutByDate(fromDate, toDate);
		System.out.println("Search size: " + stockOutList.size());
		
		model.addAttribute("stockOutList", stockOutList);
		return "stock-out";

	}
	
	@RequestMapping(value = "/performStockOutAction",
			method = RequestMethod.GET)
	public String performStockOutAction(RedirectAttributes redirectAttributes, Model model,
			@RequestParam("action") String action,
			@RequestParam("id") String id) throws Exception{
		
		System.out.println("Got " + action + " action request for id " + id);
		
		if(action.equalsIgnoreCase("View")) {
			model.addAttribute("stockOut", stockOutService.findStockOutById(Long.parseLong(id)));
			return "stock-out-view";
			
		} else if(action.equalsIgnoreCase("Edit")) {
			redirectAttributes.addFlashAttribute("stockOut", stockOutService.findStockOutById(Long.parseLong(id)));
			return "redirect:/stock-out";
			
		} else if(action.equalsIgnoreCase("Print")) {
			redirectAttributes.addFlashAttribute("stockOut", stockOutService.findStockOutById(Long.parseLong(id)));
			return "redirect:/stock-out";
			
		} else if(action.equalsIgnoreCase("Delete")) {
			stockOutService.deleteStockOutById(Long.parseLong(id));
			redirectAttributes.addFlashAttribute("successMessage", "Stock Out with id " + id + " deleted successfully!");
			return "redirect:/stock-out";
			
		} else {
			System.out.println();
			return "error";
		}
	}
	
}
