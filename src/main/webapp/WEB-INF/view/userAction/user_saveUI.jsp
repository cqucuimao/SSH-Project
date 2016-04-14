<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link href="skins/main.css" rel="stylesheet" type="text/css" />
<style>
    	
    </style>
</head>
<body class="minW">
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>用户信息</h1>
            <p>编辑用户信息</p>
        </div>
        <div class="editBlock detail p30">
        <s:form action="user_%{id == null ? 'add' : 'edit'}" name="pageForm" id="pageForm">
        	<s:hidden name="id"></s:hidden>
            <table>
                <tbody>
                	<tr>
                        <th>登录名：</th>
                        <td><s:textfield name="loginName"/> *</td>
                    </tr>
                	<tr>
                        <th>姓名：</th>
                        <td><s:textfield name="name" /> *</td>
                    </tr>
                    <tr>
                        <th width="100">所属部门：</th>
                        <td><s:select name="departmentId" cssClass="SelectStyle"
                        		list="departmentList" listKey="id" listValue="name"
                        		headerKey="" headerValue="选择部门"
                        	/>
                        </td>
                    </tr>
                    
                    <tr>
                        <th>联系方式：</th>
                        <td><s:textfield name="phoneNumber" /></td>
                    </tr>
                    <tr>
                        <th>E-mail：</th>
                        <td><s:textfield name="email"></s:textfield></td>
                    </tr>
                    <tr>
                        <th>备忘：</th>
                        <td><s:textarea name="description"></s:textarea></td>
                    </tr>
                    <tr>
                        <th>角色设置：</th>
                        <td>
	                        <s:select name="roleIds" multiple="true" size="10" 
	          				list="roleList" listKey="id" listValue="name"
	          				/>
          				</td>
                    </tr>
                    
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                            <input class="inputButton" type="submit" value="保存" />
                             <a class="p15" href="javascript:history.go(-1);">返回</a>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </s:form>
        </div>
    </div>
    <script type="text/javascript" src="js/jquery-1.7.2.js"></script>
    <script type="text/javascript" src="js/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="js/validate/messages_cn.js"></script>
    <script type="text/javascript">
	    $(function(){
			$("#pageForm").validate({
				onfocusout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					loginName:{
						required:true
					},
					name:{
						required:true
					},
					phoneNumber:{
						number:true
					},
					email:{
						required:true,
						email:true
					},
				}
			});
		});
    </script>
</body>
</html>
