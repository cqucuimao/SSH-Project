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
        <!--成功提示-->
        <div class="msgSuccess" >
            <dl>
                <dt>恭喜您，操作成功。</dt>
                <dd class="msg_time">
                    <span></span>秒钟后，自动返回~
                </dd>
                <dd>
                    您也可以快速<br/>
                    <a href="#" onclick="location.href='schedule_scheduling.action';">返回</a>
                </dd>
            </dl>
        </div>
    </div>
<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/common.js"></script>
<script type="text/javascript">
    var mm=5;
    $(function(){
        show_date_time();
        setTimeout("location.href='schedule_scheduling.action';",5000);
    });
    function show_date_time(){ 
        if(mm>0){
            window.setTimeout("show_date_time()", 1000); 
        }
        $(".msg_time>span").text(mm); 
        mm--;
    }
</script>
</body>
</html>
