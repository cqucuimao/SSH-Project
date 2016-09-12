<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cqu" uri="//WEB-INF/tlds/cqu.tld" %>
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
			<h1>车辆年审记录</h1>
		</div>		
		<div class="tab_next style2">
			<table>
				<tr>
				    <td><s:a action="carExamine_appointList"><span>预约车辆年审</span></s:a></td>
					<td class="on"><a href="#"><span>车辆年审记录</span></a></td>
				</tr>
			</table>
		</div>
		<br/>
		<div class="editBlock search">
			<s:form id="pageForm" action="carExamine_queryForm">
			<table>
				<tr>
					<td>
						<s:a cssClass="buttonA" action="carExamine_addUI?actionFlag=register">年审登记</s:a>
					</td>
					<th><s:property value="tr.getText('car.CarExamine.car')" /></th>
					<td><cqu:carSelector name="car"/></td>
					<th><s:property value="tr.getText('car.CarExamine.driver')" /></th>
					<td><cqu:userSelector name="driver"/></td>
					<th>从</th>
					<td>
						<s:textfield name="date1" id="date1" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<th>到</th>
					<td>
						<s:textfield name="date2" id="date2" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<td>
						<input class="inputButton" type="submit" value="查询"/>
					</td>
					<td>
						<s:a cssClass="buttonA" action="carExamine_remind">年审提醒</s:a>
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
							<th><s:property value="tr.getText('car.CarExamine.car')" /></th>
							<th><s:property value="tr.getText('car.CarExamine.driver')" /></th>
							<th><s:property value="tr.getText('car.CarExamine.date')" /></th>
              				<th>下次年审日期</th>
              				<th><s:property value="tr.getText('car.CarExamine.money')" /></th>
              				<th><s:property value="tr.getText('car.CarExamine.carPainterMoney')" /></th>
                			<th><s:property value="tr.getText('car.CarExamine.memo')" /></th>
                			<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td><cqu:carDetailList id="${car.id}"/></td>
							<td>${driver.name}</td>
							<td style="text-align:right"><s:date name="date" format="yyyy-MM-dd"/></td>
							<td style="text-align:right"><s:date name="nextExamineDate" format="yyyy-MM-dd"/></td>
							<td style="text-align:right">${money}</td>
							<td style="text-align:right">${carPainterMoney}</td>
							<td width="40%">${memo}</td>
							<td>
                    			<s:a action="carExamine_delete?id=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
                    			<s:a action="carExamine_editUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
                			</td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="carExamine_freshList">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	
<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>	
	<script type="text/javascript" src="js/common.js"></script>	
	<script type="text/javascript">
		$(function(){
			formatDateField2($("#date1"));
			formatDateField2($("#date2"));
	    })
	</script>
</body>
</html>
