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

import com.lksk.web.model.StockOut;
import com.lksk.web.service.ProductService;
import com.lksk.web.service.ModeratorService;
import com.lksk.web.service.PartyService;
import com.lksk.web.service.ItemService;
import com.lksk.web.service.StockOutService;

@Controller
public class StockOutController {

	@Autowired StockOutService stockOutService;
	@Autowired ProductService productService;
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
		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("parties", partyService.getAllUsers());
		model.addAttribute("moderators", moderatorService.getAllUsers());
		//		model.addAttribute("availableQuantity", "10"); th:max="${availableQuantity}"
		return "stock-out-popup";
	}

	@RequestMapping(value = "/addStockOut",
			method = RequestMethod.POST)
	public String addStockOut(Model model, StockOut stockOut, @RequestParam Long itemId,
			RedirectAttributes redirectAttributes) throws Exception{

		if(StringUtils.cleanPath(stockOut.getSPhoto().getOriginalFilename()).contains("..")) System.out.println("Photo not a a valid file");
		stockOut.setSPhotoBlob(Base64.getEncoder().encodeToString(stockOut.getSPhoto().getBytes()));

		if(StringUtils.cleanPath(stockOut.getColour().getOriginalFilename()).contains("..")) System.out.println("Colour not a a valid file");
		stockOut.setColourBlob(Base64.getEncoder().encodeToString(stockOut.getColour().getBytes()));

		stockOut.setItem(itemService.findItemById(itemId));
		stockOutService.saveStockOutToDB(stockOut);

		redirectAttributes.addFlashAttribute("successMessage", "Stock Out added successfully!");
		return "redirect:/stock-out";

	}

	@RequestMapping(value = "/searchStockOut",
			method = RequestMethod.GET)
	public String searchStockOut(Model model, 
			@RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate,
			@RequestParam("keyword") String keyword) throws Exception{
		List<StockOut> stockOutList = new ArrayList<StockOut>();

		for (StockOut stockOut : stockOutService.searchStockOutByDate(fromDate, toDate)) {
			if(stockOut.toString().toLowerCase().contains(keyword.toLowerCase())) {
				stockOutList.add(stockOut);
			}
		}
		System.out.println("Search size for fromDate:" + fromDate + " and toDate:" +toDate +" and keyword:" + keyword + " is - " +  stockOutList.size());

		model.addAttribute("stockOutList", stockOutList);
		return "stock-out";

	}

	@RequestMapping(value = "/openStockOutActionPage",
			method = RequestMethod.GET)
	public String performStockOutAction(RedirectAttributes redirectAttributes, Model model,
			@RequestParam("action") String action,
			@RequestParam("id") String id) throws Exception{

		System.out.println("Got " + action + " action request for id " + id);

		if(action.equalsIgnoreCase("View")) {
			model.addAttribute("stockOut", stockOutService.findStockOutById(Long.parseLong(id)));
			model.addAttribute("header", "Stock Out");
			model.addAttribute("submitValue", "Print");

			model.addAttribute("products", productService.getAllProducts());
			model.addAttribute("parties", partyService.getAllUsers());
			model.addAttribute("moderators", moderatorService.getAllUsers());

			return "stock-out-view";

		} else if(action.equalsIgnoreCase("Edit")) {
			model.addAttribute("stockOut", stockOutService.findStockOutById(Long.parseLong(id)));
			model.addAttribute("header", "Edit Stock Out");
			model.addAttribute("submitValue", "Edit");

			model.addAttribute("products", productService.getAllProducts());
			model.addAttribute("parties", partyService.getAllUsers());
			model.addAttribute("moderators", moderatorService.getAllUsers());

			return "stock-out-view";

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

	@RequestMapping(value = "/performStockOutAction",
			method = RequestMethod.POST)
	public String doStockOutAction(RedirectAttributes redirectAttributes, Model model, StockOut stockOut,
			@RequestParam Long itemId,
			@RequestParam("action") String action,
			@RequestParam("id") String id) throws Exception{

		System.out.println("Got " + action + " action request for id " + id);

		if(action.equalsIgnoreCase("Save")) {
			if(StringUtils.cleanPath(stockOut.getSPhoto().getOriginalFilename()).contains("..")) System.out.println("Photo not a a valid file");
			stockOut.setSPhotoBlob(Base64.getEncoder().encodeToString(stockOut.getSPhoto().getBytes()));

			if(StringUtils.cleanPath(stockOut.getColour().getOriginalFilename()).contains("..")) System.out.println("Colour not a a valid file");
			stockOut.setColourBlob(Base64.getEncoder().encodeToString(stockOut.getColour().getBytes()));

			stockOut.setItem(itemService.findItemById(itemId));
			stockOut = stockOutService.saveStockOutToDB(stockOut);

			redirectAttributes.addFlashAttribute("successMessage", "Stock Out edited successfully!");
			return "redirect:/stock-out";

		} else if(action.equalsIgnoreCase("Print")) {
			redirectAttributes.addFlashAttribute("stockOut", stockOutService.findStockOutById(Long.parseLong(id)));
			return "redirect:/stock-out";

		} else {
			System.out.println();
			return "error";
		}
	}

}
