package com.lksk.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lksk.web.model.Product;
import com.lksk.web.service.ProductService;
import com.lksk.web.service.ItemService;

@Controller
public class ProductController {
	
	@Autowired ProductService productService;
	@Autowired ItemService itemService;
	
	@GetMapping("/product")
	public String showProductPage(Model model) {
		model.addAttribute("productList", productService.getAllProducts());
		return "product";
	}
	
	@GetMapping("/createProductPage")
	public String showCreateProductPage(Model model) {
		model.addAttribute("product", new Product());
		model.addAttribute("header", "Create Product");
		return "product-popup";
	}
	
	@RequestMapping(value = "/createProduct",
			method = RequestMethod.POST)
	public String createProduct(Model model, Product product, 
			RedirectAttributes redirectAttributes) throws Exception{
		if(product.getId() == null) {
			product = productService.saveProductToDB(product);
			
		} else {
			product.setId(productService.findProductById(product.getId()).getId());
			product = productService.saveProductToDB(product);
		}
		
		redirectAttributes.addFlashAttribute("successMessage", product.getName() + " product added successfully!");
		return "redirect:/product";
		
	}
	
	@RequestMapping(value = "/searchProductRecords",
			method = RequestMethod.GET)
	public String searchProducts(Model model, @RequestParam("product") String product ) throws Exception{

		List<Product> productList= productService.findProductsByNameContaining(product);
		System.out.println("Search found for product:" + product + " with size: " + productList.size());
		
		model.addAttribute("productList", productList);
		return "product";

	}
	
	@RequestMapping(value = "/performProductAction",
			method = RequestMethod.GET)
	public String performProductAction(RedirectAttributes redirectAttributes, Model model,
			@RequestParam("action") String action,
			@RequestParam("id") Long id) throws Exception{
		
		System.out.println("Got " + action + " action request for product " + id);
		
		if(action.equalsIgnoreCase("Records")) {
			Product product = productService.findProductById(id);
			model.addAttribute("product", product);
			return "product-record";
			
		} else if(action.equalsIgnoreCase("Edit")) {
			model.addAttribute("product", productService.findProductById(id));
			model.addAttribute("header", "Edit Product");
			return "product-popup";

		} else if(action.equalsIgnoreCase("Print")) {
			
		} else if(action.equalsIgnoreCase("Delete")) {
			productService.deleteProductById(id);
			redirectAttributes.addFlashAttribute("successMessage", id + " product deleted successfully!");
			
		} else {
			System.out.println();
		}
		
		return "redirect:/product";
		
	}
	
}
