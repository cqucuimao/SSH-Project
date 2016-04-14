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
	<title>订单</title>
	<link rel="stylesheet" type="text/css" href="skins/main.css">
</head>
<body class="minW">
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1></h1>
		</div>
		<div class="tab_next style2">
			<table>
				<tr>
					<td><s:a action="completedOrderStatistic_list"><span>今日统计</span></s:a></td>
				    <td><s:a action="completedOrderStatistic_weekList"><span>本周统计</span></s:a></td>
				    <td><s:a action="completedOrderStatistic_monthList"><span>本月统计</span></s:a></td>
				    <td class="on"><s:a action="completedOrderStatistic_query"><span>查询</span></s:a></td>
				</tr>
			</table>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="completedOrderStatistic_query">
			<table>
				<tr style="height:20px;">
					<td colspan="3"></td>
					<!--  
					<td>
						<input id="todayStatistic" class="inputButton" type="button" value="今日统计"/>
					</td>
					<td>
						<input id="weekStatistic" class="inputButton" type="button" value="本周统计"/>
					</td>
					<td>
						<input id="monthStatistic" class="inputButton" type="button" value="本月统计"/>
					</td>
					<td>
						<input id="query" class="inputButton" type="button" value="查询"/>
					</td>
					-->
				</tr>
				<tr>
					<th>完成时间:</th>
					<td>
						<input class="Wdate half" name="date1" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
						-
						<input class="Wdate half" name="date2" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<th>单位名称:</th>
					<td>
						<s:textfield id="customer_organization_name" name="name" cssClass="inputText" type="text"/>
						 <!-- 
						 <input id="customer_organization_name" type="text" style="padding:4px; width:16em; margin-right:10px">
						  -->
						 <input id="customer_organization_id" type="hidden">
					</td>
					<th>联系人:</th>
					<td>
						<s:textfield name="customer.name" cssClass="inputText" type="text"/>
					</td>
					<th>金额(元):</th>
					<td>
						<s:textfield name="money1" cssClass="inputText" type="text"/>
						-
						<s:textfield name="money2" cssClass="inputText" type="text"/>
					</td>
					
					<!-- 
					<th>司机姓名:</th>
					<td>
						<s:textfield cssClass="inputText" id="driverName" type="text" name="car.driver.name"/>
						<s:textfield id="driverId" name="driverId" type="hidden"/>
					</td>
					 
					<td>
						<input class="inputButton" type="submit" value="查询"/>
					</td>
					-->
				</tr>
				<tr>
					<th>车牌号:</th>
					<td>
						<s:textfield id="car_platenumber" cssClass="inputText inputChoose" onfocus="this.blur();" name="plateNumber" type="text" />
					</td>
					<th>司机姓名:</th>
					<td>
						<s:textfield cssClass="inputText" id="driverName" type="text" name="car.driver.name"/>
						<s:textfield id="driverId" name="driverId" type="hidden"/>
					</td>
					<th>订单状态:</th>
					<td>
						<s:select name="status" list="{'所有状态','在队列','已调度','已接受','已开始','已结束','已付费','已取消'}"/>
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
							<th>订单号</th>
							<th>单位</th>
							<th>联系人</th>
							<th>计费方式</th>
							<th>开始时间</th>
							<th>结束时间</th>
							<th>司机</th>
              				<th>车型</th>
                			<th>状态</th>
                			<th>金额(元)</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${sn}</td>
							<td>${customerOrganization.name}</td>
							<td>${customer.name}</td>
							<td>${chargeMode}</td>
							<td style="text-align:right"><s:date name="planBeginDate" format="yyyy-MM-dd"/></td>
							<td style="text-align:right"><s:date name="planEndDate" format="yyyy-MM-dd"/></td>
							<td>${driver.name}</td>
							<td>${serviceType.title}</td>
							<td>${status}</td>
							<td style="text-align:right"><fmt:formatNumber value="${orderMoney}" pattern="#0"/></td>
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
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
</body>
</html>
