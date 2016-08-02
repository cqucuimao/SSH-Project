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
	<title></title>
	<link rel="stylesheet" type="text/css" href="skins/main.css">
</head>
<body class="minW">
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>加油信息</h1>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="carRefuel_queryList">
			<table>
				<tr>
					<td>
						<s:a action="carRefuel_addUI"><input id="register" class="inputButton" type="button" value="加油登记" name="button" /></s:a>
					</td>
					<th><s:property value="tr.getText('car.Car.plateNumber')" /></th>
					<td>
						<s:textfield id="car_platenumber" cssClass="carSelector inputText inputChoose" onfocus="this.blur();" name="car.plateNumber" type="text" />
					</td>
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
						<s:a action="carRefuel_excel"><input class="inputButton" id="btn" type="button" value="导入加油信息"/></s:a>
					</td>
					<td>
					<a href="#" class="modify" onclick="modify()">
					    <input class="inputButton" id="outPutOil" type="button" value="导出加油信息月报表"/>
					</a>
					<s:if test="Ture">
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
							<th><s:property value="tr.getText('car.CarRefuel.sn')" /></th>
							<th><s:property value="tr.getText('car.Car.plateNumber')" /></th>
							<th><s:property value="tr.getText('car.CarRefuel.driver')" /></th>
                			<th><s:property value="tr.getText('car.CarRefuel.date')" /></th>
                			<th><s:property value="tr.getText('car.CarRefuel.volume')" />(L)</th>
                			<th><s:property value="tr.getText('car.CarRefuel.money')" />(元)</th>
                			<th><s:property value="tr.getText('car.CarRefuel.outSource')" /></th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${sn}</td>
							<td><s:a action="carRefuel_detail?id=%{id}">${car.plateNumber}</s:a></td>
							<td>${driver.name}</td>
							<td style="text-align:right"><s:date name="date" format="yyyy-MM-dd"/></td>
                			<td style="text-align:right">${volume}</td>
							<td style="text-align:right">${money}</td>
							<s:if test="outSource"><td>是</td></s:if>
							<s:else><td>否</td></s:else>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="carRefuel_freshList">
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
	    function modify(){
	        	url="carRefuel_outPutOil_time.action";
	        	art.dialog.open(url,{
	                id: "timeModify",
	                title: "选择时间",
	                width: 330,
	                height: 200,
	                padding: 0,
	                lock: true
	            });
	        }
	        
	    formatDateField2($("#date1"));
		formatDateField2($("#date2"));
	</script>
</body>
</html>
