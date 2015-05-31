package com.iAcon.record;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;

public class SoundMeter {
	static final private double EMA_FILTER = 0.6;

	private MediaRecorder mRecorder = null;
	private double mEMA = 0.0;

	private String armFilePath;
	private final String rootRecorder = "mjGame/recorder/";
	private final String rootPlayer = "mjGame/Player/";

	long startVoiceT;
	private MediaPlayer mMediaPlayer = new MediaPlayer();

	/**
	 */
	public void player(String name, final mediaPlayerInterface player) {
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
					if (player != null) {
						player.onCompletetion();
					}
				}
			});

			mMediaPlayer.setOnErrorListener(new OnErrorListener() {

				public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
					// TODO Auto-generated method stub
					if (player != null) {
						player.onError();
					}
					return false;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ�����ļ��г���
	 */
	public void file(){
		File file = new File(rootPlayer);
		File [] fils = file.listFiles();
		for (int i = 0; i < fils.length; i++) {
			String fileUri = fils[i].getAbsolutePath();
			int index = fileUri.indexOf(".arm");
			String name = fileUri.substring(0, index);
			MediaPlayer player = new MediaPlayer();
			int musicTime = player.getDuration() / 1000;

			String time =  musicTime / 60 + ":" + musicTime % 60;
			player = null;
		}
		System.gc();
	}
	/**
	 * 
	 * @param name
	 * @param data
	 */
	public void savePlayer(String name, byte[] data) {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
		}
		File rootFile = new File(
				android.os.Environment.getExternalStorageDirectory() + "/"
						+ rootPlayer);
		if (!rootFile.exists()) {
			rootFile.mkdirs();
		}
		try {
			FileOutputStream os = new FileOutputStream(new File(rootFile, name
					+ ".amr"));
			os.write(data);
			os.flush();
			os.close();
			os = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public interface mediaPlayerInterface {
		public void onCompletetion();

		public void onError();
	}

	/**
	 * ��ȡ¼�ƺ���Ƶ�洢��·��
	 * 
	 * @return
	 */
	public String getArmFilePath() {
		return armFilePath;
	}

	RecordAmpListener listener;
	/**
	 */
	public String recordStart(RecordAmpListener listener,String name) {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return "";
		}
		this.listener = listener;
		
		mHandler.postDelayed(mPollTask, 300);
		File rootFile = new File(
				android.os.Environment.getExternalStorageDirectory() + "/"
						+ rootRecorder);
		if (!rootFile.exists()) {
			rootFile.mkdirs();
		}
		startVoiceT = SystemClock.currentThreadTimeMillis();

		File file = new File(
				rootFile.getAbsolutePath() + "/"
						+ name);
		System.out.println("音频路径"+file.getAbsolutePath());
		armFilePath = file.getAbsolutePath();
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFile(armFilePath);
			try {
				mRecorder.prepare();
				mRecorder.start();

				mEMA = 0.0;
			} catch (IllegalStateException e) {
				System.out.print(e.getMessage());
			} catch (IOException e) {
				System.out.print(e.getMessage());
			}

		}
		return armFilePath;
	}

	/**
	 * ¼�����
	 */
	public void finish() {
		recordStop();
	}

	/**
	 * ȡ��¼��
	 */
	public void cancel() {
		try {
			File file = new File(
					android.os.Environment.getExternalStorageDirectory() + "/"
							+ getArmFilePath());
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		recordStop();

	}

	/*
	 * ¼������Ϻ������ͣ ��ͣ
	 * 
	 * return int time ��ʾ�˴�¼�Ƶ�ʱ��
	 */
	private int recordStop() {
		if (mRecorder != null) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
		mHandler.removeCallbacks(mPollTask);
		
		long endVoiceT = SystemClock.currentThreadTimeMillis();
		int time = (int) ((endVoiceT - startVoiceT) / 1000);
		return time;
	}

	/**
	 * ɾ��
	 * 
	 * @param name
	 */
	public void deleteAudio(String name) {
		try {
			File file = new File(name);
			file.delete();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param filePath
	 *           ����ļ�·����ȡ���ļ�·��
	 * 
	 * @return
	 */
	public byte[] getAudio(String filePath) {
		try {
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			
			InputStream is = new FileInputStream(filePath);
			byte[] buffer = new byte[8192];
			int rc  =0;
			// read from is to buffer
			while ((rc = is.read(buffer, 0, 1024)) != -1) {
				 swapStream.write(buffer, 0, rc);
			}
			is.close();
			return swapStream.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

	/**
	 * ��ͣ
	 */

	private void pause() {
		if (mRecorder != null) {
			mRecorder.stop();
		}
	}

	public double getAmplitude() {
		if (mRecorder != null)
			return (mRecorder.getMaxAmplitude() / 2700.0);
		else
			return 0;

	}

	public double getAmplitudeEMA() {
		double amp = getAmplitude();
		mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
		return mEMA;
	}
	
	private Handler mHandler = new Handler();
	
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp =  getAmplitude();
			listener.getAmp((int)amp);
			mHandler.postDelayed(mPollTask, 300);

		}
	};
	
	public interface RecordAmpListener{
		public void getAmp(int arm);
	}
	
	
}
