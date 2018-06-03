package cn.zhao.book.service;

import java.util.List;

import cn.zhao.book.dao.BookDao;
import cn.zhao.book.domain.Book;

public class BookService {
	private BookDao bookDao = new BookDao();
	
	/**
	 * 获取全部图书
	 * @return
	 */
	public List<Book> findAll(){
		return bookDao.findAll();
	}

	/**
	 * 分类查询图书
	 * @param cid
	 * @return
	 */
	public List<Book> findByCategory(String cid) {
		// TODO Auto-generated method stub
		return bookDao.findByCategory(cid);
	}

	/**
	 * 按照bid查找图书
	 * @param bid
	 * @return
	 */
	public Book load(String bid) {
		// TODO Auto-generated method stub
		return bookDao.load(bid);
	}

	/**
	 * 　添加图书
	 * @param book
	 */
	public void add(Book book) {
		bookDao.add(book);
	}
	
	/**
	 * 删除图书
	 */
	public void delete(String bid) {
		bookDao.delete(bid);
	}

	/**
	 * 修改图书
	 * @param book
	 */
	public void edit(Book book) {
		bookDao.edit(book);
	}
}
