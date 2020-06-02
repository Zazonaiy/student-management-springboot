package com.management.util;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.util.ResourceUtils;

import cn.hutool.crypto.SecureUtil;

public class Test {
	public static void main(String[] args) {
		//ResourceUtils.getURL("classpath:").getPath()	//获取项目编译根目录
		//System.out.println(System.getProperty("user.dir"));//项目根目录
		//String path = "/src/main/resources/verifycode/";
		//File savePath = new File(System.getProperty("user.dir") + path);
		//System.out.println(savePath.getAbsolutePath());
		//String path="src/main/resources/static/key";
		//System.out.println(new File(path).getAbsolutePath());
		String key1 = "2";
		String key2 = "2";
		String c1 = SecureUtil.md5(key1);
		String c2 = SecureUtil.md5(key2);
		System.out.println(c1);
		System.out.println(c2);
	}
}
