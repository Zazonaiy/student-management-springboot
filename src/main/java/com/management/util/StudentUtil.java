package com.management.util;

import java.util.List;
import java.util.Map;

import com.management.model.Student;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class StudentUtil {
	public static JSONArray stuListToArray(List<Student> stuList) {
		JSONArray array = new JSONArray();
		for (Student stu : stuList) {
			JSONObject json = JSONUtil.parseObj(stu);
			array.add(json);
		}
		return array;
	}
	
	public static JSONArray mapListToArray(List<Map<String, Object>> mapList) {
		JSONArray array = new JSONArray();
		for (Map<String, Object> map : mapList) {
			//map.put("s_birthday", (Object)((String)map.get("s_birthday")));
			JSONObject json = new JSONObject(map);
			System.out.println(map.get("s_birthday"));
			System.out.println(json.get("s_birthday"));
			array.add(json);
			
		}
		System.out.println("=================================== " + array);
		return array;
	}
}
