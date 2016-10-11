package com.yuqincar.tags;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;


public class BlockTag extends BodyTagSupport {
    /**
     * 占位模块名称
     */
    private String name;

    private static final long serialVersionUID = 1425068108614007667L;
    
    @Override
    public int doStartTag() throws JspException{
    	System.out.println("*********in BlockTag");
        return super.doStartTag();                
    }
    
    @Override
    public int doEndTag() throws JspException {
        ServletRequest request = pageContext.getRequest();
        //block标签中的默认值
        String defaultContent = (getBodyContent() == null)?"":getBodyContent().getString();  
        //System.out.println("getBodyContent.getString: "+getBodyContent().getString());
        String bodyContent = (String) request.getAttribute(OverwriteTag.PREFIX+ name);
       // System.out.println("******OverwriteTag.PREFIX+ name "+OverwriteTag.PREFIX+ name);
        //如果页面没有重写该模块则显示默认内容
        bodyContent = StringUtils.isEmpty(bodyContent)?defaultContent:bodyContent;
        //System.out.println("bodyContent: "+bodyContent);
        try {
            pageContext.getOut().write(bodyContent);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
        // TODO Auto-generated method stub
        return super.doEndTag();
    } 
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
