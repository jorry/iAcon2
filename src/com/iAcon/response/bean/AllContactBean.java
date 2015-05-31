package com.iAcon.response.bean;

import java.io.Serializable;

public class AllContactBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * "guard_id": 1,
            "nick_name": "",
            "guard_identity": 0,
            "status": 0,
            "relat": "father",
            "is_sos": 0,
            "create_time": "2014-08-23 15:03:16",
            "operate_time": "2014-08-23 16:47:25",
            "ward_id": 2
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
	private String nick_name;
	private int status;
	private String relat;
	private int is_sos;
	private String create_time;
	private String operate_time;
	private int ward_id;
}
