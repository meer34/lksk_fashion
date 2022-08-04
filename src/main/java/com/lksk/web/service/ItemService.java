package com.lksk.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lksk.web.model.Item;
import com.lksk.web.repo.ItemRepo;

@Service
public class ItemService {

	@Autowired
	private ItemRepo itemRepo;

	public Item saveItemToDB(Item item) {
		return itemRepo.save(item);
	}

	public List<Item> getAllItems() {
		return itemRepo.findAll();
	}

	public void deleteItemById(String id) {
		itemRepo.deleteById(id);
	}

}
