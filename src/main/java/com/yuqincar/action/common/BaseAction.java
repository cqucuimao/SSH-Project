package com.yuqincar.action.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.service.privilege.DepartmentService;
import com.yuqincar.service.privilege.RoleService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.HttpMethod;
import com.yuqincar.utils.TextResolve;

public abstract class BaseAction extends ActionSupport {

	public BaseAction() {
	}
	
	public TextResolve tr = new TextResolve();
	
	protected HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
			.get(ServletActionContext.HTTP_REQUEST);
	protected HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
			.get(ServletActionContext.HTTP_RESPONSE);
	protected ServletContext context=ServletActionContext.getServletContext();

	protected int pageNum = 1; // 当前页，默认为第1页

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	
	//输出json到web页面
	public void writeJson(String json) {
		PrintWriter out;
		// 不加可能会造成前端无法识别
		response.setContentType("text/json");
		// 第二句不加可能会有乱码
		response.setCharacterEncoding("utf-8");
		
		try {
			out = response.getWriter();
			out.write(json);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public TextResolve getTr() {
		return tr;
	}

	public void setTr(TextResolve tr) {
		this.tr = tr;
	}
	
}
