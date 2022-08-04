package com.lksk.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lksk.web.model.Admin;

public interface AdminRepo extends JpaRepository<Admin, Long>{
	
}
