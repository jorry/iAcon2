package com.iAcon.response.bean;

import java.io.Serializable;

public class fetchAllGuardianGroupBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * guard_id Int 监护人编号
	 *  nick_name String 姓名 
	 *  guard_identity Int 1为监护组长，2为监护组成员
	 * relat String 被监护人与监护人什么关系。如；父亲母亲等等 
	 * is_sos Int 1:是sos成员，0：不是SOS成员
	 * create_time Date 关系创建时间
	 *  operate_time Date 批准时间 
	 *  status Int 0:未处理，1：已同意，2：已拒绝 
	 *  ward_id Int 被监护人编号 
	 *  ward_name String 被监护人姓名
	 *   group_name String 监护组名称
	 */

	private int guard_id;

	public int getGuard_id() {
		return guard_id;
	}

	public void setGuard_id(int guard_id) {
		this.guard_id = guard_id;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public int getGuard_identity() {
		return guard_identity;
	}

	public void setGuard_identity(int guard_identity) {
		this.guard_identity = guard_identity;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRelat() {
		return relat;
	}

	public void setRelat(String relat) {
		this.relat = relat;
	}

	public int getIs_sos() {
		return is_sos;
	}

	public void setIs_sos(int is_sos) {
		this.is_sos = is_sos;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getOperate_time() {
		return operate_time;
	}

	public void setOperate_time(String operate_time) {
		this.operate_time = operate_time;
	}

	public int getWard_id() {
		return ward_id;
	}

	public void setWard_id(int ward_id) {
		this.ward_id = ward_id;
	}

	public String getWard_name() {
		return ward_name;
	}

	public void setWard_name(String ward_name) {
		this.ward_name = ward_name;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	private String nick_name;
	private int guard_identity;
	private int status;
	private String relat;
	private int is_sos;
	private String create_time;
	private String operate_time;
	private int ward_id;
	private String ward_name;
	private String group_name;
}
