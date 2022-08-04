package com.lksk.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lksk.web.model.Item;

public interface ItemRepo extends JpaRepository<Item, String>{
	
}
