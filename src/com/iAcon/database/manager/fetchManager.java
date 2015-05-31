package com.iAcon.database.manager;

import java.util.ArrayList;
import java.util.List;
import com.iAcon.database.dao.DaoSession;
import com.iAcon.database.dao.fetchmsgDao;
import com.iAcon.database.dao.fetchmsgDao.Properties;
import com.iAcon.database.model.fetchmsg;
import com.iAcon.database.model.fetchmsg;
import com.iAcron.iAconApplication;
import com.iAcron.data.AppData;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;

/**
 *
 */
@SuppressLint("NewApi")
public class fetchManager {
	private DaoSession daoSession;
	private fetchmsgDao depositDao;

	public fetchManager(Context context) {
		if (null == daoSession) {
			daoSession = iAconApplication.getDaoSession(context);
		}
	}

	/**
	 * @return
	 */
	public List<fetchmsg> getList() {
		if (null == depositDao) {
			depositDao = daoSession.getFetchmsgDao();
		}
		QueryBuilder<fetchmsg> qb = depositDao.queryBuilder();
		return qb.list();
	}

	/**
	 * 
	 * 筛选 fromType = 2; 发送人 ward_id 监护人
	 * 
	 * @param ward_id
	 * @return
	 */
	public List<fetchmsg> getDepositItem(long ward_id, long to_type) {
		if (null == depositDao) {
			depositDao = daoSession.getFetchmsgDao();
		}

		System.out.println(ward_id + " getDepositItem  " + to_type);
		QueryBuilder<fetchmsg> qb = depositDao.queryBuilder();
		qb.where(Properties.Ward_id.eq(ward_id))
				.where(Properties.Type.eq(to_type)).build();

		return qb.list();
	}

	/**
	 * 清空整存宝表
	 */
	public void clearTable() {
		if (null == depositDao) {
			depositDao = daoSession.getFetchmsgDao();
		}
		QueryBuilder<fetchmsg> qb = depositDao.queryBuilder();
		DeleteQuery<fetchmsg> dq = qb.buildDelete();
		dq.executeDeleteWithoutDetachingEntities();
	}

	/**
	 * @param list
	 */
	public void addDeposits(List<fetchmsg> list) {

		if (null == depositDao) {
			depositDao = daoSession.getFetchmsgDao();
		}
		Log.i("UI", "------未读消息数：" + list.size());

		for (int i = 0; i < list.size(); i++) {
			fetchmsg msg = list.get(i);
			Log.i("UI", "------add 添加消息" + msg.getMsg_id());
			if (msg.getFrom_type() == 2) { // 被监护人
				msg.setWard_id("" + msg.getFrom_id());
			}
			msg.setMsg_status(1); // 设置未读
		}

		Log.i("UI", "  将要存储消息数" + list.size());
		depositDao.insertInTx(list);
	}

	public void update(fetchmsg jh) {
		if (null == depositDao) {
			depositDao = daoSession.getFetchmsgDao();
		}
		depositDao.update(jh);
	}

	/**
	 * @param item
	 */
	public void insertDeposit(fetchmsg item) {
		if (null == depositDao) {
			depositDao = daoSession.getFetchmsgDao();
		}
		depositDao.insertOrReplace(item);
	}

	public List<fetchmsg> select(int from_id, int to_id) {
		if (null == depositDao) {
			depositDao = daoSession.getFetchmsgDao();
		}
		List<fetchmsg> list = new ArrayList<fetchmsg>();
		System.out.println("select * from FETCHMSG where FROM_ID = '" + from_id
				+ "' and TO_ID = '" + to_id + "' or FROM_ID = '" + to_id
				+ "' and TO_ID = '" + from_id + "'");
		Cursor c = daoSession
				.getDatabase()
				.rawQuery(
						"select * from FETCHMSG where FROM_ID = '" + from_id
								+ "' and TO_ID = '" + to_id
								+ "' or FROM_ID = '" + to_id
								+ "' and TO_ID = '" + from_id + "'", null, null);
		while (c.moveToNext()) {
			fetchmsg msg = new fetchmsg();
			msg.setId(c.getLong(c.getColumnIndex("ID")));
			msg.setMsg_id(c.getLong(c.getColumnIndex("MSG_ID")));
			msg.setFrom_id(c.getInt(c.getColumnIndex("FROM_ID")));
			msg.setFrom_name(c.getString(c.getColumnIndex("FROM_NAME")));
			msg.setFrom_type(c.getInt(c.getColumnIndex("FROM_TYPE")));
			msg.setTo_id(c.getString(c.getColumnIndex("TO_ID")));
			msg.setMsg_type(c.getInt(c.getColumnIndex("MSG_TYPE")));
			msg.setMsg_status(c.getInt(c.getColumnIndex("MSG_STATUS")));
			msg.setMsg_content(c.getString(c.getColumnIndex("MSG_CONTENT")));
			msg.setCreate_time(c.getString(c.getColumnIndex("CREATE_TIME")));
			msg.setType(c.getInt(c.getColumnIndex("TYPE")));
			list.add(msg);
		}
		return list;
	}

	/**
	 * 获取全部未读消息
	 */
	public int getMsgType1() {
		if (null == depositDao) {
			depositDao = daoSession.getFetchmsgDao();
		}

		QueryBuilder<fetchmsg> qb = depositDao.queryBuilder();
		qb.where(Properties.Type.eq(1)).build();

		return qb.list().size();
	}

	public List<fetchmsg> getMsgType1FromWardId(long msg_id) {
		System.out.println("消息ID" + msg_id);
		if (null == depositDao) {
			depositDao = daoSession.getFetchmsgDao();
		}
		QueryBuilder<fetchmsg> qb = depositDao.queryBuilder();
		qb.where(Properties.Msg_id.eq(msg_id)).build();

		return qb.list();
	}

	/**
	 * 获取全部未读消息
	 */
	public List<fetchmsg> getMsgType1FromWardId(int wardId, int to_type) {
		if (null == depositDao) {
			depositDao = daoSession.getFetchmsgDao();
		}
		System.out.println(wardId + "  getMsgType1FromWardId " + to_type);
		QueryBuilder<fetchmsg> qb = depositDao.queryBuilder();
		qb.where(Properties.Msg_status.eq(1), Properties.To_id.eq(wardId),
				Properties.Type.eq(to_type)).build();

		return qb.list();
	}

}
