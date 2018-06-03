package cn.zhao.order.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import cn.zhao.book.domain.Book;
import cn.zhao.order.domain.Order;
import cn.zhao.order.domain.OrderItem;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 添加订单
	 * @param order
	 */
	public void addOrder(Order order){
		String sql = "insert into orders values(?,?,?,?,?,?)";
		//处理utils的Date改为sql的Date类型
		Timestamp ts = new Timestamp(order.getOrdertime().getTime());
		Object[] params = {order.getOid(),ts,order.getTotal(),
					order.getState(),order.getOwner().getUid(),order.getAddress()};
		try {
			qr.update(sql,params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 添加订单中的条目，使用批处理
	 * @param orderItemList
	 */
	public void addOrderItemList(List<OrderItem> orderItemList){
		String sql = "insert into orderitem values(?,?,?,?,?)";
		Object[][] params = new Object[orderItemList.size()][];
		
		//循环添加参数
		for(int i=0;i<orderItemList.size();i++){
			OrderItem item = orderItemList.get(i);
			params[i] = new Object[]{item.getIid(),item.getCount(),item.getSubtotal()
					,item.getOrder().getOid(),item.getBook().getBid()};
		}
		try {
			qr.batch(sql, params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据uid获取所有订单
	 * @param uid
	 */
	public List<Order> findByUid(String uid) {
		// TODO Auto-generated method stub
		String sql = "select * from orders where uid=?";
		try {
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class), uid);
			//遍历所有订单，为其添加OrderItem条目
			for(Order order : orderList){
				loadOrderItmes(order);//为每个订单添加属于他的所有条目
			}
			return orderList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
	}

	/*
	 * 为每个订单添加属于他的所有条目
	 */
	private void loadOrderItmes(Order order) {
		//查询两张表，orderitem和book
		String sql = "select * from orderitem o,book b where o.bid=b.bid and oid=? ";
		try {
			//结果是两张表合在一起的，所以用MapList接收
			List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(), order.getOid());
			//遍历Map，生成orderitem对象和book对象,最后合成一个orderitem对象
			List<OrderItem> orderItemList = toOrderItemList(mapList);
			order.setOrderItemList(orderItemList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
	}

	/*
	 * 遍历map，生成两个对象，合成为一个orderitem对象，并添加到集合中
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		// TODO Auto-generated method stub
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(Map<String, Object> map : mapList){
			OrderItem item = CommonUtils.toBean(map, OrderItem.class);
			Book book = CommonUtils.toBean(map, Book.class);
			item.setBook(book);
			orderItemList.add(item);
		}
		return orderItemList;
	}

	/**
	 * 根据oid查找order订单
	 * @param oid
	 * @return
	 */
	public Order findByOid(String oid) {
		String sql = "select * from orders where oid=?";
		try {
			Order order = qr.query(sql, new BeanHandler<Order>(Order.class),oid);
			loadOrderItmes(order);
			return order;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据oid查找订单状态
	 */
	public int getState(String oid){
		String sql = "select state from orders where oid=?";
		try {
			int state = (Integer)qr.query(sql, new ScalarHandler(),oid);
			return state;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据oid改变订单状态
	 */
	public void updateState(String oid,int state){
		String sql = "update orders set state=? where oid=?";
		try {
			qr.update(sql,state,oid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询所有订单
	 */
	public List<Order> findAll() {
		// TODO Auto-generated method stub
		String sql = "select * from orders";
		try {
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class));
			//遍历所有订单，为其添加OrderItem条目
			for(Order order : orderList){
				loadOrderItmes(order);//为每个订单添加属于他的所有条目
			}
			return orderList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * 按订单状态查询订单
	 * @param state
	 * @return
	 */
	public List<Order> findByState(String state) {
		// TODO Auto-generated method stub
		String sql = "select * from orders where state=?";
		try {
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class),state);
			//遍历所有订单，为其添加OrderItem条目
			for(Order order : orderList){
				loadOrderItmes(order);//为每个订单添加属于他的所有条目
			}
			return orderList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
}
