package com.management.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.dao.DialogMapper;
import com.management.model.SystemLog;

/**
 * 代理类，用于存储日志
 * 本不应该这么写，应该把代理作为一个controller再把日志操作的service和业务操作的service注入进去的，但由于最后才写的代理，避免改动过大，于是这么写。有瑕疵。
 * @author 杨伟豪
 *
 */

@Service
public class ProxyService {
	@Autowired
	private DialogMapper logMapper;
	
	public void doLog(Class<?> clazz, String method, String username) {
		if (username == null) {
			username = "noneUser";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SystemLog dialog = new SystemLog();
		
		String lTime =  df.format(new Date());
		String lPlace = clazz.getName();
		String lAction = method;
		String lTriggerObj = username;
		logMapper.doLog(lTime, lPlace, lAction, lTriggerObj);
	}
}
