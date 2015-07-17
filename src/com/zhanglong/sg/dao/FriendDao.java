package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Friend;
import com.zhanglong.sg.entity.FriendAp;
import com.zhanglong.sg.entity.FriendMsg;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.utils.Utils;

@Repository
public class FriendDao extends BaseDao {

	public List<Friend> allFriends(int roleId) {

		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Friend.class);
		@SuppressWarnings("unchecked")
		List<Friend> list = criteria.add(Restrictions.or(Restrictions.eq("roleId", roleId) , Restrictions.eq("roleId2", roleId))).list();
		return list;
	}

	public void createFriend(int roleId, int roleId2) {
		Friend friend = new Friend();
		friend.setRoleId(roleId);
		friend.setRoleId2(roleId2);
		this.getSessionFactory().getCurrentSession().save(friend);
	}

	public void deleteFriend(int roleId, int roleId2) {
		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Friend.class);
		@SuppressWarnings("unchecked")
		List<Friend> list = criteria.add(Restrictions.or(Restrictions.and(Restrictions.eq("roleId", roleId) , Restrictions.eq("roleId2", roleId2)) , Restrictions.and(Restrictions.eq("roleId", roleId2) , Restrictions.eq("roleId2", roleId)))).list();
		for (Friend friend : list) {
			this.getSessionFactory().getCurrentSession().delete(friend);
		}
	}

	public List<FriendAp> allFriendAp(int roleId) {

		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(FriendAp.class);
		@SuppressWarnings("unchecked")
		List<FriendAp> list = criteria.add(Restrictions.eq("roleId", roleId)).list();
		return list;
	}

	/**
	 * 我送的体力
	 * @param roleId
	 * @return
	 */
	public List<FriendAp> mySendAp(int roleId) {

		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(FriendAp.class);
		@SuppressWarnings("unchecked")
		List<FriendAp> list = criteria.add(Restrictions.eq("roleId2", roleId)).add(Restrictions.eq("date", Integer.valueOf(Utils.date()))).list();
		return list;
	}

	public FriendAp createFriendAp(Role role, int roleId2) {
		FriendAp friendAp = new FriendAp();
		friendAp.setRoleId(roleId2);
		friendAp.setRoleId2(role.getRoleId());
		friendAp.setAvatar(role.avatar);
		friendAp.setLevel(role.level);
		friendAp.setName(role.name);
		friendAp.setDate(Integer.valueOf(Utils.date()));
		this.getSessionFactory().getCurrentSession().save(friendAp);
		return friendAp;
	}

	public void deleteFriendAp(FriendAp friendAp) {

		Session session = this.getSessionFactory().getCurrentSession();
		session.delete(friendAp);
	}

	public List<FriendMsg> allFriendMsg(int roleId) {

		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(FriendMsg.class);
		@SuppressWarnings("unchecked")
		List<FriendMsg> list = criteria.add(Restrictions.eq("roleId", roleId)).list();
		return list;
	}

	public FriendMsg findOneMsg(int roleId, int roleId2) {
		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(FriendMsg.class);
		@SuppressWarnings("unchecked")
		List<FriendMsg> list = criteria.add(Restrictions.eq("roleId", roleId)).add(Restrictions.eq("roleId2", roleId2)).list();
		for (FriendMsg friendMsg : list) {
			return friendMsg;
		}
		return null;
	}

	public FriendMsg createFriendMsg(Role role, int roleId2) {
		FriendMsg friendMsg = new FriendMsg();
		friendMsg.setRoleId(roleId2);
		friendMsg.setRoleId2(role.getRoleId());
		friendMsg.setAvatar(role.avatar);
		friendMsg.setLevel(role.level);
		friendMsg.setName(role.name);
		this.getSessionFactory().getCurrentSession().save(friendMsg);
		return friendMsg;
	}

	public int deleteFriendMsg(int roleId) {

		String sql = "DELETE FROM role_friend_msg where role_id2 = " + roleId;

		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);

		return query.executeUpdate();
	}
}
