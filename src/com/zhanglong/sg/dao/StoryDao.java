package com.zhanglong.sg.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.entity.Story;
import com.zhanglong.sg.entity2.BaseStory;
import com.zhanglong.sg.result.Result;

@Repository
public class StoryDao extends BaseDao {

	@Resource
	private DailyTaskDao dailyTaskDao;

	@Resource
	private MissionDao missionDao;

    public Story findOne(int roleId, int storyId, int storyType) {

    	Session session = this.getSessionFactory().getCurrentSession();

    	@SuppressWarnings("unchecked")
		List<Story> list = session.createCriteria(Story.class)
    	.add(Restrictions.eq("aRoleId", roleId))
    	.add(Restrictions.eq("storyId", storyId))
    	.add(Restrictions.eq("type", storyType))
    	.list();

    	if (list.size() == 0) {
    		return null;
    	}

    	list.get(0).init();
    	return list.get(0);
    }

    public List<Story> findAll(int roleId) {

    	Session session = this.getSessionFactory().getCurrentSession();

    	@SuppressWarnings("unchecked")
		List<Story> list = session.createCriteria(Story.class)
		.add(Restrictions.eq("aRoleId", roleId))
		.addOrder(Order.asc("storyId"))
		.list();

    	for (Story story : list) {
    		story.init();
		}

    	return list;
    }

    public Story create(int roleId, int storyId, int storyType) {
    	Story story = new Story();
    	story.setARoleId(roleId);
    	story.setStoryId(storyId);
    	story.setType(storyType);
    	story.setStar(0);
    	story.init();
    	
    	Session session = this.getSessionFactory().getCurrentSession();
    	session.save(story);
    	
    	return story;
    }

    public void update(Story story, Result result) throws Exception {
    	Session session = this.getSessionFactory().getCurrentSession();
    	session.update(story);
    	result.addCopy(story);
    }

    /**
     * 关卡胜利 or 扫荡
     * @param story
     * @param num
     * @param result
     * @throws Exception 
     */
    public void addNum(Role role, Story story, int num, Result result) throws Exception {

    	story.init();
    	story.setNum(story.getNum() + num);

        if (story.getType() == BaseStory.COPY_TYPE) {
        	this.dailyTaskDao.addCopy(role, num, result);
        	this.missionDao.checkStory(role, story.getStoryId(), num, result);

        } else if (story.getType() == BaseStory.HERO_COPY_TYPE) {
        	this.dailyTaskDao.addHeroCopy(role, num, result);
        	this.missionDao.checkHeroStory(role, story.getStoryId(), num, result);
        }

        this.save(story, result);
    }

    public void save(Story story, Result result) throws Exception {
    	Session session = this.getSessionFactory().getCurrentSession();
    	session.update(story);
        result.addCopy(story);
    }
}
