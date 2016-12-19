<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>对账单列表</h1>
			<p>&nbsp;&nbsp;</p>
		</div>
		<div class="tab_next style2">
			<table>
				<tr>
				    <td><s:a action="orderStatement_newList"><span>新建对账单列表</span></s:a></td>
					<td class="on"><a href="#" class="coverOff"><span>已开票对账单列表</span></a></td>
				    <td><s:a action="orderStatement_paidList"><span>已收款对账单列表</span></s:a></td>
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
					</colgroup>
					<thead>
						<tr>
							<th>对账单名称</th>
							<th>单位</th>
							<th>起止时间</th>
							<th>订单数</th>
							<th>金额</th>
							<th>开票金额</th>
							<th>实收金额</th>
							<th>生成日期</th>
							<th>状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover" id="htcList">
					<s:iterator value="orderStatementList">
					<tr>
					    <td><s:a action="orderStatement_invoicedDetail?orderStatementId=%{id}">${name}</s:a></td>
                		<td>${customerOrganization.name}</td>
						<td><s:date name="fromDate" format="yyyy-MM-dd"/>&nbsp;&nbsp;-&nbsp;&nbsp;<s:date name="toDate" format="yyyy-MM-dd"/></td>
						<td>${orderNum}</td>
						<td><fmt:formatNumber value="${totalMoney}" pattern="#.0"/></td>
						<td><fmt:formatNumber value="${invoiceMoney}" pattern="#.0"/></td>
						<td><fmt:formatNumber value="${actualTotalMoney}" pattern="#.0"/></td>
						<td><s:date name="date" format="yyyy-MM-dd"/></td>
						<td>${status.label}</td>
						<td><s:a action="orderStatement_generatePDF?id=%{id}" class="coverOff"><i class="icon-operate-export" title="导出"></i></s:a></td>
					</tr>
					</s:iterator> 
					</tbody>
				</table>
			</div>
	   </div>
	</div>
	<script type="text/javascript">
	</script>
</cqu:border>
