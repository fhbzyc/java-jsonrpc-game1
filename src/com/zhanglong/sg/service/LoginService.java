package com.zhanglong.sg.service;

import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhanglong.sg.dao.TokenDao;
import com.zhanglong.sg.dao.UserDao;
import com.zhanglong.sg.entity.User;
import com.zhanglong.sg.model.Token;
import com.zhanglong.sg.utils.MD5;

@Service
public class LoginService extends BaseService {

	@Resource
	private TokenDao tokenDao;

	@Resource
	private UserDao userDao;

	@Transactional(rollbackFor = Throwable.class)
	public Object register(String username, String password, String imei) throws Exception {

		User user = this.userDao.getByUsername(username);
		if (user != null) {

			return this.returnError(this.lineNum(), "用户名已注册");
		}

		user = new User();
		user.setUserName(username);
		user.setPassword(MD5.digest(password));
		user.setImei(imei);
		user.setMac("");

		this.userDao.create(user);

		return this.signin(username, password);
	}

	public Object signin(String username, String password) throws Exception {

		User user = this.userDao.getByUsername(username);
		if (user == null) {

			return this.returnError(this.lineNum(), "用户名错误");
		} else if (!user.getPassword().equals(MD5.digest(password))) {

			return this.returnError(this.lineNum(), "密码错误");
		}

		this.getHandler().userId = user.getId();

		Token token2 = this.tokenDao.create(user.getId());

		return this.success(token2.getTokenS());
	}

	/**
	 * 快速登陆
	 * @param imei
	 * @param iccid
	 * @param mac
	 * @return
	 * @throws Throwable
	 */
	public Object oneKeyLogin(String imei, String iccid, String mac) throws Throwable {

		String password = "123456";

		User user = this.userDao.getByImei(imei);
		if (user != null) {

			User user2 = user.clone();
			user2.token = this.tokenDao.create(user2.getId()).getTokenS();
			user2.setPassword(password);
			return this.success(user2);
		}

		Random random = new Random();
		String username = "Z";
		for (int i = 0 ; i < 7 ; i++) {
			int r = random.nextInt(26);
			r += 65;
			username += (char)r;
		}

		username = username.toLowerCase();
		user = this.userDao.getByUsername(username);
		if (user != null) {

			return this.returnError(this.lineNum(), "用户名已注册");
		}

		user = new User();
		user.setUserName(username);
		user.setPassword(MD5.digest(password));
		user.setImei(imei);
		user.setMac("");
		user.setRegisterType(User.QUICK_REG);

		this.userDao.create(user);

		User user2 = user.clone();
		user2.token = this.tokenDao.create(user2.getId()).getTokenS();
		user2.setPassword(password);
		return this.success(user2);
	}

	/**
	 * platform - 2
	 * @param imei
	 * @param uid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public Object mi(String imei, long uid, String session) throws Exception {

		int platform = 2;

		String username = String.valueOf(uid);

		User user = this.userDao.getByUsername(platform, username);

		if (user == null) {
			user = new User();
			user.setUserName(username);
			user.setPassword(MD5.digest(""));
			user.setImei(imei);
			user.setMac("");
			user.setPlatformId(platform);

			this.userDao.create(user);
		}

		return this.signin(username, "");
	}

	/**
	 * platform - 3
	 * @param imei
	 * @param uid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public Object ky(String imei, String uid, String session) throws Exception {

		int platform = 3;

		String username = String.valueOf(uid);

		User user = this.userDao.getByUsername(platform, username);

		if (user == null) {
			user = new User();
			user.setUserName(username);
			user.setPassword(MD5.digest(""));
			user.setImei(imei);
			user.setMac("");
			user.setPlatformId(platform);

			this.userDao.create(user);
		}

		return this.signin(username, "");
	}

	/**
	 * platform - 4
	 * @param imei
	 * @param uid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public Object uuc(String imei, String uid, String session) throws Exception {

		int platform = 4;

		String username = String.valueOf(uid);

		User user = this.userDao.getByUsername(platform, username);

		if (user == null) {
			user = new User();
			user.setUserName(username);
			user.setPassword(MD5.digest(""));
			user.setImei(imei);
			user.setMac("");
			user.setPlatformId(platform);

			this.userDao.create(user);
		}

		return this.signin(username, "");
	}
}
