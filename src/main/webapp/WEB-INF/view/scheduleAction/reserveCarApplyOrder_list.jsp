<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>扩充常备车库申请</h1>
		</div>
		<div class="editBlock search">
			<table>
				<tr>
					<td>
						<s:a cssClass="buttonA" action="reserveCarApplyOrder_addUI?actionFlag=add">新建申请</s:a>
					</td>
				</tr>
			</table>
		</div>
		<div class="dataGrid">
			<div class="tableWrap">
				<table>
					
					<thead>
						<tr>
							<th><s:property value="tr.getText('order.ReserveCarApplyOrder.proposer')" /></th>
              				<th><s:property value="tr.getText('order.ReserveCarApplyOrder.carCount')" /></th>
                			<th><s:property value="tr.getText('order.ReserveCarApplyOrder.fromDate')" /></th>
                			<th><s:property value="tr.getText('order.ReserveCarApplyOrder.toDate')" /></th>
                			<th><s:property value="tr.getText('order.ReserveCarApplyOrder.newTime')" /></th>
                			
                			<th><s:property value="tr.getText('order.ReserveCarApplyOrder.status')" /></th>
			                <th>相关操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${proposer.name }</td>
							<td>${carCount }</td>
							<td><s:date name="fromDate" format="yyyy-MM-dd"/></td>
                			<td><s:date name="toDate" format="yyyy-MM-dd"/></td>
							<td><s:date name="newTime" format="yyyy-MM-dd HH:mm"/></td>
							<td>${status.label }</td>
							<td>
							<s:a action="reserveCarApplyOrder_view?id=%{id}"><i class="icon-operate-detail" title="查看"></i></s:a>
							<!-- 新建的可以删除，被驳回的可以删除-->
							<s:if test="canDelete">	
								<s:a action="reserveCarApplyOrder_delete?id=%{id}" onclick="result=confirm('确认要删除吗？'); if(!result) coverHidden(); return result;"><i class="icon-operate-delete" title="删除"></i></s:a>
							</s:if>	
							<!-- 新建状态的才能修改，已经提交的不能修改-->
							<s:if test="canUpdate">		
								<s:a action="reserveCarApplyOrder_editUI?id=%{id}&actionFlag=edit"><i class="icon-operate-edit" title="修改"></i></s:a>
							</s:if>
							<!-- 已提交审核的才能审核 -->
							<s:if test="canApprove">	
								<s:a action="reserveCarApplyOrder_approveUI?id=%{id}&actionFlag=approve"><i class="icon-operate-password" title="审核"></i></s:a>
							</s:if>
							<!-- 审核通过的才能配置车辆 -->
							<s:if test="canConfigCar">			
								<s:a action="reserveCarApplyOrder_approveCarUI?id=%{id}&actionFlag=approveCar"><i class="icon-operate-password" title="审批车辆"></i></s:a>
							</s:if>
							<!-- 审核通过的才能配置司机 -->
							<s:if test="canConfigDriver">							
								<s:a action="reserveCarApplyOrder_approveDriverUI?id=%{id}"><i class="icon-operate-password" title="审批司机"></i></s:a>
							</s:if>	
			                </td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="reserveCarApplyOrder_freshList">
				<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
</cqu:border>
