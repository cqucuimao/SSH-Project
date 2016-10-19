package com.yuqincar.action.lbs;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.utils.HttpMethod;

@Controller
@Scope("prototype")
public class BaiduApiAction {
	
	private final static String BASEURL = "http://api.map.baidu.com/geoconv/v1/?";
	private static final String ENDURL = "&from=1&to=5&ak=wzq3sn49ZUQuOFRvdoS4HaQnpZLBFBMd";
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
			
			System.out.println("in baiduApiAction");
			String json = HttpMethod.get(url);
			
			out.write(json);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
