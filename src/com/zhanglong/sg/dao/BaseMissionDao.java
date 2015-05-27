package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity2.BaseMission;

@Repository
public class BaseMissionDao extends BaseDao2 {

	private static List<BaseMission> missions;
	
	@SuppressWarnings("unchecked")
	public List<BaseMission> findAll() {

		if (missions == null) {
			Session session = this.getBaseSessionFactory().getCurrentSession();
			missions = session.createCriteria(BaseMission.class).list();
		}
		return missions;
    }

	public BaseMission findOne(int missionId) throws Exception {
		for (BaseMission baseMission : findAll()) {
			if (baseMission.getId() == missionId) {
				return baseMission.clone();
			}
		}
		throw new Exception("mission-id not found[id:" + missionId + "]");
	}
}
