package com.yuqincar.tags;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.jsp.TagUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.util.ValueStack;
import com.yuqincar.dao.order.CarServiceSuperTypeDao;
import com.yuqincar.domain.car.CarServiceSuperType;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.car.TollCharge;
import com.yuqincar.domain.order.Price;
import com.yuqincar.domain.order.PriceTable;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.order.PriceService;


public class UserSelectorTags extends TagSupport   {
	 private static final long serialVersionUID = 1L;
	 private String name;
	    public int doStartTag() throws JspException 
	    {   
	    	ValueStack stack=TagUtils.getStack(pageContext);
	    	User user=(User)stack.findValue(name);
	    	StringBuffer options = new StringBuffer();
	        JspWriter out=pageContext.getOut(); 
	        try
	        {  
	        	String labelValue=(user==null ? "" : user.getName());
	        	String idValue=(user==null ? "" : String.valueOf(user.getId()));
	        	options.append("<input id="+name+"Label name="+name+"Label class=\"inputText\" type=\"text\" onclick=\"onUserSelectorClick(\'"+name+"\');\" value=\'"+labelValue+"\' />");
	        	options.append("<input id="+name+" name="+name+" type=\"hidden\" value=\'"+idValue+"\'/>");
	        	out.println(options);
	        	
	        }
	        catch (IOException e) 
	        {   
	            e.printStackTrace();   
	        }   
	               
	        return EVAL_PAGE;  //不实现标签的体，即空体标签。   
	    }   
	  
	    public int doEndTag() throws JspException
	    {   
	    	
        return EVAL_PAGE;   
	  }

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
}
