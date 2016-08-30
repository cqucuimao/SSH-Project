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
	<title>维修</title>
	<link rel="stylesheet" type="text/css" href="skins/main.css">
</head>
<body class="minW">
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1></h1>
		</div>
		<div class="tab_next style2">
			<table>
				<tr>
					<td><s:a action="repairStatistic_list"><span>今日统计</span></s:a></td>
				    <td><s:a action="repairStatistic_weekList"><span>本周统计</span></s:a></td>
				    <td class="on"><s:a action="repairStatistic_monthList"><span>本月统计</span></s:a></td>
				    <td><s:a action="repairStatistic_query"><span>查询</span></s:a></td>
				</tr>
			</table>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="repairStatistic_monthList">
			<table>
				<tr style="height:20px;">
					<td colspan="3"></td>
				</tr>
				<tr>
					<th>选择日期:</th>
					<td>
						<s:textfield name="payDate" id="payDate" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<td>
						<input class="inputButton" type="submit" value="统计" onclick="if($('#payDate').val()!='') {$('#clicks').val('clicked');}"/>
						<s:hidden name="clicks" id="clicks"></s:hidden>
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
              				<th>维修时间</th>
              				<th>维修地点</th>
                			<th>维修内容</th>
                			<th>维修金额(元)</th>
                			<th>付款日期</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${car.plateNumber}</td>
							<td style="text-align:right"><s:date name="fromDate" format="yyyy-MM-dd"/>&nbsp;&nbsp;&nbsp;&nbsp;<s:date name="toDate" format="yyyy-MM-dd"/></td>
							<td>${repairLocation}</td>
							<td width="50%">${memo}</td>
							<td style="text-align:right"><fmt:formatNumber value="${money}" pattern="#0"/></td>
							<td style="text-align:right"><s:date name="payDate" format="yyyy-MM-dd"/></td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
		</div>
	</div>
	<s:if test="canShowImage">
	<div style="text-align:center;">
		<tr id="MoMHistogram" style="display:none">
		<th></th>
		<td>
			<img width="900" height="600" src="repairStatistic_MoMHistogram.action?dateString=${dateString}"/>		
		</td>
		</tr>
	</div>
	<div style="text-align:center; margin-top:50px">
		<tr id="YoYHistogram" style="display:none">
		<th></th>
		<td>
			<img width="900" height="600" src="repairStatistic_YoYHistogram.action?dateString=${dateString}"/>		
		</td>
		</tr>
	</div>
	</s:if>
	<s:else>
	</s:else>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>	
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
	<script type="text/javascript" src="js/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="js/validate/messages_cn.js"></script>
    <script type="text/javascript">
	     $(function(){
 			$("#pageForm").validate({
				submitout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					payDate:{
						required:true,
					},
				}
			});
	    })
	</script>
</body>
</html>
