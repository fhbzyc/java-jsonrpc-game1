package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BaseHero;
import com.zhanglong.sg.entity.BaseItem;
import com.zhanglong.sg.utils.Utils;

@Repository
public class BaseHeroDao extends BaseDao {

	private static List<BaseHero> heros;

	@SuppressWarnings("unchecked")
	public List<BaseHero> findAll() {

		if (heros == null) {
			Session session = this.getSessionFactory().getCurrentSession();
			heros = session.createCriteria(BaseHero.class).list();
		}

		return heros;
    }

	public BaseHero findOne(int heroId) throws Throwable {

		for (BaseHero baseHero : this.findAll()) {
			if (baseHero.getId() == heroId) {
				return baseHero;
			}
		}
		throw new Throwable("不存在的英雄 :  "+ heroId);
	}
}
