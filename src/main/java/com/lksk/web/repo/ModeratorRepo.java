package com.lksk.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lksk.web.model.Moderator;

public interface ModeratorRepo extends JpaRepository<Moderator, Long>{
	
}
