package cn.zhao.category.service;

import java.util.List;

import cn.zhao.book.dao.BookDao;
import cn.zhao.category.dao.CategoryDao;
import cn.zhao.category.domain.Category;

public class CategoryService {
	private CategoryDao categoryDao = new CategoryDao();
	private BookDao bookDao = new BookDao();
	
	/**
	 * 查询所有分类
	 * @return
	 */
	public List<Category> findAll(){
		return categoryDao.findAll();
	}

	
	/**
	 * 添加分类
	 * @return
	 */
	public void add(Category category) {
		// TODO Auto-generated method stub
		categoryDao.add(category);
	}

	/**
	 * 删除分类
	 * @param cid
	 * @throws CategoryException 
	 */
	public void delete(String cid) throws CategoryException {
		int count = bookDao.findCountByCid(cid);
		
		//如果分类里有书，不能删除，抛出异常
		if(count>0) throw new CategoryException("该分类不为空，不可删除！");
		
		categoryDao.delete(cid);
	}


	/**
	 * 加载分类
	 * @param cid
	 */
	public Category load(String cid) {
		// TODO Auto-generated method stub
		return categoryDao.load(cid);
	}


	/**
	 * 修改分类
	 * @param category
	 */
	public void edit(Category category) {
		// TODO Auto-generated method stub
		categoryDao.edit(category);
	}
}
