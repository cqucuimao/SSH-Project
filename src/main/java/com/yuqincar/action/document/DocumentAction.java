package com.yuqincar.action.document;

import org.activiti.engine.ProcessEngine;
import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itextpdf.text.Document;
import com.yuqincar.action.common.BaseAction;


@Controller
@Scope("prototype")
public class DocumentAction extends BaseAction {


	@Autowired
	private ProcessEngine  processEngine;
	
	public String category(){
		return "category";
	}
	
	public void testProcessEngine(){
		System.out.println("ProcessEngine="+processEngine);
	}
	
}



