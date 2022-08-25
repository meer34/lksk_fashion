package com.lksk.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lksk.web.model.CustOrder;
import com.lksk.web.service.OrderService;

@Controller
public class OrderController {

	@Autowired
	OrderService orderService;

	@GetMapping("/order")
	public String showStockIn(Model model) {
		model.addAttribute("orderList", orderService.getAllOrders());
		return "order";
	}

	@GetMapping("/addOrderPage")
	public String createOrder() {
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

	@RequestMapping(value = "/searchOrder",
			method = RequestMethod.GET)
	public String searchOrder(Model model, 
			@RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate,
			@RequestParam("keyword") String keyword ) throws Exception{

		List<CustOrder> orderList = new ArrayList<CustOrder>();

		for (CustOrder order : orderService.searchOrderByDate(fromDate, toDate)) {
			if(order.toString().toLowerCase().contains(keyword.toLowerCase())) {
				orderList.add(order);
			}
		}
		System.out.println("Search size for fromDate:" + fromDate + " and toDate:" +toDate +" and keyword:" + keyword + " is - "+ orderList.size());

		model.addAttribute("orderList", orderList);
		return "order";

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

		System.out.println("Got edit save request for id " + id);
		order = orderService.saveOrderToDB(order);

		redirectAttributes.addFlashAttribute("successMessage", "Order edited successfully!");
		return "redirect:/order";

	}

}
