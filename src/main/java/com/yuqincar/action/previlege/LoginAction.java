package com.yuqincar.action.previlege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.inject.Inject;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.privilege.PrivilegeService;
import com.yuqincar.service.privilege.UserService;

@Controller
@Scope("prototype")
public class LoginAction extends BaseAction{
 
	@Inject("struts.devMode")
	private String devMode;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PrivilegeService privilegeService;
	
	private String loginName;
	
	private String password;
	
	private String validationCode;
	
	
	/** 登录页面 */
	public String loginUI() throws Exception {
		System.out.println("in loginUI");
		return "loginUI";
	}

	/** 登录 */
	public String login() throws Exception {
		System.out.println("in login");
		System.out.println("1");
		User user;
		if(devMode.equals("false")){
			System.out.println("2");
			if(!validationCode.equals(ActionContext.getContext().getSession().get("securityCode"))){
				addFieldError("loginError", "验证码不正确！");
				return "loginUI";
			}	
			// 验证用户名与密码，如果正确就返回这个用户，否则返回null
			user = userService.getByLoginNameAndPassword(loginName, password);
		}else{
			System.out.println("2");
			user=userService.getByLoginName(loginName);
		}

		System.out.println("3");
		// 如果登录名或密码不正确，就转回到登录页面并提示错误消息
		if (user == null) {
			System.out.println("4");
			addFieldError("loginError", "登录名或密码不正确！");
			return "loginUI";
		}
		// 如果登录名与密码都正确，就登录用户
		else {
			System.out.println("5");
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
	
	public String test() {
		return "123";
	}

	public String getValidationCode() {
		return validationCode;
	}

	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}	

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isDevMode(){
		System.out.println("in isDevMode");
		return devMode.equals("true");
	}
}
