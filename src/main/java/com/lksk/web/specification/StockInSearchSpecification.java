package com.lksk.web.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.lksk.web.model.StockIn;
import com.lksk.web.util.SearchCriteria;
import com.lksk.web.util.SearchSpecification;

@SuppressWarnings("serial")
public class StockInSearchSpecification extends SearchSpecification<StockIn> implements Specification<StockIn>{

	public StockInSearchSpecification(List<SearchCriteria> criteriaList) {
		super(criteriaList);
	}
	
}
