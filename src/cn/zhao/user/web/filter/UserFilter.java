package cn.zhao.user.web.filter;

import java.io.IOException;

import javax.jws.soap.SOAPBinding.Use;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.zhao.user.domain.User;

/**
 * 过滤器，防止未登录状态查看购物车或订单
 * @author ALIENWARE
 *
 */
public class UserFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		User user = (User)req.getSession().getAttribute("session_user");
		if(user == null){
			req.setAttribute("msg", "请登录后使用此功能");
			req.getRequestDispatcher("/jsps/user/login.jsp").forward(req, res);
		}else{
			chain.doFilter(req, response);
		}
	}


	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
