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
        <div class="msgFalse" >
            <dl>
                <dt>对不起，由于超时，你正在调度的订单被剥夺！</dt>
                <dd>
                </dd>
                <dd>
                    	您可以返回
                    	&nbsp;<s:a action="schedule_queue">待调度队列</s:a>&nbsp;
                    	重新选取订单
                </dd>
            </dl>
        </div>
    </div>
<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/common.js"></script>
</body>
</html>
