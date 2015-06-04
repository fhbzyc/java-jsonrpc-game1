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

	public void save(Role role, int p, List<Hero> heros) throws JsonProcessingException {

		if (role.level() < 20) {
			return;
		}

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(heros);

		Power power = this.findOne(role.getRoleId());

		if (power == null) {
			power = new Power();

			power.setRoleId(role.getRoleId());
			power.setAvatar(role.getAvatar());
			power.setLevel(role.level());
			power.setName(role.getName());
			power.setPower(p);
			power.setData(json);
			power.setServerId(role.getServerId());
			this.getSessionFactory().getCurrentSession().save(power);
		} else if (p > power.getPower()) {
			power.setRoleId(role.getRoleId());
			power.setAvatar(role.getAvatar());
			power.setLevel(role.level());
			power.setName(role.getName());
			power.setPower(p);
			power.setData(json);
			this.getSessionFactory().getCurrentSession().update(power);
		}
	}

	public Power getOneByPower(int roleId, int serverId, int min, int max) {

		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Power.class);
		criteria = criteria
				.add(Restrictions.ne("roleId", roleId))
				.add(Restrictions.ge("power", min))
				.add(Restrictions.le("power", max))
				.add(Restrictions.eq("serverId", serverId))
				.setMaxResults(10);

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
