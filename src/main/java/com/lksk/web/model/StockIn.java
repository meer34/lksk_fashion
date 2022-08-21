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
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class StockIn {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Transient
	private MultipartFile sPhoto;
	@Transient
	private MultipartFile colour;
	@Lob
	private String sPhotoBlob;
	@Lob
	private String colourBlob;
	
	private String scanCode;
	
	@ManyToOne
	@JoinColumn(name="item")
	private Item item;
	
	@ManyToOne
	@JoinColumn(name ="party")
	private Party party;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	
	private String size;
	private String unit;
	private Integer quantity;
	private Double price;
	private Double amount;
	
	@ManyToOne
	@JoinColumn(name ="receivedBy")
	private Moderator receivedBy;
	
	private String receivedIn;
	private String remakrs;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(scanCode);
		sb.append("~").append(receivedIn).append("~").append(remakrs);
		if(item != null) {
			sb.append("~").append(item.getName());
			if(item.getProduct() != null) sb.append("~").append(item.getProduct().getName());
		}
		if(party != null) sb.append("~").append(party.getName());
		if(receivedBy != null) sb.append("~").append(receivedBy.getName());

		return sb.toString();
	}

}
