<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
						<input id="register" class="inputButton" type="button" value="洗车登记"/>
					</td>
					<th><s:property value="tr.getText('car.Car.plateNumber')" /></th>
					<td>
						<s:textfield id="car_platenumber" cssClass="carSelector inputText inputChoose" onfocus="this.blur();" name="car.plateNumber" type="text" />
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
						<input id="washShop" class="inputButton" type="button" value="洗车点管理"/>
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
							<th>车辆</th>
              				<th>司机</th>
                			<th>洗车日期</th>
                			<th>洗车点</th>
                			<th>金额</th>
                			<th>是否做内饰清洁</th>
                			<th>是否抛光打蜡</th>
                			<th>是否清洗发动机</th>
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
							<td >
							<s:if test="doInnerClean==true">
								<s:text name="是"></s:text>
								</s:if>
								<s:else>
								<s:text name="否"></s:text>
								</s:else>
							</td>
							<td >
							<s:if test="doPolishing==true">
								<s:text name="是"></s:text>
								</s:if>
								<s:else>
								<s:text name="否"></s:text>
								</s:else>
							</td>
							<td >
							<s:if test="doEngineClean==true">
								<s:text name="是"></s:text>
								</s:if>
								<s:else>
								<s:text name="否"></s:text>
								</s:else>
							</td>
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
	        $("#register").click(function(){
	            self.location.href='carWash_saveUI.action';
	        });
	        $("#washShop").click(function(){
	            self.location.href='carWashShop_list.action';
	        });
			formatDateField2($("#beginDate"));
			formatDateField2($("#endDate"));
	    })
	</script>
</body>
</html>
