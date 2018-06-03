package cn.zhao.order.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.zhao.user.domain.User;

public class Order {
	private String oid; //订单编号
	private Date ordertime;	//订单时间
	private double total;//小计，价格
	private int state;//状态，未支付，发货，收货，结束
	private User owner;//订单拥有者，用户
	private String address;//地址
	
	private List<OrderItem> orderItemList;//当前订单下所有条目

	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getOid() {
		return oid;
	}

	@Override
	public String toString() {
		return "Order [oid=" + oid + ", ordertime=" + ordertime + ", total="
				+ total + ", state=" + state + ", owner=" + owner
				+ ", address=" + address + ", orderItemList=" + orderItemList
				+ "]";
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public Date getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

}
