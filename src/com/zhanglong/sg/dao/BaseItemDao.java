package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BaseItem;

@Repository
public class BaseItemDao extends BaseDao {

	private static List<BaseItem> items;

	@SuppressWarnings("unchecked")
	public List<BaseItem> findAll() {

		if (items == null) {
			Session session = this.getSessionFactory().getCurrentSession();
			items = session.createCriteria(BaseItem.class).list();
		}

		return items;
    }

	public BaseItem findOne(int itemId) throws Exception {

		List<BaseItem> list = findAll();

		for (BaseItem baseItem : list) {
			if (baseItem.getBaseId() == itemId) {
				return baseItem.clone();
			}
		}

		throw new Exception("不存在的道具,id->" + itemId);
	}
}
