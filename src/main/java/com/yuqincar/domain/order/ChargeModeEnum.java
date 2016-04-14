package com.yuqincar.domain.order;

import com.yuqincar.utils.Text;

public enum ChargeModeEnum {
	@Text("按里程计费")
	MILE,	//按行驶里程收费
	@Text("按天计费")
	DAY,		//日租
	@Text("协议计费")
	PROTOCOL;	//协议价
	
	public static ChargeModeEnum fromString(String str){
		if(str==null || str.length()==0)
			return null;
		if(str.equals("mile") || str.equals("按里程计费"))
			return MILE;
		else if(str.equals("day") || str.equals("按天计费"))
			return DAY;
		else if(str.equals("protocol") || str.equals("按协议计费"))
			return PROTOCOL;
		return null;
	}
	
	public String toString(){
		switch(this){
		case MILE:	return "按里程计费";
		case DAY:	return "按天计费";
		case PROTOCOL:	return "按协议计费";
		}
		return "";
	}
}
