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
	<title>对账单</title>
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
					<td class="on"><s:a action="accountsReceivableStatistic_list"><span>今日统计</span></s:a></td>
				    <td><s:a action="accountsReceivableStatistic_weekList"><span>本周统计</span></s:a></td>
				    <td><s:a action="accountsReceivableStatistic_monthList"><span>本月统计</span></s:a></td>
				    <td><s:a action="accountsReceivableStatistic_query"><span>查询</span></s:a></td>
				</tr>
			</table>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="accountsReceivableStatistic_list">
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
							<th>对账单名称</th>
							<th>单位名称</th>
              				<th>起止时间</th>
              				<th>订单数</th>
                			<th>收款时间</th>
                			<th>金额(元)</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${name}</td>
							<td>${customerOrganization.name}</td>
							<td style="text-align:right"><s:date name="fromDate" format="yyyy-MM-dd"/>&nbsp;&nbsp;-&nbsp;&nbsp;<s:date name="toDate" format="yyyy-MM-dd"/></td>
							<td style="text-align:right">${orderNum}</td>
							<td style="text-align:right"><s:date name="date" format="yyyy-MM-dd"/></td>
							<td style="text-align:right"><fmt:formatNumber value="${totalMoney}" pattern="#0"/></td>
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
