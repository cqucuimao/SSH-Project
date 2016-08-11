package com.yuqincar.action.app;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.Preparable;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.common.VerificationCode;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.common.VerificationCodeService;
import com.yuqincar.service.privilege.PrivilegeService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.service.sms.impl.SMSServiceImpl;
import com.yuqincar.utils.RandomUtil;
@Controller
@Scope("prototype")
public class AppUserAction extends BaseAction implements Preparable{
	
	@Autowired
	private UserService userService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private VerificationCodeService codeService;
	@Autowired
	private SMSService smsService;
	
	private User user;
	
	private String newPwd;
	
	private String phoneNumber;
	
	private String userCode;

	public void prepare() throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("pwd");
		user = userService.getByLoginNameAndMD5Password(username, password);
		//本Action主要用于司机APP，但是有getSMSCode方法同时被司机APP和下单APP使用。当下单APP使用getSMSCode方法时，不需要user对象。
		//所以此处判断是否拥有司机APP功能。不会与下单APP冲突。
		if(user!=null && !privilegeService.canUserHasPrivilege(user, "/driver_app"))
			user=null;
	}

	
	public void login() {
		if(user==null) {
			this.writeJson("{\"status\":false}");
		}
		else {
			this.writeJson("{\"status\":true}");
		}
	}
	
	public void changePassword() {
		if(user==null || newPwd == null || newPwd.equals("") || newPwd.length()<6) {
			this.writeJson("{status:false}");
		}
		String md5 = DigestUtils.md5Hex(newPwd);
		System.out.println(md5);
		user.setPassword(md5);
		userService.update(user);
		this.writeJson("{\"status\":true}");
	}
	
	public void changePhoneNumber() {
		if(user==null || phoneNumber==null || !StringUtils.isNumeric(phoneNumber)) {
			this.writeJson("{status:false}");
		}
		user.setPhoneNumber(phoneNumber);
		userService.update(user);
		this.writeJson("{\"status\":true}");
	}
	
	public void getSMSCode() {		
		if(phoneNumber==null || !StringUtils.isNumeric(phoneNumber)){
			this.writeJson("{\"status\":false}");
			return;
		}
		String code =  RandomUtil.randomFor6();
		Map<String,String> map = new HashMap();
		map.put("param1", "手机");
		map.put("param2", code);
		String result = smsService.sendTemplateSMS(phoneNumber,SMSService.SMS_TEMPLATE_VERFICATION_CODE,map);
		
		Gson gson = new Gson();
		Map<String,String> jsonMap = gson.fromJson(result,new TypeToken<Map<String, String>>() {
		}.getType());
		System.out.println(result);
		if(jsonMap.get("res_message").equals("Success")) {
			VerificationCode sendCode = codeService.getByPhoneNumber(phoneNumber);
			if(sendCode!=null){
				//更新验证码
				System.out.println("update verificationCode!");
				code=DigestUtils.md5Hex(code);
				sendCode.setCode(code);
				codeService.update(sendCode);
			}else{
				//保存验证码
				VerificationCode vcode = new VerificationCode();
				code=DigestUtils.md5Hex(code);
				vcode.setCode(code);
				vcode.setPhoneNumber(phoneNumber);
				codeService.save(vcode);
			}
			this.writeJson("{\"status\":true}");
		} else  {
			this.writeJson("{\"status\":false}");
		}
	}

	public void verifiySMSCode() {
		if(userCode==null || phoneNumber==null || !StringUtils.isNumeric(phoneNumber)) {
			this.writeJson("{\"status\":false}");
		}
		
		VerificationCode sendCode = codeService.getByPhoneNumber(phoneNumber);
		
		if(sendCode !=null &&  sendCode.getCode().equals(userCode)) {
			this.writeJson("{\"status\":true}");
		} else {
			this.writeJson("{\"status\":false}");
		}
	}
	
	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getUserCode() {
		return userCode;
	}


	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}


	
	
}
