<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>利用输入校验完成文件过滤</title>
</head>
<body>
<s:fielderror/>
<s:form action="signature_upload" enctype="multipart/form-data">
	<s:hidden name="username" value="admin"></s:hidden>
	<s:hidden name="pwd" value="admin"></s:hidden>
	<s:hidden name="orderId" value="1"></s:hidden>
	<s:textfield name="title" label="文件标题"/><br>
	<s:file name="upload" label="选择文件"/>
	<s:submit value="上传"/>
</s:form>
</body>
</html>