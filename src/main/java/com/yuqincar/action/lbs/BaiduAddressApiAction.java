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
public class BaiduAddressApiAction {

	private final static String BASEURL = "http://api.map.baidu.com/geocoder/v2/?ak=XNcVScWmj4gRZeSvzIyWQ5TZ&callback=renderReverse&";
	private static final String ENDURL = "&output=json";
	public void api() {
		
		System.out.println("in baiduApi");
		
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
			
			System.out.println("url="+url);
			String json = HttpMethod.get(url);
			//返回的数据不是标准的json格式,外部有一层renderReverse&&renderReverse()，需要手动去除
		    //计算前缀的长度
		    int prefix="renderReverse&&renderReverse(".length();
		    String realJson=json.substring(prefix, json.length()-1);
			
			System.out.println(realJson);
			out.write(realJson);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
