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
public class StockOut {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Lob
	private String itemImageBlob;
	@Lob
	private String colourImageBlob;

	private String scanCode;

	@ManyToOne
	@JoinColumn(name="stockOutList")
	private Item item;

	@ManyToOne
	@JoinColumn(name ="customer")
	private Customer customer;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;

	private String size;
	private String unit;
	private Integer quantity;
	private Double price;
	private Double amount;
	private String colour;

	@ManyToOne
	@JoinColumn(name ="dispatchedBy")
	private Moderator dispatchedBy;

	private String dispatchedFrom;
	private String remakrs;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(scanCode);
		sb.append("~").append(dispatchedFrom).append("~").append(remakrs);
		if(item != null) {
			sb.append("~").append(item.getName());
			if(item.getProduct() != null) sb.append("~").append(item.getProduct().getName());
		}
		if(customer != null) sb.append("~").append(customer.getName());
		if(dispatchedBy != null) sb.append("~").append(dispatchedBy.getName());

		return sb.toString();
	}


}
