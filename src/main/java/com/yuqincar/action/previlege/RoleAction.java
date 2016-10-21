package com.yuqincar.action.previlege;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.Privilege;
import com.yuqincar.domain.privilege.Role;
import com.yuqincar.service.privilege.PrivilegeService;
import com.yuqincar.service.privilege.RoleService;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class RoleAction extends BaseAction implements ModelDriven<Role> {

	private Role model = new Role();
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private PrivilegeService privilegeService;
	
	private Long[] privilegeIds;

	/** 列表 */
	public String list() throws Exception {
		QueryHelper helper = new QueryHelper(Role.class, "r");

		if (model.getName() != null && !"".equals(model.getName()))
			helper.addWhereCondition("r.name like ?", "%"+model.getName()+"%");
			helper.addWhereCondition("company is null");
		PageBean pageBean = roleService.getPageBean(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}

	/** 删除 */
	public String delete() throws Exception {
		roleService.delete(model.getId());
		return "toList";
	}

	/** 添加页面 */
	public String addUI() throws Exception {
		return "saveUI";
	}

	/** 添加 */
	public String add() throws Exception {
		// 保存到数据库
		roleService.save(model);

		return "toList";
	}

	/** 修改页面 */
	public String editUI() throws Exception {
		// 准备要回显的数据
		Role role = roleService.getById(model.getId());
		ActionContext.getContext().getValueStack().push(role);
		return "saveUI";
	}

	/** 修改 */
	public String edit() throws Exception {
		// 1，从数据库中获取要修改的原始对象
		Role role = roleService.getById(model.getId());

		// 2, 设置要修改的属性
		role.setName(model.getName());
		role.setDescription(model.getDescription());

		// 3，更新到数据库
		roleService.update(role);

		return "toList";
	}

	/** 设置权限页面 */
	public String setPrivilegeUI() throws Exception {
		// 准备回显得全部角色数据
		List<Privilege> privilegeList = privilegeService.getAll();
		ActionContext.getContext().put("privilegeList", privilegeList);

		// 准备要回显的角色数据
		Role role = roleService.getById(model.getId());
		ActionContext.getContext().getValueStack().push(role);

		// 准备回显的权限数据
		privilegeIds = new Long[role.getPrivileges().size()];
		int index = 0;
		for (Privilege p : role.getPrivileges()) {
			privilegeIds[index++] = p.getId();
		}

		return "setPrivilegeUI";
	}

	/** 设置权限 */
	public String setPrivilege() throws Exception {
		// 1，从数据库中获取要修改的原始对象
		Role role = roleService.getById(model.getId());

		// 2, 设置要修改的属性
		List<Privilege> privilegeList = privilegeService.getByIds(privilegeIds);
		role.setPrivileges(new HashSet<Privilege>(privilegeList));
		
		// 3，更新到数据库
		roleService.update(role);

		return "toList";
	}


	public Long[] getPrivilegeIds() {
		return privilegeIds;
	}

	public void setPrivilegeIds(Long[] privilegeIds) {
		this.privilegeIds = privilegeIds;
	}

	public Role getModel() {
		// TODO Auto-generated method stub
		return model;
	}

}
