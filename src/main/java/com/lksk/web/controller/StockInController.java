package com.lksk.web.controller;

import java.util.ArrayList;
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

import com.lksk.web.model.StockIn;
import com.lksk.web.service.ItemService;
import com.lksk.web.service.ModeratorService;
import com.lksk.web.service.PartyService;
import com.lksk.web.service.ProductService;
import com.lksk.web.service.StockInService;

@Controller
public class StockInController {

	@Autowired StockInService stockInService;
	@Autowired ProductService productService;
	@Autowired ItemService itemService;
	@Autowired PartyService partyService;
	@Autowired ModeratorService moderatorService;

	@GetMapping({"/stock-in", "/list"})
	public String showStockIn(Model model) {
		System.out.println("Inside stock-in");
		model.addAttribute("stockInList", stockInService.getAllStockIns());
		for (StockIn si : stockInService.getAllStockIns()) {
			System.out.println("Product is: " + si.getItem().getProduct().getName());
			System.out.println("Item is: " + si.getItem().getName());
		}
		return "stock-in";
	}

	@GetMapping("/addStockInPage")
	public String showAddStockInPage(Model model) {
		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("parties", partyService.getAllUsers());
		model.addAttribute("moderators", moderatorService.getAllUsers());
		return "stock-in-popup";
	}

	@RequestMapping(value = "/addStockIn",
			method = RequestMethod.POST)
	public String addStock(Model model, StockIn stockIn, @RequestParam Long itemId,
			RedirectAttributes redirectAttributes) throws Exception{

		if(StringUtils.cleanPath(stockIn.getSPhoto().getOriginalFilename()).contains("..")) System.out.println("Photo not a a valid file");
		stockIn.setSPhotoBlob(Base64.getEncoder().encodeToString(stockIn.getSPhoto().getBytes()));
		
		if(StringUtils.cleanPath(stockIn.getColour().getOriginalFilename()).contains("..")) System.out.println("Colour not a a valid file");
		stockIn.setColourBlob(Base64.getEncoder().encodeToString(stockIn.getColour().getBytes()));
		
		stockIn.setItem(itemService.findItemById(itemId));
		stockInService.saveStockInToDB(stockIn);
		
		redirectAttributes.addFlashAttribute("successMessage", "Stock In added successfully!");
		return "redirect:/stock-in";

	}

	@RequestMapping(value = "/searchStockIn",
			method = RequestMethod.GET)
	public String searchStockIn(Model model, 
			@RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate,
			@RequestParam("keyword") String keyword ) throws Exception{

		List<StockIn> stockInList = new ArrayList<StockIn>();
		
		for (StockIn StockIn : stockInService.searchStockInByDate(fromDate, toDate)) {
			if(StockIn.toString().toLowerCase().contains(keyword.toLowerCase())) {
				stockInList.add(StockIn);
			}
		}

		System.out.println("Search size for fromDate:" + fromDate + " and toDate:" +toDate +" and keyword:" + keyword + " is - " + stockInList.size());

		model.addAttribute("stockInList", stockInList);
		return "stock-in";

	}

	@RequestMapping(value = "/openStockInActionPage",
			method = RequestMethod.GET)
	public String performStockOutAction(RedirectAttributes redirectAttributes, Model model,
			@RequestParam("action") String action,
			@RequestParam("id") String id) throws Exception{

		System.out.println("Got " + action + " action request for id " + id);

		if(action.equalsIgnoreCase("View")) {
			model.addAttribute("stockIn", stockInService.findStockInById(Long.parseLong(id)));
			model.addAttribute("header", "Stock In");
			model.addAttribute("submitValue", "Print");
			
			model.addAttribute("products", productService.getAllProducts());
			model.addAttribute("parties", partyService.getAllUsers());
			model.addAttribute("moderators", moderatorService.getAllUsers());
			
			return "stock-in-view";

		} else if(action.equalsIgnoreCase("Edit")) {
			model.addAttribute("stockIn", stockInService.findStockInById(Long.parseLong(id)));
			model.addAttribute("header", "Edit Stock In");
			model.addAttribute("submitValue", "Edit");
			
			model.addAttribute("products", productService.getAllProducts());
			model.addAttribute("parties", partyService.getAllUsers());
			model.addAttribute("moderators", moderatorService.getAllUsers());
			
			return "stock-in-view";

		} else if(action.equalsIgnoreCase("Print")) {
			model.addAttribute("stockIn", stockInService.findStockInById(Long.parseLong(id)));
			return "redirect:/stock-in";

		} else if(action.equalsIgnoreCase("Delete")) {
			stockInService.deleteStockInById(Long.parseLong(id));
			redirectAttributes.addFlashAttribute("successMessage", "Stock In with id " + id + " deleted successfully!");
			return "redirect:/stock-in";

		} else {
			System.out.println();
			return "error";
		}
	}

	@RequestMapping(value = "/performStockInAction",
			method = RequestMethod.POST)
	public String doStockInAction(RedirectAttributes redirectAttributes, Model model, StockIn stockIn,
			@RequestParam Long itemId,
			@RequestParam("action") String action,
			@RequestParam("id") String id) throws Exception{

		System.out.println("Got " + action + " action request for id " + id);
		try {
			if(action.equalsIgnoreCase("Save")) {
				if(StringUtils.cleanPath(stockIn.getSPhoto().getOriginalFilename()).contains("..")) System.out.println("Photo not a a valid file");
				stockIn.setSPhotoBlob(Base64.getEncoder().encodeToString(stockIn.getSPhoto().getBytes()));
				
				if(StringUtils.cleanPath(stockIn.getColour().getOriginalFilename()).contains("..")) System.out.println("Colour not a a valid file");
				stockIn.setColourBlob(Base64.getEncoder().encodeToString(stockIn.getColour().getBytes()));
				
				stockIn.setItem(itemService.findItemById(itemId));
				stockInService.saveStockInToDB(stockIn);
				
				redirectAttributes.addFlashAttribute("successMessage", "Stock In edited successfully!");
				return "redirect:/stock-in";

			} else if(action.equalsIgnoreCase("Print")) {
				redirectAttributes.addFlashAttribute("stockOut", stockInService.findStockInById(Long.parseLong(id)));
				return "redirect:/stock-in";

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "testerror";
	}

}
