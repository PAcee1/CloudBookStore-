package cn.zhao.user.service;

import cn.zhao.user.dao.UserDao;
import cn.zhao.user.domain.User;

public class UserService {
	private UserDao userDao = new UserDao();
	
	/**
	 * 注册功能
	 * @param form
	 */
	public void regist(User form) throws UserException{
		
		// 校验用户名
		User user = userDao.findByUsername(form.getUsername());
		if(user != null) throw new UserException("用户名已被注册！");
		
		// 校验email
		user = userDao.findByEmail(form.getEmail());
		if(user != null) throw new UserException("Email已被注册！");
		
		// 插入用户到数据库
		userDao.add(form);
	}
	
	/**
	 * 验证码校验
	 * @throws UserException 
	 */
	public void active(String code) throws UserException{
		User user = userDao.findByCode(code);
		if(user == null) throw new UserException("激活码不存在！");
		if(user.isState()) throw new UserException("用户已经激活，不需要再次激活！");
		
		//修改用户状态为true即已激活
		userDao.updateState(user.getUid(), true);
	}
	
	/**
	 * 用户登录
	 */
	public User login(User form) throws UserException{
		//向数据库中查询用户信息
		User user = userDao.findByUsername(form.getUsername());
		
		//判断是否有此用户，密码是否相同，激活状态是否已经激活
		if(user==null) throw new UserException("用户名不存在！");
		if(!user.getPassword().equals(form.getPassword())) throw new UserException("用户名或密码错误！");
		if(!user.isState()) throw new UserException("用户尚未激活！");
		
		return user;
	}
}
