package com.yuqincar.action.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.app.DriverAPPService;
import com.yuqincar.service.common.DiskFileService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;

@Controller
@Scope("prototype")
public class AppOrderSignatureAction extends BaseAction{

	@Autowired
	private DriverAPPService driverAPPService;
	@Autowired
	private UserService userService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private DiskFileService diskFileService;
	
	private String username;
	private String pwd;
	private long companyId;
	
	private User user = null;
	private Order order = null;
	
	private Long orderId;
	
	// 封装文件标题请求参数的属性
	private String title;
	// 封装上传文件域的属性
	private File upload;
	// 封装上传文件类型的属性
	private String uploadContentType;
	// 封装上传文件名的属性
	private String uploadFileName;
	// 直接在struts.xml文件中配置的属性
	private String savePath;
	// 定义该Action允许上传的文件类型
	private String allowTypes;
	// allowTypes的setter和getter方法
	public String getAllowTypes() {
		return allowTypes;
	}

	public void setAllowTypes(String allowTypes) {
		this.allowTypes = allowTypes;
	}

	// 接受struts.xml文件配置值的方法
	public void setSavePath(String value) {
		this.savePath = value;
	}

	// 获取上传文件的保存位置
	private String getSavePath() throws Exception {
		//return ServletActionContext.getServletContext().getRealPath(savePath);
		return savePath;
	}

	// title的setter和getter方法
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return (this.title);
	}

	// upload的setter和getter方法
	public void setUpload(File upload) {
		this.upload = upload;
	}

	public File getUpload() {
		return (this.upload);
	}

	// uploadContentType的setter和getter方法
	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadContentType() {
		return (this.uploadContentType);
	}

	// uploadFileName的setter和getter方法
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadFileName() {
		return (this.uploadFileName);
	}

	
	public void upload() throws Exception {
		
		prepare();
		
		if(user==null || order ==null) {
			this.writeJson("{\"status\":false}");
			return;
		}
			
		// 以服务器的文件保存地址和原文件名建立上传文件输出流
		String realPath = context.getRealPath(getSavePath());  
		System.out.println(realPath);
		FileInputStream fis = new FileInputStream(getUpload());
		
		/*FileOutputStream fos = new FileOutputStream(realPath + "\\" + orderId+".jpg");
		byte[] buffer = new byte[2048];
		int len = 0;
		while ((len = fis.read(buffer)) > 0) {
			fos.write(buffer, 0, len);
		}
		fos.close();*/
		
		/*DiskFile diskFile = new DiskFile();
		diskFile.setFileName(orderId+".jpg");
		diskFile.setFilePath(getSavePath());
		diskFile.setFileType(uploadContentType);
		diskFileService.save(diskFile);*/
		
		DiskFile diskFile = diskFileService.insertDiskFile(fis, orderId+".jpg");
		order.setSignature(diskFile);
		orderService.update(order);
		
		this.writeJson("{\"status\":true}");
		
	}
	
	public void download() throws Exception{
		prepare();
		
		if(user==null || order ==null) {
			this.writeJson("{\"status\":false}");
			return;
		}
		
		DiskFile signature = order.getSignature();
		if(signature == null) {
			this.writeJson("{\"status\":false}");
			return;
		}
		String downloadPath = signature.getFilePath()+"/"+signature.getFileName();
		this.writeJson("{\"path\":\""+downloadPath+"\"}");

	}

	/**
	 * 过滤文件类型
	 * 
	 * @param types
	 *            系统所有允许上传的文件类型
	 * @return 如果上传文件的文件类型允许上传，返回null 否则返回error字符串
	 */
	public String filterTypes(String[] types) {
		// 获取允许上传的所有文件类型
		String fileType = getUploadContentType();
		for (String type : types) {
			if (type.equals(fileType)) {
				return null;
			}
		}
		return ERROR;
	}
	
	public String uploadUI() {
		return "input";
	}

	// 执行输入校验
	public void validateExecute() {
		// 将允许上传文件类型的字符串以英文逗号（,）
		// 分解成字符串数组从而判断当前文件类型是否允许上传
		String filterResult = filterTypes(getAllowTypes().split(","));
		// 如果当前文件类型不允许上传
		if (filterResult != null) {
			// 添加FieldError
			//addFieldError("upload", "您要上传的文件类型不正确！");
			this.writeJson("{\"status\":false}");
		}
	}
	
	public void prepare() throws Exception {
		if(username!=null && pwd!=null)
			user = userService.getByLoginNameAndMD5Password(username, pwd, companyId);
		if(orderId!=null)
			order = orderService.getOrderById(orderId);
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username)  throws UnsupportedEncodingException{
		this.username = URLDecoder.decode(username,"UTF-8");
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
}
