<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>用户管理</h1>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="user_queryList">
			<table>
				<tr>
					<th><s:property value="tr.getText('privilege.User.name')" /></th>
					<td><s:textfield cssClass="inputText" name="name" type="text" /></td>
					<td>
						<input class="inputButton" type="submit" value="查询" />
					</td>
					<td>
						<s:a cssClass="buttonA" action="user_addUI">添加用户</s:a>
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
							<th><s:property value="tr.getText('privilege.User.loginName')" /></th>
              				<th><s:property value="tr.getText('privilege.User.name')" /></th>
                			<th><s:property value="tr.getText('privilege.Department.name')" /></th>
                			<th><s:property value="tr.getText('privilege.User.roles')" /></th>
                			<th><s:property value="tr.getText('privilege.User.phoneNumber')" /></th>
                			<th><s:property value="tr.getText('privilege.User.email')" /></th>
			                <th><s:property value="tr.getText('privilege.User.description')" /></th>
			                <th>相关操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${loginName}</td>
							<td>${name}</td>
							<td>${department.name}</td>
							<td width="40%">
								<s:iterator value="roles">
                					${name}
                				</s:iterator>
                			</td>
                			<td>${phoneNumber}</td>
							<td>${email}</td>
							<td>${description}&nbsp;</td>
							<td>
								<s:a action="user_editUI?id=%{id}&actionFlag=edit"><i class="icon-operate-edit" title="修改"></i></s:a>
			                </td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="user_freshList">
				<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
	<script type="text/javascript">
	</script>
</cqu:border>
