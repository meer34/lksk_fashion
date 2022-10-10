package com.lksk.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lksk.web.model.Party;

public interface PartyRepo extends JpaRepository<Party, Long>{
	
}
