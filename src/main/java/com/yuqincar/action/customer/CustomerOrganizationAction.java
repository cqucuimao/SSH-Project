package com.yuqincar.action.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.service.customer.CustomerService;
import com.yuqincar.service.order.OrderService;
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
	
	/** 列表 */
	public String list() throws Exception {
		QueryHelper helper = new QueryHelper(CustomerOrganization.class, "co");

		if (model.getName() != null && !"".equals(model.getName()))
			helper.addWhereCondition("co.name like ?", "%" + model.getName()
					+ "%");

		PageBean<CustomerOrganization> pageBean = customerOrganizationService
				.queryCustomerOrganization(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("customerOrganizationHelper", helper);
		return "list";
	}
	    
    public String popup() {  
    	System.out.println("in customerOrganization popup");
    	QueryHelper helper = new QueryHelper(CustomerOrganization.class, "c");		
		PageBean<CustomerOrganization> pageBean = customerOrganizationService.getPageBean(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "popup";
		
    }

	/** 删除 */
	public String delete() throws Exception {
		customerOrganizationService.deleteCustomerOrganization(model.getId());
		return "toList";
	}

	/** 添加页面 */
	public String addUI() throws Exception {
		return "saveUI";
	}

	/** 添加 */
	public String add() throws Exception {
		customerOrganizationService.saveCustomerOrganization(model);
		return "toList";
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
				&& customerOrganizationService.isAbbreviationExist(model.getId(),
						model.getAbbreviation()) == true) {
			addFieldError("name", "你输入的用户单位名或简称已经存在，请重新输入！");
			return "managerUI";
		}
		
		CustomerOrganization customerOrganization = customerOrganizationService
				.getById(model.getId());

		if(customerId>0){
			customerOrganization.setManager(customerService.getById(customerId));
		}
		customerOrganizationService.updateCustomerOrganization(customerOrganization);
		return "toList";
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

	public String customerOrganization(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("customerOrganizationHelper");
		PageBean pageBean = customerOrganizationService.queryCustomerOrganization(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}

}
