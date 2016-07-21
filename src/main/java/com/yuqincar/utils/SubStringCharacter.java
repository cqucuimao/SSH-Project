package com.yuqincar.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubStringCharacter {

   //获得车牌的前缀
	public String subPre(String str, int subSLength)  
            throws UnsupportedEncodingException{ 
        if (str == null)  
            return "";  
        else{ 
            int tempSubLength = subSLength;//截取字节数
            String subStr = str.substring(0, str.length()<subSLength ? str.length() : subSLength);//截取的子串  
            System.out.println(subStr);
            
            int subStrByetsL = subStr.getBytes("GBK").length;//截取子串的字节长度 
            //int subStrByetsL = subStr.getBytes().length;//截取子串的字节长度 
            System.out.println(subStrByetsL);
            // 说明截取的字符串中包含有汉字  
            while (subStrByetsL > tempSubLength){  
                int subSLengthTemp = --subSLength;
                subStr = str.substring(0, subSLengthTemp>str.length() ? str.length() : subSLengthTemp);  
                subStrByetsL = subStr.getBytes("GBK").length;
                //subStrByetsL = subStr.getBytes().length;
            }  
            return subStr; 
        }
    }
	//获得车牌的后几位数字
	public String  subLsnum(String pre,String string){
		   
		   Pattern pattern = Pattern.compile(pre);  
		   Matcher matcher = pattern.matcher(string);
		   String lsnum = matcher.replaceFirst("");
		   return lsnum.trim(); 
		}
	
}
