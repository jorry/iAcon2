package com.iAcon.response.bean;

import java.io.Serializable;

public class fetchWardTelBookBean implements Serializable{

	/**
	 * 
	 *             "tel_id": 1,
            "birthday": "1986-02-15",
            "addr": "南京市玄武区",
            "home_tel": "81111111",
            "office_tel": "",
            "email": "123@qq.com",
            "group": 1,
            "relation": "uncle"
            
	 */
	
	private int is_guard;
	
	public int getIs_guard() {
		return is_guard;
	}
	public void setIs_guard(int is_guard) {
		this.is_guard = is_guard;
	}
	private int is_sos;
	
	public int getIs_sos() {
		return is_sos;
	}
	public void setIs_sos(int is_sos) {
		this.is_sos = is_sos;
	}
	private int tel_id;
	public int getTel_id() {
		return tel_id;
	}
	public void setTel_id(int tel_id) {
		this.tel_id = tel_id;
	}
	private static final long serialVersionUID = 1L;
	private String birthday;
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getHome_tel() {
		return home_tel;
	}
	public void setHome_tel(String home_tel) {
		this.home_tel = home_tel;
	}
	public String getOffice_tel() {
		return office_tel;
	}
	public void setOffice_tel(String office_tel) {
		this.office_tel = office_tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	private String addr;
	private String home_tel;
	private String office_tel;
	private String email;
	private String group;
	private String relation;
	public boolean isChecked;
	private String tel_no;
	public String getTel_no() {
		return tel_no;
	}
	public void setTel_no(String tel_no) {
		this.tel_no = tel_no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	private String name;
	private int sex;
	
}
