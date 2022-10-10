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
import com.lksk.web.util.LKSKConstants;

@Controller
public class StockInController {

	@Autowired StockInService stockInService;
	@Autowired ProductService productService;
	@Autowired ItemService itemService;
	@Autowired PartyService partyService;
	@Autowired CustomerService customerService;
	@Autowired ModeratorService moderatorService;
	
	@GetMapping("/stock-in")
	public String showStockIn(Model model,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,
			@RequestParam(value="fromDate", required = false) String fromDate,
			@RequestParam(value="toDate", required = false) String toDate,
			@RequestParam(value="keyword", required = false) String keyword,
			@RequestParam(value="itemId", required = false) Long itemId,
			@RequestParam(value="unit", required = false) String unit) throws ParseException {
		
		Page<StockIn> listPage = null;
		
		if(keyword == null && fromDate == null && toDate == null) {
			System.out.println("StockIn home page");
			if(itemId == null && unit == null) {
				listPage = stockInService.getAllStockIns(page.orElse(1) - 1, size.orElse(LKSKConstants.INITIAL_PAGE_SIZE));
			} else {
				listPage = stockInService.getAllStockInsByItemAndUnit(itemId, unit , page.orElse(1) - 1, size.orElse(LKSKConstants.INITIAL_PAGE_SIZE));
			}
			
		} else {
			System.out.println("Searching StockIns for fromDate:" + fromDate + " and toDate:" +toDate +" and keyword:" + keyword);
			listPage = stockInService.searchStocksInByDateAndKeyword(keyword, fromDate, toDate, page.orElse(1) - 1, size.orElse(LKSKConstants.INITIAL_PAGE_SIZE));
			
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
	public String addStock(Model model, StockIn stockIn, 
			RedirectAttributes redirectAttributes) throws Exception{

		stockInService.saveStockInToDB(stockIn);

		redirectAttributes.addFlashAttribute("successMessage", "Stock In added successfully!");
		return "redirect:/stock-in";

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
			@RequestParam("id") String id) throws Exception{

		System.out.println("Got save edit request for stock_in id " + id);

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
