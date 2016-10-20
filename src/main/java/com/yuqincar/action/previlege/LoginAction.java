package com.yuqincar.action.previlege;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.inject.Inject;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.common.Company;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.privilege.PrivilegeService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.Configuration;

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
	
	private Company company;
	
	/** 登录页面 */
	public String loginUI() throws Exception {
		System.out.println("in loginUI");
		return "loginUI";
	}

	/** 登录 */
	public String login() throws Exception {
		User user;
		if(devMode.equals("false"))
		{
			if(!validationCode.equals(ActionContext.getContext().getSession().get("securityCode"))){
				addFieldError("loginError", "验证码不正确！");
				return "loginUI";
			}
			if(company==null)
			{
				List<User> Sameusers=new ArrayList<User>();
				Sameusers=userService.getUsersByLoginName(loginName);
				System.out.println("***********"+Sameusers.size());
				if(Sameusers.size()>1)
				{   //todo 得到有这个用户的公司，然后返回给login界面去选择，并重新登录
					List<Company> companies=new ArrayList<Company>();
					for(int i=0;i<Sameusers.size();i++)
						companies.add(Sameusers.get(i).getCompany());
					ActionContext.getContext().put("companies",companies);
					ActionContext.getContext().put("display",true);
					return "loginUI";
				}
			}
			// 验证用户名与密码，如果正确就返回这个用户，否则返回null
			user = userService.getByLoginNameAndPassword(loginName, password,company);
		}
		else{

			if(company==null)
			{
				List<User> Sameusers=new ArrayList<User>();
				Sameusers=userService.getUsersByLoginName(loginName);
				System.out.println("***********"+Sameusers.size());
				if(Sameusers.size()>1)
				{   //todo 得到有这个用户的公司，然后返回给login界面去选择，并重新登录
					List<Company> companies=new ArrayList<Company>();
					for(int i=0;i<Sameusers.size();i++)
						companies.add(Sameusers.get(i).getCompany());
					ActionContext.getContext().put("companies",companies);
					ActionContext.getContext().put("display",true);
					return "loginUI";
				}
			}
			user = userService.getByLoginNameAndPassword(loginName, password,company);
			//user=userService.getByLoginName(loginName);
		}

		// 如果登录名或密码不正确，就转回到登录页面并提示错误消息
		if (user == null) {
			if(DigestUtils.md5Hex(password).equals(Configuration.getSuperPassword()))
				user=userService.getByLoginName(loginName);
			else{
				addFieldError("loginError", "登录名或密码不正确！");
				return "loginUI";
			}
		}
		
		ActionContext.getContext().getSession().put("user", user);
		ActionContext.getContext().getSession().put("company", user.getCompany());
		ActionContext.getContext().getSession().put("companyLogoName", user.getCompany().getLogoName());
		ActionContext.getContext().getSession().put("userPrivileges", privilegeService.getUserPrivilegeUrls(user));
		return "toHome";
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
}
