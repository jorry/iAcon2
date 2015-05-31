package com.iAcon.recever;

import java.util.Map;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;

import com.alibaba.fastjson.JSON;
import com.iAcon.database.manager.BindWardBeanManager;
import com.iAcon.database.model.BindWardBean;
import com.iAcron.ChatMessage;
import com.iAcron.MainActivity;
import com.iAcron.R;
import com.iAcron.data.Constants;
import com.iAcron.util.LogUtil;
import com.iAcron.util.ThreadUtil;
import com.iAcron.util.Util;

/**
 * 自定义接收器
 * 
 * 1.总共有6种类型 2.跳转有两种请求；1种是程序正在运行。1种是没有运行
 */
public class MyReceiver extends BroadcastReceiver {

	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		this.mContext = context;
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			processCustomMessage(context, bundle);
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
		} else {
		}

	}

	/*
	 * 判断是否正在运行
	 */
	private boolean isRunningForeground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		if (!TextUtils.isEmpty(currentPackageName)
				&& currentPackageName.equals(context.getPackageName())) {
			return true;
		}

		return false;
	}

	/**
	 * 重新启动
	 * 
	 * @param context
	 * @param c
	 * @param type
	 * @param id
	 * @param sid
	 * @return
	 */
	public Intent getCatchIntent(String msg) {
		Intent in = new Intent();
		in.setClass(mContext, MainActivity.class);
		Bundle b = new Bundle();
		b.putString("msg", msg);
		in.putExtras(b);
		in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return in;
	}

	/**
	 * map 接收到的内容
	 * 
	 * @param pi
	 *            跳转的activity
	 */
	@SuppressWarnings("deprecation")
	public void showNofication(String message, PendingIntent pi) {
		NotificationManager nm = (NotificationManager) mContext
				.getSystemService(mContext.NOTIFICATION_SERVICE);
		Notification baseNF = new Notification();

		// 设置通知在状态栏显示的图标
		baseNF.icon = R.drawable.icon;
		String alert = message;
		if (TextUtils.isEmpty((alert))) {
			baseNF.tickerText = this.mContext.getResources().getString(
					R.string.app_name);
		} else {
			baseNF.tickerText = alert;
		}

		String title = this.mContext.getResources()
				.getString(R.string.app_name);
		// 通知时在状态栏显示的内容

		// 通知的默认参数 DEFAULT_SOUND, DEFAULT_VIBRATE, DEFAULT_LIGHTS.
		// 如果要全部采用默认值, 用 DEFAULT_ALL.
		// 此处采用默认声音
		baseNF.defaults |= Notification.DEFAULT_SOUND;
		baseNF.defaults |= Notification.DEFAULT_VIBRATE;
		baseNF.defaults |= Notification.DEFAULT_LIGHTS;

		// 通知被点击后，自动消失
		baseNF.flags |= Notification.FLAG_AUTO_CANCEL;

		// 点击'Clear'时，不清楚该通知(QQ的通知无法清除，就是用的这个)
		baseNF.flags |= Notification.FLAG_NO_CLEAR;

		// 第二个参数 ：下拉状态栏时显示的消息标题 expanded message title
		// 第三个参数：下拉状态栏时显示的消息内容 expanded message text
		// 第四个参数：点击该通知时执行页面跳转
		baseNF.setLatestEventInfo(this.mContext, title, alert, pi);

		// 发出状态栏通知
		// The first parameter is the unique ID for the Notification
		// and the second is the Notification object.
		nm.notify(R.drawable.ic_launcher, baseNF);
	}

	/**
	 * 
	 * 创建PendingIntent 工厂
	 * 
	 * @param type
	 */
	public PendingIntent createPendingInentFactoryisRunning(Intent intent) {
		PendingIntent pendingIntent = null;
		pendingIntent = PendingIntent.getActivity(this.mContext, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		return pendingIntent;
	}

	// send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {

		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

		LogUtil.i("jpush", "jpush.message  = " + message + "  " + extras);

		Map<String, Object> map = (Map<String, Object>) JSON.parse(message);
		String type = (String) map.get("msg_type");
		System.out.println("type" + type);
		BindWardBeanManager manager = new BindWardBeanManager(mContext);
		Intent in = null;
		String notifyMsg = null;
		
		boolean isRun = isRunningForeground(mContext);
		if (type.equals("notify")) { // 提取消息
			in = getClass(ChatMessage.class);
			notifyMsg = "有新消息";
			in = new Intent();
			in.putExtra("json", message);
			in.setClass(context, jPushService.class);
			context.startService(in);
			
		} else if (type.equals("SOS")) { // 报警 //完成
			BindWardBean info = manager.getUserInfo((Integer) map
					.get("ward_id"));
			if (info == null) {
				return;
			}
			int sos_level = (Integer) map.get("sos_level");

			info.setReleaseSOS(sos_level);
			manager.update(info);

			if (isRun)
				ThreadUtil.sendMessage(Constants.push_SOS, message);
			StringBuilder sb = new StringBuilder();
			sb.append("被监护人").append(info.getWard_name());
			sb.append("发生").append(map.get("sos_name")).append("警报");
			notifyMsg = sb.toString();
		} else if (type.equals("RELEASESOS")) { // 收到解除警报

			BindWardBean info = manager.getUserInfo((Integer) map
					.get("ward_id"));
			if (info == null) {
				return;
			}
			info.setReleaseSOS(0);
			manager.update(info);

			StringBuilder sb = new StringBuilder();
			String name = (String) map.get("release_name");
			String ward_name = (String) map.get("ward_name");
			sb.append(ward_name).append("的SOS警报已经被").append(name).append("解除");
			notifyMsg = sb.toString();
			if (isRun)
				ThreadUtil.sendMessage(Constants.push_RELEASESOS, message);

		} else if (type.equals("BINDAPPLY")) { // 监护组成员向监护组组长申请监护的时候
			if (isRun)
				ThreadUtil.sendMessage(Constants.push_BINDAPPLY, message);
			StringBuilder sb = new StringBuilder();
			String name = (String) map.get("applier_user_name");
			String ward_name = (String) map.get("ward_name");
			sb.append(name).append("申请成为").append(ward_name).append("的监护人");
			notifyMsg = sb.toString();
			in = getCatchIntent(message);
		} else if (type.equals("APPLYRESULT")) { // 将组长的处理结果推送给【组员】
			if (isRun)
				ThreadUtil.sendMessage(Constants.push_APPLYRESULT, message);

			StringBuilder sb = new StringBuilder();
			String ward_name = (String) map.get("ward_name");
			int apply_result = (Integer) map.get("apply_result");

			sb.append("您申请成为").append(ward_name).append("的监护人已经被")
					.append(apply_result == 1 ? "批准" : "拒绝");
			notifyMsg = sb.toString();

		} else if (type.equals("UNBIND")) { // 监护组长解绑组成员的时候
											// 您被[leader_name]从[ward_name]的监护组中移除。
			if (isRun)
				ThreadUtil.sendMessage(Constants.push_UNBIND, message);
			StringBuilder sb = new StringBuilder();
			String name = (String) map.get("leader_name");
			String ward_name = (String) map.get("ward_name");
			sb.append("您被").append(name).append("从").append(ward_name)
					.append("的监护组中移除");
			notifyMsg = sb.toString();
		}
		if (isRunningForeground(mContext)) {
			PendingIntent intent = createPendingInentFactoryisRunning(in != null ? in
					: new Intent());
			showNofication(notifyMsg, intent);
		} else {
			PendingIntent intent = createPendingInentFactoryisRunning(getCatchIntent(message));
			showNofication(notifyMsg, intent);
		}

	}
	
	
	public Intent getClass(Class<?> c){
		Intent in = new Intent();
		if(c!=null){
			in.setClass(mContext, c);
		}
		return in;
	}
}
