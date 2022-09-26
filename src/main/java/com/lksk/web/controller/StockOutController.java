package com.lksk.web.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
	public String showStockIn(Model model,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,
			@RequestParam(value="fromDate", required = false) String fromDate,
			@RequestParam(value="toDate", required = false) String toDate,
			@RequestParam(value="keyword", required = false) String keyword,
			@RequestParam(value="itemId", required = false) Long itemId) throws ParseException {
		
		Page<StockOut> listPage = null;
		
		if(keyword == null && fromDate == null && toDate == null) {
			System.out.println("StockOut home page");
			if(itemId == null) {
				listPage = stockOutService.getAllStockOuts(page.orElse(1) - 1, size.orElse(4));
			} else {
				listPage = stockOutService.getAllStockOutsByItemId(itemId, page.orElse(1) - 1, size.orElse(4));
			}
			
		} else {
			System.out.println("Searching StockOuts for fromDate:" + fromDate + " and toDate:" +toDate +" and keyword:" + keyword);
			listPage = stockOutService.searchStockOutsByDateAndKeyword(keyword, fromDate, toDate, page.orElse(1) - 1, size.orElse(4));
			
			model.addAttribute("fromDate", fromDate);
			model.addAttribute("toDate", toDate);
			model.addAttribute("keyword", keyword);
			
		}
		
		model.addAttribute("listPage", listPage);
		int totalPages = listPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

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
	public String addStockOut(Model model, StockOut stockOut, 
			RedirectAttributes redirectAttributes) throws Exception{

		stockOutService.saveStockOutToDB(stockOut);

		redirectAttributes.addFlashAttribute("successMessage", "Stock Out added successfully!");
		return "redirect:/stock-out";

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
			@RequestParam("id") String id) throws Exception{

		System.out.println("Got save edit request for stock_out id " + id);
		stockOutService.saveStockOutToDB(stockOut);

		redirectAttributes.addFlashAttribute("successMessage", "Stock Out edited successfully!");
		return "redirect:/stock-out";

	}
	
}
