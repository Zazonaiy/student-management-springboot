package com.management.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.management.model.Student;


@Mapper
public interface StudentMapper {
	public void deleteStudent(@Param("s_num")String sNum);
	
	public void updateStudent(@Param("s_num")String sNum, @Param("s_name")String sName, @Param("s_birthday")String sBirthday, @Param("s_sex")String sSex, @Param("s_photo")String sPhoto);
	
	public void addStudent(@Param("s_num")String sNum, @Param("s_name")String sName, @Param("s_birthday")String sBirthday, @Param("s_sex")String sSex, @Param("s_photo")String sPhoto);
	
	public List<Map<String, Object>> queryStudent(Map<String, Object> param);
	
	@Select("select count(t_student.id) from t_student left JOIN t_class ON t_student.class_fk = t_class.id left JOIN t_user ON t_class.manager_user_fk = t_user.id")
	public Integer queryAllStudentCount();
	
	public Student findStudentByNum(@Param("s_num") String stuNum);
}
