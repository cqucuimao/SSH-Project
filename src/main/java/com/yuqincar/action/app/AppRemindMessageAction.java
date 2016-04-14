package com.yuqincar.action.app;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.Preparable;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.order.APPRemindMessage;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.app.APPRemindMessageService;
import com.yuqincar.service.app.DriverAPPService;
import com.yuqincar.service.privilege.UserService;

@Controller
@Scope("prototype")
public class AppRemindMessageAction extends BaseAction implements Preparable {
	
	@Autowired
	private DriverAPPService driverAPPService;
	@Autowired
	private UserService userService;
	@Autowired
	private APPRemindMessageService appRemindMessageService;
	
	private String ids[];
	
	private User user;

	public void prepare() throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("pwd");
		user = userService.getByLoginNameAndPassword(username, password);
	}
	public void list() {
		this.writeJson("list");
	}
	
	public void getRemindMessage() {
		if(user == null) {
			this.writeJson("{\"status\":false}");
		}
		List<APPRemindMessage> messages = driverAPPService.getAllUnsendedRemindMessage(user);
		this.writeJson(JSON.toJSONString(messages));
	}
	
	public void confirmMessageReceived() {
		if(user == null || ids == null) {
			this.writeJson("{\"status\":false}");
		}
		for(int i=0;i<ids.length;i++) {
			String id = ids[i];
			if(id!=null || StringUtils.isNumeric(id)) {
				APPRemindMessage message = appRemindMessageService.getById(Long.valueOf(id));
				if(message!=null)
					driverAPPService.setRemindMessageSended(message);
			}
		}
		this.writeJson("{\"status\":true}");
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	
	
}
