package com.lksk.web.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lksk.web.model.CustOrder;
import com.lksk.web.service.OrderService;
import com.lksk.web.service.ProductService;
import com.lksk.web.util.LKSKConstants;

@Controller
public class OrderController {

	@Autowired OrderService orderService;
	@Autowired ProductService productService;

	@GetMapping("/order")
	public String showStockIn(Model model,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,
			@RequestParam(value="fromDate", required = false) String fromDate,
			@RequestParam(value="toDate", required = false) String toDate,
			@RequestParam(value="keyword", required = false) String keyword) throws ParseException {
		
		Page<CustOrder> listPage = null;
		
		if(keyword == null && fromDate == null && toDate == null) {
			System.out.println("Order home page");
			listPage = orderService.getAllOrders(page.orElse(1) - 1, size.orElse(LKSKConstants.INITIAL_PAGE_SIZE));
			
		} else {
			System.out.println("Searching Orders for fromDate:" + fromDate + " and toDate:" +toDate +" and keyword:" + keyword);
			listPage = orderService.searchOrdersByDateAndKeyword(keyword, fromDate, toDate, page.orElse(1) - 1, size.orElse(LKSKConstants.INITIAL_PAGE_SIZE));
			
			model.addAttribute("fromDate", fromDate);
			model.addAttribute("toDate", toDate);
			model.addAttribute("keyword", keyword);
			
		}
		
		model.addAttribute("listPage", listPage);
		int totalPages = listPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		return "order";
	}

	@GetMapping("/addOrderPage")
	public String createOrder(Model model) {
		model.addAttribute("products", productService.getAllProducts());
		return "order-create";
	}

	@RequestMapping(value = "/addOrder",
			method = RequestMethod.POST)
	public String addOrder(Model model, CustOrder order, 
			RedirectAttributes redirectAttributes) throws Exception{
		
		orderService.saveOrderToDB(order);
		redirectAttributes.addFlashAttribute("successMessage", "Order for mark " + order.getMark() + " placed successfully!");
		return "redirect:/order";

	}

	@RequestMapping(value = "/viewOrder",
			method = RequestMethod.GET)
	public String viewOrder(Model model, @RequestParam("id") String id) throws Exception{
		
		System.out.println("Got view request for order id " + id);
		CustOrder order = orderService.findOrderById(Long.parseLong(id));
		model.addAttribute("order", order);
		model.addAttribute("header", order.getMark());
		model.addAttribute("submitValue", "Print");
		return "order-view";
		
	}
	
	@RequestMapping(value = "/editOrder",
			method = RequestMethod.GET)
	public String editOrder(Model model, @RequestParam("id") String id) throws Exception{
		
		System.out.println("Got edit request for order id " + id);
		CustOrder order = orderService.findOrderById(Long.parseLong(id));
		model.addAttribute("order", order);
		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("header", order.getMark());
		model.addAttribute("submitValue", "Edit");
		return "order-view";
		
	}
	
	@RequestMapping(value = "/deleteOrder",
			method = RequestMethod.GET)
	public String deleteOrder(RedirectAttributes redirectAttributes, Model model,
			@RequestParam("id") String id) throws Exception{
		
		System.out.println("Got delete request for order id " + id);
		orderService.deleteOrderById(Long.parseLong(id));
		redirectAttributes.addFlashAttribute("successMessage", "Order with id " + id + " deleted successfully!");
		return "redirect:/order";
		
	}

	@RequestMapping(value = "/saveOrderEdit",
			method = RequestMethod.POST)
	public String saveOrderEdit(RedirectAttributes redirectAttributes, CustOrder order,
			@RequestParam("id") String id) throws Exception{

		System.out.println("Got edit save request for order id " + id);
		order = orderService.saveOrderToDB(order);

		redirectAttributes.addFlashAttribute("successMessage", "Order edited successfully!");
		return "redirect:/order";

	}

}
