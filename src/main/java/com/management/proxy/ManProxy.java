package com.management.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.stereotype.Component;

import com.management.proxy.dialog.IDialog;
import com.management.proxy.dialog.IDialogImpl;
import com.management.proxy.propjudg.IPropJudg;
import com.management.proxy.propjudg.IPropJudgImpl;

@Deprecated
public class ManProxy implements InvocationHandler{
	private IDialog dialog = new IDialogImpl();
	private IPropJudg propJudg = new IPropJudgImpl();
	private Object delegete;
	
	public Object bind(Object delegate) {
		this.delegete = delegate;
		return Proxy.newProxyInstance(delegate.getClass().getClassLoader(), delegate.getClass().getInterfaces(), this);
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		propJudg.doPropJudg(1);
		Object result = method.invoke(delegete, args);
		dialog.doDialog(method, args);
		return result;
	}
	
}
