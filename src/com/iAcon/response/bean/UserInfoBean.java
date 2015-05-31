package com.iAcon.response.bean;

public class UserInfoBean {

	/**
	 * user_id	Int	用户ID
user_name	String	用户名
email	String	电邮
mob_tel_no	String	手机号
user_ident	String	用户身份证号
sex	Int	性别。0：未知，1：男，2：女
birthday	String	生日，格式yyyy-MM-dd
qq_no	String	QQ号
user_desc	string	用户说明
nick_name	string	姓名
addr	string	地址
home_tel	string	家庭电话
office_tel	string	办公室电话

	 */

	private int user_id;
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMob_tel_no() {
		return mob_tel_no;
	}
	public void setMob_tel_no(String mob_tel_no) {
		this.mob_tel_no = mob_tel_no;
	}
	public String getUser_ident() {
		return user_ident;
	}
	public void setUser_ident(String user_ident) {
		this.user_ident = user_ident;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getQq_no() {
		return qq_no;
	}
	public void setQq_no(String qq_no) {
		this.qq_no = qq_no;
	}
	public String getUser_desc() {
		return user_desc;
	}
	public void setUser_desc(String user_desc) {
		this.user_desc = user_desc;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
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
	private String user_name;
	private String email;
	private String mob_tel_no;
	private String user_ident;
	private int sex;
	private String birthday;
	private String qq_no;
	private String user_desc;
	private String nick_name;
	private String addr;
	private String home_tel;
	private String office_tel;
}
