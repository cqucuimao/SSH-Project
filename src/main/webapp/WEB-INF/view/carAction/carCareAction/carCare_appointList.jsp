<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cqu" uri="//WEB-INF/tlds/cqu.tld" %>
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
				    <td class="on"><s:a action="carCare_appointList" href="#"><span>预约车辆保养</span></s:a></td>
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
						<s:a cssClass="buttonA" action="carCare_appoint">保养预约</s:a>
					</td>
					<th><s:property value="tr.getText('car.CarCare.car')" /></th>
					<td>
						<cqu:carSelector name="car"/>
					</td>
					<th><s:property value="tr.getText('car.CarCare.driver')" /></th>
					<td>
						<cqu:userSelector name="driver"/>
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
							<th><s:property value="tr.getText('car.CarCare.car')" /></th>
							<th><s:property value="tr.getText('car.CarCare.driver')" /></th>
              				<th><s:property value="tr.getText('car.CarCare.date')" /></th>
              				<th><s:property value="tr.getText('car.CarCare.done')" /></th>
                			<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">				        
						<tr>
							<td><cqu:carDetailList id="${id}" /> </td>
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
