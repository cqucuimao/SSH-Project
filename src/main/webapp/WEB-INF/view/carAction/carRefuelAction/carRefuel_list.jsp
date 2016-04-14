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
			<h1>加油信息</h1>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="carRefuel_list">
			<table>
				<tr>
					<td>
						<input id="register" class="inputButton" type="button" value="加油登记" name="button" />
					</td>
					<th><s:property value="tr.getText('car.Car.plateNumber')" /></th>
					<td>
						<s:textfield id="car_platenumber" cssClass="inputText inputChoose" onfocus="this.blur();" name="car.plateNumber" type="text" /></td>
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
							<th><s:property value="tr.getText('car.Car.plateNumber')" /></th>
              				<th><s:property value="tr.getText('car.CarRefuel.refuelingSite')" /></th>
                			<th><s:property value="tr.getText('car.CarRefuel.date')" /></th>
                			<th><s:property value="tr.getText('car.CarRefuel.RefuelingCharge')" />(L)</th>
                			<th><s:property value="tr.getText('car.CarRefuel.money')" />(元)</th>
			                <th>相关操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td><s:a action="carRefuel_detail?id=%{id}">${car.plateNumber}</s:a></td>
							<td>${refuelingSite}</td>
							<td style="text-align:right"><s:date name="date" format="yyyy-MM-dd"/></td>
                			<td style="text-align:right">${RefuelingCharge}</td>
							<td style="text-align:right">${money}</td>
							<td></td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="carRefuel_refuel">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	
<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>	
	<script type="text/javascript" src="js/common.js"></script>	
	
	<script type="text/javascript">
		$(function(){
	        $("#register").click(function(){
	            self.location.href='carRefuel_addUI.action';
	        });
	    })
	</script>
</body>
</html>
