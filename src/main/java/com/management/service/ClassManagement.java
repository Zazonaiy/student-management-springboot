package com.management.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.management.constant.Constants;
import com.management.dao.ClassMapper;
import com.management.model.Clazz;
import com.management.util.StudentUtil;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

@Service
public class ClassManagement {
	
	@Autowired
	private ClassMapper classMapper;
	
	public JSONObject removeClassfromUser(String classId) {
		JSONObject result = new JSONObject();
		try {
			classMapper.removeClassfromUser(classId);
			result.set("status", Constants.SUCCESS);
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			result.set("status", Constants.DB_ERROR);
			return result;
		}
	}
	
	public JSONObject addClassToUser(String classId, String username) {
		JSONObject result = new JSONObject();
		try {
			classMapper.addClassToUser(classId, username);
			result.set("status", Constants.SUCCESS);
			return result;
		}catch (Exception e) {
			e.printStackTrace();
			result.set("status", Constants.DB_ERROR);
			return result;
		}
	}
	
	public JSONObject queryNullUserClass() {
		JSONObject result = new JSONObject();
		List<Map<String, Object>> classArray = classMapper.queryNullUserClass();
		for (Map<String ,Object> perClass : classArray) {
			Integer id = (Integer) perClass.get("id");
			Integer count = classMapper.getStudentCountByClass(id);
			perClass.put("count", count);
		}
		result.set("classArray", classArray);
		return result;
	}
	
	public JSONObject queryClassByUser(String managerUser) {
		JSONObject result = new JSONObject();
		List<Map<String, Object>> classArray = classMapper.queryClassByUser(managerUser);
		for (Map<String ,Object> perClass : classArray) {
			Integer id = (Integer) perClass.get("id");
			Integer count = classMapper.getStudentCountByClass(id);
			perClass.put("count", count);
		}
		result.set("classArray", classArray);
		return result;
	}
	
	@Transactional
	public JSONObject deleteClassById(String id) {
		JSONObject result =new JSONObject();
		try {
			String[] ids = id.split(" ");
			for (String perId : ids) {
				classMapper.deleteClassById(perId);
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
	 * 计算分页参数（偏移量）
	 * @param paggingParam1
	 * @param paggingParam2
	 * @return
	 */
	private Integer getOffset(String paggingParam1, String paggingParam2) {
		Integer offset = (Integer.valueOf(paggingParam1) - 1) * (Integer.valueOf(paggingParam2));
		return offset;
	}
	
	/**
	 * 添加班级
	 * @param classNo
	 * @param className
	 * @param enterYear
	 * @param managerUserFk
	 * @return
	 */
	public JSONObject addClass(String classNo, String className, String enterYear, String managerUserFk) {
		JSONObject result = new JSONObject();
		if (classMapper.findClassByNo(classNo) != null) {
			result.set("status", Constants.NULL_RES);
		}
		if (managerUserFk == null || managerUserFk == "") {
			managerUserFk = null;
		}
		//若设置了负责老师，则判断负责老师是否存在
		if (managerUserFk != null) {
			if (classMapper.findTeacherName(managerUserFk) == null) {
				result.set("status", Constants.NULL_USER);
				return result;
			}
		}

		try {
			classMapper.addClass(classNo, className, enterYear, managerUserFk);
			result.set("status", Constants.SUCCESS);
		}catch(Exception e) {
			e.printStackTrace();
			result.set("status", Constants.DB_ERROR);
		}
		return result;
	}
	
	/**
	 * 修改班级信息
	 * @param classNo
	 * @param className
	 * @param enterYear
	 * @param managerUserFk
	 * @return
	 */
	public JSONObject updateClass(String classNo, String className, String enterYear, String managerUserFk) {
		JSONObject result = new JSONObject();
		if (classMapper.findClassByNo(classNo) == null) {
			result.set("status", Constants.NULL_RES);
			return result;
		}
		// 判断该负责老师是否存在
		if (classMapper.findTeacherName(managerUserFk)==null) {
			result.set("status", Constants.NULL_USER);
			return result;
		}
		try {
			classMapper.updateClass(classNo, className, enterYear, managerUserFk);
			result.set("status", Constants.SUCCESS);
		}catch(Exception e) {
			e.printStackTrace();
			result.set("status", Constants.DB_ERROR);
		}
		return result;
	}
	
	/**
	 * 查询班级信息
	 * @param keyword
	 * @param orderParam1
	 * @param orderParam2
	 * @param paggingParam1
	 * @param paggingParam2
	 * @return
	 */
	public JSONObject queryClass(String keyword, String orderParam1, String orderParam2, 
			String paggingParam1, String paggingParam2) {
		System.out.println(paggingParam1);
		System.out.println(paggingParam2);
		JSONObject result = new JSONObject();
		keyword = keyword == null ? "" : keyword;
		System.out.println("--------------------queryBy " + keyword);
		Integer offset = getOffset(paggingParam1, paggingParam2);
		String  pageSize = paggingParam2;
		Map<String, Object> param = new HashMap<>();
		param.put("keyword", keyword);
		param.put("orderParam1", orderParam1);
		param.put("orderParam2", orderParam2);
		param.put("offset", String.valueOf(offset));
		param.put("pageSize", pageSize);
		
		List<Map<String, Object>> classList = classMapper.queryClass(param);
		System.out.println(classList);
		for (Map<String, Object> perMap : classList) {
			Integer value = classMapper.getStudentCountByClass((Integer)perMap.get("id"));
			perMap.put("size", value);
		}
		//JSONArray stuArray = StudentUtil.mapListToArray(classList);
		result.set("classArray", classList);
		//查询总数据条数
		Integer mesCount = classMapper.queryAllClassCount();
		result.set("allRowCount", mesCount);
		//计算末尾页
		Double count = Math.floor(mesCount/Integer.valueOf(pageSize) + 1);
		System.out.println("---------------------=========== " + count);
		Integer pageCount = count.intValue();
		result.set("pageCount", pageCount);
		
		if (classList.size() == 0) {
			result.set("status",Constants.NULL_RES);
		}else {
			result.set("status", Constants.SUCCESS);
		}
		
		return result;
	}
}
