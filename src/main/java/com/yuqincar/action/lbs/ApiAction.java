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
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.utils.HttpMethod;
import com.yuqincar.utils.Configuration;
import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
public class ApiAction extends BaseAction {
	
	private final static String BASEURL = "http://api.capcare.com.cn:1045/api";
	private static final String ENDURL = "&token="+Configuration.getCapcareToken()+"&user_id="+Configuration.getCapcareUserId()+"&app_name="+Configuration.getCapcareAppName();
	private final static String BAIDUBASEURL = "http://api.map.baidu.com/geoconv/v1/?coords=";
	private static final String BAIDUENDURL = "&from=1&to=5&ak="+Configuration.getBaiduKey();
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
			
			String queryString = request.getQueryString();//获取url的参数
			String queryStringUrl =  URLDecoder.decode(queryString,"utf-8");//进行编码，中文参数会乱码
			String realQueryString = queryStringUrl.substring(0, 64);//截取前面64个字符，真正的参数
			String plateNumber = queryStringUrl.substring(64,71);//截取参数中的车牌号
			//System.out.println("车牌号="+plateNumber);
			String carId = queryStringUrl.substring(71,queryStringUrl.length());//截取参数中的车辆id号
			
			String url = BASEURL+realQueryString+ENDURL;	//请求Capcare的url
			String jsonString = HttpMethod.get(url);
			
			//从Capcare获取到String类型的结果，然后转成json类型
			JSONObject jsonObject = JSONObject.fromObject(jsonString);
			String device = jsonObject.getString("device");
			JSONObject deviceObject = JSONObject.fromObject(device);
			String position = deviceObject.getString("position");
			
			JSONObject positionObject = JSONObject.fromObject(position);
			String lng = positionObject.getString("lng");
			String lat = positionObject.getString("lat");
			String baiduString = lng+","+lat;
			String capcareStatus = positionObject.getString("status");
			String speed = positionObject.getString("speed");
			//System.out.println("lng="+lng+"&"+"lat="+lat);
			//System.out.println("capcareStatus="+capcareStatus+"&"+"speed="+speed);
			String baiduUrl = BAIDUBASEURL+baiduString+BAIDUENDURL;
			//System.out.println("baiduUrl="+baiduUrl);
			/*获取lng和lat之后，再到百度进行坐标转码*/
			String baiduJson = HttpMethod.get(baiduUrl);
			//System.out.println("baiduJson="+baiduJson);
			String firstJson = baiduJson.substring(0,1);
			String lastJson = baiduJson.substring(1,baiduJson.length());
			String addedJson = "\"plateNumber\":\""+plateNumber+"\",\"carId\":\""+carId+"\",\"speed\":\""+speed+"\",\"capcareStatus\":\""+capcareStatus+"\",";
			
			//String firstJson = jsonString.substring(0, 1);
			//String lastJson = jsonString.substring(1,jsonString.length());
			String finalJson = firstJson+addedJson+lastJson;
			out.write(finalJson);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
