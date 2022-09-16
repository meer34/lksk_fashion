package com.lksk.web.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CustOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String mark;
	private String supplier;
	
	@Lob
	private String itemImageBlob;
	@Lob
	private String colourImageBlob;
	
	private String size;
	private String sLength;
	private String sWidth;
	private String color;
	private Integer quantity;
	
	@ManyToOne
	@JoinColumn(name="item")
	private Item item;
	
	private String unit;
	private String reference;
	private Double price;
	private Double amount;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	
	private String remarks;

	@Override
	public String toString() {
		return mark + "~" + supplier + "~" + reference + "~" + remarks;
	}
	
}
