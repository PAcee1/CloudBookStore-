package cn.zhao.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 购物车
 * @author ALIENWARE
 *
 */
public class Cart {
	private Map<String,CartItem> map = new LinkedHashMap<String, CartItem>();
	
	//添加条目
	public void add(CartItem cartItem){
		if(map.containsKey(cartItem.getBook().getBid())){
			//如果存在此书，书的数量加一
			CartItem newItem = map.get(cartItem.getBook().getBid());
			newItem.setCount(newItem.getCount()+cartItem.getCount());
			map.put(cartItem.getBook().getBid(), newItem);
		}else{
			//如果没有，添加
			map.put(cartItem.getBook().getBid(), cartItem);
		}
	}
	
	//清空购物车
	public void clear(){
		map.clear();
	}
	
	//删除条目
	public void delete(String bid){
		map.remove(bid);
	}
	
	//获取所有条目
	public Collection<CartItem> getCartItems(){
		return map.values();
	}
	
	//计算总和
	public double getTotal(){
		BigDecimal total = new BigDecimal("0");
		for(CartItem c : map.values()){
			BigDecimal bd = new BigDecimal("" + c.getSubtotal());
			total = total.add(bd);
		}
		return total.doubleValue();
	}
}
