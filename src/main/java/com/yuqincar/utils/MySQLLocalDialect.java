package com.yuqincar.utils;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;

public class MySQLLocalDialect extends MySQL5InnoDBDialect {
	public MySQLLocalDialect(){  
		super();   
		registerFunction("convert_gbk", new SQLFunctionTemplate(Hibernate.STRING, "convert(?1 using gbk)"));  
	}  
}
