package com.lksk.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lksk.web.model.Moderator;
import com.lksk.web.service.ModeratorService;

@Controller
public class ModeratorController {
	
	@Autowired
	ModeratorService moderatorService;
	
	@GetMapping("/moderator")
	public String showAdminPage(Model model) {
		model.addAttribute("moderatorList", moderatorService.getAllUsers());
		return "moderator";
	}
	
	@GetMapping("/addModeratorPage")
	public String showAddModeratorPage(Model model) {
		return "moderator-create";
	}
	
	@RequestMapping(value = "/createModerator",
			method = RequestMethod.POST)
	public String createModerator(Model model, Moderator moderator, 
			RedirectAttributes redirectAttributes) throws Exception{
		
		moderator = moderatorService.saveUserToDB(moderator);
		redirectAttributes.addFlashAttribute("successMessage", "New user " + moderator.getName() + " added successfully as Moderator!");
		return "redirect:/moderator";
		
	}
	
	@RequestMapping(value = "/performModeratorPageAction",
			method = RequestMethod.GET)
	public String performItemAction(RedirectAttributes redirectAttributes, Model model,
			@RequestParam("action") String action,
			@RequestParam("id") String id,
			@RequestParam("name") String name) throws Exception{
		
		System.out.println("Got " + action + " action request for " + name);
		
		if(action.equalsIgnoreCase("View")) {
			model.addAttribute("admin", moderatorService.findUserById(Long.parseLong(id)));
			return "admin";
			
		} else if(action.equalsIgnoreCase("Edit")) {
			redirectAttributes.addFlashAttribute("stockOut", moderatorService.findUserById(Long.parseLong(id)));
			return "redirect:/moderator";
			
		} else if(action.equalsIgnoreCase("Delete")) {
			moderatorService.deleteUserById(Long.parseLong(id));
			redirectAttributes.addFlashAttribute("successMessage", "Moderator user \"" + name + "\" deleted successfully!");
			return "redirect:/moderator";
		} else {
			System.out.println("Incorrect page action received!");
			return "error";
		}
		
	}
	
}
