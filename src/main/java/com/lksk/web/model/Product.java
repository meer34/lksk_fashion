package com.lksk.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique=true)
	private String name;
	
	private String iUnit;
	
	@OneToMany(mappedBy="product")
	private List<Item> itemList;
	
	public List<List<Object>> getItemIdAndName() {
		List<List<Object>> nameIdList = new ArrayList<>();
		List<Object> nameId = null;
		
		for (Item item : itemList) {
			nameId = new ArrayList<>();
			nameId.add(item.getId());
			nameId.add(item.getName());
			nameIdList.add(nameId);
		}
		return nameIdList;
	}
	
}
