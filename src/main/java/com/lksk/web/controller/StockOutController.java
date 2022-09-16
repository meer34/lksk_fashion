package com.lksk.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lksk.web.model.StockOut;
import com.lksk.web.service.CustomerService;
import com.lksk.web.service.ItemService;
import com.lksk.web.service.ModeratorService;
import com.lksk.web.service.ProductService;
import com.lksk.web.service.StockOutService;

@Controller
public class StockOutController {

	@Autowired StockOutService stockOutService;
	@Autowired ProductService productService;
	@Autowired ItemService itemService;
	@Autowired CustomerService customerService;
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
		model.addAttribute("customers", customerService.getAllUsers());
		model.addAttribute("moderators", moderatorService.getAllUsers());
		return "stock-out-create";
	}

	@RequestMapping(value = "/addStockOut",
			method = RequestMethod.POST)
	public String addStockOut(Model model, StockOut stockOut, @RequestParam Long itemId,
			RedirectAttributes redirectAttributes) throws Exception{

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
	
	@RequestMapping(value = "/viewStockOut",
			method = RequestMethod.GET)
	public String viewStockOut(Model model, @RequestParam("id") String id) throws Exception{

		System.out.println("Got view request for stock_out id " + id);

		model.addAttribute("stockOut", stockOutService.findStockOutById(Long.parseLong(id)));
		model.addAttribute("header", "Stock Out");
		model.addAttribute("submitValue", "Print");

		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("customers", customerService.getAllUsers());
		model.addAttribute("moderators", moderatorService.getAllUsers());

		return "stock-out-view";

	}

	@RequestMapping(value = "/editStockOut",
			method = RequestMethod.GET)
	public String editStockOut(Model model, @RequestParam("id") String id) throws Exception{

		System.out.println("Got edit request for stock-out id " + id);

		model.addAttribute("stockOut", stockOutService.findStockOutById(Long.parseLong(id)));
		model.addAttribute("header", "Edit Stock Out");
		model.addAttribute("submitValue", "Save");

		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("customers", customerService.getAllUsers());
		model.addAttribute("moderators", moderatorService.getAllUsers());

		return "stock-out-view";

	}

	@RequestMapping(value = "/deleteStockOut",
			method = RequestMethod.GET)
	public String deleteStockOut(RedirectAttributes redirectAttributes, @RequestParam("id") String id) throws Exception{

		System.out.println("Got delete request for stock-out id " + id);
		stockOutService.deleteStockOutById(Long.parseLong(id));
		redirectAttributes.addFlashAttribute("successMessage", "Stock Out with id " + id + " deleted successfully!");
		return "redirect:/stock-out";

	}

	@RequestMapping(value = "/saveStockOutEdit",
			method = RequestMethod.POST)
	public String saveStockOutEdit(RedirectAttributes redirectAttributes, StockOut stockOut,
			@RequestParam Long itemId,
			@RequestParam("id") String id) throws Exception{

		System.out.println("Got save edit request for stock_out id " + id);
		stockOut.setItem(itemService.findItemById(itemId));
		stockOutService.saveStockOutToDB(stockOut);

		redirectAttributes.addFlashAttribute("successMessage", "Stock Out edited successfully!");
		return "redirect:/stock-out";

	}
	
}
