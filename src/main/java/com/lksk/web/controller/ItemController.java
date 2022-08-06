package com.lksk.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lksk.web.model.Item;
import com.lksk.web.service.ItemService;

@Controller
public class ItemController {
	
	@Autowired
	ItemService itemService;
	
	@GetMapping("/item")
	public String showItemPage(Model model) {
		model.addAttribute("itemList", itemService.getAllItems());
		return "item";
	}
	
	@GetMapping("/createItemPage")
	public String showCreateItemPage(Model model) {
		return "item-popup";
	}
	
	@RequestMapping(value = "/createItem",
			method = RequestMethod.POST)
	public String createItem(Model model, Item item, 
			RedirectAttributes redirectAttributes) throws Exception{
		item = itemService.saveItemToDB(item);
		
		redirectAttributes.addFlashAttribute("successMessage", item.getIName() + " item added successfully!");
		return "redirect:/item";
		
	}
	
	@RequestMapping(value = "/performItemAction",
			method = RequestMethod.GET)
	public String performItemAction(RedirectAttributes redirectAttributes,
			@RequestParam("action") String action,
			@RequestParam("iName") String iName) throws Exception{
		
		System.out.println("Got " + action + " action request for item " + iName);
		
		if(action.equalsIgnoreCase("View")) {
			
		} else if(action.equalsIgnoreCase("Edit")) {
			
		} else if(action.equalsIgnoreCase("Print")) {
			
		} else if(action.equalsIgnoreCase("Delete")) {
			itemService.deleteItemById(iName);
			redirectAttributes.addFlashAttribute("successMessage", iName + " item deleted successfully!");
			
		} else {
			System.out.println();
		}
		
		return "redirect:/item";
		
	}
	
}
