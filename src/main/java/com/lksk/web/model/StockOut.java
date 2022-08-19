package com.lksk.web.model;

import java.util.Date;

import javax.persistence.CascadeType;
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
public class StockOut {
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
	
	private String sCode;
	
	@ManyToOne
	@JoinColumn(name="stockOutList")
	private Item item;
	
	@ManyToOne(cascade = CascadeType.ALL)
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
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name ="dispatchedBy")
	private Moderator dispatchedBy;
	
	private String dispatchedFrom;
	private String remakrs;
	
	@Override
	public String toString() {
		return sCode + "~" + item.getProduct().getName() + "~" + item.getName() + "~" + party.getName() + "~" + dispatchedBy.getName() + "~" + dispatchedFrom + "~" + remakrs;
	}
	
	
}
