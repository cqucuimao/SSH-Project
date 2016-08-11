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
			<h1>已开票对账单信息</h1>
			<p>&nbsp;&nbsp;</p>
		</div>
		<div>
		    <input type="button" class="inputButton" id="gatherMoney" value="收款" />
            <a class="p15" href="javascript:history.go(-1);">返回</a>
		</div>
		<br/>
		<div>
		</div>
		<div id="totalMoneyDiv" style="text-align:right;">
		      <span style="color:red;font-size:18px;">总金额: </span>
		      <span id="totalPrice" style="color:red;font-size:18px;"></span>
		      <span style="color:red;font-size:18px;">${totalMoney} 元</span>
		</div>
		<div class="dataGrid">
			<div class="tableWrap">
				<table id="dataTable">
					<colgroup>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
					</colgroup>
					<thead>
						<tr>
							<th>单位</th>
							<th>乘车人</th>
							<th>计费方式</th>
							<th>车型</th>
							<th>起止时间</th>
							<th>起止地点</th>
							<th>里程(KM)</th>
							<th>天数(天)</th>
							<th>金额(元)</th>
							<th>实收金额(元)</th>
						</tr>
					</thead>
					<tbody class="tableHover">
					 <s:iterator value="orderList">
						<tr>
							<td>${customerOrganization.name}</td>
							<td>${customer.name}</td>
                			<td>${chargeMode.label}</td>
							<td>${carServiceType.title}</td>
							<td><s:date name="actualBeginDate" format="yyyy-MM-dd HH:mm"/>&nbsp;&nbsp;-&nbsp;&nbsp;<s:date name="actualEndDate" format="yyyy-MM-dd HH:mm"/></td>
							<td>${fromAddress}-${fromAddress}</td>
							<td>${actualMile}</td>
                			<td>${actualDay}</td>
                			<td>${actualTotalMoney}</td>
							<td><fmt:formatNumber value="${actualMoney}" pattern="#.0"/></td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
	   </div>
	</div>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
    <script type="text/javascript" src="js/artDialog4.1.7/jquery.artDialog.source.js"></script>
    <script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>	
	<script type="text/javascript">
	     $("#gatherMoney").click(function(){
	        self.location.href='orderStatement_gatherMoney.action?orderStatementId=${id}';
	     });
	</script>
</body>
</html>
