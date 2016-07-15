<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <link href="<%=basePath %>skins/main.css" rel="stylesheet" type="text/css" />
</head>
<body>
    <div id="msg" >
        <!--失败提示-->
        <div class="msgFalse" >
            <dl>
                <dt>导入失败，请参考Excel模板，仔细检查格式是否正确！</dt>
                <dd>
                    <a href="#" onclick="location.href='carRefuel_list.action';">返回</a>
                </dd>
            </dl>
        </div>
    </div>
<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/common.js"></script>
<script type="text/javascript">
</script>
</body>
</html>
