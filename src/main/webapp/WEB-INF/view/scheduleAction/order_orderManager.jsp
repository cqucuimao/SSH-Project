<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cqu" uri="//WEB-INF/tlds/cqu.tld" %>
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
					<th><s:property value="tr.getText('order.Order.sn')" /></th>
					<td>
						<s:textfield name="sn" cssClass="inputText" type="text"/>
					</td>
					<th><s:property value="tr.getText('order.Order.customerOrganization')" /></th>
					<td>
						<s:textfield id="customer_organization_name" name="customerOrganizationName" cssClass="inputText" type="text"/>
					    <input id="customer_organization_id" type="hidden">
					</td>					
					<th><s:property value="tr.getText('order.Order.driver')" /></th>
					<td>
						<cqu:userSelector name="driver"/>						
					</td>
					<th>计划起止时间</th>
					<td>
						<s:textfield name="planBeginDate" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
						- 
						<s:textfield name="planEndDate" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<th><s:property value="tr.getText('order.Order.status')" /></th>
					<td>
						<s:select name="status" list="{'所有状态','在队列','已调度','已接受','已开始','已上车','已下车','已结束','已付费','已取消'}"></s:select>
					</td>
					<td>
						<input class="inputButton" type="submit" value="查询" name="button" />
					</td>
					<td>
						<input class="inputButton" id="btn" type="button" value="详细查询" name="submit" />
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
						<col></col>
						<col></col>
						<col width="160"></col>
					</colgroup>
					<thead>
						<tr>
							<th><s:property value="tr.getText('order.Order.sn')" /></th>
							<th><s:property value="tr.getText('order.Order.customerOrganization')" /></th>
							<th><s:property value="tr.getText('order.Order.customer')" /></th>
							<th><s:property value="tr.getText('order.Order.chargeMode')" /></th>
							<th>计划起止时间</th>
							<th>实际起止时间</th>
							<th><s:property value="tr.getText('order.Order.serviceType')" /></th>
							<th><s:property value="tr.getText('order.Order.car')" /></th>
							<th><s:property value="tr.getText('order.Order.driver')" /></th>
							<th><s:property value="tr.getText('order.Order.saler')" /></th>
							<th><s:property value="tr.getText('order.Order.status')" /></th>
							<th class="alignCenter">操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
					<s:iterator value="recordList">
						<tr>
							<td class="alignCenter">${sn }</td>
							<td>${customerOrganization.name }</td>
							<td>${customer.name }</td>
							<td>${chargeMode.label }</td>
							<td>${planDateString}</td>
							<td>${actualDateString}</td>
							<td>${serviceType.title }</td>	
							<td>${car.plateNumber }</td>	
							<td>${driver.name }</td>
							<td>${saler.name }</td>						
							<td>${status.label }</td>
							<td class="alignCenter">
							<s:a action="order_view.action?orderId=%{id}"><i class="icon-operate-detail" title="查看"></i></s:a>
							<s:a action="order_print.action?orderId=%{id}"><i class="icon-operate-print" title="打印"></i></s:a>
							<s:if test="canEditDriverAction"> 
								<s:a action="order_editDriverAction.action?orderId=%{id}"><i class="icon-operate-modify" title="编辑司机动作"></i></s:a>
							</s:if>
							<s:if test="canEditOrderBill"> 
								<s:a action="order_editOrderBillUI.action?orderId=%{id}"><i class="icon-operate-modify" title="编辑派车单"></i></s:a>
							</s:if>
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
	<script>
		$("#btn").click(function(){
			self.location.href='order_queryUI.action';
		});
	</script>
</body>
</html>