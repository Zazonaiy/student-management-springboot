package com.management.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.management.model.Clazz;

@Mapper
public interface ClassMapper {
	public void removeClassfromUser(@Param("id")String classId);
	public void addClassToUser(@Param("id")String classId, @Param("manager_user_fk")String username);	//把一个班级录入某个老师的名下
	public List<Map<String, Object>> queryNullUserClass();	//查询未有负责老师的班级
	public List<Map<String, Object>> queryClassByUser(@Param("manager_user_fk") String managerUser);	//查老师所负责的班级
	
	public void deleteClassById(@Param("id")String id);
	
	public String findTeacherName(@Param("manager_user_fk") String managerUserFk); //用于检查负责老师是否存在
	public void addClass(@Param("class_no")String classNo, @Param("class_name")String className, 
			@Param("enter_year")String enterYear, @Param("manager_user_fk")String managerUserFk);
	public void updateClass(@Param("class_no")String classNo, @Param("class_name")String className, 
			@Param("enter_year")String enterYear, @Param("manager_user_fk")String managerUserFk);
	
	public Clazz findClassByNo(@Param("class_no") String classNo);
	
	public Integer getStudentCountByClass(@Param("class_fk") Integer classFk);
	
	@Select("select count(id) from t_class")
	public Integer queryAllClassCount();
	
	public List<Map<String, Object>> queryClass(Map<String, Object> param);
}
