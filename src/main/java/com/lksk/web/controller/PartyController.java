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

	@Autowired private PartyService partyService;

	@GetMapping("/party")
	public String showPartyPage(Model model) {
		model.addAttribute("partyList", partyService.getAllUsers());
		return "party";
	}

	@GetMapping("/add-party-page")
	public String showAddPartyPage(Model model) {
		model.addAttribute("party", new Party());
		model.addAttribute("header", "Create Party");
		return "party-create";
	}

	@RequestMapping(value = "/create-party",
			method = RequestMethod.POST)
	public String createParty(Model model, Party party, 
			RedirectAttributes redirectAttributes) throws Exception{

		party = partyService.saveUserToDB(party);
		redirectAttributes.addFlashAttribute("successMessage", "New user " + party.getName() + " added successfully as Party!");
		return "redirect:/party";

	}

	@RequestMapping(value = "/OpenPartyActionPage",
			method = RequestMethod.GET)
	public String performOrderAction(RedirectAttributes redirectAttributes, Model model,
			@RequestParam("action") String action,
			@RequestParam("id") String id) throws Exception{

		System.out.println("Got " + action + " action request for id " + id);

		if(action.equalsIgnoreCase("View")) {
			model.addAttribute("party", partyService.findUserById(Long.parseLong(id)));
			return "view-party";
			
		} else if(action.equalsIgnoreCase("Delete")) {
			partyService.deleteUserById(Long.parseLong(id));
			redirectAttributes.addFlashAttribute("successMessage", "Party with id " + id + " deleted successfully!");
			return "redirect:/party";
			
		} else {
			System.out.println();
		}

		return "redirect:/testerror";

	}

}
