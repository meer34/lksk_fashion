package com.lksk.web.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
public class CustOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String mark;
	private String supplier;
	
	@Transient
	private MultipartFile sPhoto;
	@Transient
	private MultipartFile colour;
	@Lob
	private String sPhotoBlob;
	@Lob
	private String colourBlob;
	
	private String size;
	private String sLength;
	private String sWidth;
	private String color;
	private Integer quantity;
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
