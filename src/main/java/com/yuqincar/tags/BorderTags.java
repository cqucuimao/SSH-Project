package com.yuqincar.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class BorderTags extends TagSupport   {
	 private static final long serialVersionUID = 1L;
	 private String exceptJMin;
	 private String exceptJquery;
	 public int doStartTag() throws JspException{
		 System.out.println("in doStartTag");
	    	StringBuffer options = new StringBuffer();
	        JspWriter out=pageContext.getOut(); 
	        options.append("<!DOCTYPE HTML>");
	        options.append("<html>");
	        options.append("<head>");
	        options.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />");
	        options.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
	        options.append("<title>");
	        options.append("</title>");
	        options.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"skins/main.css\">");
	        if (exceptJMin==null)
	        {
	        	options.append("<script type=\"text/javascript\" src=\"js/jquery-1.7.1.min.js\"></script>");
			}
	        else if (!exceptJMin.equals("jquery"))
	        {
		       options.append("<script type=\"text/javascript\" src=\"js/jquery-1.7.1.min.js\"></script>");	
			}	
	        options.append("<script type=\"text/javascript\" src=\"js/DatePicker/WdatePicker.js\"></script>");
	        options.append("<script type=\"text/javascript\" src=\"js/common.js\"></script>");
	        options.append("<script type=\"text/javascript\" src=\"js/Border.js\"></script>");
	        options.append("<script src=\"js/artDialog4.1.7/artDialog.source.js?skin=blue\"></script>");
	        options.append("<script src=\"js/artDialog4.1.7/plugins/iframeTools.source.js\"></script>");
	        options.append("<script type=\"text/javascript\" src=\"js/validate/jquery.validate.js\"></script>");
	        options.append("<script type=\"text/javascript\" src=\"js/validate/messages_cn.js\"></script>");
	        
	        if (exceptJquery==null)
	        {
	        	options.append("<script src=\"js/jquery-1.7.2.js\"></script>");
			}
	        else if (!exceptJquery.equals("jquery"))
	        {
	        	options.append("<script src=\"js/jquery-1.7.2.js\"></script>");	
			}	
	        options.append("</head>");
	        options.append("<body class=\"minW\">"); 
	        
	        //防止重复提交
	        options.append("<div id=\"submiting\" style=\"position: absolute; top: 0; left: 0; z-index: 10; display: none\">");
	        options.append("<table WIDTH=\"100%\" BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\">");
	        options.append("<tr height=\"180\"><td>&nbsp;</td></tr>");
	        options.append("<tr>");
	        options.append("<td width=\"30%\"></td>");
	        options.append("<td bgcolor=\"#4169E1\">");
	        options.append("<table WIDTH=\"100%\" height=\"70\" BORDER=\"0\" CELLSPACING=\"2\" CELLPADDING=\"0\">");
	        options.append("<tr><td bgcolor=\"#DDE9F5\" align=\"center\">正在处理页面请求, 请稍候...</td></tr>");
	        options.append("</table></td>");
	        options.append("<td width=\"30%\"></td></tr>");
	        options.append("</table></div>");
	        
	        options.append("<div id=\"cover\" style=\"position: absolute; top: 0; left: 0; z-index: 9; display: none; width: 100%; height: 100%\">");
	        options.append("<table WIDTH=\"100%\" height=\"100%\" BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\">");
	        options.append("<tr><td align=\"center\"><br/></td>");
	        options.append("</tr></table></div>");
	
	   try  {
	          out.println(options);
	        }catch(IOException e){
	        	e.printStackTrace();
	        }
	        return EVAL_PAGE;  
	 }   
	  
	 public int doEndTag() throws JspException{   
		 System.out.println("in doEndTag");
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

	
	 
}
