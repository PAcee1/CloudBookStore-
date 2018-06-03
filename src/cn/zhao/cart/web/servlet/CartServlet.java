package cn.zhao.cart.web.servlet;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import cn.zhao.book.service.BookService;
import cn.zhao.book.web.servlet.BookServlet;
import cn.zhao.cart.domain.Cart;
import cn.zhao.cart.domain.CartItem;

public class CartServlet extends BaseServlet {

	/**
	 * 添加条目
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 获取bid和count
		 * 从session中获取购物车
		 * 根据bid和count创建CartItem条目
		 * 购物车中添加条目
		 */
		String bid = request.getParameter("bid");
		String count = request.getParameter("count");
		Cart cart = (Cart)request.getSession().getAttribute("session_cart");
		CartItem ci = new CartItem();
		ci.setBook(new BookService().load(bid));
		ci.setCount(Integer.parseInt(count));
		
		cart.add(ci);
		
		return "f:/jsps/cart/list.jsp";
	}
	
	/**
	 * 清空购物车
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String clear(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Cart cart = (Cart)request.getSession().getAttribute("session_cart");
		cart.clear();
		return "f:/jsps/cart/list.jsp";
	}
	
	/**
	 * 删除指定条目
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Cart cart = (Cart)request.getSession().getAttribute("session_cart");
		String bid = request.getParameter("bid");
		cart.delete(bid);
		return "f:/jsps/cart/list.jsp";
	}
}
