package com.yuqincar.action.previlege;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.DriverLicense;
import com.yuqincar.domain.car.TollCharge;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.common.TreeNode;
import com.yuqincar.domain.privilege.Role;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.domain.privilege.UserGenderEnum;
import com.yuqincar.domain.privilege.UserStatusEnum;
import com.yuqincar.domain.privilege.UserTypeEnum;
import com.yuqincar.service.privilege.DepartmentService;
import com.yuqincar.service.privilege.RoleService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class UserAction extends BaseAction implements ModelDriven<User> {
	
	private User model = new User();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private RoleService roleService;
	
	private Long departmentId;
	private Long[] roleIds;
	
	private String oldPassword = "";
	private String newPassword = "";
	
	private String driverOnly;
	private String userSelectorId;
	
	private String licenseID;
	private Date expireDate;
	
	private String actionFlag;
	private int genderId;
	private int userTypeId;
	private int statusId;

	
	/** 查询 */
	public String queryList(){
		QueryHelper helper = new QueryHelper("User", "u");
		if(model.getName()!=null && !"".equals(model.getName()))
			helper.addWhereCondition("u.name like ?", "%"+model.getName()+"%");
		helper.addOrderByProperty("u.name", true);
		PageBean pageBean = userService.getPageBean(pageNum, helper);	
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("userHelper", helper);
		return "list";
	}
	
	/** 列表 */
	public String list(){
		QueryHelper helper = new QueryHelper("User", "u");
		helper.addOrderByProperty("u.id", false);
		PageBean<User> pageBean = userService.getPageBean(pageNum, helper);	
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("userHelper", helper);
		return "list";
	}
	
	public String freshList() throws Exception {
		QueryHelper helper = (QueryHelper)ActionContext.getContext().getSession().get("userHelper");
		PageBean<User> pageBean = userService.getPageBean(pageNum, helper);	
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	public String popup() {
		List<TreeNode> nodes ;
		nodes= userService.getUserTree(model.getName(), (driverOnly!=null && driverOnly.equals("true")) ? true : false);
		Gson gson = new Gson();
		ActionContext.getContext().put("nodes", gson.toJson(nodes));
		ActionContext.getContext().put("userSelectorId", userSelectorId);
		return "popup";
	}

	/** 删除 */
	public String delete() throws Exception {
		userService.delete(model.getId());
		return freshList();
	}

	/** 添加页面 */
	public String addUI() throws Exception {
		ActionContext.getContext().put("actionFlag", actionFlag);
		// 准备数据：departmentList
		ActionContext.getContext().put("departmentList", departmentService.getAll());

		// 准备数据：roleList
		List<Role> roleList = roleService.getAll();
		ActionContext.getContext().put("roleList", roleList);

		return "saveUI";
	}

	/** 添加 */
	public String add() throws Exception {
		if(UserTypeEnum.getById(userTypeId) == UserTypeEnum.DRIVER){
			DriverLicense dl = new DriverLicense();
			dl.setLicenseID(licenseID);
			dl.setExpireDate(expireDate);
			model.setDriverLicense(dl);
		}
		model.setGender(UserGenderEnum.getById(genderId));
		model.setUserType(UserTypeEnum.getById(userTypeId));
		model.setStatus(UserStatusEnum.NORMAL);//默认为正常状态
		model.setDepartment(departmentService.getById(departmentId));
		List<Role> roleList = roleService.getByIds(roleIds);
		model.setRoles(new HashSet<Role>(roleList));
		// 保存到数据库
		userService.save(model);
		ActionContext.getContext().getValueStack().push(new User());
		return freshList();
	}

	/** 修改页面 */
	public String editUI() throws Exception {
		ActionContext.getContext().put("actionFlag", actionFlag);
		// 准备回显的数据
		User user = userService.getById(model.getId());
		if(user.getUserType() == UserTypeEnum.DRIVER){
			licenseID = user.getDriverLicense().getLicenseID();
			expireDate = user.getDriverLicense().getExpireDate();
		}
		ActionContext.getContext().getValueStack().push(user);
		
		// 处理部门
		if (user.getDepartment() != null) {
			departmentId = user.getDepartment().getId();
		}
		// 处理岗位
		roleIds = new Long[user.getRoles().size()];
		int index = 0;
		for (Role role : user.getRoles()) {
			roleIds[index++] = role.getId();
		}

		// 准备数据：departmentList
		ActionContext.getContext().put("departmentList", departmentService.getAll());

		// 准备数据：roleList
		List<Role> roleList = roleService.getAll();
		ActionContext.getContext().put("roleList", roleList);

		return "saveUI";
	}

	/** 修改 */
	public String edit() throws Exception {
		//从数据库中取出原对象
		User user = userService.getById(model.getId());
		User oldUser = userService.getById(model.getId());
		//设置要修改的属性
		user.setLoginName(model.getLoginName());
		user.setName(model.getName());
		user.setBirth(model.getBirth());
		//
		user.setGender(UserGenderEnum.getById(genderId));
		user.setPhoneNumber(model.getPhoneNumber());
		user.setEmail(model.getEmail());
		user.setDescription(model.getDescription());
		user.setStatus(UserStatusEnum.getById(statusId));
		//处理关联的一个部门
		user.setDepartment(departmentService.getById(departmentId));
		//处理关联的多个岗位
		List<Role> roleList = roleService.getByIds(roleIds);
		user.setRoles(new HashSet<Role>(roleList));
		user.setUserType(UserTypeEnum.getById(userTypeId));

		//办公室员工->司机员工,新建DriverLicense
		if(oldUser.getUserType() == UserTypeEnum.OFFICE && user.getUserType() == UserTypeEnum.DRIVER){
			DriverLicense driverLicense = new DriverLicense();
			driverLicense.setLicenseID(licenseID);
			driverLicense.setExpireDate(expireDate);
			user.setDriverLicense(driverLicense);
		}
		//司机员工->司机员工，修改DriverLicense
		if(oldUser.getUserType() == UserTypeEnum.DRIVER && user.getUserType() == UserTypeEnum.DRIVER){
			DriverLicense driverLicense = user.getDriverLicense();
			driverLicense.setLicenseID(licenseID);
			driverLicense.setExpireDate(expireDate);
			user.setDriverLicense(driverLicense);
		}	
		//更新到数据库
		userService.update(user);
		ActionContext.getContext().getValueStack().push(new User());
		return freshList();
	}

	/** 初始化密码为1234 */
	public String initPassword() throws Exception {
		// 1，从数据库中取出原对象
		User user = userService.getById(model.getId());

		// 2，设置要修改的属性
		String md5 = DigestUtils.md5Hex("123456"); // 密码要使用MD5摘要
		user.setPassword(md5);

		// 3，更新到数据库
		userService.update(user);

		return "toList";
	} 
	
	public String info() {
		User user = (User) ActionContext.getContext().getSession().get("user");
		ActionContext.getContext().getValueStack().push(user);
		ActionContext.getContext().put("tabid", 1);		
		return "info";
	}
	
	public String changePassword() {
		User user = (User) ActionContext.getContext().getSession().get("user");
		user = userService.getById(user.getId());
		if(DigestUtils.md5Hex(oldPassword).equals(user.getPassword())) {
			user.setPassword(DigestUtils.md5Hex(newPassword));
			userService.update(user);
			ActionContext.getContext().getSession().put("user", user);
			ActionContext.getContext().put("pass_msg", "修改密码成功！");
		} else {
			ActionContext.getContext().put("pass_msg", "旧密码错误！");
		}
		ActionContext.getContext().put("tabid", 1);

			
		return "info";
	}
	
	public String changePhoneNumber() {
		User user = (User) ActionContext.getContext().getSession().get("user");
		user.setPhoneNumber(model.getPhoneNumber());
		userService.update(user);
		ActionContext.getContext().put("tabid", 2);
		ActionContext.getContext().put("phone_msg", "修改手机号码成功！");

		return "info";
	}
	
	public String detail(){
		User user = (User) ActionContext.getContext().getSession().get("user");
		ActionContext.getContext().getValueStack().push(user);
		ActionContext.getContext().put("tabid", 3);		
		return "info";
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Long[] roleIds) {
		this.roleIds = roleIds;
	}

	public User getModel() {
		return model;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}


	public String getDriverOnly() {
		return driverOnly;
	}


	public void setDriverOnly(String driverOnly) {
		this.driverOnly = driverOnly;
	}


	public String getUserSelectorId() {
		return userSelectorId;
	}


	public void setUserSelectorId(String userSelectorId) {
		this.userSelectorId = userSelectorId;
	}


	public String getLicenseID() {
		return licenseID;
	}


	public void setLicenseID(String licenseID) {
		this.licenseID = licenseID;
	}


	public Date getExpireDate() {
		return expireDate;
	}


	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}


	public String getActionFlag() {
		return actionFlag;
	}


	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}


	public int getUserTypeId() {
		return userTypeId;
	}


	public void setUserTypeId(int userTypeId) {
		this.userTypeId = userTypeId;
	}


	public int getStatusId() {
		return statusId;
	}


	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}


	public int getGenderId() {
		return genderId;
	}


	public void setGenderId(int genderId) {
		this.genderId = genderId;
	}
	
	
}