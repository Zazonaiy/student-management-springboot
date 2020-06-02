package com.management.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.management.cache.StudentCache;
import com.management.constant.Constants;
import com.management.service.ProxyService;
import com.management.service.UserManagement;
import com.management.util.CipherMachine;
import com.management.util.UserUtil;
import com.management.util.VerifyCode;

import cn.hutool.json.JSONObject;

@Controller
@RequestMapping("/login")
public class LoginController {
	@Autowired
	private UserManagement service;

	@RequestMapping("/loginpage")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		return "login";
	}
	
	@RequestMapping("/toHome")
	public String toHome() {
		return "homepage";
	}
	
	/*
	@RequestMapping("/cancellation")
	public String cancellation() {
		return "login";
	}
	*/
	

	@PostMapping("/doVerification")
	@ResponseBody
	public JSONObject doVerification(String inputCode, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String relCode = (String) session.getAttribute("vercode");
		JSONObject result = new JSONObject();
		System.out.println("kkkkkkkkkkkkkkkkkk  inputCode: "+inputCode);
		System.out.println("kkkkkkkkkkkkkkkkkk  relCode: "+relCode);
		
		if (inputCode == null) {
			result.set("status", Constants.VERIFYCODE_ERROR);
			return result;
		}
		
		inputCode = inputCode.trim().toLowerCase();
		relCode = relCode.trim().toLowerCase();
		if (inputCode.equals(relCode)) {
			result.set("status", Constants.SUCCESS);
			System.out.println("验证成功");
		}else {
			result.set("status", Constants.VERIFYCODE_ERROR);
			System.out.println("验证失败");
		}
		return result;
	}
	
	@RequestMapping("/getVercode")
	public void getVercode(HttpServletRequest request, HttpServletResponse response) {
		int width = 165;
		int height = 58;
		HttpSession session = request.getSession();
		
		BufferedImage verifyImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//初始图片
		JSONObject json = VerifyCode.drawRandomText(width, height, verifyImg);
		
		//把验证码的值放到session
		session.setAttribute("vercode", json.getStr("vercodeContent"));
		OutputStream ops;
		try {
			ops = response.getOutputStream();
			ImageIO.write(verifyImg, "jpg", ops);
			ops.flush();
			return ;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@PostMapping("/doLogin")
	@ResponseBody
	public JSONObject doLogin(String username, String password, HttpServletRequest request) {
		username = username.trim();
		password = password.trim();
		System.out.println(username +" ++++++++++++++++++++++++++++ " + password);
		try {
			password = CipherMachine.encryption(password);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(username +" ============================ " + password);
		JSONObject result = service.validateUser(username, password);
		HttpSession session = request.getSession();
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxx !!!!!!!!!!!!!!!" + result.getInt("status"));
		if (result.getInt("status") == Constants.SUCCESS) {
			String phonenum = service.queryUserPhoneByName(username);
			result.set("phonenum", phonenum);
			session.setAttribute(Constants.CURRENT_USER_KEY, result);
		}else {
			session.removeAttribute(Constants.CURRENT_USER_KEY);
		}
		
		
		if (result.getInt("status").equals(Constants.SUCCESS)) {
			//dialog.doLog(this.getClass(), "doLogin", username);
			if (session.getAttribute("vercode") != null) {
				session.removeAttribute("vercode");
			}
		}
		System.out.println("================== " + session.getAttribute(Constants.CURRENT_USER_KEY));
		
		return result;
		
	}
	
	
	@RequestMapping("/toUpdateUser")
	public String toUpdate(HttpServletRequest request, HttpServletResponse response) {
		return "update-user";
	}
	
	/**
	 * 重置学生账号密码
	 * @param username
	 * @return
	 */
	@PostMapping("/updateUserByRoot")
	@ResponseBody
	public JSONObject updateUserByRoot(String username, String password) {
		if (password == null || password == "") {
			password = "123456";
		}
		try {
			if (StudentCache.USER_CACHE.get(username) != null) {
				StudentCache.USER_CACHE.remove(username);
			}
			return service.updateUser(username, CipherMachine.encryption(password));
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 修改密码
	 * @param username
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@PostMapping("/doUpdate")
	@ResponseBody
	public JSONObject doUpdate(String username, String oldPassword, String newPassword, HttpServletRequest request) {
		
		String oldpass = oldPassword;
		String newpass = newPassword;
		
		try {
			oldpass = CipherMachine.encryption(oldpass);
			newpass = CipherMachine.encryption(newpass);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		JSONObject result = new JSONObject();
		/*
		//Root用户强制重置密码跳过验证
		if (oldPassword.equals("doResetByRoot")) {
			Object property = request.getAttribute("userproperty");
			if (property == null) {
				//恶意操作
				result.set("status", Constants.PASS_ERROR);
				return result;
			}
			Integer prop = (Integer) property;
			if (prop != 0) {
				//权限越界
				result.set("status", Constants.PROP_OUT_OF_BOUNDS);
				return result;
			}
			result = service.updateUser(username, newpass);
			if (StudentCache.USER_CACHE.get(username) != null) {
				StudentCache.USER_CACHE.remove(username);
			}
			dialog.doLog(this.getClass(), "doUpdate(ByRoot)", username);
			return result;
		}*/
		
		JSONObject valiJson = service.validateUser(username, oldpass);
		//验证旧密码
		if (!valiJson.getInt("status").equals(Constants.SUCCESS)) {
			result.set("status", Constants.PASS_ERROR);
			return result;
		}
		
		result = service.updateUser(username, newpass);
		//清缓存
		if (StudentCache.USER_CACHE.get(username) != null) {
			StudentCache.USER_CACHE.remove(username);
		}
		//dialog.doLog(this.getClass(), "doUpdate", username);
		return result;
	}
	
	@RequestMapping("/toRegister")
	public String toRegister() {
		return "register-user";
	}
	
	@PostMapping("registUserByRoot")
	@ResponseBody
	public JSONObject registUserByRoot(String username) {
		try {
			if (!UserUtil.strIsUsername(username)) {
				//账号格式不符合要求
				JSONObject er = new JSONObject();
				er.set("status", Constants.REGIST_FORMAT_ERROR);
				return er;
			}
			return service.addUser(username, CipherMachine.encryption("123456"), "2");
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 注册用户
	 * @param username
	 * @param password
	 * @return
	 */
	@PostMapping("/doRegist")
	@ResponseBody
	public JSONObject doRegist(String username, String password) {
		String property = "";
		
		if (!UserUtil.strIsUsername(username)) {
			JSONObject er = new JSONObject();
			er.set("status", Constants.REGIST_FORMAT_ERROR);
			return er;
		}
		try {
			password = CipherMachine.encryption(password);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject result = service.addUser(username, password, property);
		//日志
		String method = "doRegist";
		if (password.equals("doRegistByRoot")) {
			method = "doRegistByRoot";
		}
		return result;
	}
	
}
