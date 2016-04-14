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
			<h1>客户列表</h1>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="customer_list">
			<table>
				<tr>
					<td>
						<input id="addCustomer" class="inputButton" type="button" value="新增客户" name="button" />
					</td>
					<th>单位名称</th>
					<td>
						<!-- 
						<s:textfield cssClass="inputText" name="customerOrganization.name" type="text" />
						 -->
						 <s:textfield id="customer_organization_name" name="customerOrganization.name" cssClass="inputText" type="text"/>
						 <!-- 
						 <input id="customer_organization_name" type="text" style="padding:4px; width:16em; margin-right:10px">
						  -->
						 <input id="customer_organization_id" type="hidden">
						 
					</td>
					<th>姓名</th>
					<td><s:textfield cssClass="inputText" name="name" type="text" /></td>
					<th>联系方式</th>
					<td><s:textfield cssClass="inputText" name="phonesStr" type="text" /></td>
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
							<th>单位</th>
              				<th>姓名</th>
              				<th>性别</th>
              				<th>联系方式</th>
                			<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${customerOrganization.name}</td>
							<td>${name}</td>
							<td>
								<s:if test="gender==true">
								<s:text name="男"></s:text>
								</s:if>
								<s:else>
								<s:text name="女"></s:text>
								</s:else>
							</td>
							<td>
								<s:iterator value="phones" var="p" status="s">
									<s:property value="p"/>
									<s:if test="!#s.last">
										,
									</s:if>
								</s:iterator>
                			</td>
							<td>
								<s:a action="customer_editUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
								<s:if test="canDelete">
                    			<s:a action="customer_delete?id=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
                    			</s:if>
                    			<s:else></s:else>
                			</td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="customer_customer">
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
	        $("#addCustomer").click(function(){
	            self.location.href='customer_addUI.action';
	        });
	    })
	</script>
</body>
</html>
