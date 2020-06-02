package com.management.proxy.dialog;

import java.lang.reflect.Method;

@Deprecated
public interface IDialog {
	public void doDialog(Method method, Object[] args);
}
