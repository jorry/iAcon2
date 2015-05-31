package com.iAcon.response.bean;

import java.io.Serializable;

public class getWardReindBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 	remind_id	Int	用户ID
		loop_type	Int	提醒循环类型。1:一次性提醒，2：每周周期性提醒，3：每月周期性提醒
		loop_value	String	loop_type=1，此处为yyyy-MM-dd，表示在这一天的remind_time时间进行提醒。loop_type=2，此处为周几，可以多个数值，逗号分隔。每个值的范围为1～7，分别代表周一到周日。loop_type=3，类同每周，其值范围为从1～31。
		remind_time	String	提醒时间。
		remind_type	String	提醒类型，可选值包括：运动，睡觉，吃饭，喝水，吃药，其他。
		content_type	Int	提醒内容类型。1：文本，2：语音
		remind_content	String	提醒内容
		enabled	Int	该提醒是否为启用状态。1：启用，2：不启用 ,                                                                                                                                                                                                                                                                                                                                           
	 */


	public int remind_id;
	public int loop_type;
	public String loop_value;
	public String remind_time;
	public String remind_type;
	public String content_type;
	public String remind_content;
	public int enabled;
}
