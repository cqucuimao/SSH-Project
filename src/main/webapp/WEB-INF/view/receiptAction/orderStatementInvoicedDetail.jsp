<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>已开票对账单信息</h1>
			<p>&nbsp;&nbsp;</p>
		</div>
		<div>
		<s:a action="orderStatement_gatherMoney" href="#">
		    <input type="button" class="inputButton" id="gatherMoney" value="收款" />
		</s:a>
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
			非协议订单<br/><br/>
			<div class="tableWrap">
				<table id="dataTable">
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
				<br/><br/>协议订单<br/><br/>
			<div class="tableWrap">
				<!-- 协议订单 -->
				<table id="dataTable2">
					<thead>
						<tr>
							<th>单位</th>
							<th>乘车人</th>
							<th>计费方式</th>
							<th>车型</th>
							<th>计费起始日期</th>
							<th>计费结束日期</th>
							<th>金额(元)</th>
						</tr>
					</thead>
					<tbody class="tableHover">
						<s:iterator value="popoList">
							<tr>
								<td>${order.customerOrganization.name}</td>
								<td>${order.customer.name}</td>
	                			<td>${order.status.label}</td>
								<td>${order.serviceType.title}</td>
								<td><s:date name="fromDate" format="yyyy-MM-dd"/></td>
	                			<td><s:date name="toDate" format="yyyy-MM-dd"/></td>
								<td><fmt:formatNumber value="${money}" pattern="#.0"/></td>
							</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
	   </div>
	</div>
	<script type="text/javascript">
	     $("#gatherMoney").click(function(){
	        self.location.href='orderStatement_gatherMoney.action?orderStatementId=${id}';
	     });
	</script>
</cqu:border>
