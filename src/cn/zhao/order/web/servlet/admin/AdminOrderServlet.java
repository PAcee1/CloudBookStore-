package cn.zhao.order.web.servlet.admin;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import cn.zhao.order.service.OrderService;

public class AdminOrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();
	
	/**
	 * 查询所有订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 调用service方法
		 * 保存域传递给list
		 */
		request.setAttribute("orderList", orderService.findAll());
		return "/adminjsps/admin/order/list.jsp";
	}
	
	/**
	 * 按订单状态查询订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByState(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 调用service方法
		 * 保存域传递给list
		 */
		String state = request.getParameter("state");
		request.setAttribute("orderList", orderService.findByState(state));
		return "/adminjsps/admin/order/list.jsp";
	}
}
