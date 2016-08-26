package com.yuqincar.utils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.yuqincar.domain.privilege.User;

public class CheckPrivilegeInterceptor extends AbstractInterceptor {
	 
	public String intercept(ActionInvocation invocation) throws Exception {
		User user = (User) ActionContext.getContext().getSession().get("user");

		// 当前访问的URL
		String namespace = invocation.getProxy().getNamespace();
		String actionName = invocation.getProxy().getActionName();
		if (null == namespace || "".equals(namespace)) {
			namespace = "/";
		}
		if (!namespace.endsWith("/")) {
			namespace += "/";
		}
		String url = namespace + actionName;

		// 如果用户未登录，就转到登录页面
		if (user == null) {
			if (url.startsWith("/login_login")) {
				// 如果当前访问的是登录功能，就放行
				return invocation.invoke();
			} else {
				// 如果当前访问的不是登录功能，就转到登录页面
				return "loginUI";
			}
		}
		else {
			if(user.hasPrivilegeByUrl(url)){
				return invocation.invoke();
			} else {
				return "noPrivilegeUI";
			}
		}
	}
}
