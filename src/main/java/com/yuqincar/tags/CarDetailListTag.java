package com.yuqincar.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.views.jsp.TagUtils;

import com.opensymphony.xwork2.util.ValueStack;
import com.yuqincar.domain.car.Car;

public class CarDetailListTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private String id;
	private String value;
	public int doStartTag() throws JspException {
		StringBuffer options = new StringBuffer();
		JspWriter out = pageContext.getOut();
		try {
			System.out.println("id=: "+id);
			System.out.println("value=: "+value);
			options.append("<a href=\"/Web/car_carDetail.action?id="+id+"\"> "+value+" </a>");
			out.println(options);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EVAL_PAGE; // 不实现标签的体，即空体标签。
	}

	public int doEndTag() throws JspException {
		/*StringBuffer options = new StringBuffer();
		JspWriter out = pageContext.getOut();
		try {
			options.append("</s:a>");
			out.println(options);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return EVAL_PAGE;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}