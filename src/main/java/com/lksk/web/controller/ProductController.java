package com.lksk.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lksk.web.model.Product;
import com.lksk.web.service.ItemService;
import com.lksk.web.service.ProductService;

@Controller
public class ProductController {
	
	@Autowired
	ProductService productService;
	@Autowired ItemService itemService;
	
	@GetMapping("/product")
	public String showProductPage(Model model) {
		model.addAttribute("productList", productService.getAllProducts());
		return "product";
	}
	
	@GetMapping("/createProductPage")
	public String showCreateProductPage(Model model) {
		model.addAttribute("items", itemService.getAllItems());
		return "product-popup";
	}
	
	@RequestMapping(value = "/createProduct",
			method = RequestMethod.POST)
	public String createProduct(Model model, Product product, 
			RedirectAttributes redirectAttributes) throws Exception{
		productService.saveProductToDB(product);
		
		redirectAttributes.addFlashAttribute("successMessage", product.getName() + " product added successfully!");
		return "redirect:/item";
		
	}
	
}
