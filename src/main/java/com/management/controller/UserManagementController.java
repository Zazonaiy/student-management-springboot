package com.management.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.management.cache.StudentCache;
import com.management.constant.Constants;
import com.management.service.ProxyService;
import com.management.service.UserManagement;
import com.management.util.UserUtil;

import cn.hutool.json.JSONObject;

/**
 * 权限管理
 * @author 杨伟豪
 *
 */
@Controller
@RequestMapping("/management/property")
public class UserManagementController {
	@Autowired
	private UserManagement service;
	//@Autowired
	//private ProxyService dialog;
	
	@RequestMapping("gotoProperty")
	public String gotoUser(HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "gotoProperty", UserUtil.getCurrentUsername(request));
		
		return "property_view";
	}
	
	@PostMapping("/fuzzyQueryUser")
	@ResponseBody
	public JSONObject fuzzyQueryUser(String keyword, HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "fuzzyQueryUser", UserUtil.getCurrentUsername(request));
		
		JSONObject result = service.fuzzyQueryUser(keyword);
		return result;
	}
	
	@PostMapping("/bindUserPhone")
	@ResponseBody
	public JSONObject bindUserPhone (String phonenum, String username, HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "bindUserPhone", UserUtil.getCurrentUsername(request));
		JSONObject result = new JSONObject();
		if (phonenum == "" || phonenum == null) {
			result.set("status", Constants.NULL_PARAM_ERROR);
			return result;
		}
		if (username == "" || username == null) {
			result.set("status", Constants.NULL_PARAM_ERROR);
			return result;
		}
		//绑定手机号码
		result = service.bindUserPhone(phonenum, username);
		
		if (result.getInt("status") == Constants.SUCCESS) {
			//绑定手机成功，清除缓存
			if (StudentCache.USER_CACHE.get(username) != null) {
				StudentCache.USER_CACHE.remove(username);
			}
			//改变session里的手机信息
			HttpSession session = request.getSession();
			JSONObject sessionJson = (JSONObject)session.getAttribute(Constants.CURRENT_USER_KEY);
			sessionJson.set("phonenum", phonenum);
		}
		return result;
	}
	
	
}
