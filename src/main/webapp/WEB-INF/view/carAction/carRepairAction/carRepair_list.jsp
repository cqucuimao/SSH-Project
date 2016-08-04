<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
			<h1>车辆维修</h1>
		</div>		
		<div class="tab_next style2">
			<table>
				<tr>
				    <td><s:a action="carRepair_appointList"><span>预约车辆维修</span></s:a></td>
					<td class="on"><a href="#"><span>车辆维修</span></a></td>
				</tr>
			</table>
		</div>
		<br/>
		<div class="editBlock search">
			<s:form id="pageForm" action="carRepair_queryForm">
			<table>
				<tr>
					<td>
						<s:a action="carRepair_addUI"><input id="register" class="inputButton" type="button" value="维修登记" name="button" /></s:a>
					</td>
					<th><s:property value="tr.getText('car.Car.plateNumber')" /></th>
					<td>
						<s:textfield id="car_platenumber" cssClass="carSelector inputText inputChoose" onfocus="this.blur();" 
							name="car.plateNumber" type="text" />
					</td>
					<th><s:property value="tr.getText('car.CarCare.driver')" /></th>
					<td>
						<s:textfield class="userSelector inputText inputChoose" id="driverName" name="driver.name" type="text" driverOnly="true"/>
						<s:textfield id="driverId" name="driver.id" type="hidden"/>
					</td>
					<th>从</th>
					<td>
						<s:textfield name="date1" id="date1" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<th>到</th>
					<td>
						<s:textfield name="date2" id="date2" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<td>
						<input class="inputButton" type="submit" value="查询"/>
						<s:a action="carRepair_excel"><input id="repairInput" class="inputButton" type="button" value="维修信息导入"  /></s:a>
					 <s:if test="Ture">
						<a class="p15" href="javascript:history.go(-1);">返回</a>
						</s:if>
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
							<th><s:property value="tr.getText('car.CarRepair.car')" /></th>
							<th><s:property value="tr.getText('car.CarRepair.driver')" /></th>
              				<th>维修时间</th>
              				<th><s:property value="tr.getText('car.CarRepair.repairLocation')" /></th>
              				<th><s:property value="tr.getText('car.CarRepair.reason')" /></th>
                			<th><s:property value="tr.getText('car.CarRepair.money')" />(元)</th>
                			<th><s:property value="tr.getText('car.CarRepair.moneyNoGuaranteed')" />(元)</th>
                			<th><s:property value="tr.getText('car.CarRepair.memo')" /></th>
                			<th><s:property value="tr.getText('car.CarRepair.payDate')" /></th>
                			<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td><s:a action="carRepair_detail?id=%{id}">${car.plateNumber}</s:a></td>
							<td>${driver.name}</td>
							<td style="text-align:right"><s:date name="fromDate" format="yyyy-MM-dd"/>&nbsp;&nbsp;&nbsp;&nbsp;<s:date name="toDate" format="yyyy-MM-dd"/></td>
							<td>${repairLocation}</td>
							<td>${reason}</td>
							<td style="text-align:right"><fmt:formatNumber value="${money}" pattern="#0"/></td>
							<td style="text-align:right"><fmt:formatNumber value="${moneyNoGuaranteed}" pattern="#0"/></td>
							<td>${memo}</td>
							<td style="text-align:right"><s:date name="payDate" format="yyyy-MM-dd"/></td>
							<td>
                    			<s:a action="carRepair_delete?id=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
                    			<s:a action="carRepair_editUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
                			</td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="carRepair_refreshList">
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
			formatDateField2($("#date1"));
			formatDateField2($("#date2"));
	    })
	</script>
</body>
</html>
