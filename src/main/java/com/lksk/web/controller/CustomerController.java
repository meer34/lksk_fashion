package com.lksk.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lksk.web.model.Customer;
import com.lksk.web.service.CustomerService;

@Controller
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	@GetMapping("/customer")
	public String showPartyPage(Model model) {
		model.addAttribute("customerList", customerService.getAllUsers());
		return "customer";
	}
	
	@GetMapping("/add-customer-page")
	public String showAddPartyPage(Model model) {
		return "customer-create";
	}
	
	@RequestMapping(value = "/createCustomer",
			method = RequestMethod.POST)
	public String createParty(Model model, Customer customer, 
			RedirectAttributes redirectAttributes) throws Exception{
		
		customer = customerService.saveUserToDB(customer);
		redirectAttributes.addFlashAttribute("successMessage", "New customer " + customer.getName() + " added successfully as Admin!");
		return "redirect:/customer";
		
	}
	
	@RequestMapping(value = "/performCustomerPageAction",
			method = RequestMethod.GET)
	public String performItemAction(RedirectAttributes redirectAttributes, Model model,
			@RequestParam("action") String action,
			@RequestParam("id") String id,
			@RequestParam("name") String name) throws Exception{
		
		System.out.println("Got " + action + " action request for customer " + name);
		
		if(action.equalsIgnoreCase("View")) {
			model.addAttribute("customer", customerService.findUserById(Long.parseLong(id)));
			return "customer";
			
		} else if(action.equalsIgnoreCase("Edit")) {
			redirectAttributes.addFlashAttribute("customer", customerService.findUserById(Long.parseLong(id)));
			return "redirect:/customer";
			
		} else if(action.equalsIgnoreCase("Delete")) {
			customerService.deleteUserById(Long.parseLong(id));
			redirectAttributes.addFlashAttribute("successMessage", "Customer \""+ name +"\" deleted successfully!");
			return "redirect:/customer";
			
		} else {
			System.out.println("Incorrect page action received!");
			return "error";
		}
		
	}
	
}
