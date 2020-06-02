package com.management.proxy.dialog;

import java.lang.reflect.Method;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

@Deprecated
public class IDialogImpl implements IDialog{
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void doDialog(Method method, Object[] args) {
		logger.log(Level.INFO, "方法: " + method.getName() + "参数: " + args);
		
	}
	
}
