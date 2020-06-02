package com.management.proxy.propjudg;

public class IPropJudgImpl implements IPropJudg{

	@Override
	public boolean doPropJudg(Integer prop) {
		System.out.println("正在进行权限判断，目前权限: " + prop);
		return true;
	}
	
	
	
}
