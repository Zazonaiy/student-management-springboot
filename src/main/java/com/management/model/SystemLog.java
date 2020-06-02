package com.management.model;

/**
 * @param lTime 事件发生时间
 * @param lPlace 事件发生地点
 * @param lAction 进行了什么操作
 * @param lTriggerObj 进行操作的对象 外键 user表的u_name
 * @author 杨伟豪
 *
 */
public class SystemLog {
	private Integer id;
	private String lTime;
	private String lPlace;
	private String lAction;
	private String lTriggerObj;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getlTime() {
		return lTime;
	}
	public void setlTime(String lTime) {
		this.lTime = lTime;
	}
	public String getlPlace() {
		return lPlace;
	}
	public void setlPlace(String lPlace) {
		this.lPlace = lPlace;
	}
	public String getlAction() {
		return lAction;
	}
	public void setlAction(String lAction) {
		this.lAction = lAction;
	}
	public String getlTriggerObj() {
		return lTriggerObj;
	}
	public void setlTriggerObj(String lTriggerObj) {
		this.lTriggerObj = lTriggerObj;
	}
	
	
}
