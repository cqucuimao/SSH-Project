<%@page import="com.yuqincar.domain.privilege.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>skins/main.css">
</head>

<body class="minW">
<div class="space"> 
  <!-- 标题 -->
  <div class="title">
    <h1>欢迎光临${sessionScope.company.name }综合业务管理系统</h1>
    <s:property value="%{session.user}"/> 
    <p></p>
  </div>
  <div class="homeInfo"> <img width="80" height="80" src="skins/images/icon_userInfo.png" />
    <div class="form-group"> <span class="label">Name:</span> <span><s:property value="#session.user.name"/></span> </div>
    <div class="form-group"> <span class="label">手机号:</span> <span><s:property value="#session.user.phoneNumber"/></span> </div>
    <div class="form-group"> <span class="label"></div>
  </div>
  <br/><br/><br/>
  <ul class="float homeLink">
  	<s:if test="canShowScheduleBlock">
    <li>
      <div class="inside">
        <div class="homeLink1">
          	<h1>车辆调度</h1>
		</div>
        <p><s:a action="schedule_queue">目前有&nbsp;&nbsp;${orderCountInQueue}&nbsp;&nbsp;个订单等待调度</s:a></p>
      </div>
    </li>
    </s:if>
  	<s:if test="canShowWarningBlock">
    <li>
      <div class="inside">
        <div class="homeLink2">
          <h1>车辆监控</h1>
        </div>
        <p><s:a action="realtime_home">目前有&nbsp;&nbsp;${warningCount}&nbsp;&nbsp;个监控报警</s:a></p>
      </div>
    </li>
    </s:if>
  </ul>
</div>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script> 
<script type="text/javascript" src="../js/artDialog4.1.7/artDialog.js"></script> 
<script type="text/javascript" src="../js/artDialog4.1.7/plugins/iframeTools.js"></script> 
<script type="text/javascript" src="../js/common.js"></script>
</body>
</html>