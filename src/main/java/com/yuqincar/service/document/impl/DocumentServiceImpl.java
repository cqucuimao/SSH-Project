package com.yuqincar.service.document.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuqincar.dao.document.DocumentDao;
import com.yuqincar.dao.document.DocumentRecordDao;
import com.yuqincar.dao.document.EngineCheckNodeMapDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.document.Document;
import com.yuqincar.domain.document.DocumentRecord;
import com.yuqincar.domain.document.DocumentStatusEnum;
import com.yuqincar.domain.document.EngineCheckNodeMap;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.document.DocumentService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.Configuration;


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
	
	@Autowired
	private DocumentDao documentDao;
	
	@Autowired
	private DocumentRecordDao documentRecordDao;
	
	@Autowired
	private UserService userService;

	public List<EngineCheckNodeMap> getAllNodeMap() {
		return engineCheckNodeMapDao.getAll();
	}

	public void updateNodeMap(EngineCheckNodeMap nodeMap) {
		engineCheckNodeMapDao.update(nodeMap);
	}
	
	public void deployCheckProcess(String classPath) {
		Deployment deployment =
		repositoryService.createDeployment().addClasspathResource(classPath).deploy();
		System.out.println("name="+deployment.getName());
		System.out.println("Id="+deployment.getId());
	}
	
	public void deleteCheckProcess(String name) {
		repositoryService.deleteDeployment(name);
	}
	/*
	* 得到审核链条的审核人描述信息。例如：本部门主管（张三）-->人力资源主管（李四）-->经理（王五）
	*/
	public String getCheckProcessDescription(String name) {
		//TODO
        return null;
	}
	/*
	 * 提交审核。
	 * nextChecker参数是用户选择的第一个审核环节的审核人。
	 */
	public void submitToCheck(Document doc, User nextChecker) {
		
		Map<String, Object> variables = new HashMap<String, Object>();
		
		variables.put("docId", String.valueOf(doc.getId()));
		variables.put("checkerId", String.valueOf(nextChecker.getId()));
		//把doc的id作为流程的key（id）
		runtimeService.startProcessInstanceByKey(String.valueOf(doc.getId()),variables);

		doc.setStatus(DocumentStatusEnum.CHECKING);

		documentDao.update(doc);
	}
	/*
	 * 得到user当前能够审核的Document分页对象。
	 * 
	 * PageBean参数指定了分页信息。
	 */
	public PageBean<Document> getToBeChecked(int pageNum,User user) {
		List<Task> taskList = taskService.createTaskQuery().taskAssignee(user.getName()).list();
		
		List<Document> docList = new ArrayList<Document>();
		for (Task task : taskList) {
			long id = Long.valueOf((String) runtimeService.getVariable(task.getExecutionId(), "docId"));
			docList.add(documentDao.getById(id));
		}
		
		return new PageBean<Document>(pageNum, Configuration.getPageSize(), docList.size(), docList);
	}
	/*
	 * user当前能够审核Document对象吗？
	 */
	public boolean isCanBeChecked(Document doc, User user) {
		return user.equals(getCurrentCheckUser(doc));
	}
	
	/*
	* 得到一个Document对象曾经的审核人。以审核时间升序排列，如果有重复审核情况，就只显示一个。
	* 
	*/
	public List<User> getCheckedUser(Document doc) {
		List<DocumentRecord> records = doc.getRecords();
		List<User> users = new ArrayList<User>();
		for(DocumentRecord drs : records){
			users.add(drs.getApproveUser());
		}
		return new ArrayList<User>(new HashSet<User>(users));
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
		Task task = getCurrentTask(doc);
		Map<String, Object> variables = new HashMap<String, Object>();
		if (passed) {
			taskService.complete(task.getId());
		} else {
			variables.put("denyToId", denyTo);
			taskService.complete(task.getId(),variables);
			doc.setStatus((DocumentStatusEnum.REJECTED));
		}

		if (isProcessInstanceEnded(doc)) {
			if (passed)
				doc.setStatus(DocumentStatusEnum.CHECKED);
		}

		DocumentRecord documentRecord = new DocumentRecord();
		documentRecord.setApproveUser(checker);
		documentRecord.setApproveResult(passed);
		documentRecord.setApproveDate(new Date());
		documentRecord.setReasons(memo);
		documentRecordDao.save(documentRecord);
		
		documentDao.update(doc);		
	}
	
	private Task getCurrentTask(Document doc) {
		ProcessInstance processInstance = getProcessInstance(doc);
		if (processInstance == null)
			return null;
		else
			return taskService
					.createTaskQuery()
					.processInstanceId(
							getProcessInstance(doc).getId())
					.singleResult();
	}

	private ProcessInstance getProcessInstance(Document doc) {
		return runtimeService
				.createProcessInstanceQuery()
				.processInstanceId(String.valueOf(doc.getId()))
				.singleResult();
	}
	
	
	private boolean isProcessInstanceEnded(Document doc) {
		ProcessInstance processInstance = getProcessInstance(doc);
		if (processInstance == null)
			return true;
		else
			return false;
	}
	
	public User getCurrentCheckUser(Document doc) {
		Task task = getCurrentTask(doc);
		if (task == null)
			return null;
		if (task.getAssignee() != null)
			return userService
					.getById(Long.valueOf(task.getAssignee()));
		else
			return null;
	}
}
