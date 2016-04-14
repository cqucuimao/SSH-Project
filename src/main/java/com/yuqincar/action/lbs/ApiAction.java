package com.yuqincar.action.lbs;

import java.io.IOException;
import java.io.PrintWriter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.utils.HttpMethod;

@Controller
@Scope("prototype")
public class ApiAction extends BaseAction {
	
	private final static String BASEURL = "http://api.capcare.com.cn:1045/api/";
	private static final String ENDURL = "&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD";
	public void api() {
		PrintWriter out;
		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
				.get(ServletActionContext.HTTP_REQUEST);
		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
				.get(ServletActionContext.HTTP_RESPONSE);
		
		// 不加可能会造成前端无法识别
		response.setContentType("text/json");
		// 第二句不加可能会有乱码
		response.setCharacterEncoding("utf-8");
		
		try {
			out = response.getWriter();
			String queryString = request.getQueryString();
			String url = BASEURL+queryString+ENDURL;
			String json = HttpMethod.get(url);
			
			out.write(json);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
