package com.zhanglong.sg.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zhanglong.sg.dao.TokenDao;
import com.zhanglong.sg.dao.UserDao;
import com.zhanglong.sg.entity.User;
import com.zhanglong.sg.model.Token;
//import com.zhanglong.sg.token.Token;
//import com.zhanglong.sg.token.TokenInstance;


@Service
@JsonRpcService("/login")
public class UserService extends BaseClass {

	@Resource
	private TokenDao tokenDao;

	@Resource
	private UserDao userDao;
	
	public User find() {

		User user = this.userDao.findOne(1);

		return user;
	}

	@Transactional(rollbackFor = Throwable.class)
	public Object register(String username, String password) throws Throwable {

		User user = this.userDao.getByUsername(1, username);
		if (user != null) {

			throw new Throwable("用户名已注册");
		}

		user = new User();
		user.setRegisterType(0);
		user.setPlatformId(1);
		user.setName(username);
		user.setPassword(password);

		userDao.create(user);
		
		throw new Throwable("aaa");
		
	//	return user;
	}

	public Object signin(String username, String password) throws Throwable {

		User user = this.userDao.getByUsername(1, username);
		if (user == null) {
			throw new Throwable("用户名错误");
		} else if (!user.getPassword().equals(password)) {
			throw new Throwable("密码错误");
		}

		
		Token token2 = this.tokenDao.create(user.getId());
		
		
//		Token token = this.getTokenDao().findOne("f9809dce621b4b5abb4d5fb583d8c015");
		
//		System.out.print(token2.getTokenS());
		return token2.getTokenS();
	}
	
//	/**
//	 * 注册
//	 * @param user
//	 * @return
//	 * @throws Throwable
//	 */
//	public UserModel register(User user) throws Throwable {
//
//        UserService userService = this.context.getSgpService(UserService.class);
//        user = userService.register(user);
//
//        String token = TokenInstance.getToken(context.getRedisHandle()).addToken(user.getUserid());
//
//        UserModel result = new UserModel();
//
//        result.setCreateTime(user.getCreateTime());
//        result.setEmail(user.getEmail());
//        result.setIccid(user.getIccid());
//        result.setImei(user.getImei());
//        result.setLastLoginTime(user.getLastLoginTime());
//        result.setMac(user.getMac());
//        result.setNickName(user.getNickName());
//        result.setPhoneNumber(user.getPhoneNumber());
//        result.setRegistryType(user.getRegistryType());
//        result.setUpdateTime(user.getUpdateTime());
//        result.setUserid(user.getUserid());
//        result.setUserName(user.getUserName());
//        result.token = token;
//
//        return result;
//	}
//
//	/**
//	 * 快速登陆
//	 * @param imei
//	 * @param iccid
//	 * @param mac
//	 * @return
//	 * @throws Throwable
//	 */
//	public UserModel oneKeyLoign(String imei, String iccid, String mac) throws Throwable {
//
//		UserTable where = new UserTable();
//		where.setImei(imei);
//		where.setMac(mac);
//
//		SgpJpaRepository<UserTable, String> jpaRepository = this.context.getSgpJpaRepository(UserTable.class);
//		UserTable userTable = jpaRepository.findOne(SgpSpecification.create(this.context.getEntityManager(), where));
//
//		if (userTable != null) {
//	        UserModel result = new UserModel();
//
//	        result.setImei(userTable.getImei());
//	        result.setMac(userTable.getMac());
//	        result.setUserid(userTable.getId());
//	        result.setUserName(userTable.getName());
//	        result.setPassword("123456");
//
//	        String token = TokenInstance.getToken(context.getRedisHandle()).addToken(userTable.getId());
//	        result.token = token;
//
//	        Event2 event2 = new Event2();
//	        event2.setEventId("yiJianZhuCe");
//	        event2.setUuid(imei);
//
//	        HashMap<String, String> eventData = new HashMap<String, String>();
//	        eventData.put("yiJianZhuCe", new SimpleDateFormat("HH").format(new Date()));
//	        event2.setEventData(eventData);
//	        event2.start();
//
//			return result;
//		}
//
//		Random random = new Random();
//		String username = "Z";
//		for (int i = 0 ; i < 7 ; i++) {
//			int r = random.nextInt(26);
//			r += 65;
//			username += (char)r;
//		}
//
//		User user = new User();
//		user.setImei(imei);
//		user.setIccid(iccid);
//		user.setMac(mac);
//		user.setUserName(username);
//		user.setPassword("123456");
//
//		
//        UserService userService = this.context.getSgpService(UserService.class);
//        user = userService.register(user);
//
//        String token = TokenInstance.getToken(context.getRedisHandle()).addToken(user.getUserid());
//
//        UserModel result = new UserModel();
//
//        result.setCreateTime(user.getCreateTime());
//        result.setEmail(user.getEmail());
//        result.setIccid(user.getIccid());
//        result.setImei(user.getImei());
//        result.setLastLoginTime(user.getLastLoginTime());
//        result.setMac(user.getMac());
//        result.setNickName(user.getNickName());
//        result.setPhoneNumber(user.getPhoneNumber());
//        result.setRegistryType(user.getRegistryType());
//        result.setUpdateTime(user.getUpdateTime());
//        result.setUserid(user.getUserid());
//        result.setUserName(user.getUserName());
//        result.setPassword("123456");
//        result.token = token;
//
//		where.setImei(imei);
//		where.setMac(mac);
//        where.setId(user.getUserid());
//        where.setName(user.getUserName());
//        jpaRepository.save(where);
//
////        UserService userService = this.context.getSgpService(UserService.class);
////        User user = userService.register(imei, iccid, mac);
////
////        UserModel result = new UserModel();
////
////        result.setCreateTime(user.getCreateTime());
////        result.setEmail(user.getEmail());
////        result.setIccid(user.getIccid());
////        result.setImei(user.getImei());
////        result.setLastLoginTime(user.getLastLoginTime());
////        result.setMac(user.getMac());
////        result.setNickName(user.getNickName());
////        result.setPhoneNumber(user.getPhoneNumber());
////        result.setRegistryType(user.getRegistryType());
////        result.setUpdateTime(user.getUpdateTime());
////        result.setUserid(user.getUserid());
////        result.setUserName(user.getUserName());
////
////        String token = TokenInstance.getToken(context.getRedisHandle()).addToken(user.getUserid());
////
////        result.token = token;
//
//        Event2 event2 = new Event2();
//        event2.setEventId("yiJianZhuCe");
//        event2.setUuid(imei);
//
//        HashMap<String, String> eventData = new HashMap<String, String>();
//        eventData.put("yiJianZhuCe", new SimpleDateFormat("HH").format(new Date()));
//        event2.setEventData(eventData);
//        event2.start();
//
//		return result;
//	}
//
//	/**
//	 * 登陆
//	 * @param username
//	 * @param password
//	 * @return
//	 * @throws Throwable
//	 */
//	public UserModel signin(String username, String password) throws Throwable {
//
//        UserService userService = this.context.getSgpService(UserService.class);
//        User user = userService.login(username, password);
//
//        String token = TokenInstance.getToken(context.getRedisHandle()).addToken(user.getUserid());
//
//        UserModel result = new UserModel();
//
//        result.setCreateTime(user.getCreateTime());
//        result.setEmail(user.getEmail());
//        result.setIccid(user.getIccid());
//        result.setImei(user.getImei());
//        result.setLastLoginTime(user.getLastLoginTime());
//        result.setMac(user.getMac());
//        result.setNickName(user.getNickName());
//        result.setPhoneNumber(user.getPhoneNumber());
//        result.setRegistryType(user.getRegistryType());
//        result.setUpdateTime(user.getUpdateTime());
//        result.setUserid(user.getUserid());
//        result.setUserName(user.getUserName());
//        result.token = token;
//
//        return result;
//	}
//
//	/**
//	 * 同步 token
//	 * @param tokenS
//	 * @return
//	 * @throws Throwable
//	 */
//	public String userIdByToken(String tokenS) throws Throwable {
//
//		Token T = TokenInstance.getToken(this.context.getRedisHandle());
//        return T.getUid(tokenS);
//	}
}
