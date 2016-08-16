package com.yuqincar.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.views.jsp.TagUtils;

import com.opensymphony.xwork2.util.ValueStack;
import com.yuqincar.domain.privilege.User;

public class UserSelectorTags extends TagSupport {
	private static final long serialVersionUID = 1L;
	private String name;
	private boolean driverOnly;

	public int doStartTag() throws JspException {
		ValueStack stack = TagUtils.getStack(pageContext);
		User user = (User) stack.findValue(name);
		StringBuffer options = new StringBuffer();
		JspWriter out = pageContext.getOut();
		try {
			String labelValue = (user == null ? "" : user.getName());
			String idValue = (user == null ? "" : String.valueOf(user.getId()));
			options.append("<input id="
					+ name
					+ "Label name="
					+ name
					+ "Label class=\"inputText\" type=\"text\" onclick=\"onUserSelectorClick(\'"
					+ name + "\',\'"+driverOnly+"\');\" value=\'" + labelValue + "\' />");
			options.append("<input id=" + name + " name=" + name
					+ " type=\"hidden\" value=\'" + idValue + "\'/>");
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

	public boolean isDriverOnly() {
		return driverOnly;
	}

	public void setDriverOnly(boolean driverOnly) {
		this.driverOnly = driverOnly;
	}

}
