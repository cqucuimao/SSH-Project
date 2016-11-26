<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
	<link rel="stylesheet" type="text/css" href="skins/main.css">
</head>
<body class="minW">
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>配置${viewFlag }信息</h1>
		</div>
		<div class="editBlock search">
			<table>
				<tr>
					<td>
						<s:a cssClass="buttonA" action="businessParameter_addUI?viewFlag=%{viewFlag}&actionFlag=%{actionFlag}">添加</s:a>
					</td>
				</tr>
			</table>
		</div>
		<div class="dataGrid">
			<div class="tableWrap">
				<table>
					
					<thead>
						<tr>
							<th><s:property value="tr.getText('privilege.User.name')" /></th>
			                <th>相关操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
						<!-- 4s员工 -->
						<s:if test="actionFlag == 'for4S'">
					        <s:iterator value="for4SUserList">
							<tr>
								<td>${name}</td>
								<td>
									<s:a action="businessParameter_delete?for4SUserId=%{id}&actionFlag=%{actionFlag}" onclick="return confirm('确定要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
				                </td>
							</tr>
							</s:iterator> 
						</s:if>
						<!-- 保养管理员 -->
						<s:if test="actionFlag == 'forCarCare'">
					        <s:iterator value="forCarCareUserList">
							<tr>
								<td>${name}</td>
								<td>
									<s:a action="businessParameter_delete?forCarCareUserId=%{id}&actionFlag=%{actionFlag}" onclick="return confirm('确定要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
				                </td>
							</tr>
							</s:iterator> 
						</s:if>
						<!--审核人-->
						<s:if test="actionFlag == 'approveUser'">
					        <s:iterator value="approveUserList">
							<tr>
								<td>${name}</td>
								<td>
									<s:a action="businessParameter_delete?approveUserId=%{id}&actionFlag=%{actionFlag}" onclick="return confirm('确定要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
				                </td>
							</tr>
							</s:iterator> 
						</s:if>
						
						<!--申请人-->
						<s:if test="actionFlag == 'applyUser'">
					        <s:iterator value="applyUserList">
							<tr>
								<td>${name}</td>
								<td>
									<s:a action="businessParameter_delete?applyUserId=%{id}&actionFlag=%{actionFlag}" onclick="return confirm('确定要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
				                </td>
							</tr>
							</s:iterator> 
						</s:if>
						<!-- 车辆审批人-->
						<s:if test="actionFlag == 'carApproveUser'">
					        <s:iterator value="carApproveUserList">
							<tr>
								<td>${name}</td>
								<td>
									<s:a action="businessParameter_delete?carApproveUserId=%{id}&actionFlag=%{actionFlag}" onclick="return confirm('确定要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
				                </td>
							</tr>
							</s:iterator> 
						</s:if>
						<!-- 司机审批人 -->
						<s:if test="actionFlag == 'driverApproveUser'">
					        <s:iterator value="driverApproveUserList">
							<tr>
								<td>${name}</td>
								<td>
									<s:a action="businessParameter_delete?driverApproveUserId=%{id}&actionFlag=%{actionFlag}" onclick="return confirm('确定要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
				                </td>
							</tr>
							</s:iterator> 
						</s:if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>	
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
	<script type="text/javascript">
	
	</script>
</body>
</html>
