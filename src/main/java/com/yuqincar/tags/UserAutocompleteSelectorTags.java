package com.yuqincar.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.views.jsp.TagUtils;

import com.opensymphony.xwork2.util.ValueStack;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.privilege.User;

public class UserAutocompleteSelectorTags extends TagSupport {
	private static final long serialVersionUID = 1L;
	private String name;

	public int doStartTag() throws JspException {
		ValueStack stack = TagUtils.getStack(pageContext);
		User user = (User) stack.findValue(name);
		StringBuffer options = new StringBuffer();
		JspWriter out = pageContext.getOut();
		try {
			String labelValue = (user == null ? "" : user.getName());
			//String idValue = (car == null ? "" : String.valueOf(car.getId()));
			options.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"skins/jquery.autocomplete.css\">");
			options.append("<script type=\"text/javascript\" src=\"js/jquery.autocomplete.js\"></script>");
			options.append(" <script type=\"text/javascript\">");
			options.append("$(function() {");
			options.append("$(\"#"+name+"\" ).autocomplete(\"user_userAutocompleteRequest.action\",{");
			options.append("extraParams : {keyword : function(){return $(\"#"+name+"\" ).val();}},");
			options.append("formatItem: function(item) { return item;  },");
			
			options.append("mustMatch: true,");
			
			options.append("parse:function(data) {");
			options.append("var parsed = []; ");
			options.append("for (var i = 0; i < data.length; i++) { ");
			options.append("parsed[parsed.length] = {  ");
			options.append("data: data[i], ");
			options.append("value: data[i],  ");
			options.append("result: data[i]  ");
			options.append("   };  }  return parsed;  ");
			options.append("   } }); ");
			options.append("}); ");
			options.append("</script>");
			
			options.append("<input class=\"half inputText\" type=\"text\" id="+name+" name="+name+"  value=\'"+labelValue+"\'/>");
			out.println(options);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EVAL_PAGE; // 不实现标签的体，即空体标签。
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

	
}
