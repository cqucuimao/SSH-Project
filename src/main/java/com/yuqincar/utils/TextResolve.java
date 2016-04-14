package com.yuqincar.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class TextResolve {
	public static final String BASE = "com.yuqincar.domain.";

	/**
	 * 获取属性对应的中文值
	 * 
	 * @param property
	 *            privilege.user.name
	 */
	public static String getText(String qualify) {
		String names[] = qualify.split("\\.");

		if (names.length < 2) { // 参数不够
			return null;
		}

		String propertyName = names[names.length - 1];

		StringBuffer className = new StringBuffer(BASE);
		className.append(qualify.substring(0, qualify.lastIndexOf('.')));
		Class clazz = null;
		try {
			clazz = Class.forName(className.toString());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		Field field;
		try {
			field = clazz.getDeclaredField(propertyName);
			if (field.isAnnotationPresent(Text.class)) {
				Text text = field.getAnnotation(Text.class);
				return text.value();
			}
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return null;

	}

	public static void main(String[] args) {
		TextResolve tr = new TextResolve();
		String text = tr.getText("privilege.User.name");
		System.out.println(text);
	}
}
