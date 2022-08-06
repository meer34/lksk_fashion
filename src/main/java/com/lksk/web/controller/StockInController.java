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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lksk.web.model.StockIn;
import com.lksk.web.model.TotalStock;
import com.lksk.web.service.ItemService;
import com.lksk.web.service.ModeratorService;
import com.lksk.web.service.PartyService;
import com.lksk.web.service.StockInService;
import com.lksk.web.service.TotalStockService;

@Controller
public class StockInController {
	
	@Autowired StockInService stockInService;
	@Autowired TotalStockService totalStockService;
	@Autowired ItemService itemService;
	@Autowired PartyService partyService;
	@Autowired ModeratorService moderatorService;
	
	@GetMapping({"/stock-in", "/list"})
	public String showStockIn(Model model) {
		System.out.println("Inside stock-in");
		model.addAttribute("stockInList", stockInService.getAllStockIns());
		model.addAttribute("products", itemService.getAllItems());
		return "stock-in";
	}
	
	@GetMapping("/addStockInPage")
	public String showAddStockInPage(Model model) {
		model.addAttribute("products", itemService.getAllItems());
		model.addAttribute("parties", partyService.getAllUsers());
		model.addAttribute("moderators", moderatorService.getAllUsers());
		return "stock-in-popup";
	}
	
	@RequestMapping(value = "/addStockIn",
			method = RequestMethod.POST)
	public String addStock(Model model, StockIn stockIn, 
			RedirectAttributes redirectAttributes) throws Exception{
		
		String fileName = StringUtils.cleanPath(stockIn.getSPhoto().getOriginalFilename());
		if(fileName.contains("..")) System.out.println("not a a valid file");
		stockIn.setSPhotoBlob(Base64.getEncoder().encodeToString(stockIn.getSPhoto().getBytes()));
		
		stockIn = stockInService.saveStockInToDB(stockIn);
		
		if(stockIn != null) {
			TotalStock ts = totalStockService.findTotalStockByItem(stockIn.getProduct());
			ts.setQuantity(ts.getQuantity() + stockIn.getQuantity());
			totalStockService.saveTotalStockToDB(ts);
			System.out.println("Total Stock updated for product: " + stockIn.getProduct());
		}
		
		redirectAttributes.addFlashAttribute("successMessage", "Stock In added successfully!");
		return "redirect:/stock-in";
		
	}
	
	@RequestMapping(value = "/searchStockIn",
			method = RequestMethod.GET)
	public String searchStockIn(Model model, 
			@RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate ) throws Exception{

		List<StockIn> stockInList= stockInService.searchStockInByDate(fromDate, toDate);
		System.out.println("Search size: " + stockInList.size());
		
		model.addAttribute("stockInList", stockInList);
		return "stock-in";

	}
	
	@RequestMapping(value = "/performStockInAction",
			method = RequestMethod.GET)
	public String performStockOutAction(RedirectAttributes redirectAttributes, Model model,
			@RequestParam("action") String action,
			@RequestParam("id") String id) throws Exception{
		
		System.out.println("Got " + action + " action request for id " + id);
		
		if(action.equalsIgnoreCase("View")) {
			model.addAttribute("stockIn", stockInService.findStockInById(Long.parseLong(id)));
			return "stock-in-view";
			
		} else if(action.equalsIgnoreCase("Edit")) {
			redirectAttributes.addFlashAttribute("stockIn", stockInService.findStockInById(Long.parseLong(id)));
			return "redirect:/stock-in";
			
		} else if(action.equalsIgnoreCase("Print")) {
			redirectAttributes.addFlashAttribute("stockIn", stockInService.findStockInById(Long.parseLong(id)));
			return "redirect:/stock-in";
			
		} else if(action.equalsIgnoreCase("Delete")) {
			stockInService.deleteStockInById(Long.parseLong(id));
			redirectAttributes.addFlashAttribute("successMessage", "Stock Out with id " + id + " deleted successfully!");
			return "redirect:/stock-in";
			
		} else {
			System.out.println();
			return "error";
		}
	}
	
	
	
	
	
	
	@RequestMapping(value = "/NotInUseaddStockIn",
			method = RequestMethod.POST)
	@ResponseBody
	public StockIn addStock1(@RequestParam("sPhoto") MultipartFile sPhoto,
    		@RequestParam("sCode") String sCode,
    		@RequestParam("product") String product,
    		@RequestParam("party") String party,
    		@RequestParam("quantity") String quantity,
    		@RequestParam("price") String price,
    		@RequestParam("amount") String amount,
    		@RequestParam("receivedBy") String receivedBy,
    		@RequestParam("receivedIn") String receivedIn,
    		@RequestParam("remakrs") String remakrs) {
		return stockInService.saveStockInToDB(sPhoto, sCode, product, party, quantity, price, amount, receivedBy, receivedIn, remakrs);
		
	}
	
}
