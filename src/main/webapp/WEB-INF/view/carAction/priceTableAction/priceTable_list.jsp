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
			<h1>价格表管理</h1>
		</div>
		<div class="editBlock search">
			<table style="border:none">
				<tr>
					<td>
						<s:a cssClass="buttonA" action="priceTable_serviceTypeList">车型管理</s:a>
					</td>					
				</tr>
			</table>
		</div>
		<div class="dataGrid">
			<div class="tableWrap">
			
				<table>
					<thead>
						<tr>
							<th>名称<%-- <s:property value="tr.getText('customerOrganization.PriceTable.title')" /> --%></th>
							<th class="alignCenter">操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
						<s:iterator value="priceTableList">
						<tr>
							<td>${title}</td>
							<td class="alignCenter">
							<s:a action="carServiceType_editUI?id=%{id}" ><i class="icon-operate-edit" title="修改"></i></s:a>
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
	</script>
</body>
</html>
