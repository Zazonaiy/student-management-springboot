package com.management.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DialogMapper {
	
	@Insert("insert into t_sys_log (l_time, l_place, l_action, l_trigger_obj) values (#{l_time}, #{l_place}, #{l_action}, #{l_trigger_obj})")
	public void doLog(@Param("l_time")String lTime, @Param("l_place")String lPlace, @Param("l_action")String lAction, 
			@Param("l_trigger_obj")String lTriggerObj);
}
