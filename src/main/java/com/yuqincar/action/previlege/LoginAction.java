package com.yuqincar.action.previlege;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.inject.Inject;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.privilege.PrivilegeService;
import com.yuqincar.service.privilege.UserService;

@Controller
@Scope("prototype")
public class LoginAction extends BaseAction implements ModelDriven<User>{
 
	@Inject("struts.devMode")
	private String devMode;
	private User model = new User();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PrivilegeService privilegeService;
	
	
	/** 登录页面 */
	public String loginUI() throws Exception {
		System.out.println("in loginUI");
		return "loginUI";
	}

	/** 登录 */
	public String login() throws Exception {
		System.out.println(devMode);
		System.out.println("model.getName:"+model.getLoginName());
	
		// 验证用户名与密码，如果正确就返回这个用户，否则返回null
		User user = userService.getByLoginNameAndPassword(model.getLoginName(), model.getPassword());
		
		// 如果登录名或密码不正确，就转回到登录页面并提示错误消息
		if (user == null) {
			addFieldError("loginError", "登录名或密码不正确！");
			return "loginUI";
		}
		// 如果登录名与密码都正确，就登录用户
		else {
			ActionContext.getContext().getSession().put("user", user);
			ActionContext.getContext().getSession().put("userPrivileges", privilegeService.getUserPrivilegeUrls(user));
			return "toHome";
		}
	}

	/** 注销 */
	public String logout() throws Exception {
		System.out.println("in logout");
		ActionContext.getContext().getSession().remove("user");
		return "logout";
	}

	public User getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	public String test() {
		return "123";
	}

}
