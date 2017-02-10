package com.yuqincar.action.previlege;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.common.UploadedDiskFiles;
import com.yuqincar.domain.privilege.Contract;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.domain.privilege.UserStatusEnum;
import com.yuqincar.service.common.DiskFileService;
import com.yuqincar.service.privilege.ContractService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;


@Controller
@Scope("prototype")
public class ContractAction extends BaseAction implements ModelDriven<Contract>  {

	private Contract model=new Contract();
	
	@Autowired
	private ContractService contractService;
	@Autowired
	private UserService userService;
	@Autowired
	private DiskFileService diskFileService;
	
	/**查询参数*/
	private String userName;
	private Date fromDateQ;
	private Date toDateQ;
	private String actionFlag;
	
	/**添加或修改参数*/
	private User editUser;
	private Date fromDate;
	private Date toDate;
	private UploadedDiskFiles uploadedDiskFiles;
	
	private long uploadId;
	private long deletedId;

	
	/**列表*/
	public String list(){
		QueryHelper helper = new QueryHelper(Contract.class, "c");
		PageBean pageBean = contractService.queryContract(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("contractHelper", helper);
		return "list";
	}
	
	/**更新合同列表*/
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("contractHelper");
		PageBean pageBean = contractService.queryContract(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	/**查询*/
	public String queryList(){
		QueryHelper helper = new QueryHelper(Contract.class, "c");
		if(userName!=null)
			helper.addWhereCondition("c.user.name like ?", "%"+userName+"%");
		if(fromDateQ!=null && toDateQ==null)
		{
			helper.addWhereCondition("c.toDate>=?",DateUtils.getMinDate(fromDateQ));
		}
		if(toDateQ!=null && fromDateQ==null )
			helper.addWhereCondition("c.toDate<=?", DateUtils.getMaxDate(toDateQ));
		if(toDateQ!=null && fromDateQ!=null )
			helper.addWhereCondition("c.toDate between ? and ? ",
					DateUtils.getMinDate(fromDateQ), DateUtils.getMaxDate(toDateQ));
		PageBean<Contract> pageBean = contractService.queryContract(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);	
		ActionContext.getContext().getSession().put("contractHelper", helper);
		return "list";
	}
	
	/** 添加页面 */
	public String addUI() throws Exception {
		ActionContext.getContext().put("actionFlag", actionFlag);
		return "saveUI";
	}
	
	/** 添加*/
	public String add() throws Exception {
		if(model.getFromDate().after(model.getToDate()))
		{
			addFieldError("toDate", "你填写的结束日期早于开始日期！");
			return addUI();
		}
		model.setUser(editUser);
		//System.out.println("uDiskFiles size: "+model.getDiskFiles().size());
		//model.setDiskFiles(uploadedDiskFiles.getDiskFiles());
		contractService.saveContract(model);
		
		ActionContext.getContext().getValueStack().push(new Contract());
		return freshList();
	}
	
	
	/** 提醒页面 */
	public String alertUI() throws Exception {
		return "alertUI";
	}
	
	/**到期提醒列表*/
	public String alertList(){
		QueryHelper helper = new QueryHelper(Contract.class, "c");
		helper.addWhereCondition("c.user.status=?", UserStatusEnum.NORMAL);
		helper.addOrderByProperty("c.toDate", true);
		PageBean pageBean = contractService.queryContract(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("contractAlertHelper", helper);
		return "alertUI";
	}
	
	/**更新合同到期提醒列表*/
	public String alertFreshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("contractAlertHelper");
		PageBean pageBean = contractService.queryContract(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "alertUI";
	}
	
	/** 修改页面 */
	public String editUI() throws Exception {
		ActionContext.getContext().put("actionFlag", actionFlag);
		// 准备回显的数据
		Contract c = contractService.getContractById(model.getId());
		ActionContext.getContext().getValueStack().push(c);
		//员工姓名
		if (c.getUser()!= null) {
			editUser= c.getUser();
		}
		//合同起止时间
		if (c.getFromDate() != null) {
			fromDate = c.getFromDate();
		}
		if (c.getToDate() != null) {
			toDate = c.getToDate();
		}
		
		//扫描件
		/*if(c.getDiskFiles()!=null)
		{
			
		}*/
		return "saveUI";
	}
	
	/** 下载扫描件*/
	public String downloadFile() throws Exception {
		DiskFile diskFile=diskFileService.getDiskFileById(uploadId);
		//diskFileService.downloadDiskFile(diskFile, response);
		Contract c = diskFile.getContract();
		ActionContext.getContext().getValueStack().push(c);
		//员工姓名
		if (c.getUser()!= null) {
			editUser= c.getUser();
		}
		//合同起止时间
		if (c.getFromDate() != null) {
			fromDate = c.getFromDate();
		}
		if (c.getToDate() != null) {
			toDate = c.getToDate();
		}
		diskFileService.downloadDiskFile(diskFile, response);
		System.out.println("OKKKKKK");
		return "saveUI";
	}
	
	/** 删除扫描件*/
	public String deleteFile() throws Exception {
		
		DiskFile diskFile=diskFileService.getDiskFileById(deletedId);
		Contract c = diskFile.getContract();
		ActionContext.getContext().getValueStack().push(c);
		diskFileService.deleteDiskFile(deletedId);
		//员工姓名
		if (c.getUser()!= null) {
			editUser= c.getUser();
		}
		//合同起止时间
		if (c.getFromDate() != null) {
			fromDate = c.getFromDate();
		}
		if (c.getToDate() != null) {
			toDate = c.getToDate();
		}
		return "saveUI";
	}
	
	/** 修改*/
	public String edit() throws Exception {
		//从数据库中取出原对象
		Contract c = contractService.getContractById(model.getId());
		c.setUser(editUser);
		c.setFromDate(model.getFromDate());
		c.setToDate(model.getToDate());

		System.out.println("actionFlag2="+ actionFlag);
		//更新到数据库
		contractService.updateContract(c);
		ActionContext.getContext().getValueStack().push(new Contract());
		if(actionFlag.equals("editFromList"))
		{
			System.out.println("actionFlag2=OK"+ actionFlag);
			return freshList();
		}
		else {
			return alertFreshList();
		}
	}
	
	
	/** 删除*/
	public String delete() throws Exception {
		contractService.deleteContract(model.getId());
		return freshList();
	}
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public Contract getModel() {
		return model;
	}

	public void setModel(Contract model) {
		this.model = model;
	}

	public Date getFromDateQ() {
		return fromDateQ;
	}

	public void setFromDateQ(Date fromDateQ) {
		this.fromDateQ = fromDateQ;
	}

	public Date getToDateQ() {
		return toDateQ;
	}

	public void setToDateQ(Date toDateQ) {
		this.toDateQ = toDateQ;
	}
	
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public User getEditUser() {
		return editUser;
	}

	public void setEditUser(User editUser) {
		this.editUser = editUser;
	}

	public UploadedDiskFiles getUploadedDiskFiles() {
		return uploadedDiskFiles;
	}

	public void setUploadedDiskFiles(UploadedDiskFiles uploadedDiskFiles) {
		this.uploadedDiskFiles = uploadedDiskFiles;
	}

	public String getActionFlag() {
		return actionFlag;
	}

	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}

	public long getUploadId() {
		return uploadId;
	}

	public void setUploadId(long uploadId) {
		this.uploadId = uploadId;
	}

	public long getDeletedId() {
		return deletedId;
	}

	public void setDeletedId(long deletedId) {
		this.deletedId = deletedId;
	}
	
	
}
