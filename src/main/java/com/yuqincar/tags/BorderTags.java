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
	        System.out.println("path="+pageContext.getServletContext().getContextPath());
	        System.out.println("path="+pageContext.getServletContext().getServletContextName());
	        options.append("<!DOCTYPE HTML>");
	        options.append("<html>");
	        options.append("<head>");
	        options.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />");
	        options.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
	        options.append("<title>");
	        options.append("</title>");
	        options.append("<link rel=\"stylesheet\" type=\"text/css\" href=\""+pageContext.getServletContext().getContextPath()+"/skins/main.css\">");
	        //options.append("<link rel=\"stylesheet\" type=\"text/css\" href=\""+pageContext.getServletContext().getContextPath()+"/skins/jquery.autocomplete.css\">");
	        options.append("<style>"
	  +".tdDiv{width:150px;text-align:left;float:left;margin-left:0px;}"
    +".tdDiv1{width:110px;text-align:center;float:left;margin-left:0px;}"
    +".tdDiv2{width:110px;text-align:center;float:left;margin-left:33px;}"
    +".tdDiv3{width:110px;text-align:center;float:left;margin-left:33px;}"
	+"</style>");
	        options.append("<style>"
	+".tdInput{background: #FFFFFF;border: 1px solid #bbb;height: 33px; line-height: 33px;font-size: 14px;font-weight:normal;padding: 0 4px 0 6px;vertical-align: middle;width:130px;}"
    +".tdInput:focus{ background:#f0fff3;border: 1px solid #65bd77;color: #65bd77;}"
    +".tdInput1{background: #FFFFFF;border: 1px solid #bbb;height: 33px; line-height: 33px;font-size: 14px;font-weight:normal;padding: 0 4px 0 6px;vertical-align: middle;width:132px;}"
    +".tdDiv{width:100px;text-align:center;float:left;margin-left:20px}"
    +".tdDiv1{width:150px;text-align:center;float:left;margin-left:35px}"
    +".tdDiv2{width:120px;text-align:center;float:left;margin-left:33px}"
	+"</style>");
	        options.append("<style>"
	+".tdDiv{width:100px;text-align:center;float:left;margin-left:20px}"
    +".tdDiv1{width:150px;text-align:center;float:left;margin-left:35px}"
    +".tdDiv2{width:120px;text-align:center;float:left;margin-left:33px}"
	+".tdInput{background: #FFFFFF;border: 1px solid #bbb;height: 33px; line-height: 33px;font-size: 14px;font-weight:normal;padding: 0 4px 0 6px;vertical-align: middle;width:130px;}"
    +".tdInput:focus{ background:#f0fff3;border: 1px solid #65bd77;color: #65bd77;}"
    +".tdInput1{background: #FFFFFF;border: 1px solid #bbb;height: 33px; line-height: 33px;font-size: 14px;font-weight:normal;padding: 0 4px 0 6px;vertical-align: middle;width:132px;}"
	+"</style>");
	        options.append("<style >"
	+".ttt tr td:first-child "
   +"{"
        +"border: 1px solid #eaeaea !important;"
    +"}"

+"</style>");
	        /*options.append("<style type="text/css">"
		+"body, html,#allmap {width: 100%;height: 100%;margin:0;font-family:"微软雅黑";}"
		+"#BaiduMap{height:500px;width:100%;}"
+"</style>");*/
	        if (exceptJMin==null)
	        {
	        	options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/jquery-1.7.1.min.js\"></script>");
			}
	        else if (!exceptJMin.equals("jquery"))
	        {
		       options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/jquery-1.7.1.min.js\"></script>");	
			}	
	        options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/DatePicker/WdatePicker.js\"></script>");
	        options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/common.js\"></script>");
	        options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/Border.js\"></script>");
	        options.append("<script src=\""+pageContext.getServletContext().getContextPath()+"/js/artDialog4.1.7/artDialog.source.js?skin=blue\"></script>");
	        options.append("<script src=\""+pageContext.getServletContext().getContextPath()+"/js/artDialog4.1.7/plugins/iframeTools.source.js\"></script>");
	        options.append("<script src=\""+pageContext.getServletContext().getContextPath()+"/js/artDialog4.1.7/jquery.artDialog.source.js\"></script>");
	        options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/validate/jquery.validate.js\"></script>");
	        options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/validate/messages_cn.js\"></script>");
	        //options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/jquery.autocomplete.js\"></script>");
	        
	        //options.append("<script src=\""+pageContext.getServletContext().getContextPath()+"/js/artDialog4.1.7/artDialog.js\"></script>");
	        //options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/jquery.cookie.js\"></script>");
	        //options.append("<script type=\"text/javascript\" src=\""+pageContext.getServletContext().getContextPath()+"/js/jquery.pager.js\"></script>");
	        
	        if (exceptJquery==null)
	        {
	        	options.append("<script src=\""+pageContext.getServletContext().getContextPath()+"/js/jquery-1.7.2.js\"></script>");
			}
	        else if (!exceptJquery.equals("jquery"))
	        {
	        	options.append("<script src=\""+pageContext.getServletContext().getContextPath()+"/js/jquery-1.7.2.js\"></script>");	
			}	
	        options.append("</head>");
	        options.append("<body class=\"minW\">"); 
	        
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
