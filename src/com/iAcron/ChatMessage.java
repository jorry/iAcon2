package com.iAcron;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.iAcon.database.dao.fetchmsgDao;
import com.iAcon.database.manager.JianhuManager;
import com.iAcon.database.manager.fetchManager;
import com.iAcon.database.model.BindWardBean;
import com.iAcon.database.model.fetchmsg;
import com.iAcon.database.model.jianhu;
import com.iAcon.record.ChatMsgViewAdapter;
import com.iAcon.record.SoundMeter;
import com.iAcon.record.SoundMeter.mediaPlayerInterface;
import com.iAcon.response.BaseResponse;
import com.iAcon.response.fetchMsgListResponse;
import com.iAcon.response.bean.AllContactBean;
import com.iAcon.response.bean.fetchMsgListBean;
import com.iAcron.data.AppData;
import com.iAcron.data.Constants;
import com.iAcron.data.MesspagePost;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.Util;
import com.iAcron.view.MyToast;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.Handler.Callback;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 语音	
 */
public class ChatMessage extends BaseFragmentActivity implements
		OnClickListener {
	/** Called when the activity is first created. */

	private Button mBtnSend;
	private TextView mBtnRcd;
	private EditText mEditTextContent;
	private RelativeLayout mBottom;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private List<fetchMsgListBean> mDataArrays = new ArrayList<fetchMsgListBean>();
	private boolean isShosrt = false;
	private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding,
			voice_rcd_hint_tooshort;
	private ImageView img1, sc_img1;
	private SoundMeter mSensor;
	private View rcChat_popup;
	private LinearLayout del_re;
	private ImageView chatting_mode_btn, volume;
	private boolean btn_vocie = false;
	private int flag = 1;
	private Handler mHandler = new Handler();
	private long startVoiceT, endVoiceT;

	SoundMeter meter = new SoundMeter();


	BindWardBean bean;
	
	private Handler.Callback msgCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.msg_nofity_update:
				mAdapter.getFetchAll(bean);
				break;
			}
			return false;
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);

		regMsg(Constants.msg_nofity_update, msgCallback);
		
		bean = (BindWardBean) getIntent().getExtras().getSerializable("obj");
		
		findViewById(R.id.menuBtn).setVisibility(View.GONE);
		Button b = (Button) findViewById(R.id.homeBtn);
		b.setVisibility(View.VISIBLE);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		TextView tv = (TextView) findViewById(R.id.topbar_title);
		tv.setText(bean.getWard_name());
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		requestUpdateMsgAsRead();
		mAdapter.getFetchAll(bean);
		
		
	}

	/**
	 * 更新为已读
	 */
	public void requestUpdateMsgAsRead() {
		String url = "http://112.74.95.154:8080/iAcron/updateMsgAsRead";
		Common rp = new Common();
		final fetchManager manager = new fetchManager(this);
		List<fetchmsg> fets = manager.getDepositItem(bean.getWard_id(),bean.getTo_type());
		StringBuffer sb = new StringBuffer();
		for (fetchmsg msg : fets) {
			if (msg.getMsg_status() == null || msg.getMsg_status() == 1) {
				sb.append(msg.getMsg_id()).append(",");
			}
		}
		if (sb.toString().length() <= 0) {
			return;
		}
		rp.setKV("msg_id_list", sb.toString());

		RequstClient client = new RequstClient(this, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				finishWaitDialog();
				BaseResponse fm = new BaseResponse(getApplicationContext());
				fm.parse(data);
				if (fm.result == 1) {
					List<fetchmsg> fets = manager.getDepositItem(bean
							.getWard_id(),bean.getTo_type());
					for (fetchmsg msg : fets) {
						if (msg.getMsg_status() == 1) {
							msg.setMsg_status(2);
							manager.update(msg);
						}

					}

				}
			}

			@Override
			public void onAppError(int error, Throwable message) {
				finishWaitDialog();
			}

			@Override
			public void onFailure(int error, Throwable message) {
				finishWaitDialog();
			}

		});
		client.post(url, rp.getParams());
	}


	/**
	 * 发送请求 from_id 必须 Int 发送者ID，即发送者的user_id to_id 必须 String 接收者ID to_type 必须
	 * String 1:监护人，2：被监护人 msg_type 必须 Int 消息类型。1:为文字短消息；2:为语音短消息;3:系统消息
	 * msg_content 必须 String 如果为语音消息，则编码成十六进制后发出。 create_time 必须 DateTime 消息发送时间
	 */
	public void requestPost(int type) {
		System.out.println("进来几次");
		/**
		 * from
		 */
		String url = "http://112.74.95.154:8080/iAcron/sendMsg";

		Common rp = new Common();

		MesspagePost msp = new MesspagePost();

		msp.setFrom_id(Integer.parseInt(AppData.user_id.get()));
		msp.setTo_id("" + bean.getWard_id()); // 如果没有收到消息，lists可能数组越界
		int msg_type = btn_vocie ? 2 : 1;
		int to_type = bean.getTo_type()==1?1:2;
		msp.setTo_type("" + to_type);
		msp.setMsg_type(msg_type);	
		msp.setCreate_time("" + System.currentTimeMillis());
		if (msg_type == 2) {
			System.out.println("voicenAME" + voiceName);
			byte[] data = meter.getAudio(voiceName);
			msp.setMsg_content(Util.convertVoiceToHexText(data));
		} else {
			msp.setMsg_content(mEditTextContent.getText().toString());

		}

		String value = JSON.toJSONString(msp);
		rp.setKV("msg_content", value);
		reqeustPost(url, rp.getParams());
	}

	/**
	 * 音频文件转字符串啊
	 * @param voiceData
	 * @return
	 */
	public static String convertVoiceToHexText(byte[] voiceData) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < voiceData.length; i++) {
			String hex = Integer.toHexString(voiceData[i] & 0xff);
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			buf.append(hex);
		}
		return buf.toString();
	}

	/**字符串转音频文件
	 *
	 * @param strVoice
	 * @return
	 */
	public static byte[] convertHexTextToVoice(String strVoice) {
		short[] voiceData = new short[strVoice.length() / 2];
		int byteIndex = 0;
		for (int i = 0; i < strVoice.length(); i += 2) {
			voiceData[byteIndex++] = Short.parseShort(
					strVoice.substring(i, i + 2), 16);
		}
		byte[] vd = new byte[voiceData.length];
		for (int i = 0; i < voiceData.length; i++) {
			vd[i] = (byte) (voiceData[i] & 0xff);
		}
		return vd;
	}

	public void reqeustPost(String url, RequestParams rp) {
		startWaitDialog("网络连接", "努力加载中.....", true);

		RequstClient client = new RequstClient(this, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				finishWaitDialog();
				BaseResponse fm = new BaseResponse(getApplicationContext());
				fm.parse(data);
				if (fm.result == 1) {
					MyToast.showToast(ChatMessage.this, "发送完成   ---1");
					
					fetchmsg fetch = new fetchmsg();

					MyToast.showToast(ChatMessage.this, "发送完成   ---2");
					
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String time = format.format(new Date());
					int type = bean.getTo_type();
					fetch.setType(type);
					fetch.setWard_id("" + bean.getWard_id());
					fetch.setTo_id("" + bean.getWard_id());
					
					fetch.setCreate_time(time);
					fetch.setFrom_name(bean.getWard_name());
					fetch.setFrom_type(1);
					
					fetch.setFrom_id(Integer.parseInt(AppData.user_id.get()));
					int msg_type = btn_vocie ? 2 : 1;
					if (msg_type == 2) {
						byte[] data1 = meter.getAudio(voiceName);
						fetch.setMsg_content(convertVoiceToHexText(data1));
					} else {
						fetch.setMsg_content(mEditTextContent.getText()
								.toString());

					}
					
					fetch.setMsg_type(msg_type);
					fetch.setMsg_id((long)fm.msg_id);
					fetch.setMsg_status(2);
					MyToast.showToast(ChatMessage.this, "发送完成   ---4");
					
					mAdapter.set(fetch);
					mListView.setSelection(mListView.getBottom());
					MyToast.showToast(ChatMessage.this, "发送完成   --5");
					
					fetchManager m = new fetchManager(ChatMessage.this);
					m.insertDeposit(fetch);
					MyToast.showToast(ChatMessage.this, "发送完成   ---6");
					
				} else {
					if (!TextUtils.isEmpty(fm.result_msg)) {
						MyToast.showToast(ChatMessage.this, "可能网络原因，稍后重试");
					} else {
						MyToast.showToast(ChatMessage.this, fm.result_msg);
					}
				}
				MyToast.showToast(ChatMessage.this, "发送完成   ---finish");
				
				mEditTextContent.setText("");
			}

			@Override
			public void onAppError(int error, Throwable message) {
				finishWaitDialog();
			}

			@Override
			public void onFailure(int error, Throwable message) {
				finishWaitDialog();
			}

		});
		client.post(url, rp);
	}

	public void initView() {
		
		mAdapter = new ChatMsgViewAdapter(this);
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setAdapter(mAdapter);
		mBtnSend = (Button) findViewById(R.id.btn_send);
		mBtnRcd = (TextView) findViewById(R.id.btn_rcd);
		mBtnSend.setOnClickListener(this);
		mBottom = (RelativeLayout) findViewById(R.id.btn_bottom);
		chatting_mode_btn = (ImageView) this.findViewById(R.id.ivPopUp);
		volume = (ImageView) this.findViewById(R.id.volume);
		rcChat_popup = this.findViewById(R.id.rcChat_popup);
		img1 = (ImageView) this.findViewById(R.id.img1);
		sc_img1 = (ImageView) this.findViewById(R.id.sc_img1);
		del_re = (LinearLayout) this.findViewById(R.id.del_re);
		voice_rcd_hint_rcding = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_rcding);
		voice_rcd_hint_loading = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_loading);
		voice_rcd_hint_tooshort = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_tooshort);
		mSensor = new SoundMeter();
		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);

		// 语音文字切换按钮
		chatting_mode_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (btn_vocie) {
					mBtnRcd.setVisibility(View.GONE);
					mBottom.setVisibility(View.VISIBLE);
					btn_vocie = false;
					chatting_mode_btn
							.setImageResource(R.drawable.chatting_setmode_msg_btn);

				} else {
					mBtnRcd.setVisibility(View.VISIBLE);
					mBottom.setVisibility(View.GONE);
					chatting_mode_btn
							.setImageResource(R.drawable.chatting_setmode_voice_btn);
					btn_vocie = true;
				}
			}
		});

		mBtnRcd.setVisibility(View.VISIBLE);
		mBottom.setVisibility(View.GONE);
		chatting_mode_btn
				.setImageResource(R.drawable.chatting_setmode_voice_btn);
		btn_vocie = true;

		mBtnRcd.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// 按下语音录制按钮时返回false执行父类OnTouch
				return false;
			}
		});
		
	}


	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_send:
			requestPost(0);
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (!Environment.getExternalStorageDirectory().exists()) {
			Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
			return false;
		}

		if (btn_vocie) {
			System.out.println("1"); // ���涓�
			int[] location = new int[2];
			mBtnRcd.getLocationInWindow(location); // ��峰����ㄥ�����绐���ｅ�����缁�瀵瑰�����
			int btn_rc_Y = location[1];
			int btn_rc_X = location[0];
			int[] del_location = new int[2];
			del_re.getLocationInWindow(del_location);
			int del_Y = del_location[1];
			int del_x = del_location[0];
			if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
				if (!Environment.getExternalStorageDirectory().exists()) {
					Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
					return false;
				}
				System.out.println("2"); // ������瀛����
				if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {// ��ゆ�������挎��涓����浣�缃����������璇���冲����舵�������������村��
					System.out.println("3"); // 寮�濮�褰����
					mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
					rcChat_popup.setVisibility(View.VISIBLE);
					voice_rcd_hint_loading.setVisibility(View.VISIBLE);
					voice_rcd_hint_rcding.setVisibility(View.GONE);
					voice_rcd_hint_tooshort.setVisibility(View.GONE);
					mHandler.postDelayed(new Runnable() {
						public void run() {
							if (!isShosrt) {
								voice_rcd_hint_loading.setVisibility(View.GONE);
								voice_rcd_hint_rcding
										.setVisibility(View.VISIBLE);
							}
						}
					}, 300);
					img1.setVisibility(View.VISIBLE);
					del_re.setVisibility(View.GONE);
					startVoiceT = SystemClock.currentThreadTimeMillis();
					voiceName = startVoiceT + ".amr";
					start(voiceName);
					flag = 2;
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2) {// ��惧�������挎�舵�ц��褰���跺�����
																					// 锛�杩�涓�搴�璇ユ�����娑�褰����
				System.out.println("4");
				mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
				if (event.getY() >= del_Y
						&& event.getY() <= del_Y + del_re.getHeight()
						&& event.getX() >= del_x
						&& event.getX() <= del_x + del_re.getWidth()) {
					rcChat_popup.setVisibility(View.GONE);
					img1.setVisibility(View.VISIBLE);
					del_re.setVisibility(View.GONE);
					cancel();
					flag = 1;
				} else {

					voice_rcd_hint_rcding.setVisibility(View.GONE);
					stop();
					endVoiceT = SystemClock.currentThreadTimeMillis();
					flag = 1;
					int time = (int) ((endVoiceT - startVoiceT) / 100);
					if (time < 1) {
						isShosrt = true;
						voice_rcd_hint_loading.setVisibility(View.GONE);
						voice_rcd_hint_rcding.setVisibility(View.GONE);
						voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
						mHandler.postDelayed(new Runnable() {
							public void run() {
								voice_rcd_hint_tooshort
										.setVisibility(View.GONE);
								rcChat_popup.setVisibility(View.GONE);
								isShosrt = false;
							}
						}, 500);
						return false;
					}

					stop();
					endVoiceT = SystemClock.currentThreadTimeMillis();
					flag = 1;
					requestPost(1);

				}
			}
			if (event.getY() < btn_rc_Y) {// �����挎��涓����浣�缃�涓���ㄨ����冲����舵�������������村��
				System.out.println("5");
				Animation mLitteAnimation = AnimationUtils.loadAnimation(this,
						R.anim.cancel_rc);
				Animation mBigAnimation = AnimationUtils.loadAnimation(this,
						R.anim.cancel_rc2);
				img1.setVisibility(View.GONE);
				del_re.setVisibility(View.VISIBLE);
				del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg);
				if (event.getY() >= del_Y
						&& event.getY() <= del_Y + del_re.getHeight()
						&& event.getX() >= del_x
						&& event.getX() <= del_x + del_re.getWidth()) {
					del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg_focused);
					sc_img1.startAnimation(mLitteAnimation);
					sc_img1.startAnimation(mBigAnimation);
				}
			} else {

				img1.setVisibility(View.VISIBLE);
				del_re.setVisibility(View.GONE);
				del_re.setBackgroundResource(0);
			}
		}
		return super.onTouchEvent(event);
	}

	// 按下语音录制按钮时
	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	//
	// if (!Environment.getExternalStorageDirectory().exists()) {
	// Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
	// return false;
	// }
	//
	// if (btn_vocie) {
	// System.out.println("1"); // 按下
	// int[] location = new int[2];
	// mBtnRcd.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
	// int btn_rc_Y = location[1];
	// int btn_rc_X = location[0];
	// int[] del_location = new int[2];
	// del_re.getLocationInWindow(del_location);
	// int del_Y = del_location[1];
	// int del_x = del_location[0];
	//
	// System.out.println(flag+"onTouch........."+event.getAction());
	//
	// if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
	// if (!Environment.getExternalStorageDirectory().exists()) {
	// Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
	// return false;
	// }
	// System.out.println("2"); // 有内存卡
	// if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {//
	// 判断手势按下的位置是否是语音录制按钮的范围内
	// System.out.println("3"); // 开始录制
	// mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
	// rcChat_popup.setVisibility(View.VISIBLE);
	// voice_rcd_hint_loading.setVisibility(View.VISIBLE);
	// voice_rcd_hint_rcding.setVisibility(View.GONE);
	// voice_rcd_hint_tooshort.setVisibility(View.GONE);
	// mHandler.postDelayed(new Runnable() {
	// public void run() {
	// if (!isShosrt) {
	// voice_rcd_hint_loading.setVisibility(View.GONE);
	// voice_rcd_hint_rcding
	// .setVisibility(View.VISIBLE);
	// }
	// }
	// }, 300);
	// img1.setVisibility(View.VISIBLE);
	// del_re.setVisibility(View.GONE);
	// startVoiceT = SystemClock.currentThreadTimeMillis();
	// String voiceName = startVoiceT + ".amr";
	// start(voiceName);
	// flag = 2;
	//
	//
	// System.out.println("onTouch.......ACTION_DOWN && flag == 1..");
	//
	//
	//
	// }
	// } else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2) {//
	// 松开手势时执行录制完成
	// // ；这个应该是取消录制
	// System.out.println("4");
	// mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
	// if (event.getY() >= del_Y
	// && event.getY() <= del_Y + del_re.getHeight()
	// && event.getX() >= del_x
	// && event.getX() <= del_x + del_re.getWidth()) {
	// rcChat_popup.setVisibility(View.GONE);
	// img1.setVisibility(View.VISIBLE);
	// del_re.setVisibility(View.GONE);
	// cancel();
	// flag = 1;
	//
	// System.out.println("onTouch...if....MotionEvent.ACTION_UP && flag == 2..");
	//
	//
	//
	// } else {
	// System.out.println("onTouch..else.....MotionEvent.ACTION_UP && flag == 2..");
	//
	// voice_rcd_hint_rcding.setVisibility(View.GONE);
	// stop();
	// endVoiceT = SystemClock.currentThreadTimeMillis();
	// flag = 1;
	// requestPost(1);
	// }
	// }
	// if (event.getY() < btn_rc_Y) {// 手势按下的位置不在语音录制按钮的范围内
	//
	// System.out.println("onTouch....手势按下的位置不在语音录制按钮的范围内...MotionEvent.ACTION_UP && flag == 2..");
	//
	//
	// System.out.println("5");
	// Animation mLitteAnimation = AnimationUtils.loadAnimation(this,
	// R.anim.cancel_rc);
	// Animation mBigAnimation = AnimationUtils.loadAnimation(this,
	// R.anim.cancel_rc2);
	// img1.setVisibility(View.GONE);
	// del_re.setVisibility(View.VISIBLE);
	// del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg);
	// if (event.getY() >= del_Y
	// && event.getY() <= del_Y + del_re.getHeight()
	// && event.getX() >= del_x
	// && event.getX() <= del_x + del_re.getWidth()) {
	// del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg_focused);
	// sc_img1.startAnimation(mLitteAnimation);
	// sc_img1.startAnimation(mBigAnimation);
	// }
	// } else {
	// System.out.println("onTouch....啥也没有..MotionEvent.ACTION_UP && flag == 2..");
	//
	// img1.setVisibility(View.VISIBLE);
	// del_re.setVisibility(View.GONE);
	// del_re.setBackgroundResource(0);
	// }
	// }
	// return super.onTouchEvent(event);
	// }

	private static final int POLL_INTERVAL = 300;

	private Runnable mSleepTask = new Runnable() {
		public void run() {
			stop();
		}
	};

	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSensor.getAmplitude();
			updateDisplay(amp);
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);

		}
	};
	String voiceName;

	private void start(String name) {
		voiceName = mSensor.recordStart(new SoundMeter.RecordAmpListener() {

			public void getAmp(int arm) {
				// TODO Auto-generated method stub
				System.out.println("arm = " + arm);
			}
		}, name);

		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	private void stop() {
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		mSensor.finish();
		volume.setImageResource(R.drawable.amp1);
	}

	private void cancel() {
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		mSensor.cancel();
		volume.setImageResource(R.drawable.amp1);
	}

	private void updateDisplay(double signalEMA) {

		switch ((int) signalEMA) {
		case 0:
		case 1:
			volume.setImageResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			volume.setImageResource(R.drawable.amp2);

			break;
		case 4:
		case 5:
			volume.setImageResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			volume.setImageResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			volume.setImageResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			volume.setImageResource(R.drawable.amp6);
			break;
		default:
			volume.setImageResource(R.drawable.amp7);
			break;
		}
	}

}