package com.yuqincar.action.previlege;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.Department;
import com.yuqincar.service.privilege.DepartmentService;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class DepartmentAction extends BaseAction implements ModelDriven<Department> {

	private Department model = new Department();
	
	@Autowired
	private DepartmentService departmentService;
	
	public String popup() {
		
		List<Department> depts = departmentService.getAll();
		ActionContext.getContext().put("depts", depts);
		return "popup";
	}
	
	public String list() throws Exception {
		QueryHelper helper = new QueryHelper(Department.class, "d");

		if (model.getName() != null && !"".equals(model.getName()))
			helper.addWhereCondition("d.name like ?", "%"+model.getName()+"%");
		
		PageBean pageBean = departmentService.getPageBean(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}

	public String delete() throws Exception {
		departmentService.delete(model.getId());
		return "toList";
	}

	public String addUI() throws Exception {
		return "saveUI";
	}

	public String add() throws Exception {
		// 保存到数据库
		departmentService.save(model);
		return "toList";
	}

	public String editUI() throws Exception {
		// 准备回显的数据
		Department department = departmentService.getById(model.getId()); // 当前要修改的部门
		ActionContext.getContext().getValueStack().push(department);
		return "saveUI";
	}

	/** 列表 */
	public String edit() throws Exception {
		// 1，从数据库中取出要修改的原始数据
		Department department = departmentService.getById(model.getId());

		// 2，设置要修改的属性
		department.setName(model.getName());
		department.setDescription(model.getDescription());

		// 3，更新到数据库
		departmentService.update(department);
		return "toList";
	}
	//判断部门是否能删除
	public boolean isCanDeleteDepartment(){
		Department department = (Department) ActionContext.getContext().getValueStack().peek();
		if(departmentService.canDeleteDepartment(department.getId()))
			return true;
		else 
			return false;
	}

	public Department getModel() {
		// TODO Auto-generated method stub
		return model;
	}

}
