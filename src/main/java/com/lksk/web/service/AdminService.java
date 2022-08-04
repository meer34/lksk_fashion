package com.lksk.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lksk.web.model.Admin;
import com.lksk.web.repo.AdminRepo;

@Service
public class AdminService {

	@Autowired
	private AdminRepo adminRepo;

	public Admin saveUserToDB(Admin admin) {
		return adminRepo.save(admin);
	}
	
	public Admin findUserById(Long id) {
		return adminRepo.findById(id).get();
	}

	public List<Admin> getAllUsers() {
		return adminRepo.findAll();
	}

	public void deleteUserById(Long id) {
		adminRepo.deleteById(id);
	}

}
