<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>客户单位列表</h1>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="customerOrganization_queryList">
			<table>
				<tr>
					<td>
						<s:a cssClass="buttonA" action="customerOrganization_addUI">新增客户单位</s:a>
					</td>
					<th><s:property value="tr.getText('order.CustomerOrganization.name')" /></th>
					<td>
						<cqu:customerOrganizationSelector name="customerOrganization"/>
					</td>
					<td>
						<input class="inputButton" type="submit" value="查询"/>
					</td>
				</tr>
			</table>
			</s:form>
		</div>
		<div class="dataGrid">
			<div class="tableWrap">
				<table>
					
					<thead>
						<tr>
							<th><s:property value="tr.getText('order.CustomerOrganization.name')" /></th>
              				<th><s:property value="tr.getText('order.CustomerOrganization.abbreviation')" /></th>
              				<th><s:property value="tr.getText('order.CustomerOrganization.manager')" /></th>
                			<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${name}</td>
							<td>${abbreviation}</td>
							<td>${manager.name}</td>
							<td>
								<s:a action="customerOrganization_editUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
								<s:a action="customerOrganization_addFinancial?id=%{id}"><i class="icon-operate-detail" title="财务要求"></i></s:a>
                    			<s:a action="customer_checkPeople?customerOrganizationId=%{id}"><i class="icon-operate-copy" title="查看人员"></i></s:a>
								<s:if test="canDelete">
                    			<s:a action="customerOrganization_delete?id=%{id}" onclick="result=confirm('确认要删除吗？'); if(!result) coverHidden(); return result;"><i class="icon-operate-delete" title="删除"></i></s:a>
                    			</s:if>
                    			<s:else></s:else>
                			</td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="customerOrganization_freshList">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
	<script type="text/javascript">
	</script>
</cqu:border>
