package com.iAcron.data;

import android.os.Message;

public class Constants {

	public final static int openMenu = 100;
	public final static int addTab = 101;
	public final static String mapReceiver = "com.map.update";
	
	/**
	 *发送给app的消息内容是JSON数据格式的字符串。JSON格式解释如下所示：
	 */
	public final static int msg_nofity_update = 1002;
	/**
	 * 发送给app的消息内容是JSON数据格式的字符串
	 */
	public final static int push_SOS = 1003;
	public final static int msg_SOS_releace = 1004;
	
	/** 推送给【监护组长】，等待组长处理
	 * 
	 * msg_type	必须	String	由平台主动下发给app的消息公共字段。App可以通过该字段确定消息类型。
这里的值在此处始终为BINDAPPLY。
msg_time	必须	String	由平台主动下发给app的消息公共字段。推送时间
applier_user_id	必须	Int	申请者的用户ID
applier_user_name	必须	String	申请者的用户姓名
ward_id	必须	Int	申请绑定的被监护人ID
ward_name	必须	String	申请绑定的被监护人姓名
{"msg_type":"BINDAPPLY","msg_time":"2014-10-18 10:19:07","applier_user_id":4,"applier_user_name":"zsx991145188","ward_id":6,"ward_name":"张习贺"}
msg_time值说明：在推送SOS时为SOS发生时间，即为腕表上传生理信息时间；在推送绑定申请或绑定结果时，为Jpush推送的时间

	 */
	public final static int push_BINDAPPLY = 1005;
	/**
	 * 将组长的处理结果推送给【申请者】
	 * 
	 * msg_type	必须	String	由平台主动下发给app的消息公共字段。App可以通过该字段确定消息类型。
这里的值在此处始终为APPLYRESULT。
msg_time	必须	String	由平台主动下发给app的消息公共字段。推送时间
ward_id	必须	Int	申请绑定的被监护人ID
ward_name	必须	String	被监护人姓名
apply_result	必须	Int	批准结果。
1:批准，0：拒绝
refuse_reason		String	拒绝原因。只有拒绝时才存在



	 */
	public final static  int push_APPLYRESULT = 1006;
	
	/**
	 * 监护组长解绑组成员的时候
	 * 
	 * 推送给【被解除绑定的用户】
		参数名称	是否必须	类型	描述
msg_type	必须	String	由平台主动下发给app的消息公共字段。App可以通过该字段确定消息类型。
这里的值在此处始终为UNBIND。
msg_time	必须	String	由平台主动下发给app的消息公共字段。推送时间
ward_id	必须	Int	被监护人Id
ward_name	必须	String	被监护人姓名
leader_id	必须	Int	操作者（组长）Id
leader_name	必须	String	操作者（组长）的姓名
{"msg_type":"UNBIND","msg_time":"2014-10-18 13:33:13","ward_id":6,"ward_name":"张习贺","leader_id":4,"leader_name":"zsx991145188"}
--App后台接收信息
--App同时解析推送的数据，同时删除本机关于被监护人的信息
	 */
	public static final int push_UNBIND = 1007;
	/**
	 * RELEASESOS手动解除警报
	 * 
	 * 手动解除警报后，将向所有SOS成员发出一个解除警报的推送
参数名称	是否必须	类型	描述
msg_type	必须	String	由平台主动下发给app的消息公共字段。App可以通过该字段确定消息类型。
这里的值在此处始终为RELEASESOS。
msg_time	必须	String	由平台主动下发给app的消息公共字段。推送时间
ward_id	必须	Int	被监护人Id
ward_name	必须	String	被监护人姓名
release_id	必须	Int	解除警报的sos成员Id
release_name	必须	String	解除警报的sos成员姓名
 {"msg_type":"RELEASESOS","msg_time":"2014-10-19 13:55:01","release_id":3,"release_name":"zxh","ward_id":6,"ward_name":"张习贺"}
msg_time值说明：在推送SOS时为SOS发生时间，即为腕表上传生理信息时间；在推送绑定申请或绑定结果时，为Jpush推送的时间




	 */
	
	public final static int push_RELEASESOS= 1008;
	public static final int UPDATE = 1009;
	public static final int CONTACTFIINISH = UPDATE+1;
	public static final int  updateMap = CONTACTFIINISH+1;
	public static final int  contactBack = updateMap+1;
	
}
