package com.management.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.management.model.User;

@Mapper
public interface UserMapper {
	@Select("select u_phone_num from t_user where u_name=#{u_name}")
	public String queryUserPhoneByName(@Param("u_name")String username);
	public void bindUserPhone(@Param("u_phone_num")String phonenum, @Param("u_name")String username);
	public List<String> fuzzyQueryUser(@Param("u_name")String username);
	
	public User queryUserByName(@Param("u_name") String username);
	public User queryUserByPhonenum (@Param("u_phone_num") String phonenum);
	
	public int queryUsernameCountByName(@Param("u_name") String username);
	public int queryUsernameCountByPhonenum(@Param("u_phone_num") String phonenum);
	
	public void addUser(@Param("u_name")String username, @Param("u_pass")String password, @Param("u_property")String property);
	public void updateUser (@Param("u_name")String username, @Param("u_pass")String password);
	
}
