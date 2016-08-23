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
			<h1>外派员工管理</h1>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="user_queryDispatchList">
			<table>
				<tr>
					<th><s:property value="tr.getText('privilege.User.name')" /></th>
					<td><s:textfield cssClass="inputText" name="name" type="text" /></td>
					<th><s:property value="tr.getText('privilege.User.phoneNumber')" /></th>
					<td><s:textfield cssClass="inputText" name="phoneNumber" type="text" /></td>
					<td>
						<input class="inputButton" type="submit" value="查询" />
					</td>
					<td>
						<s:a cssClass="buttonA" action="user_addDispatchUI">添加外派员工</s:a>
					</td>
				</tr>
			</table>
			</s:form>
		</div>
		<div class="dataGrid">
			<div class="tableWrap">
				<table>
					
					<thead>
						<tr>
              				<th><s:property value="tr.getText('privilege.User.name')" /></th>
                			<th><s:property value="tr.getText('privilege.User.phoneNumber')" /></th>
			                <th>相关操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${name}</td>
                			<td>${phoneNumber}</td>
							<td>
								<s:a action="user_editDispatchUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
			                </td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="user_freshDispatchList">
				<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
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
