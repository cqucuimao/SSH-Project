package com.yuqincar.action.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.parser.deserializer.StringFieldDeserializer;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.dao.order.CarServiceSuperTypeDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceSuperType;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.Price;
import com.yuqincar.domain.order.PriceTable;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.service.customer.CustomerService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.order.PriceService;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CustomerOrganizationAction extends BaseAction implements
		ModelDriven<CustomerOrganization> {

	private CustomerOrganization model = new CustomerOrganization();

	@Autowired
	private CustomerOrganizationService customerOrganizationService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private OrderService orderService;
	
	private long customerId;
	
	private CustomerOrganization customerOrganization;
	
	private String customerOrganizationName;
	
	
	/** 查询*/
	public String queryList() throws Exception {
		QueryHelper helper = new QueryHelper(CustomerOrganization.class, "co");
		if(customerOrganization!=null)
			helper.addWhereCondition("co.id=?", customerOrganization.getId());
		helper.addOrderByProperty("co.id", false);
		PageBean<CustomerOrganization> pageBean = customerOrganizationService.queryCustomerOrganization(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("customerOrganizationHelper", helper);
		return "list";
	}
	/** 列表*/
	public String list(){
		QueryHelper helper = new QueryHelper(CustomerOrganization.class, "co");
		helper.addOrderByProperty("co.id", false);
		PageBean pageBean = customerOrganizationService.queryCustomerOrganization(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("customerOrganizationHelper", helper);
		return "list";
	}
	
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("customerOrganizationHelper");
		PageBean pageBean = customerOrganizationService.queryCustomerOrganization(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
    public String popup() {  
    	QueryHelper helper = new QueryHelper(CustomerOrganization.class, "c");
    	if(!StringUtils.isEmpty(customerOrganizationName))
    		helper.addWhereCondition("c.name like ?", "%"+customerOrganizationName+"%");
		PageBean<CustomerOrganization> pageBean = customerOrganizationService.getPageBean(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "popup";
		
    }

	/** 删除 */
	public String delete() throws Exception {
		customerOrganizationService.deleteCustomerOrganization(model.getId());
		return freshList();
	}

	/** 添加页面 */
	public String addUI() throws Exception {
		return "saveUI";
	}

	/** 添加 */
	public String add() throws Exception {		
		if (customerOrganizationService.isNameExist(0, model.getName()) == true
				|| customerOrganizationService.isAbbreviationExist(0,
						model.getAbbreviation()) == true) {
			addFieldError("name", "你输入的用户单位名或简称已经存在，请重新输入！");
			return addUI();
		}

		customerOrganizationService.saveCustomerOrganization(model);
		ActionContext.getContext().getValueStack().push(new CustomerOrganization());
		return freshList();
	}

	/** 修改页面 */
	public String editUI() throws Exception {

		// 准备回显的数据
		CustomerOrganization customerOrganization = customerOrganizationService
				.getById(model.getId());
		ActionContext.getContext().getValueStack().push(customerOrganization);
		ActionContext.getContext().put("customerList", customerService.getAllCustomerByOrganization(customerOrganization.getId()));
		if(customerOrganization.getManager()!=null)
			customerId=customerOrganization.getManager().getId();
		
		return "managerUI";
	}

	/** 修改 */
	public String edit() throws Exception {
		if (customerOrganizationService.isNameExist(model.getId(), model.getName()) == true
				|| customerOrganizationService.isAbbreviationExist(model.getId(),
						model.getAbbreviation()) == true) {
			addFieldError("name", "你输入的用户单位名或简称已经存在，请重新输入！");
			return editUI();
		}
		CustomerOrganization customerOrganization = customerOrganizationService
				.getById(model.getId());

		if(customerId>0)
			customerOrganization.setManager(customerService.getById(customerId));
		else
			customerOrganization.setManager(null);
		customerOrganizationService.updateCustomerOrganization(customerOrganization);		
		ActionContext.getContext().getValueStack().push(new CustomerOrganization());
		return freshList();
	}
	
	public String addFinancial(){
		CustomerOrganization customerOrganization=customerOrganizationService.getById(model.getId());
		ActionContext.getContext().getValueStack().push(customerOrganization);
		return "financialDemand";
	}
	
	public String editFinancial(){
		CustomerOrganization customerOrganization = customerOrganizationService
				.getById(model.getId());
		customerOrganization.setFinancialDemand(model.getFinancialDemand());
		customerOrganizationService.updateCustomerOrganization(customerOrganization);
		ActionContext.getContext().getValueStack().push(new CustomerOrganization());
		return freshList();
	}
	
	public CustomerOrganization getModel() {
		return model;
	}

	public boolean isCanDelete() {
		CustomerOrganization customerOrganization = (CustomerOrganization) ActionContext
				.getContext().getValueStack().peek();
		if (customerOrganizationService
				.canDeleteCustomerOrganization(customerOrganization.getId()))
			return true;
		else
			return false;
	}

	

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public CustomerOrganization getCustomerOrganization() {
		return customerOrganization;
	}
	public void setCustomerOrganization(CustomerOrganization customerOrganization) {
		this.customerOrganization = customerOrganization;
	}
	public String getCustomerOrganizationName() {
		return customerOrganizationName;
	}
	public void setCustomerOrganizationName(String customerOrganizationName) {
		this.customerOrganizationName = customerOrganizationName;
	}

}
