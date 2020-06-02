package com.management.proxy.propjudg;

@Deprecated
public interface IPropJudg {
	//权限判断，true执行，false不予执行
	public boolean doPropJudg(Integer prop);
}
