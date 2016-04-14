<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
    <title>角色列表</title>
    <%@ include file="/WEB-INF/view/public/header.jspf" %>
</head>
<body>
<h2>角色管理</h2>
<hr />

<s:form id="pageForm" action="role_list">
角色名称查询：<s:textfield size="20" cssClass="TextField" name="name" />
<s:submit/>
</s:form>

<div>
    <table cellspacing="0" cellpadding="0">
       
        <!-- 表头-->
        <thead>
            <tr>
                <td>角色名称</td>
                <td>角色描述</td>
            </tr>
        </thead>
        
        <!--显示数据列表-->
        <tbody>
        
        <s:iterator value="recordList">
            <tr class="">
                <td>${name}&nbsp;</td>
                <td>${description}&nbsp;</td>
                <td>
                	<s:a action="role_delete?id=%{id}" onclick="return delConfirm()">删除</s:a>
                    <s:a action="role_editUI?id=%{id}">修改</s:a>
                    <s:a action="role_setPrivilegeUI?id=%{id}">设置权限</s:a>
                </td>
            </tr>
        </s:iterator> 
            
        </tbody>
    </table>
    
    <!-- 其他功能超链接 -->
    <div>
    	<s:a action="role_addUI">增加角色</s:a>
    </div>
</div>
<hr />
<%-- 分页信息 --%>
<%@ include file="/WEB-INF/view/public/pageView.jspf" %>


<s:debug></s:debug>
</body>
</html>
