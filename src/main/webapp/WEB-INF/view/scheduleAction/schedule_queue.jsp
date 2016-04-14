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
	<link rel="stylesheet" type="text/css" href="<%=basePath %>skins/main.css">
</head>
<body class="minW">
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>待调度队列列表</h1>
		</div>
		<div class="editBlock search">
		<form>
			<table>
				<tr>
					<th><h2>有&nbsp;<font style="font-style: normal;font-size: 25px;color: #58cf90;">${queueSize}</font>&nbsp;条订单待调度</h2></th>
					<td>&nbsp;&nbsp;</td>
					<td>
						<s:if test="canDistributeOrderToUser">
							<s:a action="schedule_dispatchOrder">
								<input type="button" class="inputButton" value="调  度"/>
							</s:a>
						</s:if>
					</td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td>
						<s:a action="schedule_watchKeeper">
							<input type="button" class="inputButton" value="设置值班模式"/>
						</s:a>
					</td>
				</tr>
			</table>
		</form>
		</div>
		<div class="dataGrid">
			<div class="tableWrap">
				<table>
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
							<th >订单号</th>
							<th>客户姓名</th>
							<th>单位</th>
							<th>计费方式</th>
							<th>开始时间</th>
							<th>车型</th>
							<th>上车点</th>
							<th>下车点</th>
							<th class="alignCenter">操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
					<s:iterator value="recordList">
						<tr>
							<td>${sn}</td>
							<td>${customer.name}</td>
							<td>${customerOrganization.name}</td>
							<td>${chargeModeString }</td>
							<td><s:date name="planBeginDate" format="yyyy-MM-dd HH:mm"/></td>
							<td>${serviceType.title}</td>
							<td>${fromAddress.description}</td>
							<td>${toAddress.description}</td>
							<td class="alignCenter">
								<s:a action="schedule_scheduleFromQueue?scheduleFromQueueOrderId=%{id}"><i class="icon-operate-schedule" title="调度"></i></s:a>
								<s:a action="schedule_cancelOrder?cancelOrderId=%{id}" onclick="return confirm('确认要取消该订单吗？');"><i class="icon-operate-delete" title="取消"></i></s:a>
							</td>
						</tr>
						</s:iterator>
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="schedule_queue">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>		
	<script src="js/jquery-1.7.2.js"></script>
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>	
	<script type="text/javascript">
		function refresh(){
			window.location.reload();
		}
		$(document).ready(function () {
			setInterval(refresh,10000); 
		}); 
	</script>
</body>
</html>