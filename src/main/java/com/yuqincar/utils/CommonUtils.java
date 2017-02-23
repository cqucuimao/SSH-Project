package com.yuqincar.utils;

public class CommonUtils {

	public static String getSurname(String name) {
		String surname=name.substring(0,2);
		if("欧阳".equals(surname) || "诸葛".equals(surname) || "令狐".equals(surname)
				|| "东方".equals(surname) || "上官".equals(surname) || "司马".equals(surname)
				|| "夏侯".equals(surname) || "东方".equals(surname) || "尉迟".equals(surname)
				|| "皇甫".equals(surname)){
			return surname;
		}else{
			return name.substring(0,1);
		}
	}
	
}
