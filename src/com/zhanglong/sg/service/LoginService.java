package com.zhanglong.sg.service;

import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zhanglong.sg.dao.TokenDao;
import com.zhanglong.sg.dao.UserDao;
import com.zhanglong.sg.entity.User;
import com.zhanglong.sg.model.Token;

@Service
@JsonRpcService("/login")
public class LoginService extends BaseService {

	@Resource
	private TokenDao tokenDao;

	@Resource
	private UserDao userDao;

	@Transactional(rollbackFor = Throwable.class)
	public Object register(String username, String password, String imei) throws Throwable {

		User user = this.userDao.getByUsername(username);
		if (user != null) {

			return this.returnError(this.lineNum(), "用户名已注册");
		}

		user = new User();
		user.setName(username);
		user.setPassword(password);
		user.setImei(imei);
		user.setMac("");

		this.userDao.create(user);

		return this.signin(username, password);
	}

	public Object signin(String username, String password) throws Throwable {

		User user = this.userDao.getByUsername(username);
		if (user == null) {

			System.out.print("\n 登陆继续走 lineNum: " + this.lineNum());

			return this.returnError(this.lineNum(), "用户名错误");
		} else if (!user.getPassword().equals(password)) {

			System.out.print("\n 登陆继续走 lineNum: " + this.lineNum());

			return this.returnError(this.lineNum(), "密码错误");
		}

		System.out.print("\n 登陆继续走 lineNum: " + this.lineNum());

		this.getHandler().userId = user.getId();

		System.out.print("\n 登陆继续走 lineNum: " + this.lineNum());
		
		Token token2 = this.tokenDao.create(user.getId());

		System.out.print("\n 登陆继续走 lineNum: " + this.lineNum());

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
	public Object oneKeyLoign(String imei, String iccid, String mac) throws Throwable {

		Random random = new Random();
		String username = "Z";
		for (int i = 0 ; i < 7 ; i++) {
			int r = random.nextInt(26);
			r += 65;
			username += (char)r;
		}

		User user = this.userDao.getByUsername(username);
		if (user != null) {

			return this.returnError(this.lineNum(), "用户名已注册");
		}

		String password = "123456";

		user = new User();
		user.setName(username);
		user.setPassword(password);
		user.setImei(imei);
		user.setMac("");
		user.setRegisterType(1);

		this.userDao.create(user);

		user.token = (String) this.signin(username, password);
		return user;
	}
}
