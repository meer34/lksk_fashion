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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lksk.web.model.Item;
import com.lksk.web.model.StockIn;
import com.lksk.web.model.StockOut;
import com.lksk.web.service.CustomerService;
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
	@Autowired CustomerService customerService;
	@Autowired ModeratorService moderatorService;

	@GetMapping("/stock-in")
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
		return "stock-in-create";
	}

	@RequestMapping(value = "/addStockIn",
			method = RequestMethod.POST)
	public String addStock(Model model, StockIn stockIn, @RequestParam Long itemId,
			RedirectAttributes redirectAttributes) throws Exception{

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

	@RequestMapping(value = "/viewStockIn",
			method = RequestMethod.GET)
	public String viewStockIn(Model model, @RequestParam("id") String id) throws Exception{

		System.out.println("Got view request for stock_in id " + id);

		model.addAttribute("stockIn", stockInService.findStockInById(Long.parseLong(id)));
		model.addAttribute("header", "Stock In");
		model.addAttribute("submitValue", "Print");

		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("parties", partyService.getAllUsers());
		model.addAttribute("moderators", moderatorService.getAllUsers());

		return "stock-in-view";

	}

	@RequestMapping(value = "/editStockIn",
			method = RequestMethod.GET)
	public String editStockIn(Model model, @RequestParam("id") String id) throws Exception{

		System.out.println("Got edit request for stock-in id " + id);

		model.addAttribute("stockIn", stockInService.findStockInById(Long.parseLong(id)));
		model.addAttribute("header", "Edit Stock In");
		model.addAttribute("submitValue", "Save");

		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("parties", partyService.getAllUsers());
		model.addAttribute("moderators", moderatorService.getAllUsers());

		return "stock-in-view";

	}

	@RequestMapping(value = "/deleteStockIn",
			method = RequestMethod.GET)
	public String deleteStockIn(RedirectAttributes redirectAttributes, @RequestParam("id") Long id) throws Exception{

		System.out.println("Got delete request for stock-in id " + id);
		StockIn stockIn = stockInService.findStockInById(id);
		Item item = stockIn.getItem();
		Integer quantity = item.getQuantity(stockIn.getUnit());
		if(quantity - stockIn.getQuantity() >= 0) {
			stockInService.deleteStockInById(id);
			redirectAttributes.addFlashAttribute("successMessage", "Stock In with id " + id + " deleted successfully!");
		} else {
			redirectAttributes.addFlashAttribute("successMessage", "Not allowed to deleted this Stock In since " 
													+ item.getStockOutQuantity(stockIn.getUnit()) + " Stock Out quantity present for this item unit!");
		}
		
		return "redirect:/stock-in";

	}

	@RequestMapping(value = "/saveStockInEdit",
			method = RequestMethod.POST)
	public String saveStockInEdit(RedirectAttributes redirectAttributes, StockIn stockIn,
			@RequestParam Long itemId,
			@RequestParam("id") String id) throws Exception{

		System.out.println("Got save edit request for stock_in id " + id);

		stockIn.setItem(itemService.findItemById(itemId));
		stockInService.saveStockInToDB(stockIn);

		redirectAttributes.addFlashAttribute("successMessage", "Stock In edited successfully!");
		return "redirect:/stock-in";

	}

	@RequestMapping(value = "/addStockInForScanCode",
			method = RequestMethod.GET)
	public String addStockInForScanCode(Model model, @RequestParam("action") String action, 
			@RequestParam("scanCode") String scanCode) throws Exception{

		System.out.println("Got prefetch request for id " + scanCode);

		StockIn stockIn = stockInService.findStockInByScanCode(scanCode);
		stockIn.setId(0);

		model.addAttribute("stockIn", stockIn);
		model.addAttribute("header", "New Stock In");
		model.addAttribute("submitValue", "Save");

		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("parties", partyService.getAllUsers());
		model.addAttribute("moderators", moderatorService.getAllUsers());

		return "stock-in-view";

	}

	@RequestMapping(value = "/checkIfScanCodeExistsForStockIn",
			method = RequestMethod.GET)
	@ResponseBody
	public String checkIfScanCodeExistsForStockIn(@RequestParam String scanCode) {
		System.out.println("Searching StockIn for scan code - " + scanCode);
		if(stockInService.findStockInByScanCode(scanCode) != null) {
			return "Exist";
		} else {
			return "Not Exist";
		}
	}
	
	@RequestMapping(value = "/addStockOutForScanCode",
			method = RequestMethod.GET)
	public String addStockOutForScanCode(Model model, @RequestParam("action") String action, 
			@RequestParam("scanCode") String scanCode) throws Exception{

		System.out.println("Got prefetch request for id " + scanCode);
		
		StockIn stockIn = stockInService.findStockInByScanCode(scanCode);
		
		StockOut stockOut = new StockOut();
		stockOut.setId(0);
		stockOut.setItemImageBlob(stockIn.getItemImageBlob());
		stockOut.setColourImageBlob(stockIn.getColourImageBlob());
		stockOut.setScanCode(stockIn.getScanCode());
		stockOut.setItem(stockIn.getItem());
		stockOut.setUnit(stockIn.getUnit());
		stockOut.setSize(stockIn.getSize());
		stockOut.setDispatchedBy(stockIn.getReceivedBy());
		stockOut.setDispatchedFrom(stockIn.getReceivedIn());
		
		model.addAttribute("stockOut", stockOut);
		model.addAttribute("header", "New Stock Out");
		model.addAttribute("submitValue", "Save");

		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("customers", customerService.getAllUsers());
		model.addAttribute("moderators", moderatorService.getAllUsers());

		return "stock-out-view";

	}

	@RequestMapping(value = "/checkIfScanCodeExistsForStockOut",
			method = RequestMethod.GET)
	@ResponseBody
	public String checkIfScanCodeExists(@RequestParam String scanCode) {
		System.out.println("Searching StockOut for scan code - " + scanCode);
		if(stockInService.findStockInByScanCode(scanCode) != null) {
			return "Exist";
		} else {
			return "Not Exist";
		}
	}

}
