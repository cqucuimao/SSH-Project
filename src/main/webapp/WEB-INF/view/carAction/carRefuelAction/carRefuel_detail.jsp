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
			<h1>车辆加油详细信息</h1>
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
                    			<th width="90"><s:property value="tr.getText('car.CarRefuel.sn')" />：</th>
                    			<td>${sn}</td>
                			</tr>
							<tr>
                    			<th><s:property value="tr.getText('car.Car.plateNumber')" />：</th>
                    			<td>${car.plateNumber}</td>
                			</tr>
							<tr>
                    			<th><s:property value="tr.getText('car.CarRefuel.driver')" />：</th>
                    			<td>${driver.name}</td>
                			</tr>
                			<tr>
                    			<th><s:property value="tr.getText('car.CarRefuel.date')" />：</th>
                    			<td><s:date name="date" format="yyyy-MM-dd"/></td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarRefuel.volume')" />(L)：</th>
                				<td>${volume}</td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarRefuel.money')" />(元)：</th>
                				<td><fmt:formatNumber value="${money}" pattern="#0"/></td>
                			</tr>
                			<tr>
	                			<th><s:property value="tr.getText('car.CarRefuel.outSource')" /></th>
	                			<s:if test="outSource"><td>是</td></s:if>
								<s:else><td>否</td></s:else>
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
