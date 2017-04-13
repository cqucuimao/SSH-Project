<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>订单管理</h1>
		</div>
		<div class="tab_next style2">
			<table>
				<tr>
					<td><s:a action="order_orderManager"><span>订单列表</span></s:a></td>					
				    <td><s:a action="order_unAcceptedOrderRemind"><span>还未接受的订单</span></s:a></td>
				    <td class="on"><a href="#" class="coverOff"><span>协议订单到期提醒</span></a></td>
				</tr>
			</table>
		</div>
		<br/>
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
						<col width="150"></col>
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
							<s:a action="order_print.action?sn=%{sn}"><i class="icon-operate-print" title="打印"></i></s:a>
							</td>
						</tr>
						</s:iterator>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</cqu:border>