package com.lksk.web.model;

import java.util.List;

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
public class Party {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String phone;
	private String address;
	private String gst;
	
	@OneToMany(mappedBy="party")
	private List<StockIn> stockInList;
	
	@OneToMany(mappedBy="party")
	private List<StockOut> stockOutList;
	
	@Override
	public String toString() {
		return name + "~" + phone + "~" + address + "~" + gst;
	}
	
}
