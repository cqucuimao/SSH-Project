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
			<h1>车辆年审详细信息</h1>
		</div>
		<div class="editBlock detail p30">
				<table>
					<colgroup>
					<col width="80"></col>
					<col></col>
					<col></col>
					<col></col>
					<col></col>
					<col width="120"></col>
					</colgroup>
					<tbody class="tableHover">
							<tr>
                    			<th width="100"><s:property value="tr.getText('car.Car.plateNumber')" />：</th>
                    			<td>${car.plateNumber}</td>
                			</tr>
                			<tr>
                    			<th><s:property value="tr.getText('car.CarExamine.date')" />：</th>
                    			<td><s:date name="date" format="yyyy-MM-dd"/></td>
                			</tr>
                			<tr>
                    			<th>下次年审日期：</th>
                    			<td><s:date name="examineIntervalYear" format="yyyy-MM-dd"/></td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarExamine.memo')" />：</th>
                				<td><textarea readonly>${memo}</textarea></td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarExamine.appointment')" />：</th>
                				<td>
                					<s:if test="appointment==true">
									<s:text name="是"></s:text>
									</s:if>
									<s:else>
									<s:text name="否"></s:text>
									</s:else>
                				</td>
                			</tr>
					</tbody>
					<tfoot>
                		<tr>
                    	<td colspan="2">
                        	<a class="p15" href="javascript:history.go(-1);">返回</a>
                    	</td>
                		</tr>
            		</tfoot>
				</table>
		</div>
	</div>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>	
	<script type="text/javascript">
		$(function(){
			
	    })
	</script>
</body>
</html>
