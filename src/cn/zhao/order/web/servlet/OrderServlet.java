package cn.zhao.order.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import cn.zhao.cart.domain.Cart;
import cn.zhao.cart.domain.CartItem;
import cn.zhao.order.domain.Order;
import cn.zhao.order.domain.OrderItem;
import cn.zhao.order.service.OrderException;
import cn.zhao.order.service.OrderService;
import cn.zhao.user.domain.User;

public class OrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();
	
	/**
	 * 确认收货
	 */
	public String confirm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String oid = request.getParameter("oid");
		try {
			orderService.confirm(oid);
			request.setAttribute("msg", "恭喜你，确认收货成功！");
		} catch (OrderException e) {
			request.setAttribute("msg", e.getMessage());
		}
		return "f:/jsps/msg.jsp";
	}
	
	/**
	 * 根据订单号获取所有条目
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 获取oid参数
		 * 调用service的findByOid方法
		 * 获取order对象
		 * 存入request域
		 * 转发
		 */
		String oid = request.getParameter("oid");
		Order order = orderService.findByOid(oid);
		request.setAttribute("order", order);
		return "f:/jsps/order/desc.jsp";
	}
	
	/**
	 * 我的订单，获取所有订单和其条目
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 获取uid
		 * 调用service的findByUid方法，传递uid参数
		 * 获取orderList集合
		 * 保存集合
		 * 转发到list.jsp页面 
		 */
		User user = (User) request.getSession().getAttribute("session_user");
		List<Order> orderList = orderService.findByUid(user.getUid());
		request.setAttribute("orderList", orderList);
		return "f:/jsps/order/list.jsp";
	}
	
	/**
	 * 添加订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 从session中获取cart购物车，
		 * 创建Order和OrderItem
		 * 设置Order和OrderItem
		 * 调用service的add方法
		 * 把Order对象存入request域
		 * 转发到order/desc.jsp
		 */
		Cart cart = (Cart) request.getSession().getAttribute("session_cart");
		User user = (User) request.getSession().getAttribute("session_user");
		
		//创建Order订单对象
		Order order = new Order();
		order.setOid(CommonUtils.uuid());//设置订单编号
		order.setState(1);//设置状态为未付款
		order.setOrdertime(new Date());//设置当前时间
		order.setOwner(user);//设置订单拥有者
		order.setTotal(cart.getTotal());//设置总价格
		
		//创建orderItemList，订单条目集合
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		//循环Cart添加参数
		for(CartItem cartitem : cart.getCartItems()){
			OrderItem oi = new OrderItem();
			
			oi.setIid(CommonUtils.uuid());//设置条目编号
			oi.setCount(cartitem.getCount());//设置总数量
			oi.setSubtotal(cartitem.getSubtotal());//设置价格总计
			oi.setOrder(order);//设置所属订单
			oi.setBook(cartitem.getBook());//设置图书
			//添加
			orderItemList.add(oi);
		}	
		order.setOrderItemList(orderItemList);
		
		//清空购物车
		cart.clear();
		//调用service的add方法
		orderService.add(order);
		request.setAttribute("order", order);
		return "f:/jsps/order/desc.jsp";
	}
}
