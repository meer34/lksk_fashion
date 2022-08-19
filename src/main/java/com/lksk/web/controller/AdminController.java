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
import com.lksk.web.model.User;
import com.lksk.web.service.AdminService;

@Controller
public class AdminController {

	@Autowired AdminService adminService;

	@GetMapping("/admin")
	public String showAdminPage(Model model) {
		model.addAttribute("adminList", adminService.getAllUsers());
		return "admin";
	}

	@GetMapping("/addAdminPage")
	public String showAddAdminPage(Model model) {
		model.addAttribute("admin", new Admin());
		model.addAttribute("header", "Create Admin");
		return "admin-create";
	}

	@RequestMapping(value = "/createAdmin",
			method = RequestMethod.POST)
	public String createAdmin(Model model, Admin admin, 
			RedirectAttributes redirectAttributes) throws Exception{

		if(admin.getId() == null) {
			admin.setUser(new User(admin.getName(), admin.getPhone(), true, "ADMIN"));
			admin = adminService.saveUserToDB(admin);
			
		} else {
			Admin tempAdmin = adminService.findUserById(admin.getId());
			
			tempAdmin.setName(admin.getName());
			tempAdmin.setPhone(admin.getPhone());
			tempAdmin.setAddress(admin.getAddress());
			
			tempAdmin.getUser().setUsername(admin.getName());
			tempAdmin.getUser().setPhone(admin.getPhone());
			
			admin = adminService.saveUserToDB(tempAdmin);
		}
		
		redirectAttributes.addFlashAttribute("successMessage", "New user " + admin.getName() + " added successfully as Admin!");
		return "redirect:/admin";

	}

	@RequestMapping(value = "/OpenAdminActionPage",
			method = RequestMethod.GET)
	public String performOrderAction(RedirectAttributes redirectAttributes, Model model,
			@RequestParam("action") String action,
			@RequestParam("id") String id) throws Exception{

		System.out.println("Got " + action + " action request for id " + id);

		if(action.equalsIgnoreCase("View")) {
			model.addAttribute("admin", adminService.findUserById(Long.parseLong(id)));
			return "view-admin";

		} else if(action.equalsIgnoreCase("Edit")) {
			model.addAttribute("admin", adminService.findUserById(Long.parseLong(id)));
			model.addAttribute("header", "Edit Admin");
			return "admin-create";

		} else if(action.equalsIgnoreCase("Delete")) {
			adminService.deleteUserById(Long.parseLong(id));
			redirectAttributes.addFlashAttribute("successMessage", "Admin with id " + id + " deleted successfully!");
			return "redirect:/admin";

		} else {
			System.out.println();
		}

		return "redirect:/testerror";

	}

}
