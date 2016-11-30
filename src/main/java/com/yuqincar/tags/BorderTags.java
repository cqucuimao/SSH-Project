package com.yuqincar.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;

public class BorderTags extends TagSupport   {
	 private static final long serialVersionUID = 1L;
	 private String exceptJMin;
	 private String exceptJquery;
	 private String bodyClass;
	 private String bodyStyle;
	 public int doStartTag() throws JspException{
	    	StringBuffer options = new StringBuffer();
	        JspWriter out=pageContext.getOut(); 
	        options.append("<!DOCTYPE HTML>");
	        options.append("<html>");
	        options.append("<head>");
	        options.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />");
	        options.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
	        options.append("<title>");
	        options.append("</title>");
	        options.append("<link rel=\"stylesheet\" type=\"text/css\" href=\""+pageContext.getServletContext().getContextPath()+"/skins/main.css\">");
	        if (exceptJMin==null || !exceptJMin.equals("on"))
	        {
	        	options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/jquery-1.7.1.min.js\"></script>");
			}
	        if (exceptJquery==null || !exceptJquery.equals("on"))
	        {
	        	options.append("<script src=\""+pageContext.getServletContext().getContextPath()+"/js/jquery-1.7.2.js\"></script>");
			}
	        options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/DatePicker/WdatePicker.js\"></script>");
	        options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/common.js\"></script>");
	        options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/Border.js\"></script>");
	        options.append("<script src=\""+pageContext.getServletContext().getContextPath()+"/js/artDialog4.1.7/artDialog.source.js?skin=blue\"></script>");
	        options.append("<script src=\""+pageContext.getServletContext().getContextPath()+"/js/artDialog4.1.7/plugins/iframeTools.source.js\"></script>");
//	        options.append("<script src=\""+pageContext.getServletContext().getContextPath()+"/js/artDialog4.1.7/jquery.artDialog.source.js\"></script>");
	        options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/validate/jquery.validate.js\"></script>");
	        options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/validate/messages_cn.js\"></script>");
	        //options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/jquery.autocomplete.js\"></script>");
	        
	        //options.append("<script src=\""+pageContext.getServletContext().getContextPath()+"/js/artDialog4.1.7/artDialog.js\"></script>");
	        //options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/jquery.cookie.js\"></script>");
	        //options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/jquery.pager.js\"></script>");

	        
	        options.append("</head>");
	        options.append("<body");
	        if(!StringUtils.isEmpty(bodyClass))
	        	options.append(" class=\"").append(bodyClass).append("\"");
	        else
	        	options.append(" class=\"minW\"");
	        
	        if(!StringUtils.isEmpty(bodyStyle))
	        	options.append(" style=\"").append(bodyStyle).append("\"");
	        options.append(">");
	        //防止重复提交       
	        options.append("<div id=\"submiting\" style=\"left:0px;top:0px;position:fixed;height:100%;width:100%;overflow:hidden;z-index:10;display:none\">");
	        options.append("	<table WIDTH=\"100%\" BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\">");
	        options.append("		<tr height=\"360\">");
	        options.append("			<td>&nbsp;</td>");
	        options.append("		</tr>");
	        options.append("		<tr>");
	        options.append("			<td width=\"30%\"></td>");
	        options.append("			<td style=\"text-align:center\">");
	        options.append("				<table width=\"100%\" style=\"text-align:center\" CELLSPACING=\"0\" CELLPADDING=\"0\">");
	        options.append("					<tr>");
	        options.append("						<td style=\"text-align:center\">");
	        options.append("							<img style=\"text-align:center\" src=\""+pageContext.getServletContext().getContextPath()+"/skins/images/cover.png\"/>");
	        options.append("						</td>");
	        options.append("					</tr>");
	        options.append("				</table>");
	        options.append("			</td>");
	        options.append("			<td width=\"30%\"></td>");
	        options.append("		</tr>");
	        options.append("	</table>");
	        options.append("</div>");
	        
	        options.append("<div id=\"cover\" style=\"background:#cdd0cf;filter:alpha(opacity=10);opacity:.7;left:0px;top:0px;position:fixed;height:100%;width:100%;overflow:hidden;z-index:9;display:none\">");
	        options.append("	<table WIDTH=\"100%\" height=\"100%\" BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\">");
	        options.append("		<tr>");
	        options.append("			<td align=\"center\">");
	        options.append("				<br/>");
	        options.append("			</td>");
	        options.append("		</tr>");
	        options.append("	</table>");
	        options.append("</div>");
	
	   try  {
	          out.println(options);
	        }catch(IOException e){
	        	e.printStackTrace();
	        }
	        return EVAL_PAGE;  
	 }   
	  
	 public int doEndTag() throws JspException{  
	    	StringBuffer options = new StringBuffer();
	        JspWriter out=pageContext.getOut(); 
	        
	        options.append("</body>");
	        options.append("</html>");
	        try{
	        	out.println(options);
	        }catch(IOException e){
	        	e.printStackTrace();
	        }
	        return EVAL_PAGE;   
	  }

	public String getExceptJMin() {
		return exceptJMin;
	}

	public void setExceptJMin(String exceptJMin) {
		this.exceptJMin = exceptJMin;
	}

	public String getExceptJquery() {
		return exceptJquery;
	}

	public void setExceptJquery(String exceptJquery) {
		this.exceptJquery = exceptJquery;
	}

	public String getBodyClass() {
		return bodyClass;
	}

	public void setBodyClass(String bodyClass) {
		this.bodyClass = bodyClass;
	}

	public String getBodyStyle() {
		return bodyStyle;
	}

	public void setBodyStyle(String bodyStyle) {
		this.bodyStyle = bodyStyle;
	}	 
}
