package com.lksk.web.service;

import java.util.List;
import java.util.Optional;

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
	
	public Item findItemById(Long id) {
		Optional<Item> op = itemRepo.findById(id);
		return !op.isEmpty() ? op.get(): null;
	}
	
	public Item findItemByName(String name) {
		return itemRepo.findByName(name);
	}

	public List<Item> getAllItems() {
		return itemRepo.findAllByOrderByIdDesc();
	}
	
	public List<Item> getItemsByProduct(String name) {
		return itemRepo.findByProduct(name);
	}

	public void deleteItemById(Long id) {
		itemRepo.deleteById(id);
	}
	
	public List<String> findAllItemNames() {
		return itemRepo.findAllItemNames();
	}

	public List<String> findItemNamesByProductName(String name) {
		return itemRepo.findItemNamesByProduct(name);
	}

	public List<Item> findItemksByNameContaining(String name) {
		return itemRepo.findItemByNameContainingIgnoreCase(name);
	}

	public List<String> findUnitsByItemId(Long id) {
		return itemRepo.findUnitsByItemId(id);
	}

}
