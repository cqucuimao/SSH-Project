<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>外派员工管理</h1>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="user_queryDispatchList">
			<table>
				<tr>
					<th><s:property value="tr.getText('privilege.User.name')" /></th>
					<td><s:textfield cssClass="inputText" name="name" type="text" /></td>
					<th><s:property value="tr.getText('privilege.User.phoneNumber')" /></th>
					<td><s:textfield cssClass="inputText" name="phoneNumber" type="text" /></td>
					<td>
						<input class="inputButton" type="submit" value="查询" />
					</td>
					<td>
						<s:a cssClass="buttonA" action="user_addDispatchUI">添加外派员工</s:a>
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
              				<th><s:property value="tr.getText('privilege.User.name')" /></th>
                			<th><s:property value="tr.getText('privilege.User.phoneNumber')" /></th>
			                <th>相关操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${name}</td>
                			<td>${phoneNumber}</td>
							<td>
								<s:a action="user_editDispatchUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
								<s:if test="canDeleteUser"> 
									<s:a action="user_deleteDispatch?id=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
								</s:if>
			                </td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="user_freshDispatchList">
				<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
	<script type="text/javascript">
	</script>
</cqu:border>
