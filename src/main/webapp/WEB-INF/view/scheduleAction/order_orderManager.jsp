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
			<h1>订单管理</h1>
		</div>
		<div class="tab_next style2">
			<table>
				<tr>
					<td class="on"><a href="#"><span>订单列表</span></a></td>
				    <td><s:a action="order_unAcceptedOrderRemind"><span>还未接受的订单</span></s:a></td>
				    <td><s:a action="order_protocolOrderRemind"><span>协议订单到期提醒</span></s:a></td>
				</tr>
			</table>
		</div>
		<br/>
		<div class="editBlock search">
		<s:form action="order_orderManagerQueryForm">
			<table>
				<tr>
					<th>订单号</th>
					<td>
						<s:textfield name="sn" cssClass="inputText" type="text"/>
					</td>
					<th>单位</th>
					<td>
						<s:textfield id="customer_organization_name" name="customerOrganizationName" cssClass="inputText" type="text"/>
					    <input id="customer_organization_id" type="hidden">
					</td>					
					<th>司机</th>
					<td>
						<s:textfield class="inputText" id="driverName" type="text" name="driverName"/>
						<s:textfield id="driverId" name="driverId" type="hidden"/>						
					</td>
					<th>计划开始时间</th>
					<td>
						<s:textfield name="planBeginDate" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
						- 
						<s:textfield name="planEndDate" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<th>状态</th>
					<td>
						<s:select name="status" list="{'所有状态','在队列','已调度','已接受','已开始','已结束','已付费','已取消'}"></s:select>
					</td>
					<td>
						<input class="inputButton" type="submit" value="查询" name="button" />&nbsp;&nbsp;&nbsp;&nbsp;
						<!-- <a href="order_toBeEndProtocal.action">
							<input class="inputButton" type="button" value="协议订单到期提醒"/>
						</a>
						-->
					</td>
				</tr>
			</table>
		</s:form>
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
						<col width="150"></col>
					</colgroup>
					<thead>
						<tr>
							<th>订单号</th>
							<th>单位</th>
							<th>姓名</th>
							<th>计费方式</th>
							<th>计划起止时间</th>
							<th>实际起止时间</th>
							<th>司机</th>
							<th>车型</th>
							<th>状态</th>
							<th class="alignCenter">操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
					<s:iterator value="recordList">
						<tr>
							<td class="alignCenter">${sn }</td>
							<td>${customerOrganization.name }</td>
							<td>${customer.name }</td>
							<td>${chargeModeString }</td>
							<td>${planDateString}</td>
							<td>${actualDateString}</td>
							<td>${driver.name }</td>
							<td>${serviceType.title }</td>							
							<td>${statusString }</td>
							<td class="alignCenter">
							<s:a action="order_view.action?orderId=%{id}"><i class="icon-operate-detail" title="查看"></i></s:a>
							<s:a action="order_print.action?orderId=%{id}"><i class="icon-operate-print" title="打印"></i></s:a>
							<s:a action="order_operate.action?orderId=%{id}"><i class="icon-operate-modify" title="编辑司机动作"></i></s:a>
							</td>
						</tr>
						</s:iterator>
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="order_list">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/DatePicker/WdatePicker.js"></script>
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>	
	<script type="text/javascript" src="<%=basePath%>js/common.js"></script>
</body>
</html>