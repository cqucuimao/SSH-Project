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
			<h1>客户单位列表</h1>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="customerOrganization_queryList">
			<table>
				<tr>
					<td>
						<input id="addCustomerOrganization" class="inputButton" type="button" value="新增客户单位" name="button" />
					</td>
					<th><s:property value="tr.getText('order.CustomerOrganization.name')" /></th>
					<td>
						 <s:textfield id="customer_organization_name" name="name" cssClass="inputText" type="text"/>
						 <input id="customer_organization_id" type="hidden">
					</td>
					<td>
						<input class="inputButton" type="submit" value="查询"/>
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
							<th><s:property value="tr.getText('order.CustomerOrganization.name')" /></th>
              				<th><s:property value="tr.getText('order.CustomerOrganization.abbreviation')" /></th>
              				<th><s:property value="tr.getText('order.CustomerOrganization.manager')" /></th>
                			<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${name}</td>
							<td>${abbreviation}</td>
							<td>${manager.name}</td>
							<td>
								<s:a action="customerOrganization_editUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
								<s:a action="customerOrganization_addFinancial?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
                    			<s:a action="customer_checkPeople?id=%{id}"><i class="icon-operate-print" title="查看人员"></i></s:a>
								<s:if test="canDelete">
                    			<s:a action="customerOrganization_delete?id=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
                    			</s:if>
                    			<s:else></s:else>
                			</td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="customerOrganization_freshList">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	
	<script src="js/jquery-1.7.2.js"></script>
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
	
		
	<script type="text/javascript">
		$(function(){
	        $("#addCustomerOrganization").click(function(){
	            self.location.href='customerOrganization_addUI.action';
	        });
	    })
	</script>
</body>
</html>
