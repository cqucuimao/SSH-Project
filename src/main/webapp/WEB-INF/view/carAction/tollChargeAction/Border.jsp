<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();//显示根目录
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

    	<s:if test="jqueryNeed">
        	<script type="text/javascript" src="js/jquery.js"></script>
    	</s:if>
    	<div id="submiting" style="position: absolute; top: 0; left: 0; z-index: 10; display: none">
        	<table WIDTH="100%" BORDER="0" CELLSPACING="0" CELLPADDING="0">
            	<tr height="180">
                	<td>&nbsp;</td>
            	</tr>
            	<tr>
                	<td width="30%"></td>
                	<td bgcolor="#4169E1">
                   		<table WIDTH="100%" height="70" BORDER="0" CELLSPACING="2" CELLPADDING="0">
                        	<tr>
                            	<td bgcolor="#DDE9F5" align="center">正在处理页面请求, 请稍候...</td>
                        	</tr>
                    	</table>
                	</td>
                	<td width="30%"></td>
            	</tr>
        	</table>
    	</div>
    	<div id="cover" style="position: absolute; top: 0; left: 0; z-index: 9; display: none; width: 100%; height: 100%">
        	<table WIDTH="100%" height="100%" BORDER="0" CELLSPACING="0" CELLPADDING="0">
            	<tr>
                	<td align="center"><br/>
                	</td>
            	</tr>
        	</table>
    	</div>
    	<div id="position">
    		<%-- <span><img src="${asset:context:/skin/images/positionIcon.gif}" width="9" height="12" align="absmiddle"/> 当前位置：</span> --%>
        	<s:iterator source="locations" value="location" index="index">
            	<s:if test="linkable">
                	<s:a action="prop:pageName">${location}</s:a>
                	<s:else>
                    	${location}
                	</s:else>
            	</s:if>
            	<s:if test="hasMoreLocation">
                	&nbsp;&gt;&nbsp;
            	</s:if>
        	</s:iterator>
    	</div>
    	<s:if test="hasMessages">
        	<div class="operNote">${messages}</div>
    	</s:if>
    	<div id="contents" style="width:97%">
        	<%--  <s:body/>  --%>
    	</div>
    
    <input type="hidden" value="${awokePage}" id="awokePage"/>
    <script>
    	registerClick();
    </script>
</body>
</html>