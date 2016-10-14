package com.yuqincar.action.lbs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

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
	//private static final String ENDURL = "&from=1&to=5&ak=XNcVScWmj4gRZeSvzIyWQ5TZ";
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
			String baiduQueryString =  URLDecoder.decode(queryString,"utf-8");
			System.out.println("baiduQueryString:"+baiduQueryString);
			String[] array = baiduQueryString.split("carId");
			String realBaiduQueryString="";
			String plateNumber = "";
			String carId = "";
			String addedJson = "";
			if(array.length>1){
				realBaiduQueryString = array[0].substring(0,array[0].length()-7);

				plateNumber = array[0].substring(array[0].length()-7, array[0].length());
				carId = array[1];
				addedJson = "\"plateNumber\":\""+plateNumber+"\",\"carId\":\""+carId+"\",";
			}else{
				realBaiduQueryString = array[0];
			}
			System.out.println("realBaiduQueryString"+realBaiduQueryString);
			String url = BASEURL+realBaiduQueryString+ENDURL;
			String json = HttpMethod.get(url);
			System.out.println(json);
			String firstJson = json.substring(0, 1);
			String lastJson = json.substring(1,json.length());
			String finalJson = firstJson+addedJson+lastJson;
			out.write(finalJson);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
