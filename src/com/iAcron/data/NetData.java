package com.iAcron.data;

import com.loopj.android.http.RequestParams;

/**
 * 接口数据
 * 
 * @author jorry
 * 
 */
public class NetData {

	public final static String requestCode = "http://218.61.5.10:9321/iAcron/reqCheckCode";
	public final static String getRegister = "http://218.61.5.10:9321/iAcron/register";
	public final static String login = "http://218.61.5.10:9321/iAcron/login";
	public final static String resetPassword = "http://218.61.5.10:9321/iAcron/resetPassword";

	/**
	 * 
	 * 注册：请求验证码接口
	 * 
	 * @param phone
	 *            手机号
	 * @param requestType
	 *            验证码用途。 1:注册新用户，2:重置密码
	 * 
	 * @return
	 */
	public static RequestParams getRequstCode(String phone, int requestType) {
		RequestParams pa = new RequestParams();
		pa.add("phone", phone);
		pa.add("requestType", "" + requestType);
		return pa;
	}

	/**
	 * 注册：
	 * 
	 * http://218.61.5.10:9321/iAcron/register
	 * 
	 * user_name 必须 String 用户名 mob_tel_no 必须 String 用户手机号 email String 用户电子邮件
	 * pwd 必须 String 密码 check_code 必须 String
	 * 为防止恶意注册，需要在注册是提供验证码（在页面注册时通过该方式，通过手机注册时，此处即收到的短信验证码。手机验证码需要加强。）
	 * 
	 * @return
	 */
	public static String getRegister(String userName, String mob_tel_no,
			String email, String pwd, String check_code) {
		StringBuilder sb = new StringBuilder(getRegister);
		sb.append("?").append("getRegister=").append(userName);
		sb.append("&").append("mob_tel_no").append(mob_tel_no);
		sb.append("&").append("email").append(email);
		sb.append("&").append("pwd").append(pwd);
		sb.append("&").append("check_code").append(check_code);
		return sb.toString();
	}

	/**
	 * 登录
	 * 
	 * http://218.61.5.10:9321/iAcron/login
	 * 
	 * login_name 必须 String 账号，可以是用户名，手机号或邮箱号 pwd 必须 String 密码
	 * 
	 * @return
	 */
	public static String requestLogin(String login_name, String pwd) {
		StringBuilder sb = new StringBuilder(login);
		sb.append("&").append("login_name").append(login_name);
		sb.append("&").append("pwd").append(pwd);
		return sb.toString();
	}

	/**
	 * 密码重置
	 * 
	 * login_name 必须 String 登录名称，可以是用户名/手机号/邮箱地址 new_pwd 必须 String 新密码
	 * apply_type 必须 Int 1:代表手机验证2:代表邮箱验证 check_code 必须 String 验证码
	 * 
	 * @return
	 */
	public static String resetPassword(String login_name, String new_pwd,
			int apply_type, String check_code) {
		StringBuilder sb = new StringBuilder(resetPassword);
		sb.append("&").append("login_name").append(login_name);
		sb.append("&").append("new_pwd").append(new_pwd);
		sb.append("&").append("apply_type").append(apply_type);
		sb.append("&").append("check_code").append(check_code);
		return sb.toString();
	}

}
