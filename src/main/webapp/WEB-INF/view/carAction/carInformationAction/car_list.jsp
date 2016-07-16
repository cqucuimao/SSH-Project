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
			<h1>基本信息</h1>
		</div>
		<div class="editBlock search">
			<s:form action="car_list">
			<table>
				<tr>
					<td>
					<input id="register" class="inputButton" type="button" value="车辆登记" name="button" />
					</td>
					<th><s:property value="tr.getText('car.Car.plateNumber')" /></th>
					<td>
						<s:textfield id="car_platenumber" name="plateNumber" class="carSelector inputText inputChoose" onfocus="this.blur();" type="text" />
					</td>
					<th><s:property value="tr.getText('car.CarServiceType.title')" /></th>
					<td><s:select name="serviceType.title" cssClass="SelectStyle"
                        		list="carServiceTypeList" listKey="title" listValue="title"
                        		headerKey="" headerValue="选择服务类型"
                        		/>
                    </td>
					<th><s:property value="tr.getText('car.Car.driver')" /></th>
					<td><s:textfield class="userSelector inputText" id="driver" type="text" name="driver.name" driverOnly="true"/>
							<s:textfield id="driverId" name="driverId" type="hidden"/></td>
					<td>
						<input class="inputButton" type="submit" value="查询" name="submit" />
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
							<th><s:property value="tr.getText('car.CarServiceType.title')" /></th>
              				<th><s:property value="tr.getText('car.Car.plateNumber')" /></th>
                			<th><s:property value="tr.getText('car.Car.model')" /></th>
                			<th><s:property value="tr.getText('car.Car.transmissionType')" /></th>
                			<th><s:property value="tr.getText('car.Car.registDate')" /></th>
                			<th><s:property value="tr.getText('car.Car.driver')" /></th>
                			<th><s:property value="tr.getText('car.ServicePoint.name')" /></th>
			                <th>相关操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${serviceType.title }</td>
							<td>${plateNumber }</td>
							<td>${model }</td>
							<td>${transmissionType.label }</td>
							<td><s:date name="registDate" format="yyyy-MM-dd"/></td>
							<td>${driver.name }</td>
                			<td>${servicePoint.name }</td>							
							<td>							
			                    <s:a action="car_editUI?id=%{id}&actionFlag=edit"><i class="icon-operate-edit" title="修改"></i></s:a>
			                    <s:a action="car_detail?id=%{id}"><i class="icon-operate-detail" title="GPS定位器"></i></s:a>	 
			                    <s:if test="canDeleteCar">
			                	<s:a action="car_delete?id=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
			                	</s:if>
			                	<s:else></s:else>            
			                </td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="car_queryList">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %> 
			</s:form>
		</div>
	</div>
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="<%=basePath%>js/DatePicker/WdatePicker.js"></script>
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>	
	<script type="text/javascript" src="<%=basePath%>js/common.js"></script>	
	


	<script type="text/javascript">
		$(function(){
	        $("#register").click(function(){
	            self.location.href='car_addUI.action?actionFlag=register';
	        });
	    })
	</script>
</body>
</html>
