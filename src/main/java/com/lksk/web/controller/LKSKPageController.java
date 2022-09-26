package com.lksk.web.controller;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lksk.web.model.Item;
import com.lksk.web.service.ItemService;
import com.lksk.web.service.StockInService;
import com.lksk.web.service.StockOutService;

@Controller
public class LKSKPageController {

	@Autowired StockInService stockInService;
	@Autowired StockOutService stockOutService;
	@Autowired ItemService itemService;

	@RequestMapping("/login")
	public String login() {
		return "index";
	}
	
	@RequestMapping("/accessDenied")
	public String accessDenied() {
		return "access-denied";
	}

	@GetMapping({"/", "/home"})
	public String showLandingPage(Model model) {
		model.addAttribute("itemNames", itemService.findAllItemNames());
		model.addAttribute("itemList", itemService.getAllItems());
		return "home";
	}

	@RequestMapping(value = "/searchStockDashboard",
			method = RequestMethod.GET)
	public String searchTotalStock(Model model, 
			@RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate,
			@RequestParam(value="item", required = false) String itemParam) throws Exception{

		List<Item> itemList = new ArrayList<>();
		Item tempItem = null;
		
		if(itemParam != null && !"".equalsIgnoreCase(itemParam)) {
			tempItem = new Item();
			tempItem.setName(itemParam);
			tempItem.setStockInList(stockInService.searchStockInByItemAndDate(itemParam, fromDate, toDate));
			tempItem.setStockOutList(stockOutService.searchStockOutByItemAndDate(itemParam, fromDate, toDate));
			itemList.add(tempItem);
			
		} else {
			for (String itemName : itemService.findAllItemNames()) {
				tempItem = new Item();
				tempItem.setName(itemName);
				tempItem.setStockInList(stockInService.searchStockInByItemAndDate(itemName, fromDate, toDate));
				tempItem.setStockOutList(stockOutService.searchStockOutByItemAndDate(itemName, fromDate, toDate));
				itemList.add(tempItem);
			}
		}
		
		model.addAttribute("itemNames", itemService.findAllItemNames());
		model.addAttribute("itemList", itemList);
		return "home";

	}

	@GetMapping("/stock")
	public String showTotalStockPage(Model model) {
		model.addAttribute("itemList", itemService.getAllItems());
		return "stock";
	}

	@RequestMapping(value = "/searchTotalStock",
			method = RequestMethod.GET)
	public String searchTotalStock(Model model, @RequestParam("item") String item ) throws Exception{

		List<Item> itemList = itemService.findItemksByNameContaining(item);

		model.addAttribute("itemList", itemList);
		return "stock";

	}
	
	@GetMapping("/downloadAndroidApp")
	public ResponseEntity<Resource> downloadFileFromLocal() {
		System.out.println("Received download request for android app");

		String fileBasePath = "/files/lksk/app/android/";
		Path path = Paths.get(fileBasePath + "lksk_fashion.apk");
		Resource resource = null;
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

}
