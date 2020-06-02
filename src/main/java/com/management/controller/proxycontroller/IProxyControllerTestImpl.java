package com.management.controller.proxycontroller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.management.controller.StudentManagementController;

@Deprecated
@Controller
public class IProxyControllerTestImpl implements IProxyControllerTest{
	@Autowired()
	private StudentManagementController studentMan;
	
	@Override
	public String gotoStudent(HttpServletRequest request) {
		System.out.println(studentMan);
		System.out.println(studentMan.gotoStudent(request));
		return studentMan.gotoStudent(request);
	}

}
