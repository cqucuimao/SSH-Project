<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
	<title>用户信息</title>
    <%@ include file="/WEB-INF/view/public/header.jspf" %>
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
<h2>用户信息</h2>
<hr>
<!--显示表单内容-->
<div id=MainArea>

    <s:form action="user_%{id == null ? 'add' : 'edit'}">
        <s:hidden name="id"></s:hidden>
        
        <!-- 表单内容显示 -->
        <div class="ItemBlockBorder">
            <div class="ItemBlock">
                <table cellpadding="0" cellspacing="0" class="mainForm">
                    <tr><td width="100">所属部门</td>
                        <td>
                        	<s:select name="departmentId" cssClass="SelectStyle"
                        		list="departmentList" listKey="id" listValue="name"
                        		headerKey="" headerValue="选择部门"
                        	/>
                        </td>
                    </tr>
                    <tr><td>登录名</td>
                        <td><s:textfield name="loginName" cssClass="InputStyle"/> *
							
						</td>
                    </tr>
                    <tr><td>姓名</td>
                        <td><s:textfield name="name" cssClass="InputStyle"/> *</td>
                    </tr>
					<tr><td>性别</td>
                        <td>
                        	<s:radio name="gender" list="%{    {'男', '女'}    }"></s:radio>
						</td>
                    </tr>
					<tr><td>联系电话</td>
                        <td><s:textfield name="phoneNumber" cssClass="InputStyle"/></td>
                    </tr>
                    <tr><td>E-mail</td>
                        <td><s:textfield name="email" cssClass="InputStyle"/></td>
                    </tr>
                    <tr><td>备注</td>
                        <td><s:textarea name="description" cssClass="TextareaStyle"></s:textarea></td>
                    </tr>
                </table>
            </div>
        </div>
        
		<div class="">
        	 岗位设置 :
        </div>
        
        <!-- 表单内容显示 -->
        <div>
          	<s:select name="roleIds" cssClass="SelectStyle"
          		multiple="true" size="10" 
          		list="roleList" listKey="id" listValue="name"
          	/>
          	
        </div>		
		<hr>
        <!-- 表单操作 -->
        <div >
            <input type="submit" value="提交"/>
            <a href="javascript:history.go(-1);">返回</a>
        </div>
    </s:form>
</div>

</body>
</html>
