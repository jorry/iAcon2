package com.iAcon.record;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iAcon.database.manager.fetchManager;
import com.iAcon.database.model.BindWardBean;
import com.iAcon.database.model.fetchmsg;
import com.iAcron.ChatMessage;
import com.iAcron.R;
import com.iAcron.data.AppData;
import com.iAcron.util.Util;

public class ChatMsgViewAdapter extends BaseAdapter {

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	private static final String TAG = ChatMsgViewAdapter.class.getSimpleName();

	ArrayList<fetchmsg> lists;

	private Context ctx;

	private LayoutInflater mInflater;

	public ChatMsgViewAdapter(Context context) {
		ctx = context;
		this.lists = new ArrayList<fetchmsg>();
		mInflater = LayoutInflater.from(context);
		lists = new ArrayList<fetchmsg>();
	}

	public void getFetchAll(BindWardBean bean) {
		fetchManager m = new fetchManager(ctx);

		lists.addAll(m.select(bean.getWard_id(),
				Integer.parseInt(AppData.user_id.get())));
		notifyDataSetChanged();

	}

	public void set(fetchmsg bean) {
		lists.add(bean);
		notifyDataSetChanged();
	}

	public int getCount() {
		System.out.println("getCount = " + lists.size());
		return lists == null ? 0 : lists.size();
	}

	public Object getItem(int position) {
		return lists.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		fetchmsg entity = lists.get(position);

		if (entity.getMsg_type() == 1) {
			return IMsgViewType.IMVT_COM_MSG;
		} else {
			return IMsgViewType.IMVT_TO_MSG;
		}

	}

	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		final fetchmsg entity = lists.get(position);
		int type = entity.getFrom_id();

		ViewHolder viewHolder = null;
		if (type != Integer.parseInt(AppData.user_id.get())) {
			convertView = mInflater.inflate(
					R.layout.chatting_item_msg_text_left, null);
		} else {
			convertView = mInflater.inflate(
					R.layout.chatting_item_msg_text_right, null);
		}

		viewHolder = new ViewHolder();
		viewHolder.tvSendTime = (TextView) convertView
				.findViewById(R.id.tv_sendtime);
		viewHolder.tvUserName = (TextView) convertView
				.findViewById(R.id.tv_username);
		viewHolder.tvContent = (TextView) convertView
				.findViewById(R.id.tv_chatcontent);
		viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
		viewHolder.isComMsg = type == 1 ? true : false;

		convertView.setTag(viewHolder);

		viewHolder.tvSendTime.setText(entity.getCreate_time());

		if (entity.getMsg_type() == 2) {
			viewHolder.tvContent.setText("");
			viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.chatto_voice_playing, 0);
			viewHolder.tvTime.setText(entity.getCreate_time());
		} else {
			viewHolder.tvContent.setText(entity.getMsg_content());
			viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					0, 0);
			viewHolder.tvTime.setText("");
		}
		viewHolder.tvContent.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				playMusic(ChatMessage.convertHexTextToVoice(entity
						.getMsg_content()));
			}
		});
		viewHolder.tvUserName.setText(entity.getFrom_name());

		return convertView;
	}

	static class ViewHolder {
		public TextView tvSendTime;
		public TextView tvUserName;
		public TextView tvContent;
		public TextView tvTime;
		public boolean isComMsg = true;
	}

	/**
	 * @Description
	 * @param name
	 */
	public static void playMusic(byte[] name) {
		MediaPlayer mMediaPlayer = new MediaPlayer();

		String temp = Util.savePlayer("linshi", name);
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(temp);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void stop() {

	}

}
