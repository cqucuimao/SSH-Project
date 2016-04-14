<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
    <title>部门列表</title>
    <%@ include file="/WEB-INF/view/public/header.jspf" %>
</head>
<body>
<h2>部门管理</h2>
<hr />

<s:form id="pageForm" action="department_list">
部门名称查询：<s:textfield size="20" cssClass="TextField" name="name" />
<s:submit/>
</s:form>

<div>
    <table cellspacing="0" cellpadding="0">
       
        <!-- 表头-->
        <thead>
            <tr>
                <td>部门名称</td>
                <td>部门描述</td>
            </tr>
        </thead>
        
        <!--显示数据列表-->
        <tbody>
        
        <s:iterator value="recordList">
            <tr class="">
                <td>${name}&nbsp;</td>
                <td>${description}&nbsp;</td>
                <td>
                	<s:a action="department_delete?id=%{id}" onclick="return delConfirm()">删除</s:a>
                    <s:a action="department_editUI?id=%{id}">修改</s:a>
                </td>
            </tr>
        </s:iterator> 
            
        </tbody>
    </table>
    
    <!-- 其他功能超链接 -->
    <div>
    	<s:a action="department_addUI">增加部门</s:a>
    </div>
</div>
<hr />
<%-- 分页信息 --%>
<%@ include file="/WEB-INF/view/public/pageView.jspf" %>


<s:debug></s:debug>
</body>
</html>
