package com.lksk.web.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
		return "order-popup";
	}

	@RequestMapping(value = "/addOrder",
			method = RequestMethod.POST)
	public String addOrder(Model model, CustOrder order, 
			RedirectAttributes redirectAttributes) throws Exception{

		if(StringUtils.cleanPath(order.getSPhoto().getOriginalFilename()).contains("..")) System.out.println("Photo not a a valid file");
		order.setSPhotoBlob(Base64.getEncoder().encodeToString(order.getSPhoto().getBytes()));
		
		if(StringUtils.cleanPath(order.getColour().getOriginalFilename()).contains("..")) System.out.println("Colour not a a valid file");
		order.setColourBlob(Base64.getEncoder().encodeToString(order.getColour().getBytes()));
		
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


	@RequestMapping(value = "/openOrderActionPage",
			method = RequestMethod.GET)
	public String performOrderAction(RedirectAttributes redirectAttributes, Model model,
			@RequestParam("action") String action,
			@RequestParam("id") String id) throws Exception{

		System.out.println("Got " + action + " action request for id " + id);

		if(action.equalsIgnoreCase("View")) {
			CustOrder order = orderService.findOrderById(Long.parseLong(id));
			model.addAttribute("order", order);
			model.addAttribute("header", order.getMark());
			model.addAttribute("submitValue", "Print");
			return "order-view";

		} else if(action.equalsIgnoreCase("Edit")) {
			CustOrder order = orderService.findOrderById(Long.parseLong(id));
			model.addAttribute("order", order);
			model.addAttribute("header", order.getMark());
			model.addAttribute("submitValue", "Edit");
			return "order-view";

		} else if(action.equalsIgnoreCase("Print")) {
			redirectAttributes.addFlashAttribute("order", orderService.findOrderById(Long.parseLong(id)));
			return "redirect:/order";

		} else if(action.equalsIgnoreCase("Delete")) {
			orderService.deleteOrderById(Long.parseLong(id));
			redirectAttributes.addFlashAttribute("successMessage", "Order with id " + id + " deleted successfully!");

		} else {
			System.out.println();
		}

		return "redirect:/order";

	}

	@RequestMapping(value = "/performOrderAction",
			method = RequestMethod.POST)
	public String performOrderAction(RedirectAttributes redirectAttributes, Model model, CustOrder order,
			@RequestParam("action") String action,
			@RequestParam("id") String id) throws Exception{

		System.out.println("Got " + action + " action request for id " + id);

		if(action.equalsIgnoreCase("Save")) {
			String fileName = StringUtils.cleanPath(order.getSPhoto().getOriginalFilename());
			if(fileName.contains("..")) System.out.println("not a a valid file");
			order.setSPhotoBlob(Base64.getEncoder().encodeToString(order.getSPhoto().getBytes()));

			order = orderService.saveOrderToDB(order);

			redirectAttributes.addFlashAttribute("successMessage", "Order edited successfully!");
			return "redirect:/order";

		} else if(action.equalsIgnoreCase("Print")) {
			redirectAttributes.addFlashAttribute("order", orderService.findOrderById(Long.parseLong(id)));
			return "redirect:/order";

		} else {
			System.out.println();
			return "error";
		}
	}

}
