package com.lksk.web.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique=true)
	private String name;
	
	@ManyToOne
	@JoinColumn(name="product")
	private Product product;
	
	private String unit;
	private Integer quantity;
	
	@OneToMany(mappedBy="item")
	private List<StockIn> stockInList;
	
	@OneToMany(mappedBy="item")
	private List<StockOut> stockOutList;
	
	
	public int getQuantity() {
		quantity = 0;
		for (StockIn stockIn : stockInList) {
			quantity += stockIn.getQuantity();
		}
		for (StockOut stockOut : stockOutList) {
			quantity -= stockOut.getQuantity();
		}
		return quantity;
		
	}
	
	public int getStockInQuantity() {
		int stockInQuantity = 0;
		for (StockIn stockIn : stockInList) {
			stockInQuantity += stockIn.getQuantity();
		}
		return stockInQuantity;
	}
	
	public int getStockOutQuantity() {
		int stockOutQuantity = 0;
		for (StockOut stockOut : stockOutList) {
			stockOutQuantity += stockOut.getQuantity();
		}
		return stockOutQuantity;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
