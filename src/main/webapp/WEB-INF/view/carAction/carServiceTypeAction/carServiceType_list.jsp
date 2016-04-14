<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
	<link rel="stylesheet" type="text/css" href="skins/main.css">
</head>
<body class="minW">
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>车型管理</h1>
		</div>
		<div class="editBlock search">
			<table>
				<tr>
					<td>
						<input id="register" class="inputButton" type="button" value="车型登记" name="button" />
					</td>					
				</tr>
			</table>
		</div>
		<div class="dataGrid">
			<div class="tableWrap">
			
				<table>
					<thead>
						<tr>
							<th><s:property value="tr.getText('car.CarServiceType.title')" /></th>
							<th><s:property value="tr.getText('car.CarServiceType.pricePerKM')" />(元/KM)</th>
							<th><s:property value="tr.getText('car.CarServiceType.pricePerDay')" />(元/天)</th>
							<th><s:property value="tr.getText('car.CarServiceType.personLimit')" /></th>
							<th class="alignCenter">操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
						<s:iterator value="carServiceTypeList">
						<tr>
							<td>${title}</td>
							<td><fmt:formatNumber value="${pricePerKM}" pattern="#0.0"/></td>
							<td><fmt:formatNumber value="${pricePerDay}" pattern="#0"/></td>
							<td>${personLimit}</td>
							<td class="alignCenter">
							<s:a action="carServiceType_editUI?id=%{id}" ><i class="icon-operate-edit" title="修改"></i></s:a>
							<s:if test="canDeleteServiceType">
								<s:a action="carServiceType_delete?id=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>								
							</s:if>
							<s:else>
							</s:else>
							</td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	
<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>	
	<script type="text/javascript" src="js/common.js"></script>	
	<script type="text/javascript">
		$(function(){
	        $("#register").click(function(){
	            self.location.href='carServiceType_addUI.action';
	        });
	    })
	</script>
</body>
</html>
