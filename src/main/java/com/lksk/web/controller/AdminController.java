package com.lksk.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lksk.web.model.Admin;
import com.lksk.web.service.AdminService;

@Controller
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@GetMapping("/admin")
	public String showAdminPage(Model model) {
		model.addAttribute("adminList", adminService.getAllUsers());
		return "admin";
	}
	
	@GetMapping("/addAdminPage")
	public String showAddAdminPage(Model model) {
		return "admin-create";
	}
	
	@RequestMapping(value = "/createAdmin",
			method = RequestMethod.POST)
	public String createAdmin(Model model, Admin admin, 
			RedirectAttributes redirectAttributes) throws Exception{
		
		admin = adminService.saveUserToDB(admin);
		redirectAttributes.addFlashAttribute("successMessage", "New user " + admin.getName() + " added successfully as Admin!");
		return "redirect:/admin";
		
	}
	
	@RequestMapping(value = "/performAdminPageAction",
			method = RequestMethod.GET)
	public String performItemAction(RedirectAttributes redirectAttributes, Model model,
			@RequestParam("action") String action,
			@RequestParam("id") String id,
			@RequestParam("name") String name) throws Exception{
		
		System.out.println("Got " + action + " action request for admin user " + name);
		
		if(action.equalsIgnoreCase("View")) {
			model.addAttribute("admin", adminService.findUserById(Long.parseLong(id)));
			return "admin";
			
		} else if(action.equalsIgnoreCase("Edit")) {
			redirectAttributes.addFlashAttribute("stockOut", adminService.findUserById(Long.parseLong(id)));
			return "redirect:/admin";
			
		} else if(action.equalsIgnoreCase("Delete")) {
			adminService.deleteUserById(Long.parseLong(id));
			redirectAttributes.addFlashAttribute("successMessage", "Admin user \""+ name +"\" deleted successfully!");
			return "redirect:/admin";
			
		} else {
			System.out.println("Incorrect page action received!");
			return "error";
		}
		
	}
	
}
