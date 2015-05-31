package com.iAcron.data;

public class UpdateContact {

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
	public String getQq_no() {
		return qq_no;
	}
	public void setQq_no(String qq_no) {
		this.qq_no = qq_no;
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
	private String tel_no;
	private String name;
	private int sex;
	private String birthday;
	private String addr;
	private String qq_no;
	private String home_tel;
	private String office_tel;
	private String email;
	private String group;
	private int tel_id;
	/**
	 * tel_no	String	电话号码。必须
name	String	姓名。必须
sex	Int	性别。0：未知，1：男，2：女
birthday	Date	生日，yyyy-MM-dd格式
addr	String	地址
qq_no	string	QQ号
home_tel	String	家庭电话
office_tel	String	办公室电话
email	String	Email
group	Int	组别

	 */
	public int getTel_id() {
		return tel_id;
	}
	public void setTel_id(int tel_id) {
		this.tel_id = tel_id;
	}
}
