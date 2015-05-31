package com.iAcon.database.manager;

import java.util.List;

import com.iAcon.database.dao.DaoSession;
import com.iAcon.database.dao.jianhuDao;
import com.iAcon.database.model.jianhu;
import com.iAcron.iAconApplication;

import android.content.Context;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;
/**
 * 整存宝manager
 * @author shisong
 *
 */
public class JianhuManager {
	private DaoSession daoSession;
	private jianhuDao depositDao;
	
	public JianhuManager(Context context) {
		if (null == daoSession) {
			daoSession = iAconApplication.getDaoSession(context);
		}
	}
	
	/**
	 * 获取整存宝列表数据
	 * @return
	 */
	public List<jianhu> getList() {
		if (null == depositDao) {
			depositDao = daoSession.getJianhuDao();
		}
		QueryBuilder<jianhu> qb = depositDao.queryBuilder();
		return qb.list();
	}
	
	/**
	 * 清空整存宝表
	 */
	public void clearTable() {
		if (null == depositDao) {
			depositDao = daoSession.getJianhuDao();
		}
		QueryBuilder<jianhu> qb = depositDao.queryBuilder();
		DeleteQuery<jianhu> dq = qb.buildDelete();
		dq.executeDeleteWithoutDetachingEntities();
	}
	
	/**
	 * @param list
	 */
	public void addDeposits(List<jianhu> list) {
		if (null == depositDao) {
			depositDao = daoSession.getJianhuDao();
		}
		clearTable();
		depositDao.insertInTx(list);
	}
	
	public void update(jianhu jh){
		if (null == depositDao) {
			depositDao = daoSession.getJianhuDao();
		}
		depositDao.update(jh);
	}
	
	/**
	 * 存储整存宝详情
	 * @param item
	 */
	public void insertDeposit(jianhu item) {
		if (null == depositDao) {
			depositDao = daoSession.getJianhuDao();
		}
		depositDao.insertOrReplace(item);
	}
}
