package com.zhanglong.sg.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.CopyStar;
import com.zhanglong.sg.model.CopyStarModel;

@Repository
public class CopyStarDao extends BaseDao {

	public List<CopyStarModel> configs() {

		List<CopyStarModel> list = new ArrayList<CopyStarModel>();

		// 类型 (1 普通 2经验 ) , 章节 , 星星 , 道具    
		list.add(new CopyStarModel(1 , 1 , 18 , new int[][]{new int[]{4208 , 6} , new int[]{3006 , 2}}));
		list.add(new CopyStarModel(1 , 1 , 36 , new int[][]{new int[]{4208 , 8} , new int[]{3000 , 2}}));
		list.add(new CopyStarModel(1 , 1 , 54 , new int[][]{new int[]{4208 , 16} , new int[]{3002 , 2}}));
		list.add(new CopyStarModel(1 , 2 , 18 , new int[][]{new int[]{4208 , 6} , new int[]{3014 , 2}}));
		list.add(new CopyStarModel(1 , 2 , 36 , new int[][]{new int[]{4208 , 8} , new int[]{3015 , 2}}));
		list.add(new CopyStarModel(1 , 2 , 54 , new int[][]{new int[]{4208 , 16} , new int[]{3016 , 2}}));
		list.add(new CopyStarModel(1 , 3 , 18 , new int[][]{new int[]{4208 , 8} , new int[]{3018 , 2}}));
		list.add(new CopyStarModel(1 , 3 , 36 , new int[][]{new int[]{4208 , 12} , new int[]{3019 , 2}}));
		list.add(new CopyStarModel(1 , 3 , 54 , new int[][]{new int[]{4208 , 18} , new int[]{3056 , 1}}));
		list.add(new CopyStarModel(1 , 4 , 18 , new int[][]{new int[]{4208 , 8} , new int[]{3052 , 1}}));
		list.add(new CopyStarModel(1 , 4 , 36 , new int[][]{new int[]{4208 , 12} , new int[]{3054 , 1}}));
		list.add(new CopyStarModel(1 , 4 , 54 , new int[][]{new int[]{4208 , 18} , new int[]{3064 , 1}}));  
		list.add(new CopyStarModel(1 , 5 , 18 , new int[][]{new int[]{4208 , 10} , new int[]{3068 , 1}}));
		list.add(new CopyStarModel(1 , 5 , 36 , new int[][]{new int[]{4208 , 15} , new int[]{3069 , 1}}));
		list.add(new CopyStarModel(1 , 5 , 54 , new int[][]{new int[]{4208 , 30} , new int[]{3070 , 1}}));
		list.add(new CopyStarModel(1 , 6 , 18 , new int[][]{new int[]{4208 , 10} , new int[]{3073 , 1}}));
		list.add(new CopyStarModel(1 , 6 , 36 , new int[][]{new int[]{4208 , 15} , new int[]{3074 , 1}}));
		list.add(new CopyStarModel(1 , 6 , 54 , new int[][]{new int[]{4208 , 30} , new int[]{3077 , 1}}));
		list.add(new CopyStarModel(1 , 7 , 18 , new int[][]{new int[]{4208 , 10} , new int[]{3075 , 1}}));
		list.add(new CopyStarModel(1 , 7 , 36 , new int[][]{new int[]{4208 , 15} , new int[]{3076 , 1}}));
		list.add(new CopyStarModel(1 , 7 , 54 , new int[][]{new int[]{4208 , 30} , new int[]{4286 , 5}}));
		list.add(new CopyStarModel(1 , 8 , 18 , new int[][]{new int[]{4208 , 10} , new int[]{3083 , 1}}));
		list.add(new CopyStarModel(1 , 8 , 36 , new int[][]{new int[]{4208 , 15} , new int[]{3084 , 1}}));
		list.add(new CopyStarModel(1 , 8 , 54 , new int[][]{new int[]{4208 , 30} , new int[]{4290 , 5}}));
		list.add(new CopyStarModel(2 , 1 , 6 , new int[][]{new int[]{4009 , 2} , new int[]{4209 , 1}}));
		list.add(new CopyStarModel(2 , 1 , 12 , new int[][]{new int[]{4012 , 2} , new int[]{4209 , 2}}));
		list.add(new CopyStarModel(2 , 1 , 18 , new int[][]{new int[]{4011 , 2} , new int[]{4209 , 3}}));
		list.add(new CopyStarModel(2 , 2 , 6 , new int[][]{new int[]{4002 , 2} , new int[]{4209 , 1}}));
		list.add(new CopyStarModel(2 , 2 , 12 , new int[][]{new int[]{4017 , 2} , new int[]{4209 , 2}}));
		list.add(new CopyStarModel(2 , 2 , 18 , new int[][]{new int[]{4016 , 2} , new int[]{4209 , 3}}));
		list.add(new CopyStarModel(2 , 3 , 6 , new int[][]{new int[]{4020 , 2} , new int[]{4209 , 1}}));
		list.add(new CopyStarModel(2 , 3 , 12 , new int[][]{new int[]{4004 , 2} , new int[]{4209 , 2}}));
		list.add(new CopyStarModel(2 , 3 , 18 , new int[][]{new int[]{4005 , 2} , new int[]{4209 , 3}}));
		list.add(new CopyStarModel(2 , 4 , 6 , new int[][]{new int[]{4010 , 2} , new int[]{4210 , 1}}));
		list.add(new CopyStarModel(2 , 4 , 12 , new int[][]{new int[]{4015 , 2} , new int[]{4210 , 2}}));
		list.add(new CopyStarModel(2 , 4 , 18 , new int[][]{new int[]{4001 , 2} , new int[]{4210 , 3}}));
		list.add(new CopyStarModel(2 , 5 , 6 , new int[][]{new int[]{4003 , 2} , new int[]{4210 , 1}}));
		list.add(new CopyStarModel(2 , 5 , 12 , new int[][]{new int[]{4009 , 2} , new int[]{4210 , 2}}));
		list.add(new CopyStarModel(2 , 5 , 18 , new int[][]{new int[]{4002 , 2} , new int[]{4210 , 3}}));
		list.add(new CopyStarModel(2 , 6 , 6 , new int[][]{new int[]{4012 , 2} , new int[]{4210 , 1}}));
		list.add(new CopyStarModel(2 , 6 , 12 , new int[][]{new int[]{4016 , 2} , new int[]{4210 , 2}}));
		list.add(new CopyStarModel(2 , 6 , 18 , new int[][]{new int[]{4017 , 2} , new int[]{4210 , 3}}));
		list.add(new CopyStarModel(2 , 7 , 6 , new int[][]{new int[]{4010 , 2} , new int[]{4211 , 1}}));
		list.add(new CopyStarModel(2 , 7 , 12 , new int[][]{new int[]{4015 , 2} , new int[]{4211 , 2}}));
		list.add(new CopyStarModel(2 , 7 , 18 , new int[][]{new int[]{4001 , 2} , new int[]{4211 , 3}}));
		list.add(new CopyStarModel(2 , 8 , 6 , new int[][]{new int[]{4020 , 2} , new int[]{4211 , 1}}));
		list.add(new CopyStarModel(2 , 8 , 12 , new int[][]{new int[]{4004 , 2} , new int[]{4211 , 2}}));
		list.add(new CopyStarModel(2 , 8 , 18 , new int[][]{new int[]{4005 , 2} , new int[]{4211 , 3}}));

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CopyStar> findAll(int roleId) {
		Session session = this.getSessionFactory().getCurrentSession();
		return session.createCriteria(CopyStar.class).add(Restrictions.eq("roleId", roleId)).addOrder(Order.asc("id")).list();
	}

	public void save(CopyStar copyStar) {

		Session session = this.getSessionFactory().getCurrentSession();
		session.save(copyStar);
	}
}
