package com.management.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.cache.StudentCache;
import com.management.constant.Constants;
import com.management.dao.UserMapper;
import com.management.model.User;

import cn.hutool.json.JSONObject;


@Service
public class UserManagement {
	@Autowired
	private UserMapper userMapper;
	
	public String queryUserPhoneByName(String username) {
		return userMapper.queryUserPhoneByName(username);
	}
	
	/**
	 * 绑定手机号码
	 * @param phonenum
	 * @return
	 */
	public JSONObject bindUserPhone(String phonenum, String username) {
		JSONObject result = new JSONObject();
		try {
			userMapper.bindUserPhone(phonenum, username);
			result.set("status", Constants.SUCCESS);
		}catch(Exception e) {
			System.out.println("----------------------- bind phone fail   by " + phonenum + " has been binded");
			result.set("status", Constants.DB_ERROR);
		}
		return result;
	}
	
	/**
	 * 模糊查询用户 用户名
	 * @param keyword
	 * @return
	 */
	public JSONObject fuzzyQueryUser(String keyword) {
		JSONObject result = new JSONObject();
		if (keyword == null) {
			keyword = "";
		}
		List<String> userArray = new ArrayList<>();
		try {
			userArray = userMapper.fuzzyQueryUser(keyword);
			result.set("status", Constants.SUCCESS);
		}catch(Exception e) {
			e.printStackTrace();
			result.set("status", Constants.DB_ERROR);
		}
		
		result.set("userArray", userArray);
		return result;
	}
	
	/**
	 * 修改密码
	 * @param username
	 * @param password
	 * @return
	 */
	public JSONObject updateUser(String username, String password) {
		JSONObject result = new JSONObject();
		int num = userMapper.queryUsernameCountByName(username);
		//验证账号是否存在，不存在的账号不能修改密码
		if (num < 1) {
			result.set("status", Constants.NULL_USER);
			return result;
		}
		try {
			userMapper.updateUser(username, password);
			result.set("status", Constants.SUCCESS);
			return result;
		}catch (Exception e) {
			e.printStackTrace();
			result.set("status", Constants.DB_ERROR);
			return result;
		}
		
	}
	
	/**
	 * 注册
	 * @param username
	 * @param password
	 * @return
	 */
	public JSONObject addUser(String username, String password, String property) {
		JSONObject result = new JSONObject();
		int num = userMapper.queryUsernameCountByName(username);
		if (num > 0) {
			//数据已经存在
			result.set("status", Constants.NULL_RES);
			return result;
		}
		try {
			userMapper.addUser(username, password, property);
			result.set("status", Constants.SUCCESS);
			return result;
		}catch (Exception e) {
			e.printStackTrace();
			result.set("status", Constants.DB_ERROR);
			return result;
		}
	}
	
	/**
	 * 手机登录验证
	 * @param phonenum 手机号
	 * @param password 密码
	 * @param cacheJson 缓存
	 * @return
	 */	
	private JSONObject validateUserByPhoneNum(String phonenum, String password, JSONObject cacheJson) {
		JSONObject result = new JSONObject();
		
		if (cacheJson == null) {
			//从数据库找该手机号码的用户
			
			int num = userMapper.queryUsernameCountByPhonenum(phonenum);
			if (num < 1) {
				//没有找到改手机号
				result.set("isSuccess", false);
				return result;				
			}
			
			User user = userMapper.queryUserByPhonenum(phonenum);
			String username = user.getuName();
			String relpass = user.getuPass();
			if (relpass.equals(password)) {
				//数据库验证通过
				result.set("isSuccess", true);
				result.set("username", username);
				return result;
			}else {
				//数据库验证失败
				result.set("isSuccess", false);
				return result;
			}			
		}else {
			//从缓存中验证
			if (!cacheJson.getStr("password").equals(password)) {
				//缓存验证失败
				result.set("isSuccess", false);
				return result;
			}else {
				//缓存验证正确
				result.set("isSuccess", true);
				return result;
			}			
		}
	}
	/**
	 * 登录验证
	 * @param param 所需的所有参数 : 账号/手机号  密码
	 * @return
	 */	
	public JSONObject validateUser(String username, String password) {
		JSONObject result = new JSONObject();
		JSONObject cacheJson = StudentCache.USER_CACHE.get(username);
		JSONObject param = new JSONObject();//返回的结果
		
		if (cacheJson == null) {
			int num = userMapper.queryUsernameCountByName(username);
			if (num < 1) {
				//账号不存在于数据库中，现在去找这个账号是否是手机号
				JSONObject phoneValidateResult = validateUserByPhoneNum(username, password, null);
				if (phoneValidateResult.getBool("isSuccess")) {
					param.set("phonenum", username);//现在的username为手机号，把它放入手机号属性
					param.set("username", phoneValidateResult.getStr("username"));//添加真正的username
					param.set("password", password);
					StudentCache.USER_CACHE.put(username, param);	//账号
					param.set("status", Constants.SUCCESS);
					User user = userMapper.queryUserByName(username);
					param.set("property", user.getuProperty());
				}else {
					//手机号验证失败
					//StudentCache.USER_CACHE.put(username, param);
					param.set("status", Constants.NULL_USER);
				}
				param.remove("password");
				return param;
			}
			
			//账号存在，手机号就不可能同时存在了,找到了账号故username不可能是手机号
			User user = userMapper.queryUserByName(username);
			String relpass = user.getuPass();
			if (relpass.equals(password)) {
				//密码正确，通过
				JSONObject json = new JSONObject();
				json.set("username", username);
				json.set("password", password);
				json.set("property", user.getuProperty());
				json.set("photo", user.getuPhoto());
				StudentCache.USER_CACHE.put(username, json);
				param.set("username", username);
				param.set("property", user.getuProperty());
				param.set("photo", user.getuPhoto());
				param.set("status", Constants.SUCCESS);
			}else {
				//数据库账号验证失败(密码错误)	
				param.set("status", Constants.PASS_ERROR);
			}
			param.remove("password");
			return param;
		}else {
			//JSONObject cache = (JSONObject)cacheJson.get("username");
			String cachePass = cacheJson.getStr("password");
			if(!cachePass.equals(password)) {
				//缓存账号/手机号验证失败	
				System.out.println("XXXX "+cachePass);
				param.set("status", Constants.PASS_ERROR);
			}else {
				//缓存账号/手机号验证通过
				param.set("status", Constants.SUCCESS);
				param.set("username", cacheJson.getStr("username"));
				param.set("property", cacheJson.getStr("property"));
			}
			param.remove("password");
			return param;
		}
		
		
		
	}
	
}
