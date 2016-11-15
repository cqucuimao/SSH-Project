package com.yuqincar.action.lbs;

import com.yuqincar.utils.Configuration;
import com.yuqincar.utils.HttpMethod;

public class GetBaiduMsg {
	
	private final static String BAIDUBASEURL = "http://api.map.baidu.com/geoconv/v1/?coords=";
	private static final String BAIDUENDURL = "&from=1&to=5&ak="+Configuration.getBaiduKey();
	//访问百度API进行坐标转换，每次最多100个
	public String excute(String positionStr){
		return HttpMethod.get(BAIDUBASEURL+positionStr+BAIDUENDURL);
	}
}
