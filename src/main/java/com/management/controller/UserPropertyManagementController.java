package com.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.management.service.UserManagement;

@Controller
@RequestMapping("/management/user")
public class UserPropertyManagementController {
	@Autowired
	private UserManagement service;
	
	@RequestMapping("gotoUser")
	public String gotoUser() {
		return "user_view";
	}
	
	
}
