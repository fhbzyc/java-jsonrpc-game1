package com.zhanglong.sg.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BaseItemShop;

@Repository
public class BaseItemShopDao extends BaseDao {

	private static List<BaseItemShop> list;

	@SuppressWarnings("unchecked")
	public List<BaseItemShop> findAll() {

		if (BaseItemShopDao.list == null) {
			Session session = this.getSessionFactory().getCurrentSession();
			BaseItemShopDao.list = session.createCriteria(BaseItemShop.class).list();
		}

		return BaseItemShopDao.list;
	}

	public List<BaseItemShop> findByType(int type) throws CloneNotSupportedException {

		ArrayList<BaseItemShop> result = new ArrayList<BaseItemShop>();
		List<BaseItemShop> list = this.findAll();
		for (BaseItemShop baseItemShop : list) {
			if (baseItemShop.getType() == type) {
				result.add(baseItemShop.clone());
			}
		}
		return result;
	}
}
