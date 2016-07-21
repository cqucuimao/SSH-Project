<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
    <meta name="keywords"content="渝勤汽车,YUQINAUTO" />
    <meta name="description"content="渝勤汽车" />
    <title></title>
    <link title="" type="image/x-icon" rel="icon" href="skins/images/favicon.ico" />
    <link rel="stylesheet" type="text/css" href="skins/login.css">
    <%
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	%>
</head>
<body>
    <div class="bg"><div class="inner"></div></div>
    <div class="loginWrap">
        <h1 class="logo"><em></em>综合业务管理系统</h1>
        <div class="loginBox">
			<s:form action="login_login" >
                <div class="note">
                	<s:property value="errors['loginError'][0]"/>
                	<s:property value="errors['loginName'][0]" />
                	<s:if test="!devMode">
                		<s:property value="errors['password'][0]" />
                		<s:property value="errors['validationCode'][0]" />
                	</s:if>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <span class="input-group-addon">
                            <i class="icon-user"></i>
                        </span>
                        <s:textfield cssClass="inputText" name="loginName" type="text" id="login-account" />
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <span class="input-group-addon">
                            <i class="icon-password"></i>
                        </span>
                        	<s:password id="login-password" cssClass="inputText" showPassword="false" name="password" />
                        
                    </div>
                </div>
                <div class="form-group vaildate">
                    <div class="input-group">
                        <span class="input-group-addon">
                            <i class="icon-vaildate"></i>
                        </span>
                        <s:textfield cssClass="inputText" name="validationCode" type="text" id="login-validationCode"/>
                    </div>
                    <img width="80" height="39" id="validationCodeImage"  onclick="this.src=this.src+'?rand='+Math.random();"/>
                </div>
                <div class="form-group">
                    <input class="inputButton" type="submit" value="登 录" />
                </div>
			</s:form>
        </div>
    </div>
    
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript">
		$(function(){
	        $("#validationCodeImage").click(function(){
				$("#validationCodeImage").attr("src","app/securityCodeImage.action?rand="+Math.random());
	        });
			$("#validationCodeImage").attr("src","app/securityCodeImage.action?rand="+Math.random());
	    })
	</script>
</body>
</html>
