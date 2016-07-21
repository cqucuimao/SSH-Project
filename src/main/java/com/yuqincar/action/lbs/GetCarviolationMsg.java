package com.yuqincar.action.lbs;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.bouncycastle.jce.provider.JDKDSASigner.stdDSA;

import com.alibaba.fastjson.JSONArray;
import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.utils.HttpMethod;

import jxl.demo.Write;
import net.sf.json.JSONObject;
 
 
/**
 * 查询车辆违章接口
 *
 */
public class GetCarviolationMsg {
	private  String BASEURL = "http://api.jisuapi.com/illegal/query?appkey=d6fed7ac1fc72c43";
	private  String key     ="d6fed7ac1fc72c43";
    private String carorg;    //管局名称 hangzhou
    private String lsprefix;  //车牌前缀  %e6%b5%99
    private String lsnum;     //车牌剩余部分 AH5b57
    private String lstype;    //车辆类型   02
    private String frameno;   //车架号 根据管局需要输入 229561
    private String engineno;  //发动机号 根据管局需要输入
    private int    iscity;    //是否返回城市 1返回 默认0不返回 不一定100%返回结果，准确度90% town、lat、lng仅供参考
	public String excute( String carorg,String lsprefix,String lsnum ,String lstype,String frameno,String engineno,int iscity ){
		carorg=carorg;
		lsprefix=lsprefix;
		lsnum=lsnum;
		lstype=lstype;
		frameno=frameno;
		engineno=engineno;
		iscity=iscity;
		String url=
		BASEURL+"&carorg="+carorg+"&lsprefix="+lsprefix+"&lsnum="+lsnum+"&lstype="+lstype+"&frameno="+frameno+"&iscity"+iscity;
	    // "http://api.jisuapi.com/illegal/query?appkey=d6fed7ac1fc72c43&carorg=chongqing&lsprefix=%E6%B8%9D&lsnum=CFU007&lstype=02&frameno=LSGPC54R0AF047043";
		 return  HttpMethod.get(url);//通过工具类获取返回数据
		 
		
    }
}
