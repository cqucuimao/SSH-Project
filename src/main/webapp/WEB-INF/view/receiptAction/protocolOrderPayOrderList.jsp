<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1> 协议订单收款单</h1>
			<p>&nbsp;&nbsp;</p>
		</div>
		<div class="tab_next style2">
			<table>
				<tr>
					<td class="on"><a href="#" class="coverOff"><span>协议订单收款管理</span></a></td>
				    <td><s:a action="popoAddStatement_list"><span>添加到对账单</span></s:a></td>
				</tr>
			</table>
		</div>
		<br/>
		
		<div class="editBlock search">
		<s:form action="protocolOrderPayOrder_MangerQueryForm">
			<table>
				<tr>
				    <td>
						<s:a cssClass="buttonA" action="protocolOrderPayOrder_addUI.action?actionFlag=register">新增</s:a>
					</td>
					<th>订单号</th>
					<td>
						<s:textfield name="protocolOrderSn" cssClass="inputText" type="text"/>
					</td>
					<th>客户单位</th>
					<td>
						<cqu:customerOrganizationSelector name="protocolOrderCustomerOrganization"/>
					</td>	
					<th>是否收款</th>
					<td>
						<s:select name="protocolOrderPaid" list="{'全部','否','是'}"></s:select>
					</td>
					<td>
						<input class="inputButton" type="submit" value="查询" name="button" />
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
					</colgroup>
					<thead>
						<tr>
							<th>订单</th>
							<th>单位</th>
							<th>客户</th>
							<th>联系方式</th>
							<th>车型</th>
							<th>执行车辆</th>
							<th>执行司机</th>
							<th>计费起止日期</th>
							<th>金额</th>
							<th>所属收款单</th>
							<th>是否收款</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover" id="htcList">
					<s:iterator value="recordList"> 
					<tr>
					    <td>${order.sn}</td>
					   <td>${order.customerOrganization.name}</td>
						   <td>${order.customer.name}</td>
						   <td>${order.phone}</td>
						   <td>${order.serviceType.title}</td>
						   <td>${order.car.plateNumber}</td>
						   <td>${order.driver.name}</td>
						<td><s:date name="fromDate" format="yyyy-MM-dd"/>&nbsp;&nbsp;-&nbsp;&nbsp;<s:date name="toDate" format="yyyy-MM-dd"/></td>
						<td><fmt:formatNumber value="${money}" pattern="#.0"/></td>
						<td>${orderStatement.name}</td>
						<td>
						<s:if test="paid==true">
								<s:text name="是"></s:text>
								</s:if>
								<s:else>
								<s:text name="否"></s:text>
								</s:else>
						</td>
						<td>
						<s:if test="orderStatement==null">
							<s:a action="protocolOrderPayOrder_editUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
                    		<s:a action="protocolOrderPayOrder_delete?id=%{id}" onclick="result=confirm('确认要删除吗？'); if(!result) coverHidden(); return result;">
                    		<i class="icon-operate-delete" title="删除"></i>
                    		</s:a>
                    	</s:if>
                    	<s:else></s:else>
						</td>
					</tr>
					</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="protocolOrderPayOrder_freshList">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
	   </div>
	</div>
	<script type="text/javascript">
	</script>
</cqu:border>
