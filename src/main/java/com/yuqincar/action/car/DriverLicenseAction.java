package com.yuqincar.action.car;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.DriverLicense;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.domain.privilege.UserStatusEnum;
import com.yuqincar.domain.privilege.UserTypeEnum;
import com.yuqincar.service.car.DriverLicenseService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.QueryHelper;
import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
public class DriverLicenseAction extends BaseAction implements ModelDriven<User> {

	private User model=new User();
	
	private String actionFlag;
	private Date expireDate;
	
	@Autowired
	private DriverLicenseService driverLicenseService;
	
	@Autowired
	private UserService userService;
	
	/**列表*/
	public String list(){
		QueryHelper helper = new QueryHelper(User.class, "u");
		helper.addWhereCondition("u.userType=?", UserTypeEnum.DRIVER);
		helper.addWhereCondition("u.status=?", UserStatusEnum.NORMAL);
		helper.addOrderByProperty("u.driverLicense.expireDate", true);
		PageBean pageBean = userService.getPageBean(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("driverLicenseHelper", helper);
		return "list";
	}
	
	/**更新列表*/
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("driverLicenseHelper");
		PageBean pageBean = userService.getPageBean(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	/** 修改页面 */
	public String editUI() throws Exception {
		ActionContext.getContext().put("actionFlag", actionFlag);
		// 准备回显的数据
		User u = userService.getById(model.getId());
		ActionContext.getContext().getValueStack().push(u);
		
		if (u.getDriverLicense()!= null) {
			expireDate = u.getDriverLicense().getExpireDate();
		}
		return "saveUI";
	}
	
	/** 修改*/
	public String edit() throws Exception {
		if(model.getDriverLicense().getExpireDate()==null)
		{
			addFieldError("expireDate", "驾照到期日期为必填项！");
			return editUI();
		}
		//从数据库中取出原对象
		DriverLicense dl =userService.getById(model.getId()).getDriverLicense();
		
		dl.setExpireDate(model.getDriverLicense().getExpireDate());
		
		//更新到数据库
		driverLicenseService.updateDriverLicense(dl);
		ActionContext.getContext().getValueStack().push(new DriverLicense());
		return freshList();
	}
	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public User getModel() {
		return model;
	}

	public void setModel(User model) {
		this.model = model;
	}

	
}
