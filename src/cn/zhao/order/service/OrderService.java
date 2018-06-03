package cn.zhao.order.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.jdbc.JdbcUtils;
import cn.zhao.order.dao.OrderDao;
import cn.zhao.order.domain.Order;

public class OrderService {
	private OrderDao orderDao = new OrderDao();
	
	/**
	 * 事务添加订单
	 * @param order
	 */
	public void add(Order order){
		try {
			JdbcUtils.beginTransaction();
			
			orderDao.addOrder(order);
			orderDao.addOrderItemList(order.getOrderItemList());
			
			JdbcUtils.commitTransaction();
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据uid获取所有订单
	 * @param uid
	 * @return
	 */
	public List<Order> findByUid(String uid) {
		return orderDao.findByUid(uid);
	}

	/**
	 * 根据oid获取指定订单
	 * @param oid
	 * @return
	 */
	public Order findByOid(String oid) {
		// TODO Auto-generated method stub
		return orderDao.findByOid(oid);
	}
	
	/**
	 * 根据oid改变订单状态
	 * @throws OrderException 
	 */
	public void confirm(String oid) throws OrderException{
		int state = orderDao.getState(oid);
		if(state != 3) throw new OrderException("对不起，您的订单确认失败");
		
		//修改订单状态为4
		orderDao.updateState(oid,4);
	}

	/**
	 * 查询所有订单
	 * @return
	 */
	public List<Order> findAll() {
		// TODO Auto-generated method stub
		return orderDao.findAll();
	}

	public List<Order> findByState(String state) {
		// TODO Auto-generated method stub
		return orderDao.findByState(state);
	}
}
