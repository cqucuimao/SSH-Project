package com.yuqincar.action.car;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.ServicePoint;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.order.OrderService;

@Controller
@Scope("prototype")
/*
 * 由于取消的Address，ServicePoint也考虑要删除，所以本类注释了很多代码，否则汇报编译错误。
 */
public class ServicePointAction extends BaseAction implements ModelDriven<ServicePoint> {

	private ServicePoint model = new ServicePoint();

	@Autowired
	private CarService carService;

	@Autowired
	private OrderService orderService;

	Long locationId;

	/** 列表 */
	public String list() throws Exception {

		// System.out.println("++++++++++++");

		List<ServicePoint> servicePointList = carService.getAllServicePoint();
		ActionContext.getContext().put("servicePointList", servicePointList);
		return "list";
	}

	/** 删除 */
	public String delete() throws Exception {
		// if(carService.canDeleteServicePoint(model.getId()))
		carService.deleteServicePoint(model.getId());
		return "toList";
	}

	/** 添加页面 */
	public String addUI() throws Exception {

		return "saveUI";
	}

	/** 添加 */
	public String add() throws Exception {
//		addressService.saveAddresses(address);
//		// 封装对象
//		model.setPointAddress(address.get(0));

		// 保存到数据库
		carService.saveServicePoint(model);

		return "toList";
	}

	/** 修改页面 */
	public String editUI() throws Exception {
		// 准备回显的数据
		ServicePoint servicePoint = carService.getServicePointById(model.getId());
		ActionContext.getContext().getValueStack().push(servicePoint);
//
//		address = new ArrayList<Address>();
//		address.add(servicePoint.getPointAddress());
		return "saveUI";
	}

	/** 修改 */
	public String edit() throws Exception {
//		// 从数据库中取出原对象
//		ServicePoint servicePoint = carService.getServicePointById(model.getId());
//		// 设置要修改的属性
//		long oldId = servicePoint.getPointAddress().getId();
//		servicePoint.setName(model.getName());
//		addressService.saveAddresses(address);
//		servicePoint.setPointAddress(address.get(0));
//		carService.updateServicePoint(servicePoint);
//		addressService.deleteAddress(oldId);

		return "toList";
	}

	/** 验证位置信息 */
	public void validateAdd() {
//		if (address == null || address.size() == 0 || address.get(0).getDescription().length() == 0
//				|| address.get(0).getDetail().length() == 0 || address.get(0).getLocation().getLongitude() == 0
//				|| address.get(0).getLocation().getLatitude() == 0) {
//			addFieldError("pointAddress", "地址信息不能为空！");
//		}
	}
	//判断驻车点能否删除
	public boolean isCanDeleteServicePoint(){
		ServicePoint servicePoint = (ServicePoint) ActionContext.getContext().getValueStack().peek();
		if(carService.canDeleteServicePoint(servicePoint.getId()))
			return true;
		else 
			return false;
	}
	public ServicePoint getModel() {

		return model;
	}

}