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
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
	<meta name="keywords"content="渝勤汽车,YUQINAUTO" />
	<meta name="description"content="渝勤汽车" />
	<title></title>
	<link title="" type="image/x-icon" rel="icon" href="<%=basePath %>skins/images/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="<%=basePath %>skins/main.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath %>js/artDialog4.1.7/skins/blue.css">
</head>
<body>
	<table cellpadding="0" cellspacing="0" class="body-layout">
		<tbody>
	    	<tr>
	    		<!-- 左侧菜单 -->
	        	<td id="leftWrap" class="leftWrap">
	            	<iframe id="leftFrame" name="leftFrame" frameborder="0" height="100%" width="100%" src="about:blank"></iframe>
	            </td>
	            <!-- 右侧主要内容 -->
	            <td id="rightWrap" class="rightWrap">
	            	<!-- 右侧头部 -->
	            	<div id="header" class="header">
	            		<a href="#" id="split" class="split" title="显示/隐藏菜单"></a>
	            		<div class="userInfo">
							欢迎光临${sessionScope.company.name}综合业务管理系统
							<a class="popUserInfo" title="点击可修改用户信息和密码" href="#"><s:property value="#session.user.loginName"/></a>
							<em>|</em>
							<s:a action="login_logout">退出</s:a>
						</div>
	            	</div>
	            	<!-- 右侧内容区域 -->
	            	<iframe id="rightFrame" name="rightFrame" frameborder="0" height="100%" width="100%" src="about:blank"></iframe>
	            </td>
	        </tr>
	    </tbody>
	</table>
	<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="<%=basePath %>js/jquery.easing.1.3.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/index.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/artDialog4.1.7/artDialog.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/artDialog4.1.7/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		$(".popUserInfo").click(function(){
            popup("我的信息","user_info.action",700,500,"usertInfo");
		})
	</script>
	<script type="text/javascript">
	$(function(){
		/*初始化时为iframe载入默认页面 satrt*/
		if(IE === 6) {
			var leftWrap = document.getElementById('leftWrap'),
				rightWrap = document.getElementById('rightWrap'),
				leftIframeHtml = '<iframe id="leftFrame" name="leftFrame" frameborder="0" height="100%" width="100%" scrolling="yes" src="home_left.action"></iframe>',
				rightIframeHtml = '<iframe id="rightFrame" name="rightFrame" frameborder="0" height="100%" width="100%" scrolling="yes" src="scheduling/scheduling.html"></iframe>';
			
			leftWrap.innerHTML = leftIframeHtml;
			rightWrap.innerHTML = rightIframeHtml;
		} else {
			leftFrame[0].src = 'home_left.action';
			rightFrame[0].src = 'home_welcome.action';
		}
		/*初始化时为iframe载入默认页面 end*/
		leftFrame = $('#leftFrame');
	    rightFrame = $('#rightFrame');
	    // 隐藏body的滚动条
	    $("body").css('overflow', 'hidden');
	    if (IE < 8) {
	        $("html").css('overflow', 'hidden');
	    }
	    $(window).resize(function() {resizeBodyHeight();});//窗口大小改变时重新设置iframe高度
	    resizeBodyHeight(); //设置iframe高度
	    
	    /* 左侧菜单收折效果 start */
		var leftWrap = $('#leftWrap'),
			rightWrap = $('#rightWrap'),
			Split = $('#split'),
			leftWrapWidthInit = leftWrap.width();
			
		Split.on('click',function(event){
			event.preventDefault();
			var leftWrapWidth = leftWrap.width();
			if(leftWrapWidth === leftWrapWidthInit){
				//if ie, 去掉动画
				if(IE){
					leftWrap.find('iframe').width(0);
					leftWrap.width(0);
					return;
				}
				//change iframe width
				leftWrap.find('iframe').animate({width: '0px'},{easing: 'easeInQuart', duration: 100});
				leftWrap.animate({
					width: '0px'
				},{
					easing: 'easeInQuart', 
					duration: 100,
					complete:function(){
	                    rightWrap.find("#split").addClass("fold");
	                }
				});
			}else{
				if(IE){
					leftWrap.width(leftWrapWidthInit);
					leftWrap.find('iframe').width('100%');
					return;
				}
				leftWrap.animate({
					width: leftWrapWidthInit+'px'
				},{
					easing: 'easeOutQuint', 
					duration: 100,
					complete:function(){
	                    rightWrap.find("#split").removeClass("fold");
	                }
				});
				leftWrap.find('iframe').animate({width: leftWrapWidthInit+'px'},{easing: 'easeOutQuint', duration: 100});
				
			}
		});
		/* 左侧菜单收折效果 end */
	});
	</script>
</body>
</html>