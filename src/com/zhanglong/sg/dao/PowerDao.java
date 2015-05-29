package com.zhanglong.sg.dao;

import java.util.List;
import java.util.Random;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.Power;
import com.zhanglong.sg.entity.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class PowerDao extends BaseDao {

    public Power findOne(int roleId) {

    	Session session = this.getSessionFactory().getCurrentSession();
    	return (Power) session.get(Power.class, roleId);
    }

	public void save(Role role, int power, List<Hero> heros) throws JsonProcessingException {
		Power power2 = this.findOne(role.getRoleId());
		
		boolean insert = false;
		if (power2 == null) {
			power2 = new Power();
			insert = true;
		}

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(heros);
		
		power2.setRoleId(role.getRoleId());
		power2.setAvatar(role.getAvatar());
		power2.setLevel(role.level());
		power2.setName(role.getName());
		power2.setPower(power);
		power2.setData(json);
		power2.setServerId(role.getServerId());

		if (insert) {

			this.getSessionFactory().getCurrentSession().save(power2);
		} else {

			this.getSessionFactory().getCurrentSession().update(power2);
		}
	}

	public Power getOneByPower(int roleId, int serverId, int min, int max) {

		//String sql = "SELECT * FROM role_powers WHERE role_id != ? AND role_power >= ? AND role_power <= ? AND server_id = ? LIMIT 10";

		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Power.class);
		criteria = criteria.add(Restrictions.ne("roleId", roleId)).add(Restrictions.ge("power", min)).add(Restrictions.le("power", max)).add(Restrictions.eq("serverId", serverId)).setMaxResults(10);

//		SQLQuery sqlQuery = session.createSQLQuery(sql);
//		sqlQuery.setParameter(1, roleId);
//		sqlQuery.setParameter(2, min);
//		sqlQuery.setParameter(3, max);
//		sqlQuery.setParameter(4, serverId);

		@SuppressWarnings("unchecked")
		List<Power> list = criteria.list();

		if (list.size() == 0) {
			return null;
		}
		
		Random random = new Random();
		int index = random.nextInt(list.size());
		return list.get(index);
	}
}
