package com.yuqincar.token;

import java.util.HashMap;
import java.util.Map;
import com.yuqincar.utils.HttpMethod;

/**
 * 获取token值，使用期限30天
 * @author cocoa
 *
 */
public class MessageToken {
	public static void main(String[] args) {
		Map<String,String> params = new HashMap<String,String>();
		
		String url = "https://oauth.api.189.cn/emp/oauth2/v3/access_token";
		params.put("grant_type", "client_credentials");
		params.put("app_id", "593884530000249253");
		params.put("app_secret", "ba1a8529eef141b3d5b631d998f33fd0");
		
		String json = HttpMethod.post(url, params);
		System.out.println(json);
		// token = d49cdd0c70071ddaa1a7c1c92b86a0181456484895955 "expires_in":2592000
	}
}
