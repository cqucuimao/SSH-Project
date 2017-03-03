package com.yuqincar.action.car;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
@Controller
@SuppressWarnings("serial")
public class DiskFileAction extends ActionSupport {
	
	private File uploadify;//上传文件file对象
	private String uploadifyFileName;//上传文件名
	private String uploadifyContentType;//上传文件类型
	private String description;//上传文件的描述
	private String uploadDir;//保存上传文件的目录,相对于web应用程序的根路径,在struts.xml文件中配置
	private String diskName;
	private String sessionName;
	public String getDiskName() {
		return diskName;
	}
	public void setDiskName(String diskName) {
		this.diskName = diskName;
	}

	public String execute(){
		System.out.println("11111111111111");
		sessionName="diskFile_"+diskName;
		ArrayList<File> uploadLists=new ArrayList<File>();
		String newFileName=null;
		//得到当前时间开始流逝的毫秒数,将这个毫秒数作为上传文件新的文件名
		long now=new Date().getTime();
		//得到保存上传文件的真实路径
		//String path=ServletActionContext.getServletContext().getRealPath(uploadDir);
		//String path=ServletActionContext.getServletContext().getRealPath("d://files");
		File dir=new File("d:\\files");
		//如果这个目录不存在,则创建它
		if (!dir.exists()) {
			dir.mkdir();
		}
		int index=uploadifyFileName.lastIndexOf(".");
		
		//判断上传文件是否有扩展名,以时间戳作为新的文件名
		if (index!=-1) {
			newFileName=now+uploadifyFileName.substring(index);
			//newFileName=uploadifyFileName;
			//System.out.println(uploadifyFileName+"***********");
		}else {
			newFileName=Long.toString(now);
		}
		BufferedOutputStream bos=null;
		BufferedInputStream bis=null;
		
		//读取保存在临时目录下的上传文件,写入到新的文件中
		try {
			FileInputStream fis=new FileInputStream(uploadify);
			bis=new BufferedInputStream(fis);
			
			File file=new File(dir,newFileName);
			FileOutputStream fos=new FileOutputStream(file);
			bos=new BufferedOutputStream(fos);
			
			byte [] buf=new byte[4096];
			int len=-1;
			while ((len=bis.read(buf))!=-1) {
				bos.write(buf,0,len);
			}
			System.out.println("********sessionName in diskFile: "+sessionName);
			/*if(first.equals("first"))
			{
				 //System.out.println("**********000000000: "+first);
				if((ArrayList<File>)ActionContext.getContext().getSession().get(sessionName)!=null)
				{
					List<File> fileList=(List<File>)ActionContext.getContext().getSession().get(sessionName); 
					for(File filed:fileList)
						filed.delete();
					ActionContext.getContext().getSession().remove(sessionName);
					System.out.println("我上次上传 了，但是没有提交！所以要删除了");
				}
			}*/
			
			if((ArrayList<File>)ActionContext.getContext().getSession().get(sessionName)==null)
			{
				ActionContext.getContext().getSession().put(sessionName,uploadLists);		
			}
			uploadLists=(ArrayList<File>)ActionContext.getContext().getSession().get(sessionName);
			uploadLists.add(file);
			ActionContext.getContext().getSession().put(sessionName,uploadLists);
			for(int i=0;i<uploadLists.size();i++)
				System.out.println("*******"+uploadLists.get(i));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (null!=bis) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null!=bos) {
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public File getUploadify() {
		return uploadify;
	}

	public void setUploadify(File uploadify) {
		this.uploadify = uploadify;
	}

	public String getUploadifyFileName() {
		return uploadifyFileName;
	}

	public void setUploadifyFileName(String uploadifyFileName) {
		this.uploadifyFileName = uploadifyFileName;
	}

	public String getUploadifyContentType() {
		return uploadifyContentType;
	}

	public void setUploadifyContentType(String uploadifyContentType) {
		this.uploadifyContentType = uploadifyContentType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}
	
	
}
