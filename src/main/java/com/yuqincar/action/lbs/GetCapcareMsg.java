package com.yuqincar.action.lbs;

import com.yuqincar.utils.Configuration;
import com.yuqincar.utils.HttpMethod;

/**
 * 访问凯步关爱的API，获取车辆行驶的信息
 * @author cocoa
 *
 */
public class GetCapcareMsg {
	
	private static String url = "http://api.capcare.com.cn:1045/api/device.list.do?token="+Configuration.getCapcareToken()+
								 "&user_id="+Configuration.getCapcareUserId()+"&app_name="+Configuration.getCapcareAppName();
	
	//访问凯步的API  http://api.capcare.com.cn:1045/api/device.list.do?token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD
	public String excute(){
		return HttpMethod.get(url);
	}
}
