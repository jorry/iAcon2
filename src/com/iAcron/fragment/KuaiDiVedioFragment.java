package com.iAcron.fragment;

import java.io.File;

import com.alibaba.fastjson.JSONObject;
import com.iAcon.record.SoundMeter;
import com.iAcon.response.BaseResponse;
import com.iAcon.response.kuaiDiList;
import com.iAcon.response.bean.family_express_lis;
import com.iAcron.BaseFragmentActivity;
import com.iAcron.ChatMessage;
import com.iAcron.R;
import com.iAcron.iAconApplication;
import com.iAcron.fragment.DFragment.ButtonOnClick;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.LayoutBuilder;
import com.iAcron.util.Util;
import com.iAcron.view.MyToast;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class KuaiDiVedioFragment extends BaseFregment implements
		OnClickListener {

	EditText zyEt;

	TextView mm, ss;
	TextView startRecordTv;

	ImageView ting, delete, changeToTxt;
	Button send, save;

	private int flag = 1;
	private Handler mHandler = new Handler();
	private long startVoiceT, endVoiceT;
	private SoundMeter mSensor;
	private ImageView ampImage;
	private LinearLayout ampLayout;
	SoundMeter meter = new SoundMeter();
	family_express_lis bean;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.kuaidi_vedio_view, null);
		v.findViewById(R.id.menuBtn).setVisibility(View.GONE);
		

		TextView tv = (TextView)v.findViewById(R.id.topbar_title);
		tv.setText("自定义亲情快递");

		Button img = (Button) v.findViewById(R.id.homeBtn);
		img.setVisibility(View.VISIBLE);
		img.setBackgroundResource(R.drawable.u8);
		img.setOnClickListener(this);

		if (getArguments() != null) {
			
			bean = (family_express_lis) getArguments().getSerializable("obj");
			if(bean.getContent_type()==2){
				byte data[] = ChatMessage.convertHexTextToVoice(bean
						.getExpress_content());
				voiceName = Util.savePlayer("linshi", data);
			}
		}

	

		mSensor = new SoundMeter();
		zyEt = (EditText) v.findViewById(R.id.vedio_zy_edit);
		if (bean != null) {
			if(bean.getContent_type()==2){
				zyEt.setText(bean.getContent_desc());
			}
		}
		ampImage = (ImageView) v.findViewById(R.id.amp);
		ampLayout = (LinearLayout) v.findViewById(R.id.amplayout);
		ampLayout.setVisibility(View.GONE);

		mm = (TextView) v.findViewById(R.id.vedio_mm_tv);
		ss = (TextView) v.findViewById(R.id.vedio_ss_tv);
		startRecordTv = (TextView) v.findViewById(R.id.vedio_startrecord);
		startRecordTv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if (!Environment.getExternalStorageDirectory().exists()) {
					Toast.makeText(activity, "No SDCard", Toast.LENGTH_LONG)
							.show();
					return false;
				}

				System.out.println("1"); //
				int[] location = new int[2];
				arg0.getLocationInWindow(location); //
				int btn_rc_Y = location[1];
				int btn_rc_X = location[0];
				int[] del_location = new int[2];
				int del_Y = del_location[1];
				int del_x = del_location[0];
				if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
					if (!Environment.getExternalStorageDirectory().exists()) {
						Toast.makeText(activity, "No SDCard", Toast.LENGTH_LONG)
								.show();
						return false;
					}
					ampLayout.setVisibility(View.VISIBLE);
					System.out.println("2");
					System.out.println("3"); // 寮�濮�褰����
					mHandler.postDelayed(new Runnable() {
						public void run() {
						}
					}, 300);
					startVoiceT = SystemClock.currentThreadTimeMillis();
					voiceName = startVoiceT + ".amr";
					start(voiceName);
					flag = 2;
				} else if (event.getAction() == MotionEvent.ACTION_UP
						&& flag == 2) {
					System.out.println("4");
					endVoiceT = SystemClock.currentThreadTimeMillis();
					int time = (int) ((endVoiceT - startVoiceT) / 100);
					if (time >= 60) {
						MyToast.showToast(activity, "录音时长不能超过一分钟");
						deleteFile();
						return false;
					}
					if (time < 1) {
						deleteFile();
						return false;
					}
					stop(time);

					flag = 1;
				}
				return true;
			}
		});
		ting = (ImageView) v.findViewById(R.id.vedio_ting_img);
		ting.setOnClickListener(this);
		delete = (ImageView) v.findViewById(R.id.vedio_delete_img);
		delete.setOnClickListener(this);
		changeToTxt = (ImageView) v.findViewById(R.id.vedio_changeto_txt);
		changeToTxt.setOnClickListener(this);
		send = (Button) v.findViewById(R.id.vedio_send_bt);
		send.setOnClickListener(this);

		save = (Button) v.findViewById(R.id.vedio_save_bt);
		save.setOnClickListener(this);
		return v;
	}

	private void stop(int time) {
		ampLayout.setVisibility(View.GONE);
		ss.setText("0");
		mm.setText("0");
		MyToast.showToast(activity, "录音完成");
		if (time > 10) {
			ss.setText("" + time / 10);
			mm.setText("" + time % 10);
		} else {
			ss.setText("" + time);
		}
		mHandler.removeCallbacks(mPollTask);
		mSensor.finish();
	}

	private static final int POLL_INTERVAL = 300;

	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSensor.getAmplitude();
			updateDisplay(amp);
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);

		}
	};

	private void updateDisplay(double signalEMA) {

		switch ((int) signalEMA) {
		case 0:
		case 1:
			ampImage.setImageResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			ampImage.setImageResource(R.drawable.amp2);

			break;
		case 4:
		case 5:
			ampImage.setImageResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			ampImage.setImageResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			ampImage.setImageResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			ampImage.setImageResource(R.drawable.amp6);
			break;
		default:
			ampImage.setImageResource(R.drawable.amp7);
			break;
		}
	}

	String voiceName = "";

	private void start(String name) {
		voiceName = mSensor.recordStart(new SoundMeter.RecordAmpListener() {

			public void getAmp(int arm) {
				// TODO Auto-generated method stub
				System.out.println("arm = " + arm);
			}
		}, name);

		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	/**
	 * 更新
	 * 
	 * @param bean
	 */
	public void reuqestHttpOfUpdate(family_express_lis bean) {

		activity.startWaitDialog("提示", "发送消息", false);
		((BaseFragmentActivity) activity).startWaitDialog(null, null, false);
		String url = "http://112.74.95.154:8080/iAcron/updateFamilyExpressItem";

		Common rp = new Common();
		iAconApplication app = (iAconApplication) getActivity()
				.getApplication();
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());
		try {
			JSONObject jb = new JSONObject();
			jb.put("express_id", bean.getExpress_id());
			byte[] data = meter.getAudio(voiceName);
			jb.put("express_content",Util.convertVoiceToHexText(data));
			jb.put("content_type", bean.getContent_type());
			jb.put("content_desc", bean.getContent_desc());
			
			rp.setKV("family_express", jb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequstClient client = new RequstClient(getActivity(),
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						activity.finishWaitDialog();
						BaseResponse q = new BaseResponse(activity);
						q.parse(data);
						if (q.result == 1) {
							MyToast.showToast(activity, "发送完成");
						} else {
							MyToast.showToast(activity, q.result_msg);
						}
						backStackFragment(QinQingKuaiDiListView.class);
					}

					@Override
					public void onAppError(int error, Throwable message) {
						// TODO Auto-generated method stub
						activity.finishWaitDialog();

					}

					@Override
					public void onFailure(int error, Throwable message) {
						// TODO Auto-generated method stub
						activity.finishWaitDialog();

					}

				});
		client.post(url, rp.getParams());
	}

	/**
	 * 增加
	 * 
	 * @param bean
	 */
	public void reuqestHttpOfAdd(family_express_lis bean) {
	
		activity.startWaitDialog("提示", "发送消息", false);
		((BaseFragmentActivity) activity).startWaitDialog(null, null, false);
		String url = "http://112.74.95.154:8080/iAcron/addFamilyExpressItem";

		Common rp = new Common();
		iAconApplication app = (iAconApplication) getActivity()
				.getApplication();
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());
		try {
			JSONObject jb = new JSONObject();
			jb.put("express_id", bean.getExpress_id());
			byte[] data = meter.getAudio(voiceName);
			jb.put("express_content",Util.convertVoiceToHexText(data));
			jb.put("content_type",bean.getContent_type());
			jb.put("content _desc", bean.getContent_desc());
			rp.setKV("family_express_content", jb.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequstClient client = new RequstClient(getActivity(),
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						activity.finishWaitDialog();
						BaseResponse q = new BaseResponse(activity);
						q.parse(data);
						if (q.result == 1) {
							MyToast.showToast(activity, "发送完成");
						} else {
							MyToast.showToast(activity, q.result_msg);
						}
						backStackFragment(QinQingKuaiDiListView.class);
					}

					@Override
					public void onAppError(int error, Throwable message) {
						// TODO Auto-generated method stub
						activity.finishWaitDialog();

					}

					@Override
					public void onFailure(int error, Throwable message) {
						// TODO Auto-generated method stub
						activity.finishWaitDialog();

					}

				});
		client.post(url, rp.getParams());
	}

	public void deleteFile() {
		final File f = new File(voiceName);
		if (f != null && f.exists()) {

			DFragment d = new DFragment("删除文件", "确定删除此音频文件么", "取消", "删除");
			d.setButtonOnClick(new ButtonOnClick() {

				@Override
				public void poOnclick() {
					f.delete();
				}

				@Override
				public void neOnclick() {
				}
			});
			d.show(getFragmentManager(), "kuaiDiVedio");

		} else {
			MyToast.showToast(activity, "还没有录制");
		}
	}

	public boolean fileIsExists() {
		File f = new File(voiceName);
		if (f != null && f.exists()) {
			return true;
		} else {
			return false;
		}
	}

	private MediaPlayer mMediaPlayer = new MediaPlayer();

	public void player(String name) {
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(name);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
				}
			});

			mMediaPlayer.setOnErrorListener(new OnErrorListener() {

				public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
					// TODO Auto-generated method stub
					return false;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.homeBtn:
			backStackFragment();
			break;
		case R.id.vedio_send_bt:
		case R.id.vedio_save_bt:
			if (!fileIsExists()) {
				MyToast.showToast(activity, "请先录音");
				return;
			} 
			if(TextUtils.isEmpty(zyEt.getText().toString())){
				MyToast.showToast(activity, "请填写摘要");
				return;
			}
			if (bean == null) {
				bean = new family_express_lis();
			}
			
			bean.setContent_type(2);
			bean.setContent_desc(zyEt.getText().toString());
			
			if (getArguments() == null) {
				reuqestHttpOfAdd(bean);
			} else {
				reuqestHttpOfUpdate(bean);
			}
			break;
		case R.id.vedio_changeto_txt:
			LayoutBuilder lb = new LayoutBuilder(activity);
			lb.replaceRegister(KuaiDiTxtFragment.class, getArguments());
			break;
		case R.id.vedio_delete_img:

			deleteFile();
			break;
		case R.id.vedio_ting_img:
			if (fileIsExists()) {
				player(voiceName);
			} else {
				MyToast.showToast(activity, "请先录音");
			}
			break;
		default:
			break;
		}
	}
}
