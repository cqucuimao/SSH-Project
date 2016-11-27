package com.yuqincar.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.struts2.util.StrutsTypeConverter;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.common.UploadedDiskFiles;
import com.yuqincar.service.common.DiskFileService;

public class DiskFileConverter extends StrutsTypeConverter {
	
	@Autowired
	DiskFileService diskFileService;
	
	private UploadedDiskFiles diskfiles;
	
	@SuppressWarnings("unchecked")
	public Object convertFromString(Map context, String[] values, Class toClass) { 
		   List<File> fileList=(List<File>)ActionContext.getContext().getSession().get("uploadLists"); 
		   List<DiskFile> diskFileList=new ArrayList<DiskFile>(fileList.size());
		   diskfiles=new UploadedDiskFiles();
		   for(File file:fileList){
			   DiskFile diskfile=null;
			   try{
				   diskfile=diskFileService.insertDiskFile(new FileInputStream(file), file.getName());
			   }catch(Exception e){
				   e.printStackTrace();
			   }
			   diskFileList.add(diskfile);
		   }
		   diskfiles.setDiskFiles(diskFileList);
		   for(int j=0;j<fileList.size();j++)
			{
			    	File file=fileList.get(j);
			    	file.delete();
			}
			ActionContext.getContext().getSession().remove("uploadLists");
		   return diskfiles;
		   
    }  
  
    public String convertToString(Map context, Object o) {
    	
    		return "";  
    }  
}
