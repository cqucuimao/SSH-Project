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
					<td class="on"><a href="#" class="coverOff"><span>订单列表</span></a></td>
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
						<cqu:customerOrganizationSelector name="customerOrganization"/>
					</td>					
					
					<th><s:property value="tr.getText('order.Order.driver')" /></th>
					
					<td>
						<cqu:userAutocompleteSelector name="driver"/> 				
					</td>
					<th>计划起止时间</th>
					<td>
						<s:textfield name="planBeginDate" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
						- 
						<s:textfield name="planEndDate" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<th><s:property value="tr.getText('order.Order.status')" /></th>
					<td>
						<s:select name="status" list="{'所有状态','未完成','在队列','已调度','已接受','已开始','已上车','已下车','已结束','已付费','已取消'}"></s:select>
					</td>
					<td>
						<input class="inputButton" type="submit" value="查询" name="button" />
					</td>
					<td>
						<s:a cssClass="buttonA" action="order_queryUI">详细查询</s:a>
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
							<td class="alignCenter" style="color:${orderColor};">${sn }</td>
							<td style="color:${orderColor};">${customerOrganization.name }</td>
							<td style="color:${orderColor};">${customer.name }</td>
							<td style="color:${orderColor};">${chargeMode.label }</td>
							<td style="color:${orderColor};">${planDateString}</td>
							<td style="color:${orderColor};">${actualDateString}</td>
							<td style="color:${orderColor};">${serviceType.title }</td>	
							<td style="color:${orderColor};">${car.plateNumber }</td>	
							<td style="color:${orderColor};">${driver.name }</td>
							<td style="color:${orderColor};">${saler.name }</td>						
							<td style="color:${orderColor};">${status.label }</td>
							<td class="alignCenter" style="color:${orderColor};">
							<s:a action="order_view.action?orderId=%{id}"><i class="icon-operate-detail" title="查看"></i></s:a>
							<s:a action="order_print.action?orderId=%{id}"><i class="icon-operate-print" title="打印"></i></s:a>
							<!-- 
							<s:if test="canEditDriverAction"> 
								<s:a action="order_editDriverAction.action?orderId=%{id}"><i class="icon-operate-password" title="编辑司机动作"></i></s:a>
							</s:if>
							 -->
							<s:if test="canEditOrderBill"> 
								<s:a action="order_editOrderBillUI.action?orderId=%{id}"><i class="icon-operate-edit" title="编辑派车单"></i></s:a>
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
</cqu:border>