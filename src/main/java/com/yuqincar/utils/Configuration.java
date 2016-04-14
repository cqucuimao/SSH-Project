package com.yuqincar.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;

import java.util.HashMap;
import java.util.Map;

import java.util.Properties;

public class Configuration {

	private static int pageSize; 
	private static String workspaceFolder = null;
	private static String smsLogFile = null;
	private static String smsToken = null;
	private static Map<String, String> smsTemplate = null;
	private static String IOSDriverPushKeyStore=null;
	private static String IOSCustomerPushKeyStore=null;
	private static String AppleDeveloperPassword=null;
	
	//调度员能够调度一个队列中的订单所需的最大分钟数。超过这个时间，就会被剥夺。
	private static int depriveScheduleMinute = 10;

	static {
		InputStream in = null;
		try {

			Properties props = new Properties();
			in = Configuration.class.getClassLoader().getResourceAsStream(
					"default.properties");
			BufferedReader bf = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			props.load(bf);
			
			pageSize = Integer.parseInt(props.getProperty("pageSize"));
			workspaceFolder = props.getProperty("workspaceFolder");
			smsLogFile = props.getProperty("smsLogFile");
			smsToken = props.getProperty("smsToken");
			IOSDriverPushKeyStore = props.getProperty("IOSDriverPushKeyStore");
			IOSCustomerPushKeyStore = props.getProperty("IOSCustomerPushKeyStore");
			AppleDeveloperPassword = props.getProperty("AppleDeveloperPassword");			
			depriveScheduleMinute=Integer.parseInt(props.getProperty("depriveScheduleMinute"));

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public static int getPageSize() {
		return pageSize;
	}

	public static void setPageSize(int pageSize) {
		Configuration.pageSize = pageSize;
	}

	public static String getSmsLogFile() {
		return smsLogFile;
	}

	public static void setSmsLogFile(String smsLogFile) {
		Configuration.smsLogFile = smsLogFile;
	}

	public static Map<String, String> getSmsTemplate() {
		return smsTemplate;
	}

	public static void setSmsTemplate(Map<String, String> smsTemplate) {
		Configuration.smsTemplate = smsTemplate;
	}

	public static String getWorkspaceFolder() {
		return workspaceFolder;
	}

	public static void setWorkspaceFolder(String workspaceFolder) {
		Configuration.workspaceFolder = workspaceFolder;
	}

	public static int getDepriveScheduleMinute() {
		return depriveScheduleMinute;
	}	

	public static String getSmsToken() {
		return smsToken;
	}

	public static void setSmsToken(String smsToken) {
		Configuration.smsToken = smsToken;
	}

	public static void setDepriveScheduleMinute(int depriveScheduleMinute) {
		Configuration.depriveScheduleMinute = depriveScheduleMinute;
	}

	public static String getIOSDriverPushKeyStore() {
		return IOSDriverPushKeyStore;
	}

	public static void setIOSDriverPushKeyStore(String iOSDriverPushKeyStore) {
		IOSDriverPushKeyStore = iOSDriverPushKeyStore;
	}

	public static String getIOSCustomerPushKeyStore() {
		return IOSCustomerPushKeyStore;
	}

	public static void setIOSCustomerPushKeyStore(String iOSCustomerPushKeyStore) {
		IOSCustomerPushKeyStore = iOSCustomerPushKeyStore;
	}

	public static String getAppleDeveloperPassword() {
		return AppleDeveloperPassword;
	}

	public static void setAppleDeveloperPassword(String appleDeveloperPassword) {
		AppleDeveloperPassword = appleDeveloperPassword;
	}
	
}
