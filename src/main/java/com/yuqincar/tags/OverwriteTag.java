package com.yuqincar.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;

public class OverwriteTag extends BodyTagSupport {

    private static final long serialVersionUID = 5901780136314677968L;
    //模块名的前缀
    public static final String PREFIX = "JspTemplateBlockName_";
    //模块名
    private String name;
    
    @Override
    public int doStartTag() throws JspException {
    	System.out.println("*********in overwrite!");
        // TODO Auto-generated method stub
        return super.doStartTag();
    }
    
    @Override
    public int doEndTag() throws JspException {
        ServletRequest request = pageContext.getRequest();
        //标签内容
        BodyContent bodyContent = getBodyContent();
        request.setAttribute(PREFIX+name,  StringUtils.trim(bodyContent.getString()));        
        // TODO Auto-generated method stub
       //System.out.println("prefix+name: "+PREFIX+name);
       //System.out.println("trim: "+StringUtils.trim(bodyContent.getString()));
       return super.doEndTag();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    
}