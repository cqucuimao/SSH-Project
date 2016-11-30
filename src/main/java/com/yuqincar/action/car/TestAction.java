package com.yuqincar.action.car;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.common.UploadedDiskFiles;
@Controller
@Scope("prototype")
public class TestAction extends BaseAction {
	private UploadedDiskFiles uDiskFiles;
	private UploadedDiskFiles uDiskFiles2;

   public String test(){
	   System.out.println("*************in carwash testAction !");
	   if(uDiskFiles!=null)
		   System.out.println("uDiskFiles size: "+uDiskFiles.getDiskFiles().size());
	       
	   if(uDiskFiles2!=null)
		   System.out.println("uDiskFiles2 size: "+uDiskFiles2.getDiskFiles().size());
	  return "test";
   }
   
    public UploadedDiskFiles getuDiskFiles2() {
		return uDiskFiles2;
	}
	public void setuDiskFiles2(UploadedDiskFiles uDiskFiles2) {
		this.uDiskFiles2 = uDiskFiles2;
	}
	public UploadedDiskFiles getuDiskFiles() {
		return uDiskFiles;
	}
	public void setuDiskFiles(UploadedDiskFiles uDiskFiles) {
		this.uDiskFiles = uDiskFiles;
	}
   
	
}	

