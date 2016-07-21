package com.yuqincar.action.customer;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Hibernate;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Controller;

import com.mchange.v2.c3p0.impl.NewProxyResultSet;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Result;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.TollCharge;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.service.customer.CustomerService;
import com.yuqincar.utils.QueryHelper;


@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction implements ModelDriven<Customer> {

	public String getPhonesStr() {
		return phonesStr;
	}

	public void setPhonesStr(String phonesStr) {
		this.phonesStr = phonesStr;
	}

	private String phonesStr;
	
	private Customer model = new Customer();
	

	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerOrganizationService customerOrganizationService;
	
	/** 查询*/
	public String queryList() throws Exception {
		QueryHelper helper = new QueryHelper(Customer.class, "c");
		
		if(model.getName()!=null && !"".equals(model.getName()))
			helper.addWhereCondition("c.name like ?", "%"+model.getName()+"%");
		
		if(model.getCustomerOrganization()!=null && model.getCustomerOrganization().getName()!=null && !""
				.equals(model.getCustomerOrganization().getName()))
			helper.addWhereCondition("c.customerOrganization.name like ?", 
					"%"+model.getCustomerOrganization().getName()+"%");
		
		if(phonesStr!=null && phonesStr.length()!=0)
			helper.addWhereCondition("? in elements(c.phones)", phonesStr);
		helper.addOrderByProperty("c.id", false);
		PageBean<Customer> pageBean = customerService.queryCustomer(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("customerHelper", helper);
		return "list";
	}
	/**列表*/
	public String list(){
		QueryHelper helper = new QueryHelper(Customer.class, "c");
		helper.addOrderByProperty("c.id", false);
		PageBean pageBean = customerService.queryCustomer(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("customerHelper", helper);
		return "list";
	}
	
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("customerHelper");
		PageBean pageBean = customerService.queryCustomer(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	/** 删除*/
	public String delete() throws Exception {
		customerService.deleteCustomer(model.getId());
		return freshList();
	}
	
	/** 添加页面 */
	public String addUI() throws Exception {
		return "saveUI";
	}
	
	/** 添加*/
	public String add() throws Exception {
		
		String orgName=model.getCustomerOrganization().getName();
		
		CustomerOrganization cO = customerOrganizationService
				.getCustomerOrganizationByName(orgName);
		if(cO==null){		
			CustomerOrganization customerOrganization=new CustomerOrganization();
			customerOrganization.setName(orgName);
			customerOrganizationService.saveCustomerOrganization(customerOrganization);
		}
		model.setCustomerOrganization(customerOrganizationService.getCustomerOrganizationByName(orgName));
		
		
		String[] array = phonesStr.split(",");
		List<String> list = java.util.Arrays.asList(array);
		model.setPhones(list);
		
		customerService.saveCustomer(model);
		ActionContext.getContext().getValueStack().push(new Customer());
		return freshList();
	}
	
	/** 修改页面*/
	public String editUI() throws Exception {
		// 准备回显的数据
		Customer customer = customerService.getById(model.getId());
		
		if(customer.getPhones()!=null){
			StringBuffer sb=new StringBuffer();
			for(String phone:customer.getPhones())
				sb.append(phone).append(",");
			sb.setLength(sb.length()-1);
			phonesStr = sb.toString();
		}
		
		ActionContext.getContext().getValueStack().push(customer);
		return "saveUI";
	}
	
	/** 修改*/
	public String edit() throws Exception {
		//从数据库中取出原对象
		Customer customer = customerService.getById(model.getId());
		customer.setName(model.getName());
		String[] array = phonesStr.split(",");
		List<String> list = java.util.Arrays.asList(array);
		customer.setPhones(list);
		customer.setGender(model.isGender());
		customer.setAddresses(model.getAddresses());
		
		//更新到数据库
		customerService.updateCustomer(customer);
		ActionContext.getContext().getValueStack().push(new Customer());
		return freshList();
	}

	public Customer getModel() {
		return model;
	}
	
	/** 验证联系方式*/
	public void validateAdd(){
		String regExp = "^(1([38]\\d|4[57]|5[0-35-9]|7[06-8])\\d{8})(,(1([38]\\d|4[57]|5[0-35-9]|7[06-8])\\d{8}))*$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(phonesStr);
		
		if(m.find()==false){
			addFieldError("phonesStr", "如果输入多个电话号码，请以英文逗号隔开！");
		}
	}
	
	
	/** 验证联系方式*/
	public void validateEdit(){
		String regExp = "^(1([38]\\d|4[57]|5[0-35-9]|7[06-8])\\d{8})(,(1([38]\\d|4[57]|5[0-35-9]|7[06-8])\\d{8}))*$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(phonesStr);
		
		if(m.find()==false){
			addFieldError("phonesStr", "如果输入多个电话号码，请以英文逗号隔开！");
		}
	}
	
	
	public boolean isCanDelete(){
		Customer customer = (Customer)ActionContext.getContext().getValueStack().peek();
		if(customerService.canDeleteCustomer(customer.getId()))
			return true;
		else
		return false;
	}
	
	/**  查看人员*/
	public String checkPeople() {
		CustomerOrganization customerOrganization = customerOrganizationService
				.getById(model.getId());
		//System.out.println("我在防护眼镜"+customerOrganization.getName());
		QueryHelper helper = new QueryHelper(Customer.class, "c");
		
		if(model.getCustomerOrganization()!=null && model.getCustomerOrganization().getName()!=null && !""
				.equals(model.getCustomerOrganization().getName()))
			helper.addWhereCondition("select customerOrganization.name from c where customerOrganization.name=?", 
					"%"+customerOrganization.getName()+"%");
			helper.addWhereCondition("c.customerOrganization.name like ?", 
				"%"+customerOrganization.getName()+"%");
		
		PageBean<Customer> pageBean = customerService.queryCustomer(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		//Session session = HibernateUtil.currentSession();
		//if(customerOrganization.getName()==customer.getCustomerOrganization().getName())
			
		return "list";
	}

}



