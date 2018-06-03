package cn.zhao.category.web.servlet.admin;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import cn.zhao.category.domain.Category;
import cn.zhao.category.service.CategoryException;
import cn.zhao.category.service.CategoryService;

public class AdminCategoryServlet extends BaseServlet {
	private CategoryService categoryService = new CategoryService();
	
	/**
	 * 查询所有分类
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 调用Service中findAll方法，得到所有分类
		 * 保存要域中
		 * 转发到adminjsps/admin/category/list.jsp
		 */
		request.setAttribute("categoryList", categoryService.findAll());
		return "f:/adminjsps/admin/category/list.jsp";
	}
	
	/**
	 * 添加
	 */
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 获得参数分类名称cname,创建Category类，补全cid
		 * 调用Service中add方法传递category
		 * 调用findAll(),返回list页面
		 */
		String cname = request.getParameter("cname");
		Category category = new Category();
		category.setCname(cname);
		category.setCid(CommonUtils.uuid());
		
		categoryService.add(category);
		return findAll(request, response);
	}
	
	/**
	 * 删除分类
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 获得参数分类名称cname,创建Category类，补全cid
		 * 调用service的delete方法，传递cid参数
		 * 	如果抛出异常，保存错误信息，转发到msg.jsp
		 * 	如果没有抛出异常，调用findAll方法
		 */
		String cid = request.getParameter("cid");
		try{
			categoryService.delete(cid);
			return findAll(request, response);
		}catch(CategoryException e){
			request.setAttribute("msg", e.getMessage());
			return "f:/adminjsps/msg.jsp";
		}
	}
	
	/**
	 * 加载分类
	 */
	public String editPre(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 修改前要先加载分类，把要修改的分类放到表单中
		 * 调用service#load方法
		 * 返回到mod页面
		 */
		String cid = request.getParameter("cid");
		Category category = categoryService.load(cid);
		request.setAttribute("category", category);
		return "f:/adminjsps/admin/category/mod.jsp";
	}
	
	/**
	 * 修改分类
	 */
	public String edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 获取表单数据
		 * 传递参数，调用service
		 * 转发findAll
		 */
		String cid = request.getParameter("cid");
		String cname = request.getParameter("cname");
		Category category = new Category();
		category.setCname(cname);
		category.setCid(cid);
		categoryService.edit(category);
		return findAll(request, response);
	}
}
