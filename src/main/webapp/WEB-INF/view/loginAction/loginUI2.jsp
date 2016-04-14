<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv=Content-Type content="text/html; charset=gbk" />
	<title></title>
	<%@ include file="/WEB-INF/view/public/header.jspf" %>
	<script type="text/javascript">
		if( window.parent != window ){
			window.parent.location.href = window.location.href;
		}
	</script>
</head>

<body >
<s:form action="login_login" validate="true">
	用户名：<s:textfield size="20" cssClass="TextField" name="loginName" /><s:property value="errors['loginName'][0]" /><br />
	密码：<s:password size="20" cssClass="TextField" showPassword="false" name="password" /><s:property value="errors['password'][0]" /><br />
	<%-- 显示错误消息 --%>
	<div style="color:red">
		<s:property value="errors['loginError'][0]"/>
	</div>
	<s:fielderror></s:fielderror>
	<s:submit/>
</s:form>

<s:debug></s:debug>	

</body>

</html>

