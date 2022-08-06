package com.lksk.web.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LKSKPageController {
	
	@GetMapping("/")
	public String showLandingPage() {
		return "home";
	}
	
	@GetMapping("/home")
	public String showHome() {
		return "home";
	}
	
	@GetMapping("/index")
	public String showIndex() {
		return "home";
	}
	
	@RequestMapping("/login")
	public String login() {
		return "index";
	}
	
}
