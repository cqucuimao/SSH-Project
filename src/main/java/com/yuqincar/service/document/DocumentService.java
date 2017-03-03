package com.yuqincar.service.document;

import java.util.List;

import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.document.Document;
import com.yuqincar.domain.document.EngineCheckNodeMap;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.base.BaseService;

public interface DocumentService extends BaseService{
	public List<EngineCheckNodeMap> getAllNodeMap();

	public void updateNodeMap(EngineCheckNodeMap nodeMap);
	
	/*
	 * 部署审核流程。
	 * classPath是流程定义文件
	 */
	public void deployCheckProcess(String classPath);/*

	/*
	 * 删除审核流程。不需要Service调用，审核流程改变时调用。
	 * name就是定义流程时取的名字
	 */
	public void deleteCheckProcess(String name);
	
	/*
	* 得到审核链条的审核人描述信息。例如：本部门主管（张三）-->人力资源主管（李四）-->经理（王五）
	*/
	public String getCheckProcessDescription(String name);

	/*
	 * 提交审核。
	 * 
	 * nextChecker参数是用户选择的第一个审核环节的审核人。
	 */
	public void submitToCheck(Document doc, User user);

	/*
	 * 得到user当前能够审核的Document分页对象。
	 * 
	 * pagination参数指定了分页信息。
	 */
	public PageBean<Document> getToBeChecked(int pageNum,User user);

	/*
	 * user当前能够审核Document对象吗？
	 */
	public boolean isCanBeChecked(Document doc, User user);

	/*
	*
	* 得到一个Document对象曾经的审核人。以审核时间升序排列，如果有重复审核情况，就只显示一个。
	*
	*
	*/
	public List<User> getCheckedUser(Document doc);

	/*
	 * 审核Document对象。
	 * 
	 * checker参数代表了当前审核人。
	 * 
	 * passed参数代表了审核结果，true表示通过，false表示驳回。
	 * 
	 * memo参数代表了审核意见。
	 * 
	 * denyTo参数代表了拨回到哪个审核人？只有到passed为false时才有意义，否则传null。
	 * 
	 */
	public void check(Document doc, User checker,boolean passed, String memo,User denyTo);
}
