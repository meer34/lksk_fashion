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

import com.google.gson.Gson;
import com.lksk.web.model.Item;
import com.lksk.web.model.Product;
import com.lksk.web.service.ItemService;
import com.lksk.web.service.ProductService;
import com.lksk.web.service.StockOutService;

@Controller
public class ItemController {

	@Autowired ItemService itemService;
	@Autowired ProductService productService;
	@Autowired StockOutService stockOutService;

	@GetMapping("/createItemPage")
	public String showCreateItemPage(Model model) {
		model.addAttribute("item", new Item());
		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("header", "Create Item");
		return "item-create";
	}

	@RequestMapping(value = "/createItem",
			method = RequestMethod.POST)
	public String createItem(Model model, Item item,
			@RequestParam Long productId) throws Exception{
		
		Product product = productService.findProductById(productId);
		System.out.println("### Unit is:" + item.getUnits());
		System.out.println("### Processed unit is:" + item.getUnits());
		item.setProduct(product);
		item = itemService.saveItemToDB(item);

		model.addAttribute("product", product);
		model.addAttribute("successMessage", item.getName() + " item added successfully!");
		return "product-record";

	}

	@RequestMapping(value = "/editItem",
			method = RequestMethod.GET)
	public String editItem(Model model,
			@RequestParam("id") Long id,
			@RequestParam("product") String product) throws Exception{
		
		System.out.println("Got edit request for item with id " + id);
		model.addAttribute("item", itemService.findItemById(id));
		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("header", "Edit Item");
		return "item-create";
		
	}
	
	@RequestMapping(value = "/deleteItem",
			method = RequestMethod.GET)
	public String deleteItem(RedirectAttributes redirectAttributes,
			@RequestParam("id") Long id,
			@RequestParam("product") String product) throws Exception{
		
		System.out.println("Got delete request for item with id " + id);
		itemService.deleteItemById(id);
		redirectAttributes.addFlashAttribute("successMessage", "Item deleted successfully!");
		return "redirect:/product";
		
	}

	@RequestMapping(value = "/searchItemRecords",
			method = RequestMethod.GET)
	public String searchProducts(Model model, @RequestParam("product") String productName, @RequestParam("item") String itemSearch ) throws Exception{

		Product product = productService.findProductByName(productName);
		
		List<Item> itemList = new ArrayList<>();
		for (Item item : product.getItemList()) {
			if(item.getName().toLowerCase().contains(itemSearch.toLowerCase())) {
				itemList.add(item);
			}
		}
		product.setItemList(itemList);
		
		model.addAttribute("product", product);
		return "product-record";

	}
	
	@RequestMapping(value = "/loadItemByProductId",
			method = RequestMethod.GET)
	@ResponseBody
	public String loadItemByProduct(@RequestParam Long id) {
		System.out.println("Searching items for product id " + id);
		return new Gson().toJson(productService.findProductById(id).getItemIdAndName());
	}
	
	@RequestMapping(value = "/loadUnitByItemId",
			method = RequestMethod.GET)
	@ResponseBody
	public String loadUnitByItem(@RequestParam Long id) {
		System.out.println("Searching units for item id " + id);
		return new Gson().toJson(itemService.findItemById(id).getUnits());
	}
	
	@RequestMapping(value = "/loadMaxQuantityByItemIdAndUnit",
			method = RequestMethod.GET)
	@ResponseBody
	public String loadAvailableStockByItem(@RequestParam Long itemId, @RequestParam String unit) {
		System.out.println("Searching available stock for item id - " + itemId + " and unit - " + unit);
		return String.valueOf(itemService.findItemById(itemId).getQuantity(unit));
	}
	
	@RequestMapping(value = "/loadMaxQuantityByItemIdAndUnitAndStockOutId",
			method = RequestMethod.GET)
	@ResponseBody
	public String checkIfQuantityPermittedByItem(@RequestParam Long itemId, 
			@RequestParam String unit, @RequestParam Long stockOutId) {
		
		Integer quantity = itemService.findItemById(itemId).getQuantity(unit)
				+ stockOutService.findStockOutById(stockOutId).getQuantity();
		System.out.println("Max quantity for stockOut edit: " + quantity + " for id: " + stockOutId );
		return String.valueOf(quantity);
		
	}

}
