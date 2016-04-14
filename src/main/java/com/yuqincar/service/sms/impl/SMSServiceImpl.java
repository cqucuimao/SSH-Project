package com.yuqincar.service.sms.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.Configuration;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.HttpInvoker;
import com.yuqincar.utils.RandomUtil;

@Service
public class SMSServiceImpl implements SMSService {
		
	private static String SMS_GATE_URL="http://api.189.cn/v2/emp/templateSms/sendSms";
	private static String APP_ID = "593884530000249253";//应用ID------登录平台在应用设置可以找到
	private static String APP_SECRET = "ba1a8529eef141b3d5b631d998f33fd0";//应用secret-----登录平台在应用设置可以找到
	
	private void sendSMSToFile(String phoneNumber, String templateId, String content) {
		File file = new File(Configuration.getSmsLogFile());
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(file, true));
			pw.print(phoneNumber);
			pw.println();
			pw.print(templateId);
			pw.println();
			pw.println(content);
			pw.println();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pw != null)
				pw.close();
		}
	}

	public String sendTemplateSMS(String phoneNumber,String templateId, Map<String, String> params) {
		Gson gson = new Gson();
		String template_param = gson.toJson(params);
		System.out.println(template_param);
		
		//sendSMSToFile(phoneNumber,templateId,template_param);
		
		String postEntity;
		try {
			postEntity = "app_id=" + APP_ID + "&access_token="
					+ Configuration.getSmsToken() + "&acceptor_tel=" + phoneNumber + "&template_id="
					+ templateId + "&template_param=" + template_param
					+ "&timestamp=" + URLEncoder.encode(DateUtils.getYMDHMSString(new Date()), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			throw new RuntimeException(e1);
		}
		String resJson = "";
		try {
			resJson = HttpInvoker.httpPost1(SMS_GATE_URL, null, postEntity);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return resJson;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String result = "";
		try {
			Map<String,String> map = new HashMap();
			map.put("param1", "手机");
			map.put("param2", RandomUtil.randomFor6());
			map.put("param3","3" );
			result = new SMSServiceImpl().sendTemplateSMS("13883101475",SMSService.SMS_TEMPLATE_VERFICATION_CODE,map);
			System.out.println(result);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
