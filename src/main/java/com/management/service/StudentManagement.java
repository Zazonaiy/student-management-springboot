package com.management.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.management.constant.Constants;
import com.management.dao.StudentMapper;
import com.management.model.Student;
import com.management.util.StudentUtil;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

@Service
public class StudentManagement {
	@Autowired
	private StudentMapper studentMapper;
	
	@Transactional
	public JSONObject deleteStudent(String sNum) {
		JSONObject result = new JSONObject();
		try {
			String[] stuNums = sNum.split(" ");
			for (String num : stuNums) {
				studentMapper.deleteStudent(num);
			}
			result.set("status", Constants.SUCCESS);
			return result;
		}catch (Exception e) {
			e.printStackTrace();
			result.set("status", Constants.DB_ERROR);
			return result;
		}
		
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
	public JSONObject updateStudent(String sNum, String sName, String sBirthday, String sSex, String sPhoto) {
		JSONObject result = new JSONObject();
		if(studentMapper.findStudentByNum(sNum) == null) {
			result.set("status", Constants.NULL_RES);
			return result;
		}
		try {
			studentMapper.updateStudent(sNum, sName, sBirthday, sSex, sPhoto);
			result.set("status", Constants.SUCCESS);
		}catch (Exception e) {
			result.set("status", Constants.DB_ERROR);	
		}
		return result;
		
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
	public JSONObject addStudent(String sNum, String sName, String sBirthday, String sSex, String sPhoto) {
		JSONObject result = new JSONObject();
		if(studentMapper.findStudentByNum(sNum) != null) {
			result.set("status", Constants.SNUM_EXIST);
			return result;
		}
		try {
			studentMapper.addStudent(sNum, sName, sBirthday, sSex, sPhoto);
			result.set("status", Constants.SUCCESS);
		}catch (Exception e) {
			result.set("status", Constants.DB_ERROR);	
		}
		return result;
		
	}
	
	/**
	 * 计算分页的偏移量
	 * @param paggingParam1 currentPage
	 * @param paggingParam2 pageSize
	 * @return
	 */
	private Integer getOffset(String paggingParam1, String paggingParam2) {
		Integer offset = (Integer.valueOf(paggingParam1) - 1) * (Integer.valueOf(paggingParam2));
		return offset;
	}
	
	/**
	 * 查询学生信息
	 * @param keyword
	 * @param classId
	 * @param orderParam1
	 * @param orderParam2
	 * @param paggingParam1 当前页
	 * @param paggingParam2 每页大小
	 * @return
	 */
	public JSONObject queryStudent(String keyword, String classId, String orderParam1, String orderParam2, 
			String paggingParam1, String paggingParam2) {
		System.out.println(paggingParam1);
		System.out.println(paggingParam2);
		System.out.println("--------------------------------------");
		JSONObject result = new JSONObject();
		classId = classId == null ? "" : classId;
		keyword = keyword == null ? "" : keyword;
		
		System.out.println("--------------------queryBy " + keyword);
		
		//计算分页偏移量 offset = (当前页-1) * 每页大小
		System.out.println(paggingParam1);
		System.out.println(paggingParam2);
		Integer offset = (Integer.valueOf(paggingParam1) - 1) * (Integer.valueOf(paggingParam2));
		String pageSize = paggingParam2;
		Map<String, Object> param = new HashMap<>();
		param.put("classId", classId);
		param.put("keyword", keyword);
		param.put("orderParam1", orderParam1);
		param.put("orderParam2", orderParam2);
		param.put("offset", String.valueOf(offset));
		param.put("pageSize", pageSize);
		
		List<Map<String, Object>> stuList = studentMapper.queryStudent(param);
		JSONArray stuArray = StudentUtil.mapListToArray(stuList);
		System.out.println("---------------------- " + stuArray);
		result.set("stuArray", stuArray);
		//查询总数据条数
		Integer mesCount = studentMapper.queryAllStudentCount();
		result.set("allRowCount", mesCount);
		//计算末尾页
		Double count = Math.floor(mesCount/Integer.valueOf(pageSize) + 1);
		System.out.println("---------------------=========== " + count);
		Integer pageCount = count.intValue();
		result.set("pageCount", pageCount);
		
		if (stuList.size() == 0) {
			result.set("status", Constants.NULL_RES);
		}else {
			result.set("status", Constants.SUCCESS);
		}
		
		return result;
		
	}
	
	public JSONObject getStudentByNum(String keyword) {
		JSONObject json = new JSONObject();
		Student student = studentMapper.findStudentByNum(keyword);
		if (student == null) {
			json.set("status", Constants.NULL_RES);
			return json;
		}
		json.set("stu", student);
		json.set("status", Constants.SUCCESS);
		return json;
	}
	
}
