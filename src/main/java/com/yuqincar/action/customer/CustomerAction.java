package com.yuqincar.action.customer;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.service.customer.CustomerService;
import com.yuqincar.utils.QueryHelper;


@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction implements ModelDriven<Customer> {

	private String phonesStr;
	
	private String phone;
	
	private Customer model = new Customer();
	

	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerOrganizationService customerOrganizationService;
	
	private Long customerOrganizationId;
	
	/** 查询*/
	public String queryList() throws Exception {
		QueryHelper helper = new QueryHelper(Customer.class, "c");
		
		if(model.getName()!=null && !"".equals(model.getName()))
			helper.addWhereCondition("c.name like ?", "%"+model.getName()+"%");
		
		if(model.getCustomerOrganization()!=null)
			helper.addWhereCondition("c.customerOrganization=?",model.getCustomerOrganization());
		
		if(phone!=null && phone.length()!=0)
			helper.addWhereCondition("? in elements(c.phones)", phone);
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
		String[] array = phonesStr.split(";");
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
				sb.append(phone).append(";");
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
		String[] array = phonesStr.split(";");
		List<String> list = java.util.Arrays.asList(array);
		customer.setPhones(list);
		customer.setGender(model.getGender());
		
		//更新到数据库
		customerService.updateCustomer(customer);
		ActionContext.getContext().getValueStack().push(new Customer());
		return freshList();
	}

	public Customer getModel() {
		return model;
	}		
	
	public boolean isCanDelete(){
		Customer customer = (Customer)ActionContext.getContext().getValueStack().peek();
		Customer manager = customer.getCustomerOrganization().getManager();
		if(manager == null || customer.getId() != manager.getId()){
			if(customerService.canDeleteCustomer(customer.getId()))
				return true;
			else
				return false;
		}else{
			return false;
		}	
	}
	
	/**  查看人员*/
	public String checkPeople() {
		QueryHelper helper = new QueryHelper(Customer.class, "c");
		helper.addWhereCondition("c.customerOrganization.id=?", customerOrganizationId);		
		PageBean<Customer> pageBean = customerService.queryCustomer(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);			
		return "list";
	}

	public Long getCustomerOrganizationId() {
		return customerOrganizationId;
	}

	public void setCustomerOrganizationId(Long customerOrganizationId) {
		this.customerOrganizationId = customerOrganizationId;
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhonesStr() {
		return phonesStr;
	}

	public void setPhonesStr(String phonesStr) {
		this.phonesStr = phonesStr;
	}
}



