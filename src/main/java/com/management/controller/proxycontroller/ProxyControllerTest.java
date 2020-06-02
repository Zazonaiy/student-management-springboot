package com.management.controller.proxycontroller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.management.controller.StudentManagementController;
import com.management.proxy.ManProxy;

@Deprecated
@Controller
@RequestMapping("/management/proxy/student")
public class ProxyControllerTest implements IProxyControllerTest{
	
	private IProxyControllerTest proxy = (IProxyControllerTest) new ManProxy().bind(new IProxyControllerTestImpl());
	//@Autowired()
	//private StudentManagementController studentMan;
	
	
	@Override
	@RequestMapping("/gotoStudent")
	public String gotoStudent(HttpServletRequest request) {
		return proxy.gotoStudent(request);
		//return studentMan.gotoStudent(request);
	}
}
