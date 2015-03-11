package com.zhanglong.sg.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zhanglong.sg.dao.MailDao;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Mail;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.Reward;
import com.zhanglong.sg.result.Result;

@Service
@JsonRpcService("/mail")
public class MailService extends BaseClass {

	@Resource
	private MailDao mailDao;

	/**
	 * 未读邮件
	 * @param tokenS
	 * @param page
	 * @return
	 * @throws Throwable
	 */
	public HashMap<String, Object> receive(int page) throws Throwable {

		int roleId = this.roleId();

		if (page < 1) {
			throw new Throwable("参数出错");
		}

        List<Mail> list = this.mailDao.findAll(roleId, page);

//        RoleModel Role = new RoleModel(roleId);
//
//        if (Role.getLevel() < 10) {
//        	// 10级以下的不知道为啥会发竞技场奖励  删之 
//        	SgpJpaRepository<me.gall.sgp.node.pojo.app.Mail, String> jpaRepository = null;
//        	boolean find = false;
//        	List<me.gall.sgp.node.pojo.app.Mail> newList = new ArrayList<me.gall.sgp.node.pojo.app.Mail>();
//   
//            for (me.gall.sgp.node.pojo.app.Mail mail : list) {
//    			if (mail.getTitle().equals("竞技场每日排名奖励")) {
//    				if (jpaRepository == null) {
//    					jpaRepository = this.context.getSgpJpaRepository(me.gall.sgp.node.pojo.app.Mail.class);
//    				}
//    				jpaRepository.delete(mail);
//    				find = true;
//    			} else {
//    				newList.add(mail);
//    			}
//    		}
//            if (find) {
//            	list = newList;
//            }
//        }

		Result result = new Result();
		result.setValue("mail", list);
        return result.toMap();
	}

	/**
	 * 读邮件
	 * @param tokenS
	 * @param mailId
	 * @throws Throwable
	 */
	public void read(int mailId) throws Throwable {

		Mail mail = this.mailDao.findOne(mailId);
		if (mail != null) {
			mail.setStatus(Mail.READ);
			this.mailDao.update(mail);
		}
	}

	/**
	 * 邮件标为已读
	 * @param tokenS
	 * @param mailId
	 * @return
	 * @throws Throwable
	 */
	public HashMap<String, Object> delete(int mailId) throws Throwable {

		int roleId = this.roleId();

		Mail mail = this.mailDao.findOne(mailId);
		if (mail == null) {
			throw new Throwable("没有这封邮件");
		} else if (mail.getRoleId() != roleId) {
			throw new Throwable("不是你的邮件,不是阅读");
		}

		this.mailDao.delete(mail);

		Result result = new Result();

		if (mail.getAttachment().equals("")) {
			return result.toMap();
		}

        try {
        	ObjectMapper mapper = new ObjectMapper();
        	HashMap<String, Reward> map = mapper.readValue(mail.getAttachment(), new TypeReference<Map<String, Reward>>(){});

            Reward reward = map.get("reward");
            if (reward != null) {

            	Role role = this.roleDao.findOne(roleId);
            	this.rewardDao.get(role, reward, "邮件领取<" + mail.getTitle() + ">", FinanceLog.STATUS_MAIL_GET, result);
            }

        } catch (Exception e) {
        	throw new Throwable(e.getMessage());
        }

		return result.toMap();
	}
}