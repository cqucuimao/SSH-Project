package com.yuqincar.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.service.privilege.impl.UserServiceImpl;

public class AppCheckPrivilegeInterceptor extends AbstractInterceptor {

	private static UserService userService = new UserServiceImpl();
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		System.out.println("in AppCheckPrivilegeInterceptor check");
        ActionContext context=ActionContext.getContext();    
        HttpServletRequest request = (HttpServletRequest)context.get(ServletActionContext.HTTP_REQUEST);     
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
				.get(ServletActionContext.HTTP_RESPONSE);
		
		// 不加可能会造成前端无法识别
		response.setContentType("text/json");
		// 第二句不加可能会有乱码
		response.setCharacterEncoding("utf-8");
        
        String username=request.getParameter("username");
        String password=request.getParameter("pwd");
        long companyId=Long.valueOf(request.getParameter("companyId"));
        User user = userService.getByLoginNameAndMD5Password(username, password,companyId);
        
        if(user == null) {
        	try {
        		PrintWriter out;
    			out = response.getWriter();
    			String queryString = request.getQueryString();
    			out.write("{error:权限不足}");
    			out.flush();
    			out.close();
    			return "";
    		} catch (IOException e) {
    			throw new RuntimeException(e);
    		}
        } else {
        	return invocation.invoke();
        }
	}

}
