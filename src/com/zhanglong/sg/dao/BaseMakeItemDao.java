package com.zhanglong.sg.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity2.BaseMakeItem;

@Repository
public class BaseMakeItemDao extends BaseDao2 {

	private static List<BaseMakeItem> items;

	@SuppressWarnings("unchecked")
	public List<BaseMakeItem> findAll() {

		if (items == null) {
			Session session = this.getBaseSessionFactory().getCurrentSession();
			items = session.createCriteria(BaseMakeItem.class).list();
		}
		return items;
    }

	public List<BaseMakeItem> findByItemId(int itemId) {

		List<BaseMakeItem> list = findAll();
		
		List<BaseMakeItem> result = new ArrayList<BaseMakeItem>();

		for (BaseMakeItem baseMakeItem : list) {
			if (baseMakeItem.getPk().getBaseItem().getBaseId() == itemId) {
				result.add(baseMakeItem);
			}
		}

		return result;
	}
}
