<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>已收款对账单信息</h1>
			<p>&nbsp;&nbsp;</p>
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
						</tr>
					</thead>
					<tbody class="tableHover">
					 <s:iterator value="orderList">
						<tr>
							<td>${customerOrganization.name}</td>
							<td>${customer.name}</td>
                			<td>${status.label}</td>
							<td>${carServiceType.title}</td>
							<td><s:date name="actualBeginDate" format="yyyy-MM-dd HH:mm"/>&nbsp;&nbsp;-&nbsp;&nbsp;<s:date name="actualEndDate" format="yyyy-MM-dd HH:mm"/></td>
							<td>${fromAddress}-${fromAddress}</td>
							<td>${actualMile}</td>
                			<td>${actualDay}</td>
							<td><fmt:formatNumber value="${actualMoney}" pattern="#.0"/></td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
	   </div>
	   <br/>
	   <br/>
	   <a class="p15" href="javascript:history.go(-1);">返回</a>
	</div>
</cqu:border>
