package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity2.BaseHero;

@Repository
public class BaseHeroDao extends BaseDao2 {

	private static List<BaseHero> heros;

	@SuppressWarnings("unchecked")
	public List<BaseHero> findAll() {

		if (heros == null) {
			Session session = this.getBaseSessionFactory().getCurrentSession();
			heros = session.createCriteria(BaseHero.class).list();
		}

		return heros;
    }

	public BaseHero findOne(int heroId) throws Exception {

		for (BaseHero baseHero : this.findAll()) {
			if (baseHero.getId() == heroId) {
				return baseHero;
			}
		}
		throw new Exception("不存在的英雄 :  "+ heroId);
	}
}
