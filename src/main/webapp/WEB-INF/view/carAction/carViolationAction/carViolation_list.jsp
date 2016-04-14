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
	<title>违章信息</title>
	<link rel="stylesheet" type="text/css" href="skins/main.css">
</head>
<body class="minW">
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>违章信息列表</h1>
		</div>
		<div class="editBlock search">
			<table>
				<tr>
					<td>
						<input id="register" class="inputButton" type="button" value="违章登记" name="button" />
					</td>
					<th><s:property value="tr.getText('car.Car.plateNumber')" /></th>
					<td><s:textfield id="car_platenumber" cssClass="inputText inputChoose" onfocus="this.blur();" name="plateNumber" type="text" /></td>
					<td>
						<input class="inputButton" type="button" value="查询" name="button" />
					</td>
				</tr>
			</table>
		</div>
		<div class="dataGrid">
			<div class="tableWrap">
				<table>
					<thead>
						<tr>
							<th>车牌号</th>
							<th>时间</th>
							<th>地点</th>
							<th>扣分情况</th>
							<th>罚款（元）</th>
							<th class="alignCenter">操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
						<tr>
							<td>渝A BG349</td>
							<td>2015/12/9 15:00</td>
							<td>大坪</td>
							<td>2分</td>
							<td>100</td>
							<td class="alignCenter">
								<s:a href="#"><i class="icon-operate-print" title="导出"></i></s:a>
							</td>
						</tr>
						
					</tbody>
				</table>
			</div>
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
		</div>
	</div>
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="<%=basePath%>js/DatePicker/WdatePicker.js"></script>
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>	
	<script type="text/javascript" src="<%=basePath%>js/common.js"></script>
	<script type="text/javascript">
		$(function(){
			$("#register").click(function(){
				location.href='carViolation_addUI.action';
			});
		})
	</script>
</body>
</html>