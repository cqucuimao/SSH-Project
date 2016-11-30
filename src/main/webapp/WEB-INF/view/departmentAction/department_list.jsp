<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>部门管理</h1>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="department_list">
			<table>
				<tr>
					<th><s:property value="tr.getText('privilege.Department.name')" /></th>
					<td><s:textfield cssClass="inputText" name="name" type="text" /></td>
					<td>
						<input class="inputButton" type="submit" value="查询"/>
					</td>
					<td>
						<s:a cssClass="buttonA" action="department_addUI">添加部门</s:a>
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
							<th><s:property value="tr.getText('privilege.Department.name')" /></th>
              				<th><s:property value="tr.getText('privilege.Department.description')" /></th>
			                <th>相关操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${name}</td>
							<td width="40%">${description}</td>
							<td>
							    <s:a action="department_editUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
							    <s:if test="canDeleteDepartment">
			                		<s:a action="department_delete?id=%{id}" onclick="result=confirm('确认要删除吗？'); if(!result) coverHidden(); return result;"><i class="icon-operate-delete" title="删除"></i></s:a>
			                	</s:if>
			                </td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
		</div>
	</div>
	<script type="text/javascript">
	</script>
</cqu:border>
