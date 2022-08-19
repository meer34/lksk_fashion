package com.lksk.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lksk.web.model.Item;

public interface ItemRepo extends JpaRepository<Item, Long>{
	
	List<Item> findByProduct(String name);
	
	@Query("select name from Item p where p.product = ?1")
	List<String> findItemNamesByProduct(String name);
	
	@Query("select name from Item")
	List<String> findAllItemNames();

	List<Item> findByProductAndNameContainingIgnoreCase(String product, String item);

	List<Item> findItemByNameContainingIgnoreCase(String name);
	
	@Query("select unit from Item p where p.id = ?1")
	List<String> findUnitsByItemId(Long name);
	
	@Query("select p from Item p where p.name = ?1")
	Item findByName(String name);

	List<Item> findAllByOrderByIdDesc();

}
