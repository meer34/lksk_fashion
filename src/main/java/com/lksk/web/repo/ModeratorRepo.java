package com.lksk.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lksk.web.model.Moderator;

public interface ModeratorRepo extends JpaRepository<Moderator, Long>{

	List<Moderator> findByPhoneAndIdNot(String phone, Long id);
	
}
