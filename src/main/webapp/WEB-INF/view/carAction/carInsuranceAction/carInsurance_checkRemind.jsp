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
			<h1>车辆保险提醒信息列表</h1>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="carInsurance_remind">
			<table>
				<tr>
					<td>
						<input id="export" class="inputButton" type="button" value="导出"/>
						<a class="p15" href="javascript:history.go(-1);">返回</a>
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
							<th><s:property value="tr.getText('car.Car.plateNumber')" /></th>
							<th><s:property value="tr.getText('car.Car.driver')" /></th>
							<th><s:property value="tr.getText('privilege.User.phoneNumber')" /></th>
							<th><s:property value="tr.getText('car.Car.insuranceExpiredDate')" /></th>
							<th><s:property value="tr.getText('car.Car.insuranceExpired')" /></th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${plateNumber}</td>
							<td>${driver.name}</td>
							<td>${driver.phoneNumber}</td>
							<td style="text-align:right"><s:date name="insuranceExpiredDate" format="yyyy-MM-dd"/></td>
							<s:if test="insuranceExpired">
								<td>否</td>
							</s:if>
							<s:else>
								<td><font color="red">是</font></td>
							</s:else>
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
			
	    })
	</script>
</body>
</html>
