package cn.zhao.user.web.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import cn.zhao.cart.domain.Cart;
import cn.zhao.user.domain.User;
import cn.zhao.user.service.UserException;
import cn.zhao.user.service.UserService;

public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();

	//用户退出登录
	public String quit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getSession().invalidate();
		return "r:/index.jsp";
	}
	
	//用户登录
	public String login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 获取表单数据，封装到form类对象中
		 * 表单校验
		 * 调用service的login方法，传递form
		 * 	如果出现异常，保存错误信息，转发到login页面
		 * 登录成功，保存session，重定向到主页
		 */
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		
		//表单校验
		String username = form.getUsername();
		String password = form.getPassword();
		String email = form.getEmail();
		Map<String,String> errors = new HashMap<String,String>();
		if(username == null || username.trim().isEmpty()) {
			errors.put("username", "用户名不能为空！");
		}
		if(password == null || password.trim().isEmpty()) {
			errors.put("password", "密码不能为空！");
		}	
		//判断是否错误
		if(errors.size() > 0) {
			// 1. 保存错误信息
			// 2. 保存表单数据
			// 3. 转发到regist.jsp
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);
			return "f:/jsps/user/login.jsp";
		}
		
		try {
			User user = userService.login(form);
			//登录成功
			request.getSession().setAttribute("session_user", user);
			//添加一个购物车
			request.getSession().setAttribute("session_cart", new Cart());
			return "r:/index.jsp";
		} catch (UserException e) {
			//抛出异常，保存错误信息，保存表单数据，转发到login页面
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			return "f:/jsps/user/login.jsp";
		}
		
		
	}
	
	//用户邮箱验证
	public String active(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 获取code表单数据
		 * 调用service的active方法
		 * 	如果抛出异常，保存错误信息，转发到msg页面
		 * 保存正确信息，转发到msg页面
		 */
		String code = request.getParameter("code");
		try {
			userService.active(code);
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
			return "f:/jsps/msg.jsp";
		}
		request.setAttribute("msg", "恭喜，激活成功！");
		return "f:/jsps/msg.jsp";
	}
	
	//用户注册
	public String regist(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 1.封装表单数据到User类对象form中
		 * 2.向form中添加uid和code验证码
		 * 3.验证表单数据完整性
		 * 	- 错误，保存错误信息，保存表单数据，转发到regist页面
		 * 4.调用service的regist方法，传递form对象
		 * 	- 是否抛出异常，若抛出，保存错误信息，保存表单数据，转发到regist页面
		 * 5.发邮件
		 * 6.保存正确信息，转发到msg页面
		 */
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		// 补全
		form.setUid(CommonUtils.uuid());
		form.setCode(CommonUtils.uuid() + CommonUtils.uuid());
		
		//输入校验
		//创建一个Map，用来封装错误信息，其中key为表单字段名称，值为错误信息
		Map<String,String> errors = new HashMap<String,String>();
		//获取form中的username、password、email进行校验
		String username = form.getUsername();
		String password = form.getPassword();
		String email = form.getEmail();
		
		if(username == null || username.trim().isEmpty()) {
			errors.put("username", "用户名不能为空！");
		} else if(username.length() < 3 || username.length() > 10) {
			errors.put("username", "用户名长度必须在3~10之间！");
		}
		if(password == null || password.trim().isEmpty()) {
			errors.put("password", "密码不能为空！");
		} else if(password.length() < 3 || password.length() > 10) {
			errors.put("password", "密码长度必须在3~10之间！");
		}
		if(email == null || email.trim().isEmpty()) {
			errors.put("email", "Email不能为空！");
		} else if(!email.matches("\\w+@\\w+\\.\\w+")) {
			errors.put("email", "Email格式错误！");
		}
		
		//判断是否错误
		if(errors.size() > 0) {
			// 1. 保存错误信息
			// 2. 保存表单数据
			// 3. 转发到regist.jsp
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
		
		//添加用户
		try {
			userService.regist(form);
		} catch (UserException e) {
			/*
			 * 1. 保存异常信息
			 * 2. 保存form
			 * 3. 转发到regist.jsp
			 */
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
		
		//添加成功，发邮件，保存正确信息，转发到msg中
		//发邮件
		sendMail(form);
		
		request.setAttribute("msg", "恭喜，注册成功！请马上到邮箱激活");
		return "f:/jsps/msg.jsp";
	}
	
	//发送邮件
	public void sendMail(User form) {
		try{
			Properties props1 = new Properties();
			props1.load(this.getClass().getClassLoader()
					.getResourceAsStream("email_template.properties"));
			String to = form.getEmail();//获取收件人
			String content = props1.getProperty("content");//获取邮件内容
			content = MessageFormat.format(content, form.getCode());//替换{0}
			
			Properties props = new Properties();
			props.setProperty("mail.host", "smtp.qq.com");
			props.setProperty("mail.smtp.auth", "true");
			//端口号，QQ邮箱端口号
	        props.put("mail.smtp.port", "587");

	        // 构建授权信息，用于进行SMTP进行身份验证
	        Authenticator authenticator = new Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                // 用户名、密码
	                return new PasswordAuthentication("8709867@qq.com", "rnzeqldghopxcagc");
	            }
	        };
			Session session = Session.getInstance(props, authenticator);
			
			//MimeMessage
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("8709867@qq.com"));//设置发送人
			msg.setRecipients(RecipientType.TO, to);//设置接收人
			msg.setSubject("云书网上书城注册激活邮件");//设置主题
			msg.setContent(content,"text/html;charset=utf-8");//设置正文
			
			//发送
			Transport.send(msg);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
}
