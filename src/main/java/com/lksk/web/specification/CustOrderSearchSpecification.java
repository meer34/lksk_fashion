package com.lksk.web.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.lksk.web.model.CustOrder;
import com.lksk.web.util.SearchCriteria;
import com.lksk.web.util.SearchSpecification;

@SuppressWarnings("serial")
public class CustOrderSearchSpecification extends SearchSpecification<CustOrder> implements Specification<CustOrder>{

	public CustOrderSearchSpecification(List<SearchCriteria> criteriaList) {
		super(criteriaList);
	}
	
}
