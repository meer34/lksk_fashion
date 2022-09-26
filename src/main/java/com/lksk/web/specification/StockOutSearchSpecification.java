package com.lksk.web.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.lksk.web.model.StockOut;
import com.lksk.web.util.SearchCriteria;
import com.lksk.web.util.SearchSpecification;

@SuppressWarnings("serial")
public class StockOutSearchSpecification extends SearchSpecification<StockOut> implements Specification<StockOut>{

	public StockOutSearchSpecification(List<SearchCriteria> criteriaList) {
		super(criteriaList);
	}
	
}
