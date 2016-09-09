package com.yuqincar.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.struts2.ServletActionContext;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.inject.Context;
import com.yuqincar.dao.car.CarDao;
import com.yuqincar.dao.car.impl.CarDaoImpl;
import com.yuqincar.domain.car.Car;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.car.impl.CarServiceImpl;
import javax.annotation.Resource;

public class CarDetailListTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private String id;
	@Resource
	private CarService carService;
	//CarDao carDao = new CarDaoImpl();
	/*WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());  
	CarServiceType carServiceType = (CarServiceType)wac.getBean("carServiceType"); 
	*/
	public int doStartTag() throws JspException {
		StringBuffer options = new StringBuffer();
		JspWriter out = pageContext.getOut();
		try {
			System.out.println("id=: "+id);
			WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());  

		   CarService carService = (CarService)wac.getBean("carService"); 
			/*ApplicationContext  ac = new ClassPathXmlApplicationContext("applicationContext.xml");
			CarService carService = (CarService)ac.getBean("carService");*/
			System.out.println("carService:= "+carService);
			String  value=carService.getCarById(Long.parseLong(id)).getPlateNumber();
			
			options.append("<a href=\"/Web/car_carDetail.action?id="+id+"\"> "+value+" </a>");
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

}
