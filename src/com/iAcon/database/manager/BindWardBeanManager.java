package com.iAcon.database.manager;

import java.util.ArrayList;
import java.util.List;

import com.iAcon.database.dao.BindWardBeanDao;
import com.iAcon.database.dao.DaoSession;
import com.iAcon.database.dao.BindWardBeanDao;
import com.iAcon.database.dao.BindWardBeanDao.Properties;
import com.iAcon.database.model.BindWardBean;
import com.iAcon.database.model.fetchmsg;
import com.iAcon.database.model.jianhu;
import com.iAcon.database.model.BindWardBean;
import com.iAcron.iAconApplication;
import com.iAcron.data.AppData;

import android.content.Context;
import android.provider.UserDictionary.Words;
import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;

/**
 *
 */
public class BindWardBeanManager {
	private DaoSession daoSession;
	private BindWardBeanDao depositDao;

	public BindWardBeanManager(Context context) {
		if (null == daoSession) {
			daoSession = iAconApplication.getDaoSession(context);
		}
	}

	/**
	 * @param list
	 */
	public void addDeposits(List<BindWardBean> list) {
		if (null == depositDao) {
			depositDao = daoSession.getBindWardBeanDao();
		}
		clearTable();
		for (BindWardBean b:list) {
			b.setUser_id(AppData.user_id.get());
		}
		depositDao.insertInTx(list);
	}

	/**
	 * 插入
	 * @param item
	 */
	public void insertDeposit(BindWardBean item) {
		if (null == depositDao) {
			depositDao = daoSession.getBindWardBeanDao();
		}
		item.setUser_id(AppData.user_id.get());
		depositDao.insert(item);
	}
	
	/**
	 * @return
	 */
	public List<BindWardBean> getList() {
		if (null == depositDao) {
			depositDao = daoSession.getBindWardBeanDao();
		}
		QueryBuilder<BindWardBean> qb = depositDao.queryBuilder();
		qb.where(Properties.User_id.eq(Long.parseLong(AppData.user_id.get())));
		return qb.list();
	}

	/**
	 * 清除表
	 */
	public void clearTable() {
		if (null == depositDao) {
			depositDao = daoSession.getBindWardBeanDao();
		}
		QueryBuilder<BindWardBean> qb = depositDao.queryBuilder();
		DeleteQuery<BindWardBean> dq = qb.buildDelete();
		dq.executeDeleteWithoutDetachingEntities();
	}

	public void update(BindWardBean jh) {
		if (null == depositDao) {
			depositDao = daoSession.getBindWardBeanDao();
		}
		jh.setUser_id(AppData.user_id.get());
		depositDao.update(jh);
	}
	
	public void deleteUserInfo(int uid) {
		if (null == depositDao) {
			depositDao = daoSession.getBindWardBeanDao();
		}
		QueryBuilder<BindWardBean> qb = depositDao.queryBuilder();
		qb.where(Properties.Ward_id.eq(uid));
		qb.buildDelete();
	}
	
	public  BindWardBean getUserInfo(int  wardId) {
		if (null == depositDao) {
			depositDao = daoSession.getBindWardBeanDao();
		}
		QueryBuilder<BindWardBean> qb = depositDao.queryBuilder();
		qb.where(Properties.Ward_id.eq(wardId)).build();
		return qb.unique();
	}
	
}
