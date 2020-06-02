package com.management.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.management.constant.Constants;

import cn.hutool.json.JSONObject;

public class HomePageInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		String username = "error";
		if (session.getAttribute(Constants.CURRENT_USER_KEY)!=null) {
			JSONObject json = (JSONObject)session.getAttribute(Constants.CURRENT_USER_KEY);
			username = json.getStr("username");
		}
		request.setAttribute("username", username);
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
		//System.out.println("访问完成， modelAndView="+modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		//System.out.println("加载完成");
	}
}
