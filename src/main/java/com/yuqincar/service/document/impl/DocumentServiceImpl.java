package com.yuqincar.service.document.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cmd.DeployCmd;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuqincar.dao.document.EngineCheckNodeMapDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.document.Document;
import com.yuqincar.domain.document.EngineCheckNodeMap;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.document.DocumentService;

@Service
public class DocumentServiceImpl implements DocumentService{
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private ManagementService managementService;
	
	@Autowired
	private EngineCheckNodeMapDao engineCheckNodeMapDao;

	public List<EngineCheckNodeMap> getAllNodeMap() {
		return engineCheckNodeMapDao.getAll();
	}

	public void updateNodeMap(EngineCheckNodeMap nodeMap) {
		engineCheckNodeMapDao.update(nodeMap);
	}
	
	public void deployCheckProcess(String classPath) {
		
		repositoryService.createDeployment().addClasspathResource(classPath).deploy();
	}
	
	public void deleteCheckProcess(String name) {
		repositoryService.deleteDeployment(name);
	}
	/*
	* 得到审核链条的审核人描述信息。例如：本部门主管（张三）-->人力资源主管（李四）-->经理（王五）
	*/
	public String getCheckProcessDescription(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	 * 提交审核。
	 * nextChecker参数是用户选择的第一个审核环节的审核人。
	 */
	public void submitToCheck(Document doc, User nextChecker) {
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("entityId", String.valueOf(doc.getId()));

		/*if (projectStepRecord.isApplyForIgnore()) {
			if (projectStepRecord.getStep().getSkipWorkFlow().getSteps().size() > 0) {
				String firstStep = projectStepRecord.getStep()
						.getSkipWorkFlow().getSteps().get(0);
				variables.put(firstStep, String.valueOf(nextChecker.getId()));
			}
		} else {
			if (projectStepRecord.getStep().getWorkFlow(projectStepRecord)
					.getSteps().size() > 0) {
				String firstStep = projectStepRecord.getStep()
						.getWorkFlow(projectStepRecord).getSteps().get(0);
				variables.put(firstStep, String.valueOf(nextChecker.getId()));
			}
		}

		String workFlow = null;
		if (projectStepRecord.isApplyForIgnore())
			workFlow = projectStepRecord.getStep().getSkipWorkFlow().getLabel();
		else
			workFlow = projectStepRecord.getStep()
					.getWorkFlow(projectStepRecord).getLabel();

		executionService.startProcessInstanceByKey(workFlow, variables,
				String.valueOf(projectStepRecord.getId()));

		if (isProcessInstanceEnded(projectStepRecord))
			projectStepRecord.setCheckStatus(CheckStatusEnum.FINISHED);
		else
			projectStepRecord.setCheckStatus(CheckStatusEnum.STARTED);

		projectStepRecordDao.update(projectStepRecord);*/
	}
	/*
	 * 得到user当前能够审核的Document分页对象。
	 * 
	 * pagination参数指定了分页信息。
	 */
	public PageBean<Document> getToBeChecked(User user) {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	 * user当前能够审核Document对象吗？
	 */
	public boolean isCanBeChecked(Document doc, User user) {
		// TODO Auto-generated method stub
		return false;
	}
	/*
	*
	* 得到一个Document对象曾经的审核人。以审核时间升序排列，如果有重复审核情况，就只显示一个。
	*
	*
	*/
	public List<User> getCheckedUser(Document doc) {
		// TODO Auto-generated method stub
		return null;
	}
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
	public void check(Document doc, User checker, boolean passed, String memo, User denyTo) {
		// TODO Auto-generated method stub
		
	}

}
