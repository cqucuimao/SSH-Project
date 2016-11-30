<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<style>
	.SelectStyle {
		width: 250px;
		BORDER-TOP: #91c0e3 1px solid;
		BORDER-BOTTOM: #91c0e3 1px solid;
		BORDER-LEFT: #91c0e3 1px solid;
		BORDER-RIGHT: #91c0e3 1px solid;
		color: #004779;
		font-family: "Tahoma";
	}
	</style>
	<h2>设置角色权限</h2>
	<hr />
	<s:form action="role_setPrivilege">
		<s:hidden name="id"></s:hidden>
		<!-- 权限列表 -->
		<div>
			<s:select name="privilegeIds" cssClass="SelectStyle" multiple="true"
				size="15" list="privilegeList" listKey="id" listValue="name" />
		</div>
		<hr>
		<!-- 表单操作 -->
		<div>
			<input type="submit" value="提交" />
			<a href="javascript:history.go(-1);">返回</a>
		</div>
	</s:form>
</cqu:border>
