package com.lksk.web.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Moderator {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String phone;
	private String address;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="user_id", unique=true)
	private User user;
	
	@OneToMany(mappedBy="receivedBy")
	private List<StockIn> stockInList;
	
	@OneToMany(mappedBy="dispatchedBy")
	private List<StockOut> stockOutList;
	
}
