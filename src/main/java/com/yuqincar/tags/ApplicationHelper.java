package com.yuqincar.tags;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class ApplicationHelper implements ApplicationContextAware {
	 
    private static ApplicationContext applicationContext;
 
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    	ApplicationHelper.applicationContext = applicationContext;
    }
 
    public static Object getBean(String beanName) {
    	return applicationContext.getBean(beanName);
    }
 
    public static <T> T getBean(String beanName, Class<T> clazs) {
        return clazs.cast(getBean(beanName));
    }
 
}