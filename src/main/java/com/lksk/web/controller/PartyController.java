package com.lksk.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lksk.web.model.Party;
import com.lksk.web.service.PartyService;

@Controller
public class PartyController {
	
	@Autowired
	PartyService partyService;
	
	@GetMapping("/party")
	public String showPartyPage(Model model) {
		model.addAttribute("partyList", partyService.getAllUsers());
		return "party";
	}
	
	@GetMapping("/add-party-page")
	public String showAddPartyPage(Model model) {
		return "party-create";
	}
	
	@RequestMapping(value = "/create-party",
			method = RequestMethod.POST)
	public String createParty(Model model, Party admin, 
			RedirectAttributes redirectAttributes) throws Exception{
		
		admin = partyService.saveUserToDB(admin);
		redirectAttributes.addFlashAttribute("successMessage", "New user " + admin.getName() + " added successfully as Admin!");
		return "redirect:/party";
		
	}
	
	@RequestMapping(value = "/performPartyPageAction",
			method = RequestMethod.GET)
	public String performItemAction(RedirectAttributes redirectAttributes, Model model,
			@RequestParam("action") String action,
			@RequestParam("id") String id,
			@RequestParam("name") String name) throws Exception{
		
		System.out.println("Got " + action + " action request for party " + name);
		
		if(action.equalsIgnoreCase("View")) {
			model.addAttribute("party", partyService.findUserById(Long.parseLong(id)));
			return "party";
			
		} else if(action.equalsIgnoreCase("Edit")) {
			redirectAttributes.addFlashAttribute("party", partyService.findUserById(Long.parseLong(id)));
			return "redirect:/party";
			
		} else if(action.equalsIgnoreCase("Delete")) {
			partyService.deleteUserById(Long.parseLong(id));
			redirectAttributes.addFlashAttribute("successMessage", "Party user \""+ name +"\" deleted successfully!");
			return "redirect:/party";
			
		} else {
			System.out.println("Incorrect page action received!");
			return "error";
		}
		
	}
	
}
