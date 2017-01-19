package com.yuqincar.tags;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.views.jsp.TagUtils;
import org.springframework.expression.spel.ast.OpAnd;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.privilege.Department;
import com.yuqincar.domain.privilege.User;

public class DiskFileSelectorTags extends TagSupport {
	private static final long serialVersionUID = 1L;
	private String name;
	private String uploadLimit="0";
	private String mutilfiles="true";
	private String fileQueue;

	public int doStartTag() throws JspException {
		int x=(int)(Math.random()*100);
		fileQueue="fileQueue_"+String.valueOf(x);
		String sessionName="diskFile_"+name;
		if(ActionContext.getContext().getSession().get(sessionName)!=null)
		{
			 List<File> fileList=(List<File>)ActionContext.getContext().getSession().get(sessionName);
			 for(File file:fileList)
			 {
				 file.delete();
			 }
		}
		ActionContext.getContext().getSession().remove(sessionName);
		System.out.println("sessionNmae in selector: "+sessionName);
		
		StringBuffer options = new StringBuffer();
		JspWriter out = pageContext.getOut();
		System.out.println("mutifiles: "+uploadLimit);
		
		if(uploadLimit.equals("true"))
		{
			uploadLimit="1";
			mutilfiles="false";
		}	
		else
			uploadLimit="0";
		try {
			options.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"js/uploadify.css\">");
			options.append("<script type=\"text/javascript\" src=\"http://lib.sinaapp.com/js/jquery/1.9.1/jquery-1.9.1.min.js\"></script>");	
			options.append("<script type=\"text/javascript\" src=\"js/jquery.uploadify.min.js\"></script>");
			
			options.append(" <script type=\"text/javascript\">");
			options.append("$(function(){ ");
			options.append("$(\"#"+name+"\").uploadify({    ");
			options.append("'debug'     : false, ");
			options.append("'auto'           : true, ");
			options.append(" 'swf'           : 'js/uploadify.swf',");
			options.append("'uploader'       : 'diskFile.action', ");
			options.append("'queueID'        : '"+fileQueue+"', ");
			options.append("'width'          : '75', ");
			options.append("'height'         : '24',  ");
			options.append(" 'queueSizeLimit': '-1', ");
			options.append("'uploadLimit'    : "+uploadLimit+", ");
			options.append(" 'fileTypeDesc'  : '视频文件',");
			options.append(" 'formData'      :{'diskName':'"+name+"'},");
			options.append(" 'fileTypeExts'  : '*.*;*.jpg;*.gif',");
			options.append("'multi'          : "+mutilfiles+", ");
			options.append("'buttonText'     : '文件上传', ");
			options.append("'fileSizeLimit'  : '500MB', ");
			options.append(" 'fileObjName'   : 'uploadify',");
			options.append(" 'method'        : 'post',");
			options.append(" 'removeCompleted' : false,");
			options.append(" 'onFallback':function(){ ");
			options.append(" alert(\"您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。\"); },   ");
			options.append("'onUploadSuccess' : function(file, data, response){ },");
			options.append("'onQueueComplete' : function(){ } ,");
			options.append("'onUploadStart':function(file){ ");
			options.append(" },");
			options.append("'onCancel'           :function(file){ ");
			options.append(" alert(file.name+\"canceled\");");
			options.append("	} ");
			options.append("  });");
			options.append("}); ");
			options.append("</script>");
			
			options.append(" <input type=\"file\" id=" + name + " name=\"uploadify\">");
			options.append("<div id=" + fileQueue + "> </div>  ");
			options.append("<input  multiFiles="+uploadLimit+" name=" + name
					+ " value="+ name +" type=\"hidden\" />");
			out.println(options);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {

		return EVAL_PAGE;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUploadLimit() {
		return uploadLimit;
	}

	public void setUploadLimit(String uploadLimit) {
		this.uploadLimit = uploadLimit;
	}

	public String getMutilfiles() {
		return mutilfiles;
	}

	public void setMutilfiles(String mutilfiles) {
		this.mutilfiles = mutilfiles;
	}

}
