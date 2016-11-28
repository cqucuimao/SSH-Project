package com.yuqincar.action.car;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarWash;
import com.yuqincar.domain.car.CarWashShop;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.common.DiskFiles;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.common.UploadedDiskFiles;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.car.CarWashService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.ExcelUtil;
import com.yuqincar.utils.QueryHelper;
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

