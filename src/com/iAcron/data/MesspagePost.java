package com.iAcron.data;

public class MesspagePost {

	/*
	 * 8 from_id 必须 Int 发送者ID，即发送者的user_id to_id 必须 String 接收者ID to_type 必须
	 * String 1:监护人，2：被监护人 msg_type 必须 Int 消息类型。1:为文字短消息；2:为语音短消息;3:系统消息
	 * msg_content 必须 String 如果为语音消息，则编码成十六进制后发出。 create_time 必须 DateTime 消息发送时间
	 */

	private int from_id;

	public int getFrom_id() {
		return from_id;
	}

	public void setFrom_id(int from_id) {
		this.from_id = from_id;
	}

	public String getTo_id() {
		return to_id;
	}

	public void setTo_id(String to_id) {
		this.to_id = to_id;
	}

	public String getTo_type() {
		return to_type;
	}

	public void setTo_type(String to_type) {
		this.to_type = to_type;
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


	private String to_id;
	private String to_type;
	private int msg_type;
	private String msg_content;
	private String create_time;

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	@Override
	public String toString() {
		return "{" + "from_id='" + from_id + '\'' + ", to_id=" + to_id
				+ ", to_type=" + to_type + ", msg_type=" + msg_type
				+ ", msg_content=" + msg_content + ", create_time=" + create_time + '}';
	}
}
