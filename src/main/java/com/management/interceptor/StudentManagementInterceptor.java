package com.management.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.management.constant.Constants;
import com.management.constant.Property;
import com.management.service.ProxyService;

import cn.hutool.json.JSONObject;

@Component
public class StudentManagementInterceptor implements HandlerInterceptor{
	@Autowired
	private ProxyService dialog;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String url = request.getRequestURL().toString();
		System.out.println("即将访问" + url + " handler=" + handler.getClass());
		if (handler instanceof HandlerMethod) {
			//获取contextPath
			String scheme = request.getScheme();
			String serverName = request.getServerName();
			int port = request.getServerPort();
			String ctxPath = request.getContextPath();
			if (port == 80) {
				ctxPath = scheme+"://"+serverName+ctxPath;
			}else {
				ctxPath = scheme+"://"+serverName+":"+port+ctxPath;
			}
			request.setAttribute("ctxPath", ctxPath);
		
			//HttpSession session = request.getSession();
			//System.out.println("xxxxxxxxxxxxxxxx" + session.getAttribute(Constants.CURRENT_USER_KEY));
			
			HttpSession session = request.getSession();
			System.out.println("xxxxxxxxxxxxxxxx" + session.getAttribute(Constants.CURRENT_USER_KEY));
			if (session.getAttribute(Constants.CURRENT_USER_KEY) != null) {
				JSONObject json = (JSONObject) session.getAttribute(Constants.CURRENT_USER_KEY);
				Integer property = json.getInt("property");
				String photo = json.getStr("photo");
				String userPhone = json.getStr("phonenum");
				request.setAttribute("username", json.getStr("username"));
				request.setAttribute("userproperty", property);
				if (photo != null) {
					request.setAttribute("userphoto", ctxPath + photo);
				}else {
					request.setAttribute("userphoto", ctxPath + "/fileupload/user/default/default.jpg");
				}
				if (userPhone != null) {
					request.setAttribute("userphone", userPhone);
				}else {
					request.setAttribute("userphone", "null");
				}
				String userposition = getPropertyName(property);
				if (userposition.equals("error")) {
					System.out.println("-------------------------property error");
				//	request.getRequestDispatcher(ctxPath + "/login/loginpage").forward(request, response);
				//	return false;
				}
				request.setAttribute("userposition", userposition);
			}
			request.setAttribute("sysTitle", "学生信息管理系统(SpringBoot)");
		
		}
		
		
		
		

		return true;
	}
	private String getPropertyName(Integer property) {
		if (property == null ) {
			return "error";
		}
		if (property == Property.ROOT) {
			return "ROOT";
		}else if (property == Property.TEACHER) {
			return "教师";
		}else if (property == Property.STUDENT) {
			return "学生";
		}else {
			return "error";
		}
	}
	
	private boolean canDoMethod(String method, Integer prop) {
		boolean result = true;
		switch(method) {
			case "deleteStudent" : 
				if (prop > 1) {
					result = false;
				}
				break;
			case "updateStudent" : 
				if (prop > 1) {
					result = false;
				}
				break;
			case "addStudent" : 
				if (prop > 1) {
					result = false;
				}
				break;
			case "removeClassfromUser" :
				if (prop > 1) {
					result = false;
				}
				break;
			case "addClassToUser" : 
				if (prop > 1) {
					result = false;
				}
				break;
			case "deleteClass" : 
				if (prop > 1) {
					result = false;
				}
				break;
			case "addClass" : 
				if (prop > 1) {
					result = false;
				}
				break;
			case "updateClass" :
				if (prop > 1) {
					result = false;
				}
				break;
			case "updateUserByRoot" :
				if (prop != 0) {
					result = false;
				}
				break;
			case "registUserByRoot" :
				if (prop != 0) {
					result = false;
				}
				break;
		}
		return result;
	}
	
	private void doDialog(Class<?> clazz, String method, String username) {
		dialog.doLog(clazz, method, username);
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
		if (handler instanceof HandlerMethod) {
			//权限判断
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			String methodName = handlerMethod.getMethod().getName();
			Integer prop = (Integer)request.getAttribute("userproperty");
			if (!canDoMethod(methodName, prop)) {
				String ctxPath = (String)request.getAttribute("ctxPath");
				response.sendRedirect(ctxPath + "/gotoStudent");
			}
			
			//记录日志
			doDialog(handlerMethod.getClass(), methodName, (String)request.getAttribute("username"));
		}
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		//System.out.println("加载完成");
	}
}
