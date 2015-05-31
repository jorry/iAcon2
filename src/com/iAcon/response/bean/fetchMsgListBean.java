package com.iAcon.response.bean;

public class fetchMsgListBean {

	/**
	 *  "content": "我",
            "fromId": 1,
            "fromName": "张三",
            "fromType": 1,
            "msgId": 7,
            "msgType": 1,
            "time": "2014-08-31 22:42:27",
            "toId": 1,
            "toType": 0
            
	 *  “msg_id”: 124888,
        ”from_id”: 12,
        ”from_name”: ”张三”,
        ”from_type”: 1,
        ”to_id”: 332,
        ”msg_type”: 1,
        ”你吃饭了吗？”,
        ”create_time”: ”2014-8-1718: 11: 58”
	 */
	
	private int msg_id;
	public int getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(int msg_id) {
		this.msg_id = msg_id;
	}
	public int getFrom_id() {
		return from_id;
	}
	public void setFrom_id(int from_id) {
		this.from_id = from_id;
	}
	public String getFrom_name() {
		return from_name;
	}
	public void setFrom_name(String from_name) {
		this.from_name = from_name;
	}
	public int getFrom_type() {
		return from_type;
	}
	public void setFrom_type(int from_type) {
		this.from_type = from_type;
	}
	public int getTo_id() {
		return to_id;
	}
	public void setTo_id(int to_id) {
		this.to_id = to_id;
	}
	public int getMsg_type() {
		return msg_type;
	}
	public void setMsg_type(int msg_type) {
		this.msg_type = msg_type;
	}
	public String getMsg_content() {
		return msg_content;
	}
	public void setMsg_content(String msg_content) {
		this.msg_content = msg_content;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	private int from_id;
	private String from_name;
	private int from_type;
	private int to_id;
	private int msg_type;
	private String msg_content;
	private String create_time;
}
