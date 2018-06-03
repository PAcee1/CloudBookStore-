package cn.zhao.category.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.itcast.jdbc.TxQueryRunner;
import cn.zhao.category.domain.Category;

public class CategoryDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 查询所有分类
	 * @return
	 */
	public List<Category> findAll(){
		String sql = "select * from category";
		try {
			List<Category> list = qr.query(sql, 
					new BeanListHandler<Category>(Category.class));
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}

	/**
	 * 添加分类
	 * @return
	 */
	public void add(Category category) {
		String sql = "insert into category values(?,?)";
		try {
			qr.update(sql,category.getCid(),category.getCname());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}

	/**
	 * 删除图书分类
	 * @param cid
	 */
	public void delete(String cid) {
		String sql = "delete from category where cid=?";
		try {
			qr.update(sql,cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}

	/**
	 * 加载分类
	 * @param cid
	 * @return
	 */
	public Category load(String cid) {
		String sql = "select * from category where cid=?";
		try {
			Category c = qr.query(sql, new BeanHandler<Category>(Category.class),cid);
			return c;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}

	/**
	 * 修改分类
	 * @param category
	 */
	public void edit(Category category) {
		String sql = "update category set cname=? where cid=?";
		try {
			qr.update(sql,category.getCname(),category.getCid());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}
}
