package com.yuqincar.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dbunit.dataset.datatype.StringIgnoreCaseDataType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysql.fabric.xmlrpc.base.Array;
import com.yuqincar.action.lbs.GetBaiduMsg;
import com.yuqincar.action.lbs.GetCapcareMsg;
import com.yuqincar.domain.monitor.CapcareMessage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CapcareMessageUtils {
	public static Map<String,CapcareMessage> capcareMap = new HashMap<String, CapcareMessage>();
	
	public static Map<String, String> statusMap = new HashMap<String, String>();
	
	public static int limitLen = 100;
	
	public static void getCapcareMessage() {
		GetCapcareMsg gcm = new GetCapcareMsg();
		String capcareMessageString = gcm.excute();
		
		JSONObject jsonObject = JSONObject.fromObject(capcareMessageString);
		JSONArray devices = jsonObject.getJSONArray("devices");
		JSONArray notNullDevices = new JSONArray();
		//需要访问百度API进行坐标转换，每次最多100个坐标
		int sum = devices.size();
		//去除其中坐标为0的元素  && 去除mode不为A的元素
		for(int i=0;i<sum;i++){
			String lng = devices.getJSONObject(i).getJSONObject("position").get("lng").toString();
			String lat = devices.getJSONObject(i).getJSONObject("position").get("lat").toString();
			String mode = devices.getJSONObject(i).getJSONObject("position").get("mode").toString();
			if(capcareMap.size()!=0)	//如果是第一次启动，不管mode的值是什么，都要获取位置
			{
				if(!mode.equals("A"))	
					continue;
			}
			if(! lng.equals("0.0") && ! lat.equals("0.0")){
				notNullDevices.add(devices.getJSONObject(i));
			}
		}
		int positionNum = notNullDevices.size(); 
		int groups = positionNum/limitLen + 1;//组数
		int lastGroupNum = positionNum%limitLen;//最后一组的个数
		//请求百度API进行坐标转换
		for(int i=0;i<groups;i++){
			String positionStr = "";
			if(i != groups-1){						//不是最后一组
				for(int j=0;j<limitLen;j++){
						if(j != 0){
							positionStr +=";";
						}
						positionStr = positionStr +notNullDevices.getJSONObject(i*limitLen+j).getJSONObject("position").get("lng").toString()+","+
								notNullDevices.getJSONObject(i*limitLen+j).getJSONObject("position").get("lat").toString();
				}
			}else{									//最后一组特别处理
				for(int j=0;j<lastGroupNum;j++){
						if(j != 0 ){
							positionStr +=";";
						}
						positionStr = positionStr +notNullDevices.getJSONObject(i*limitLen+j).getJSONObject("position").get("lng").toString()+","+
								notNullDevices.getJSONObject(i*limitLen+j).getJSONObject("position").get("lat").toString();
					}
			}
			//再对每一组进行坐标转换
			GetBaiduMsg gbm = new GetBaiduMsg();
			JSONObject baiduJasonObject = JSONObject.fromObject(gbm.excute(positionStr));
			JSONArray baiduPositionArray = baiduJasonObject.getJSONArray("result");
			int len = baiduPositionArray.size();
			//再对每一组封装数据
			for(int k=0;k<len;k++){
					CapcareMessage cm = new CapcareMessage();
					String plateNumber = notNullDevices.getJSONObject(i*len+k).get("name").toString();
					cm.setPlateNumber(plateNumber);
					cm.setLongitude(baiduPositionArray.getJSONObject(k).get("x").toString());
					cm.setLatitude(baiduPositionArray.getJSONObject(k).get("y").toString());
					cm.setStatus(notNullDevices.getJSONObject(i*len+k).getJSONObject("position").get("status").toString());
					cm.setSpeed(notNullDevices.getJSONObject(i*len+k).getJSONObject("position").get("speed").toString());
					cm.setDirection(notNullDevices.getJSONObject(i*len+k).getJSONObject("position").get("direction").toString());
					capcareMap.put(plateNumber, cm);
			}
		}
	}
}
