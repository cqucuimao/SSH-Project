package com.yuqincar.utils;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class SMSToken {

	public static String getSMSToken() {
		Map<String,String> params = new HashMap<String,String>();
		
		String url = "https://oauth.api.189.cn/emp/oauth2/v3/access_token";
		params.put("grant_type", "client_credentials");
		params.put("app_id", "593884530000249253");
		params.put("app_secret", "ba1a8529eef141b3d5b631d998f33fd0");
		
		String str = HttpMethod.post(url, params);
		//System.out.println(str);
		JSONObject json = JSONObject.fromObject(str);
		String token = json.getString("access_token");
		System.out.println("token="+token);
		/**{"res_code":"0",
		 * 	  "res_message":"Success",
		 *	  "access_token":"cac0fe969bf628b0b8b16afdfd0e517c1474371427613",
		 * 	  "expires_in":2452765}
		 */
		return token;
	}
}
