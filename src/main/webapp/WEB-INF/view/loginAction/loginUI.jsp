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
                <div class="form-group" >
                    <input class="inputButton" type="submit" value="登 录" />
                </div>
                <br/>
			</s:form>
        </div>
    </div>
    
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript">
		$(function(){

		
			 var userAgent = navigator.userAgent,    

             rMsie = /(msie\s|trident.*rv:)([\w.]+)/,    

             rFirefox = /(firefox)\/([\w.]+)/,    

             rOpera = /(opera).+version\/([\w.]+)/,    

             rChrome = /(chrome)\/([\w.]+)/,    

             rSafari = /version\/([\w.]+).*(safari)/;   

             var browser;   

             var version;   

             var ua = userAgent.toLowerCase();   

             function uaMatch(ua) {   

                 var match = rMsie.exec(ua);   

                 if (match != null) {   

                     return { browser : "IE", version : match[2] || "0" };   

                 }   

                 var match = rFirefox.exec(ua);   

                 if (match != null) {   

                     return { browser : match[1] || "", version : match[2] || "0" };   

                 }   

                 var match = rOpera.exec(ua);   

                 if (match != null) {   

                     return { browser : match[1] || "", version : match[2] || "0" };   

                 }   

                 var match = rChrome.exec(ua);   

                 if (match != null) {   

                     return { browser : match[1] || "", version : match[2] || "0" };   

                 }   

                 var match = rSafari.exec(ua);   

                 if (match != null) {   

                     return { browser : match[2] || "", version : match[1] || "0" };   

                 }   

                 if (match != null) {   

                     return { browser : "", version : "0" };   

                 }   

             }   

             var browserMatch = uaMatch(userAgent.toLowerCase());   

             if (browserMatch.browser) {   

                 browser = browserMatch.browser;   

                 version = browserMatch.version;   

             }   

				if(browser!="IE" || version<9 ){
					alert("请使用IE9及其以上版本访问本系统，否则某些功能可能会有问题。")
				}
			
	        $("#validationCodeImage").click(function(){
				$("#validationCodeImage").attr("src","app/securityCodeImage.action?rand="+Math.random());
	        });
			$("#validationCodeImage").attr("src","app/securityCodeImage.action?rand="+Math.random());
	    })
	</script>
</body>
</html>
