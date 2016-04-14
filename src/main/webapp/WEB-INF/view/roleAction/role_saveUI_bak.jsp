<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
<title>角色信息</title>
<%@ include file="/WEB-INF/view/public/header.jspf"%>
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
</head>
<body>

	<!-- 标题显示 -->
	<h2>角色信息</h2>
	<hr>
	<!--显示表单内容-->
	<div id=MainArea>

		<s:form action="role_%{id == null ? 'add' : 'edit'}">
			<s:hidden name="id"></s:hidden>

			<!-- 表单内容显示 -->
			<div class="ItemBlockBorder">
				<div class="ItemBlock">
					<table cellpadding="0" cellspacing="0" class="mainForm">
						<tr>
							<td>角色名称</td>
							<td><s:textfield name="name" /> *</td>
						</tr>
						<tr>
							<td>描述</td>
							<td><s:textfield name="description" /> *</td>
						</tr>
					</table>
				</div>
			</div>

			<hr>
			<!-- 表单操作 -->
			<div>
				<input type="submit" value="提交" /> <a
					href="javascript:history.go(-1);">返回</a>
			</div>
		</s:form>
	</div>


</body>
</html>
