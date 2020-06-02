package com.management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.management.interceptor.HomePageInterceptor;
import com.management.interceptor.LoginInterceptor;
import com.management.interceptor.ManagementInterceptor;
import com.management.interceptor.StudentManagementInterceptor;


@Configuration
public class StudentManagementConfig implements WebMvcConfigurer {
	@Autowired
	private StudentManagementInterceptor studentManagementInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(studentManagementInterceptor).addPathPatterns("/**");
		registry.addInterceptor(new HomePageInterceptor()).addPathPatterns("/login/toHome/*");
		registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/login/loginpage/*");
		registry.addInterceptor(new ManagementInterceptor()).addPathPatterns("/management/**");
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/fileupload/**").addResourceLocations("file:/G:/Java_learning/fileupload/");
	}
}
