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
			<h1>预约车辆保养</h1>
		</div>
		<div class="tab_next style2">
			<table>
				<tr>
				    <td class="on"><a href="#"><span>预约车辆保养</span></a></td>
					<td><s:a action="carCare_list"><span>车辆保养记录</span></s:a></td>
				</tr>
			</table>
		</div>
		<br/>
		<div class="editBlock search">
			<s:form id="pageForm" action="carCare_queryAppointForm">
			<table>
				<tr>
					<td>
						<s:a action="carCare_appoint"><input id="appoint" class="inputButton" type="button" value="保养预约"/></s:a>
					</td>
					<th><s:property value="tr.getText('car.Car.plateNumber')" /></th>
					<td>
						<s:textfield id="car_platenumber" cssClass="carSelector inputText inputChoose" onfocus="this.blur();" name="car.plateNumber" type="text" />
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
							<th><s:property value="tr.getText('car.CarCare.driver')" /></th>
              				<th><s:property value="tr.getText('car.CarCare.date')" /></th>
              				<th><s:property value="tr.getText('car.CarCare.done')" /></th>
                			<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">				        
						<tr>
							<td>${car.plateNumber}</td>
							<td>${driver.name}</td>
							<td><s:date name="date" format="yyyy-MM-dd"/></td>
							<td>
								<s:if test="done==true">
									<s:text name="是"></s:text>
								</s:if>
								<s:else>
									<s:text name="否"></s:text>
								</s:else>
							</td>
							<td>
								<s:if test="done==false">
                    				<s:a action="carCare_deleteAppointment?id=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
                    				<s:a action="carCare_editAppointmentUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
                    			</s:if>                    			
                			</td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="carCare_freshAppointList">
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
		$(function(){
			formatDateField2($("#date1"));
			formatDateField2($("#date2"));
	    })
	</script>
</body>
</html>
