package com.iAcron.util;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
/**
 * 线程工具类
 * @author shisong
 *
 */
public class ThreadUtil {
	private static Handler uiHandler = null;
	private static ScheduledExecutorService pool = null;
	private static HandlerThread handlerThread = null;
	private static Handler queueHandler = null;
	private static long mainThreadID = 0;
	
	private static MultiHashMap<Integer, Handler.Callback> callbacks = new MultiHashMap<Integer, Handler.Callback>(12);

	private static void checkInited() {
		if (uiHandler == null) {
			init(3);
		}
	}

	public static Handler getUIHandler() {
		checkInited();
		return uiHandler;
	}

	public static Handler getQueueHandler() {
		checkInited();
		return queueHandler;
	}

	public static ScheduledExecutorService getPool() {
		checkInited();
		return pool;
	}

	public static synchronized void shutdown() {
		uiHandler = null;
		queueHandler = null;
		handlerThread.quit();
		handlerThread = null;
		if (pool != null) {
			pool.shutdown();
			pool = null;
		}
	}

	public static synchronized void shutdownNow() {
		uiHandler = null;
		queueHandler = null;
		handlerThread.quit();
		handlerThread = null;
		if (pool != null) {
			pool.shutdownNow();
			pool = null;
		}
	}

	public static boolean isInMainThread() {
		return mainThreadID == Thread.currentThread().getId();
	}

	private static Handler.Callback msgCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			int size = 0;
			Handler.Callback[] arrays = null;
			synchronized (callbacks) {
				ArrayList<Handler.Callback> ls = callbacks.get(msg.what);
				if (null == ls) {
					return false;
				}
				size = ls.size();
				arrays = new Handler.Callback[size];
				ls.toArray(arrays);
			}
			if (null != arrays) {
				for (Handler.Callback observer : arrays) {
					if (null != observer) {
						try {
							if(observer.handleMessage(msg)) {
								LogUtil.w("duopeng", "global handler callback.handleMessage return true!!");
								return true;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			return false;
		}
	};
	

	public static void setMessageListener(int what, Handler.Callback callback) {
		synchronized (callbacks) {
			ArrayList<Handler.Callback> ls = callbacks.get(what);
			if(ls != null) {
				ls.clear();
			}
			callbacks.put(what, callback);
		}
	}
	public static void addMessageListener(int what, Handler.Callback callback) {
		synchronized (callbacks) {
			callbacks.put(what, callback);
		}
	}
	public static void addMessageListener(Handler.Callback callback, int what, int...whatN) {
		synchronized (callbacks) {
			callbacks.put(what, callback);
			for(int n : whatN) {
				callbacks.put(n, callback);
			}
		}
	}

	public static void removeMessageListener(int what, Handler.Callback callback) {
		synchronized (callbacks) {
			callbacks.remove(what, callback);
		}
	}
	public static void removeMessageListener(Handler.Callback callback) {
		synchronized (callbacks) {
			callbacks.removeValue(callback);
		}
	}
	public static boolean sendMessage(int what) {
		return getUIHandler().sendEmptyMessage(what);
	}
	public static boolean sendMessage(int what, Object obj) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		return getUIHandler().sendMessage(msg);
	}
	public static boolean sendMessage(Message msg) {
		return getUIHandler().sendMessage(msg);
	}

	/**
	 * 初始�? 必须在主线程调用
	 */
	public static synchronized void init(int poolSize) {
		
		// if (Thread.currentThread().getId() != Process.myTid()) {
		// throw new
		// IllegalStateException("ThreadUtil.init MUST called in main thread");
		// }
		if (uiHandler != null) {
			return;
//			throw new IllegalStateException("ThreadUtil.init already inited");
		}
		mainThreadID = Thread.currentThread().getId();
		uiHandler = new Handler(Looper.getMainLooper(), msgCallback);

		handlerThread = new HandlerThread("queueThread");
		handlerThread.start();
		queueHandler = new Handler(handlerThread.getLooper());
		if (poolSize > 0) {
			pool = Executors.newScheduledThreadPool(poolSize, new ThreadFactory() {

				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setPriority(Thread.MIN_PRIORITY);
					t.setDaemon(true);
					return t;
				}
			});
		}
	}

}
