package com.yuqincar.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarService;

public class CarDetailListTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private String id;
	private String textColor;
	public int doStartTag() throws JspException {
		StringBuffer options = new StringBuffer();
		JspWriter out = pageContext.getOut();
		User user = (User) pageContext.getSession().getAttribute("user");
		if (user == null) {
			throw new RuntimeException("没有登录用户！");
		}

		try {
			System.out.println("id=: "+id);
			WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());  
		    CarService carService = (CarService)wac.getBean("carServiceImpl"); 
			String  value=carService.getCarById(Long.parseLong(id)).getPlateNumber();
			if (user.hasPrivilegeByUrl("/car_carDetail")) 
			{
				options.append("<a href=\"/Web/car_carDetail.action?id="+id+"\">");
			} else {
				options.append("<a href=\"#\"> <font color="+textColor+">");
			}
			
			if(!StringUtils.isEmpty(textColor))
				options.append("<font color='"+textColor+"'>");
			
			options.append(value);

			if(!StringUtils.isEmpty(textColor))
				options.append("</font>");
			
			options.append("</a>");
			
			out.println(options);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EVAL_PAGE; // 不实现标签的体，即空体标签。
	}

	public int doEndTag() throws JspException {
		
		return EVAL_PAGE;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

}
