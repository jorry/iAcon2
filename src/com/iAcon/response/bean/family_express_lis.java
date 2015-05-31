package com.iAcon.response.bean;

import java.io.Serializable;

public class family_express_lis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2148251252786626967L;
	private String express_content;

	private String content_desc;

	public String getContent_desc() {
		return content_desc;
	}

	public void setContent_desc(String content_desc) {
		this.content_desc = content_desc;
	}

	public String getExpress_content() {
		return express_content;
	}

	private int content_type;

	public int getContent_type() {
		return content_type;
	}

	public void setContent_type(int content_type) {
		this.content_type = content_type;
	}

	public void setExpress_content(String express_content) {
		this.express_content = express_content;
	}

	public int getExpress_id() {
		return express_id;
	}

	public void setExpress_id(int express_id) {
		this.express_id = express_id;
	}

	private int express_id;
}
