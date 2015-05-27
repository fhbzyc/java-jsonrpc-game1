package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Item;
import com.zhanglong.sg.result.Result;

@Repository
public class ItemDao extends BaseDao {

    public ItemDao() {
    }

	public List<Item> findAll(int roleId) {

		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Item.class);
		@SuppressWarnings("unchecked")
		List<Item> list = criteria.add(Restrictions.eq("roleId", roleId)).list();
		return list;
    }

    public Item findOne(int pk) {

    	Session session = this.getSessionFactory().getCurrentSession();
    	return (Item) session.get(Item.class, pk);
    }

    public Item findOneByItemId(int roleId, int itemId) {

    	Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Item.class);
		@SuppressWarnings("unchecked")
		List<Item> list = criteria.add(Restrictions.eq("roleId", roleId)).add(Restrictions.eq("itemId", itemId)).list();

		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
    }

    public Item addSoul(int roleId, int itemId, int num, Result result) throws Exception {

    	result.addRandomItem(new int[]{itemId + 6000 , 1});
    	return this.additem(roleId, itemId, num, result);
    }

    public Item addItem(int roleId, int itemId, int num, Result result) throws Exception {

    	result.addRandomItem(new int[]{itemId , num});
    	return this.additem(roleId, itemId, num, result);
    }

    public Item subItem(Item item, int num, Result result) throws Exception {

        if (item == null) {
            return item;
        }

        int itemNum = item.getNum();
        itemNum -= num;

        Session session = this.getSessionFactory().getCurrentSession();
        if (itemNum > 0) {
        	item.setNum(itemNum);
        	session.update(item);

        } else {
            // 删除道具
        	session.delete(item);
            item.setNum(0);
        }

    	result.addItem(item);
        return item;
    }

    public Item additem(int roleId, int itemId, int num, Result result) throws Exception {

        Item item = this.findOneByItemId(roleId, itemId);

        Session session = this.getSessionFactory().getCurrentSession();

        if (item == null) {

        	item = new Item();
        	item.setRoleId(roleId);
        	item.setItemId(itemId);
        	item.setNum(num);
        	item.setLevel(0);
        	session.save(item);
        } else {
            item.setNum(item.getNum() + num);
            session.update(item);
        }
        result.addItem(item);

        return item;
    }
}
