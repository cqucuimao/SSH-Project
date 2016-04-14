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
					<td class="on"><s:a action="completedOrderStatistic_list.action"><span>今日统计</span></s:a></td>
				    <td><s:a action="completedOrderStatistic_weekList.action"><span>本周统计</span></s:a></td>
				    <td><s:a action="completedOrderStatistic_monthList.action"><span>本月统计</span></s:a></td>
				    <td><s:a action="completedOrderStatistic_query.action"><span>查询</span></s:a></td>
				</tr>
			</table>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="completedOrderStatistic_list">
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
					<th>选择日期:</th>
					<td>
						<s:textfield name="date" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<td>
						<input class="inputButton" type="submit" value="统计"/>
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
