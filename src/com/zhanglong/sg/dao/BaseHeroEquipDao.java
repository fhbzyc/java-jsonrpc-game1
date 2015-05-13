package com.zhanglong.sg.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BaseHeroEquip;

@Repository
public class BaseHeroEquipDao extends BaseDao {

	private static List<BaseHeroEquip> heroEquips;

	@SuppressWarnings("unchecked")
	public List<BaseHeroEquip> findAll() {

		if (heroEquips == null) {
			Session session = this.getSessionFactory().getCurrentSession();
			heroEquips = session.createCriteria(BaseHeroEquip.class).addOrder(Order.asc("pk")).list();
		}

		return heroEquips;
    }

	public List<BaseHeroEquip> findByHeroId(int heroId) {

		List<BaseHeroEquip> list = new ArrayList<BaseHeroEquip>();
		for (BaseHeroEquip baseHeroEquip : findAll()) {
			if (baseHeroEquip.getPk().getBaseHero().getId() == heroId) {
				list.add(baseHeroEquip);
			}
		}
		return list;
	}
}
