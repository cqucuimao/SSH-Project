package com.yuqincar.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.views.jsp.TagUtils;

import com.opensymphony.xwork2.util.ValueStack;
import com.yuqincar.domain.car.Car;

public class CarSelectorTags extends TagSupport {
	private static final long serialVersionUID = 1L;
	private String name;
	private String synchDriver;
	private boolean readonly=false;

	public int doStartTag() throws JspException {
		ValueStack stack = TagUtils.getStack(pageContext);
		Car car = (Car) stack.findValue(name);
		StringBuffer options = new StringBuffer();
		JspWriter out = pageContext.getOut();
		try {
			String labelValue = (car == null ? "" : car.getPlateNumber());
			String idValue = (car == null ? "" : String.valueOf(car.getId()));
			if (readonly) {
				options.append("<input id="
						+ name
						+ "Label name="
						+ name
						+ "Label class=\"inputText\" type=\"text\" readonly="+readonly+" value=\'" + labelValue + "\' />");
			}
			else {
				options.append("<input id="
						+ name
						+ "Label name="
						+ name
						+ "Label class=\"inputText\" type=\"text\" onclick=\"onCarSelectorClick(\'"
						+ name + "\',\'"+synchDriver+"\');\" readonly="+readonly+" value=\'" + labelValue + "\' />");
			}
			
			options.append("<input id="+name+" name="+name+" type=\"hidden\" value=\'"+idValue+"\'/>");
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

	public String getSynchDriver() {
		return synchDriver;
	}

	public void setSynchDriver(String synchDriver) {
		this.synchDriver = synchDriver;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	

	
}
