package com.management.model;

public class User {
	private Integer id;
	private String uName;
	private String uPass;
	private String uPhoneNum;
	private Integer uProperty;
	private String uPhoto;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public String getuPass() {
		return uPass;
	}
	public void setuPass(String uPass) {
		this.uPass = uPass;
	}
	public String getuPhoneNum() {
		return uPhoneNum;
	}
	public void setuPhoneNum(String uPhoneNum) {
		this.uPhoneNum = uPhoneNum;
	}
	public Integer getuProperty() {
		return uProperty;
	}
	public void setuProperty(Integer uProperty) {
		this.uProperty = uProperty;
	}
	public String getuPhoto() {
		return uPhoto;
	}
	public void setuPhoto(String uPhoto) {
		this.uPhoto = uPhoto;
	}
	
	
}
