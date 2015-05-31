package com.iAcon.response.bean;

import java.io.Serializable;

public class queryWardGuardianList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public queryWardGuardianList(){
		
	}
	public  queryWardGuardianList(int userid,String nick_name,String guard_identity){
		this.user_id = userid;
		this.nick_name = nick_name;
		this.guard_identity = guard_identity;
	}
	private int user_id;

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}


	private String nick_name;
	private String guard_identity;

	public String getGuard_identity() {
		return guard_identity;
	}
	public void setGuard_identity(String guard_identity) {
		this.guard_identity = guard_identity;
	}
}
