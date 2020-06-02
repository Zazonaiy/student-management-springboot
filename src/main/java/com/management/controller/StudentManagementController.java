package com.management.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.util.WebUtils;

import com.management.constant.Constants;
import com.management.model.Student;
import com.management.service.ProxyService;
import com.management.service.StudentManagement;
import com.management.util.UserUtil;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;

@Controller
@RequestMapping("/management/student")
public class StudentManagementController {
	@Autowired 
	private StudentManagement service;
	//@Autowired
	//private ProxyService dialog;
	
	@RequestMapping("/gotoStudent")
	public String gotoStudent(HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "gotoStudent", UserUtil.getCurrentUsername(request));
		
		return "student_view";
	}
	
	@RequestMapping("/uploadFiles")
	@ResponseBody
	public String uploadFiles(MultipartFile file, HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "uploadFiles", UserUtil.getCurrentUsername(request));
		
		if (file.isEmpty()) {
			return "error: 文件为空";
		}
		String fileName = file.getOriginalFilename();
		String filePath = "G:/Java_learning/fileupload/student/";
		String uuid = IdUtil.fastSimpleUUID();
		File dir = new File(filePath + uuid + "/");
		if (!dir.exists()) {
			dir.mkdir();
		}
		File dest = new File(filePath + uuid + "/" + fileName);
		try {
			file.transferTo(dest);
			return "/fileupload/student/" + uuid + "/" + fileName;
		}catch (IOException e) {
			e.printStackTrace();
			return "error: IOException";
		}
		
		
	}
	
	@PostMapping("/deleteStudent")
	@ResponseBody
	public JSONObject deleteStudent(String sNum, HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "deleteStudent", UserUtil.getCurrentUsername(request));
		
		JSONObject result = service.deleteStudent(sNum);
		
		return result;
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
	 * 修改学生信息
	 * @param sNum
	 * @param sName
	 * @param sBirthday
	 * @param sSex
	 * @param sPhoto
	 * @return
	 */
	@PostMapping("/updateStudent")
	@ResponseBody
	public JSONObject updateStudent(String sNum, String sName, String sBirthday, String sSex, String sPhoto, HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "updateStudent", UserUtil.getCurrentUsername(request));
		JSONObject json = new JSONObject();
		if (!isAllNotNull(sNum, sName, sBirthday, sSex)) {
			json.set("status", Constants.NULL_PARAM_ERROR);
			return json;
		}
		json = service.updateStudent(sNum, sName, sBirthday, sSex, sPhoto);
		return json;
	}
	
	/**
	 * 添加学生
	 * @param sNum
	 * @param sName
	 * @param sBirthday
	 * @param sSex
	 * @param sPhoto
	 * @return
	 */
	@PostMapping("/addStudent")
	@ResponseBody
	public JSONObject addStudent(String sNum, String sName, String sBirthday, String sSex, String sPhoto, HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "addStudent", UserUtil.getCurrentUsername(request));
		
		JSONObject json = new JSONObject();
		if (!isAllNotNull(sNum, sName, sBirthday, sSex)) {
			json.set("status", Constants.NULL_PARAM_ERROR);
			return json;
		}
		json = service.addStudent(sNum, sName, sBirthday, sSex, sPhoto);
		return json;
	}
	
	private void initQueryParam(String keyword, String classId, String orderParam1, String orderParam2,
			String paggingParam1, String paggingParam2) {
		if (keyword == null) {
			keyword = "";
		}
		if (orderParam1 == null) {
			orderParam1 = "s_num";
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
	@PostMapping("/queryStudent")
	@ResponseBody
	public String queryStudent(String keyword, String classId, String orderParam1, String orderParam2,
			String paggingParam1, String paggingParam2, HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "queryStudent", UserUtil.getCurrentUsername(request));
		
		initQueryParam(keyword, classId, orderParam1, orderParam2, paggingParam1, paggingParam2);
		
		JSONObject stuJson = service.queryStudent(keyword, classId, orderParam1, orderParam2, paggingParam1, paggingParam2);
		stuJson.set("paggingParam1", paggingParam1);
		stuJson.set("paggingParam2", paggingParam2);
		
		
		return stuJson.toString();
	}
	
	@GetMapping("/getStudentByNum")
	@ResponseBody
	public JSONObject getStudentByNum(String keyword, HttpServletRequest request) {
		//日志
		//dialog.doLog(this.getClass(), "getStudentByNum", UserUtil.getCurrentUsername(request));
		
		JSONObject json = new JSONObject();
		if (keyword == null) {
			json.set("status", Constants.NULL_PARAM_ERROR);
			return json;
		}
		json = service.getStudentByNum(keyword);
		return json;
		
	}
	
	
}
