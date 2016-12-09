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
					<td><s:a action="orderStatement_newList"><span>未收款对账单列表</span></s:a></td>
				    <td><s:a action="orderStatement_invoicedList"><span>已开票对账单列表</span></s:a></td>
				    <td class="on"><a href="#" class="coverOff"><span>已收款对账单列表</span></a></td>
				</tr>
			</table>
		</div>
		<br/>
		<div class="editBlock search">
			<s:form id="pageForm" action="orderStatement_paidListQueryForm">
			<table>
				<tr>
					<th>单位名称</th>
					<td>
						<cqu:customerOrganizationSelector name="customerOrganization"/>
					</td>
					<th>对账单时间</th>
					<td>
						<s:textfield class="Wdate half" style="width:120px;" type="text" name="fromDate" id="startTime" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" >
						  <s:param name="value"><s:date name="fromDate" format="yyyy-MM-dd"/></s:param>
						</s:textfield>
						- 
						<s:textfield class="Wdate half" style="width:120px;" type="text" name="toDate" id="endTime" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" >
						  <s:param name="value"><s:date name="toDate" format="yyyy-MM-dd"/></s:param>
						</s:textfield>
                    </td>
                    <td>
						<input class="inputButton" type="submit" value="查询" />
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
					</colgroup>
					<thead>
						<tr>
							<th>对账单名称</th>
							<th>单位</th>
							<th>起止时间</th>
							<th>订单数</th>
							<th>金额(元)</th>
							<th>生成日期</th>
						</tr>
					</thead>
					<tbody class="tableHover" id="htcList">
					<s:iterator value="recordList">
					<tr>
					    <td><s:a action="orderStatement_paidDetail?orderStatementId=%{id}">${name}</s:a></td>
                		<td>${customerOrganization.name}</td>
						<td><s:date name="fromDate" format="yyyy-MM-dd"/>&nbsp;&nbsp;-&nbsp;&nbsp;<s:date name="toDate" format="yyyy-MM-dd"/></td>
						<td>${orderNum}</td>
						<td><fmt:formatNumber value="${totalMoney}" pattern="#.0"/></td>
						<td><s:date name="date" format="yyyy-MM-dd"/></td>
					</tr>
					</s:iterator> 
					</tbody>
				</table>
			</div>
	   </div>
	   <s:form id="pageForm" action="orderStatement_freshPaidList">
	   		<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
	   </s:form>
	</div>
	<script type="text/javascript">
	</script>
</cqu:border>