package com.management.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.management.constant.Constants;
import com.management.service.ClassManagement;
import com.management.service.ProxyService;
import com.management.util.UserUtil;

import cn.hutool.json.JSONObject;


@Controller
@RequestMapping("/management/class")
public class ClassManagementController {
	@Autowired 
	private ClassManagement service;
	//@Autowired ProxyService dialog;
	
	
	@RequestMapping("/gotoClass")
	public String gotoClass(HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "gotoClass", UserUtil.getCurrentUsername(request));
		
		return "class_view";
	}
	
	@PostMapping("/removeClassfromUser")
	@ResponseBody
	public JSONObject removeClassfromUser(String classId, HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "removeClassfromUser", UserUtil.getCurrentUsername(request));
		
		JSONObject result = service.removeClassfromUser(classId);
		return result;
	}
	
	@PostMapping("/addClassToUser")
	@ResponseBody
	public JSONObject addClassToUser(String classId, String username, HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "addClassToUser", UserUtil.getCurrentUsername(request));
		JSONObject result = service.addClassToUser(classId, username);
		return result;
	}
	
	@PostMapping("/queryNullUserClass")
	@ResponseBody
	public JSONObject queryNullUserClass(HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "queryNullUserClass", UserUtil.getCurrentUsername(request));
		JSONObject result = service.queryNullUserClass();
		return result;
	}
	
	@PostMapping("/queryClassByUser")
	@ResponseBody
	public JSONObject queryClassByUser(String managerUser, HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "queryClassByUser", UserUtil.getCurrentUsername(request));
		
		JSONObject result = service.queryClassByUser(managerUser);
		return result;
	}
	
	@PostMapping("/deleteClass")
	@ResponseBody
	public JSONObject deleteClass(String id, HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "deleteClass", UserUtil.getCurrentUsername(request));
		
		JSONObject result = service.deleteClassById(id);
		
		return result;
	}
	
	private void initQueryParam(String keyword, String orderParam1, String orderParam2,
			String paggingParam1, String paggingParam2) {
		if (keyword == null) {
			keyword = "";
		}
		if (orderParam1 == null) {
			orderParam1 = "class_no";
		}
		if (orderParam2 == null) {
			orderParam2 = "asc";
		}
		if (paggingParam1 == null) {
			paggingParam1 = "1";
		}
		if (paggingParam2 == null) {
			paggingParam2 = "5";
		} 
	}
	
	/**
	 * 判断一系列参数是否有null
	 * @param param
	 * @return
	 */
	private boolean isAllNotNull(String ...param) {
		for (String str : param) {
			if (str == null) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 添加班级
	 * @param classNo
	 * @param className
	 * @param enterYear
	 * @param managerUserFk
	 * @return
	 */
	@PostMapping("/addClass")
	@ResponseBody
	public JSONObject addClass(String classNo, String className, String enterYear, String managerUserFk, HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "addClass", UserUtil.getCurrentUsername(request));
		
		String[] str = enterYear.split("-");
		enterYear = str[0];
		JSONObject json = new JSONObject();
		if (!isAllNotNull(classNo, className, enterYear)) {
			json.set("status", Constants.NULL_PARAM_ERROR);
			return json;
		}
		json = service.addClass(classNo, className, enterYear, managerUserFk);
		return json;
	}
	
	/**
	 * 更新班级信息
	 * @param classNo
	 * @param className
	 * @param enterYear
	 * @param managerUserFk
	 * @return
	 */
	@PostMapping("/updateClass")
	@ResponseBody
	public JSONObject updateClass(String classNo, String className, String enterYear, String managerUserFk, HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "updateClass", UserUtil.getCurrentUsername(request));
		
		String[] str = enterYear.split("-");
		enterYear = str[0];
		JSONObject json = new JSONObject();
		if (!isAllNotNull(classNo, className, enterYear, managerUserFk)) {
			json.set("status", Constants.NULL_PARAM_ERROR);
			return json;
		}
		json = service.updateClass(classNo, className, enterYear, managerUserFk);
		return json;
	}
	
	/**
	 * 查询课程信息 controller
	 * @param keyword
	 * @param orderParam1
	 * @param orderParam2
	 * @param paggingParam1
	 * @param paggingParam2
	 * @return
	 */
	@PostMapping("/queryClass")
	@ResponseBody
	public String queryClass(String keyword, String orderParam1, String orderParam2, String paggingParam1, String paggingParam2, HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "queryClass", UserUtil.getCurrentUsername(request));
		
		initQueryParam(keyword, orderParam1, orderParam2, paggingParam1, paggingParam2);
		
		JSONObject classJson = service.queryClass(keyword, orderParam1, orderParam2, paggingParam1, paggingParam2);
		classJson.set("paggingParam1", paggingParam1);
		classJson.set("paggingParam2", paggingParam2);
		
		return classJson.toString();
	}
}
