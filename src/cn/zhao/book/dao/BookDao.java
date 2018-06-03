package cn.zhao.book.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import cn.zhao.book.domain.Book;
import cn.zhao.category.domain.Category;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 获取全部图书
	 * @return
	 */
	public List<Book> findAll(){
		String sql = "select * from book where del=false";
		try {
			List<Book> bookList = qr.query(sql, new BeanListHandler<Book>(Book.class));
			return bookList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * 按cid分类查找图书
	 */
	public List<Book> findByCategory(String cid) {
		String sql = "select * from book where cid = ? and del=false";
		try {
			List<Book> bookList = qr.query(sql, 
					new BeanListHandler<Book>(Book.class),cid);
			return bookList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取图书详情，按bid查找图书
	 * @param bid
	 * @return
	 */
	public Book load(String bid) {
		String sql = "select * from book where bid = ?";
		try {
			Map<String,Object> map = qr.query(sql, new MapHandler(),bid);
			Book book = CommonUtils.toBean(map, Book.class);
			Category c = CommonUtils.toBean(map, Category.class);
			book.setCategory(c);
			return book;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据分类cid，获取图书数量
	 * @param cid
	 * @return
	 */
	public int findCountByCid(String cid) {
		String sql = "select count(*) from book where cid = ? and del=false";
		try {
			Number count = (Number)qr.query(sql, new ScalarHandler(),cid);
			return count.intValue();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 添加图书
	 * @param book
	 */
	public void add(Book book) {
		try {
			String sql = "insert into book values(?,?,?,?,?,?)";
			Object[] params = {book.getBid(), book.getBname(), book.getPrice(),
					book.getAuthor(), book.getImage(), book.getCategory().getCid()};
			qr.update(sql, params);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 删除图书
	 */
	public void delete(String bid) {
		try {
			String sql = "update book set del=true where bid=?";
			qr.update(sql,bid);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改图书
	 * @param book
	 */
	public void edit(Book book) {
		try {
			String sql = "update book set bname=?,price=?,author=?,image=?,cid=? where bid=?";
			Object[] params = { book.getBname(), book.getPrice(),book.getAuthor(),
					book.getImage(), book.getCategory().getCid(),book.getBid()};
			qr.update(sql, params);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
