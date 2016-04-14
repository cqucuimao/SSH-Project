package com.yuqincar.action.car;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.common.DiskFileService;

@Controller
@Scope("prototype")
public class CarServiceTypeAction extends BaseAction implements ModelDriven<CarServiceType>{
	
	private CarServiceType model = new CarServiceType();
		
	@Autowired
	private CarService carService;
	
	@Autowired
	private DiskFileService diskFileService;
	
	private Long orderId;
	private Long imageId;
	private String flag;
	
	// 封装文件标题请求参数的属性
	private String fileTitle;
	// 封装上传文件域的属性
	private File upload;
	// 封装上传文件类型的属性
	private String uploadContentType;
	// 封装上传文件名的属性
	private String uploadFileName;
	// 直接在struts.xml文件中配置的属性
	private String savePath;
	
	/** 列表 */
	public String list() throws Exception {
		List<CarServiceType> carServiceTypeList = carService.getAllCarServiceType();
		ActionContext.getContext().put("carServiceTypeList",carServiceTypeList);
		return "list";
		
	}
	/** 删除 */
	public String delete() throws Exception {
		if(carService.canDeleteCarServiceType(model.getId()))
			carService.deleteCarServiceType(model.getId());
		return "toList";
	}
	
	/** 添加页面 */
	public String addUI() throws Exception {
		model.setPersonLimit(4);
		return "saveUI";
	}
	
	/** 添加 */
	public String add() throws Exception {
		//if(model.getPicture() != null){
			DiskFile diskFile = diskFileService.insertDiskFile(new FileInputStream(getUpload()), uploadFileName);
			model.setPicture(diskFile);
		//}
		// 保存到数据库
		carService.saveCarServiceType(model);

		return "toList";
	}
	
	/** 修改页面 */
	public String editUI() throws Exception {
		// 准备回显的数据
		flag = "edit";

		CarServiceType carServiceType = carService.getCarServiceTypeById(model.getId());
		if(carServiceType.getPicture() != null){
			imageId = carServiceType.getPicture().getId();
		}

		ActionContext.getContext().getValueStack().push(carServiceType);

		return "saveUI";
	}
	
	public void getImage(){
		diskFileService.downloadDiskFile(diskFileService.getDiskFileById(imageId), response);
	}
	
	
	/** 修改 */
	public String edit() throws Exception {
		//从数据库中取出原对象
		CarServiceType carServiceType = carService.getCarServiceTypeById(model.getId());
		//设置要修改的属性
		carServiceType.setTitle(model.getTitle());
		carServiceType.setPricePerKM(model.getPricePerKM());
		carServiceType.setPricePerDay(model.getPricePerDay());
		carServiceType.setPersonLimit(model.getPersonLimit());
		carServiceType.setPriceDescription(model.getPriceDescription());
		diskFileService.updateDiskFileData(carServiceType.getPicture(), new FileInputStream(getUpload()), uploadFileName);
		
		//更新到数据库
		carService.updateCarServiceType(carServiceType);

		return "toList";
	}
	//判断车型能否删除
	public boolean isCanDeleteServiceType(){
		CarServiceType carServiceType = (CarServiceType) ActionContext.getContext().getValueStack().peek();
		if(carService.canDeleteCarServiceType(carServiceType.getId()))
			return true;
		else 
			return false;
	}
	
	public CarServiceType getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getFileTitle() {
		return fileTitle;
	}

	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	
	public Long getImageId() {
		return imageId;
	}
	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}