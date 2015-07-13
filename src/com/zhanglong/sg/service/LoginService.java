package com.zhanglong.sg.service;

import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

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

	public Object register(String username, String password, String imei) throws Exception {

        username.replace(" ", "");
        username.replace("	", "");
        username.replace("\n", "");

		if (username.length() < 6 || username.length() > 10) {
			return this.returnError(this.lineNum(), "用户名必须6~10位");
		}

		if (password.length() < 6 || password.length() > 20) {
			return this.returnError(this.lineNum(), "密码必须6~20位");
		}

		User user = this.userDao.getByUsername(username);
		if (user != null) {

			return this.returnError(this.lineNum(), "用户名已注册");
		}

		user = new User();
		user.setUserName(username);
		user.setPassword(MD5.digest(password));
		user.setImei(imei);
		//user.setMac("");

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

		this.userDao.update(user);

		this.getHandler().userId = user.getId();

		String tokenS = this.tokenDao.getTokenByUserId(user.getId());
		if (tokenS != null) {
			Token token = this.tokenDao.findOne(tokenS);
			if (token.getServerId() == 0) {
				return this.success(token.getTokenS());
			}
		}

		Token token = this.tokenDao.create(user.getId());
		return this.success(token.getTokenS());
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

			this.userDao.update(user);

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
		//user.setMac("");
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
			//user.setMac("");
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
			//user.setMac("");
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
			//user.setMac("");
			user.setPlatformId(platform);

			this.userDao.create(user);
		}

		return this.signin(username, "");
	}

	/**
	 * 
	 * @param imei
	 * @param uniqueId
	 * @param passId
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public Object mm(String imei, String uniqueId, String passId, String token) throws Exception {

		int platform = 5;

		String username = String.valueOf(passId);

		User user = this.userDao.getByUsername(platform, username);

		if (user == null) {
			user = new User();
			user.setUserName(username);
			user.setPassword(MD5.digest(""));
			user.setImei(imei);
			//user.setMac("");
			user.setPlatformId(platform);

			this.userDao.create(user);
		}

		return this.signin(username, "");
	}

	/**
	 * 小皮
	 * @param imei
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public Object xp(String imei, String token) throws Exception {

		int platform = 6;

		String username = token;

		User user = this.userDao.getByUsername(platform, username);

		if (user == null) {
			user = new User();
			user.setUserName(username);
			user.setPassword(MD5.digest(""));
			user.setImei(imei);
			//user.setMac("");
			user.setPlatformId(platform);

			this.userDao.create(user);
		}

		return this.signin(username, "");
	}

	public Object s4399(String imei, String uid) throws Exception {

		int platform = 7;

		String username = uid;

		User user = this.userDao.getByUsername(platform, username);

		if (user == null) {
			user = new User();
			user.setUserName(username);
			user.setPassword(MD5.digest(""));
			user.setImei(imei);
			//user.setMac("");
			user.setPlatformId(platform);

			this.userDao.create(user);
		}

		return this.signin(username, "");
	}
}
