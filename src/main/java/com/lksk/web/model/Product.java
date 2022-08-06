package com.lksk.web.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Product {
	@Id
	private String name;
	private String size;
	private Integer unit;
	
}
