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
			<h1>部门管理</h1>
			<p>部门信息的维护</p>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="department_list">
			<table>
				<tr>
					<th>部门名称</th>
					<td><s:textfield cssClass="inputText" name="name" type="text" /></td>
					<td>
						<input class="inputButton" type="submit" value="查询"/>
						<input id="add" class="inputButton" type="button" value="添加部门" name="button" />
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
							<th>部门名称</th>
              				<th>部门描述</th>
			                <th>相关操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${name}</td>
							<td>${description}</td>
							<td>
			                	<s:a action="department_delete?id=%{id}" onclick="return confirm('确认要删除吗？');">删除</s:a>
                    			<s:a action="department_editUI?id=%{id}">修改</s:a>
			                </td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
		</div>
	</div>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>	
	<script type="text/javascript">
		$(function(){
	        $("#add").click(function(){
	            self.location.href='department_addUI.action';
	        });
	    })
	</script>
	    	<s:debug></s:debug>
	
</body>
</html>
