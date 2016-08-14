package com.yuqincar.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.views.jsp.TagUtils;

import com.opensymphony.xwork2.util.ValueStack;
import com.yuqincar.domain.car.CarServiceType;


public class ServiceTypeSelectorTags extends TagSupport   {
	 private static final long serialVersionUID = 1L;
	 private String name;
	  
	    public int doStartTag() throws JspException 
	    {   
	    	 ValueStack stack=TagUtils.getStack(pageContext);
	    	 CarServiceType serviceType=(CarServiceType)stack.findValue(name);
	    	 StringBuffer options = new StringBuffer();
	         JspWriter out=pageContext.getOut(); 
	        try
	        {  
	        	/*WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());  
	        	CarServiceType carServiceType = (CarServiceType)wac.getBean("carServiceType"); 
	        	*/
	        	System.out.println("getValue(name)="+getValue(name));
	        	String labelValue=(serviceType==null ? "" : serviceType.getTitle());
	        	String idValue=(serviceType==null ? "" : String.valueOf(serviceType.getId()));
	        	options.append("<input id="+name+"Label name="+name+"Label class=\"inputText\" type=\"text\" onclick=\"onServiceTypeSelectorClick(\'"+name+"\');\" value=\'"+labelValue+"\'/>");
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
