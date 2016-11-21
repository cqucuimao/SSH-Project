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
		   System.out.println("in car ****************************222222");
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
		   
//		   ArrayList<File> uploads=new ArrayList<File>();
//		   ArrayList<DiskFile> diskFiles2=new ArrayList<DiskFile>();
//		   diskfiles=new DiskFiles();
//		   if ((ArrayList<File>)ActionContext.getContext().getSession().get("uploadLists")!=null) 
//		   {
//			   uploads=(ArrayList<File>)ActionContext.getContext().getSession().get("uploadLists");
//			   
//			   for(int i=0;i<uploads.size();i++)
//			   {
//				    File file=uploads.get(i);
//				    FileInputStream fis = null;
//					try 
//					{
//						fis = new FileInputStream(file);
//					} 
//					catch (FileNotFoundException e) 
//					{
//						e.printStackTrace();
//					}
//					 DiskFile diskfile=diskFileService.insertDiskFile(fis, file.getName());
//					 diskFiles2.add(diskfile);
//					try {
//						fis.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					
//			   }
//			    diskfiles.setDiskFiles(diskFiles2);
//			    for(int j=0;j<uploads.size();j++)
//			    {
//			    	File file=uploads.get(j);
//			    	file.delete();
//			    }
//			    ActionContext.getContext().getSession().remove("uploadLists");
//			    return diskfiles;   
//		}
//		
//		return null;
    }  
  
    public String convertToString(Map context, Object o) {
    	
    		return "";  
    }  
}
