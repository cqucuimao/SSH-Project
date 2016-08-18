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
		<s:form id="queryForm" action="carViolation_queryForm">
			<table>
				<tr>
					<td>
						<s:a cssClass="buttonA" action="carViolation_saveUI">违章登记</s:a>
					</td>
					<th><s:property value="tr.getText('car.CarViolation.car')" /></th>
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
							<th>车牌号</th>
							<th>司机</th>
							<th>时间</th>
							<th>地点</th>
							<th>违章事实</th>
							<th>罚分</th>
							<th>罚款（元）</th>
							<th>是否已经处理</th>
							<th>处理日期</th>
							<th class="alignCenter">操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
						<s:iterator value="recordList">
				        
						<tr>
							<td>${car.plateNumber}</td>
							<td>${driver.name}</td>
							<td ><s:date name="date" format="yyyy-MM-dd HH:mm"/></td>
							<td>${place}</td>
							<td >${description }</td>
							<td >${penaltyPoint }</td>
							<td >${penaltyMoney }</td>
							<td >
							<s:if test="dealt==true">
								<s:text name="是"></s:text>
								</s:if>
								<s:else>
								<s:text name="否"></s:text>
								</s:else>
							</td>
							<td ><s:date name="dealtDate" format="yyyy-MM-dd"/></td>
							<td>
                    			<s:a action="carViolation_delete?id=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
                    			<s:if test="canUpdateCarViolation">
                    			<s:a action="carViolation_editUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
                    			</s:if>
          					</td> 
						</tr>
						</s:iterator> 
						
					</tbody>
				</table>
			</div>
			 <s:form id="pageForm" action="carViolation_freshList">
				<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="<%=basePath%>js/DatePicker/WdatePicker.js"></script>
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>	
	<script type="text/javascript" src="<%=basePath%>js/common.js"></script>
	<script type="text/javascript">
		$(function(){
			formatDateField2($("#beginDate"));
			formatDateField2($("#endDate"));
		})
		
	</script>
</body>
</html>