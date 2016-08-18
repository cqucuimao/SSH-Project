<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cqu" uri="//WEB-INF/tlds/cqu.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();//显示根目录
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
			<h1>洗车登记</h1>
		</div>
		<div class="editBlock search">
			<s:form id="queryForm" action="carWash_queryForm">
			<table>
				<tr>
					<td>
						<s:a cssClass="buttonA" action="carWash_saveUI">洗车登记</s:a>
					</td>
					<th><s:property value="tr.getText('car.CarWash.car')" /></th>
					<td>
						<cqu:carSelector name="car"/>
					</td>
					<th>从</th>
					<td>
						<s:textfield name="beginDate" id="beginDate" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<th>到</th>
					<td>
						<s:textfield name="endDate" id="endDate" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<td>
						<input class="inputButton" type="submit" value="查询"/>
					</td>
					<td>
						<s:a cssClass="buttonA" action="carWashShop_list">洗车点管理</s:a> 
					</td>
					<td>
						<s:a cssClass="buttonA" action="carWash_excel">洗车信息导入</s:a> 
					</td>
					<td>
						<s:if test="carId!=null">
							<a class="p15" href="javascript:history.go(-1);">返回</a>
						</s:if>
					</td>
				</tr>
			</table>
			</s:form>
		</div>
		<div class="dataGrid">
			<div class="tableWrap">
				<table>
					<thead>
						<tr>
							<th><s:property value="tr.getText('car.CarWash.car')" /></th>
              				<th><s:property value="tr.getText('car.CarWash.driver')" /></th>
                			<th><s:property value="tr.getText('car.CarWash.date')" /></th>
                			<th><s:property value="tr.getText('car.CarWash.shop')" /></th>
                			<th><s:property value="tr.getText('car.CarWash.money')" /></th>
                			<th><s:property value="tr.getText('car.CarWash.innerCleanMoney')" /></th>
                			<th><s:property value="tr.getText('car.CarWash.polishingMoney')" /></th>
                			<th><s:property value="tr.getText('car.CarWash.engineCleanMoney')" /></th>
                			<th><s:property value="tr.getText('car.CarWash.cushionCleanMoney')" /></th>
                			<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
				        
						<tr>
							<td>${car.plateNumber}</td>
							<td>${driver.name}</td>
							<td ><s:date name="date" format="yyyy-MM-dd"/></td>
							<td>${shop.name}</td>
							<td >${money }</td>
							<td >${innerCleanMoney }</td>
							<td >${polishingMoney }</td>
							<td >${engineCleanMoney }</td>
							<td >${cushionCleanMoney }</td>
							
							
							<td>
                    			<s:a action="carWash_delete?id=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
                    			<s:a action="carWash_editUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
          					</td> 
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<!-- //底部页面显示 -->
			<s:form id="pageForm" action="carWash_freshList">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>	
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
	<script type="text/javascript">
		$(function(){
			formatDateField2($("#beginDate"));
			formatDateField2($("#endDate"));
	    })
	</script>
</body>
</html>
