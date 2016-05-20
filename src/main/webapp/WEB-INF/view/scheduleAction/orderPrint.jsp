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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style>
	@media print 
	{
		.noprint{display:none;}
	}
	td{
	height:30px;
	}
	.ttt{
	float:left;margin-left:10px;width:100px;height:30px;
	}
</style>
<title></title>
<link href="<%=basePath %>skins/main.css" rel="stylesheet" type="text/css" />
</head>
<body class="" style="width:100%">
    <div class="space">
            <h1 align="center">重庆市渝勤汽车服务有限公司派车单</h1>
        <div class="">
<table id="tableId" style="border-color:black" border="1" width="100%">
		<tbody>
			<tr>
				<td style="border:none" width="100">订单号:${sn }</td>
				<td style="border:none" colspan="4"></td>
				<td style="border:none" class="alignRight" width="200">渝勤运</td>
			</tr>
			<tr>
				<td class="alignCenter">用车单位</td>
				<td colspan="2">&nbsp;&nbsp;${customerOrganization.abbreviation }</td>
				<td class="alignCenter">联系人电话</td>
				<td colspan="2">&nbsp;&nbsp;${customer.name}&nbsp;&nbsp;(${phone })</td>
				
			</tr>
			<tr>
				<td class="alignCenter">订车时间</td>
				<td colspan="2">&nbsp;&nbsp;<s:date name="scheduleTime" format="yyyy-MM-dd hh:mm"/></td>
				<td class="alignCenter">定车地点</td>
				<td colspan="2">&nbsp;&nbsp;${fromAddress}</td>
			</tr>
			<tr>
				<td class="alignCenter">车型</td>
				<td colspan="2" >&nbsp;&nbsp;${serviceType.title }</td>
				<td class="alignCenter">车牌号</td>
				<td colspan="2" >&nbsp;&nbsp;${car.plateNumber }</td>
			</tr>
			<tr>
				<td class="alignCenter">驾驶员/电话</td>
				<td colspan="5" width="550">&nbsp;&nbsp;${driver.name }&nbsp;&nbsp;(${driver.phoneNumber })</td>
			</tr>
			<tr>
				<td class="alignCenter">时间</td>
				<td class="alignCenter" colspan="5" width="550">经过地点摘要</td>
			</tr>
			<s:iterator value="abstractTrackList">
			<tr>
				<td align="center">${abstractTime}</td>
				<td colspan="5" align="center">${abstractAddress}</td>
			</tr>
			</s:iterator>
			<tr>
				<td class="alignCenter">过路停车</td>
				<td width="150">　</td>
				<td class="alignCenter" width="100">餐宿补贴</td>
				<td width="150"></td>
				<td class="alignCenter" width="150">其他费用</td>
				<td></td>
			</tr>
			<tr>
				<td class="alignCenter">出站路码</td>
				<td>　</td>
				<td class="alignCenter">回站路码</td>
				<td>　</td>
				<td class="alignCenter">合计里程（KM）</td>
				<td>&nbsp;&nbsp;${actualMile }</td>
			</tr>
			<tr>
				<td class="alignCenter">上车时间</td>
				<td><s:date name="actualBeginDate" format="yyyy-MM-dd HH:mm"/></td>
				<td class="alignCenter">下车时间</td>
				<td><s:date name="actualEndDate" format="yyyy-MM-dd HH:mm"/></td>
				<td class="alignCenter">合计时间（h）</td>
				<td style="padding-left:8px"></td>
			</tr>
			<tr>
				<td class="alignCenter" style="height:60px">结算金额</td>
				<td colspan="3">&nbsp;&nbsp;<fmt:formatNumber value="${actualMoney}" pattern="#0.0"/></td>
				<td class="alignCenter" style="border-right:none">用车单位签字：</td>
				<td style="border-left:none; padding-left:25px">
				<img id="imgId" width="150" height="50" style="display:none" src="order_getSignature.action?imageId=${signature.id}" />
				<input id="signatureId" value="${signature.id }" style="display:none"/>
				</td>
			</tr>
			<tr>
				<td colspan="5" style="border:none;padding-left:50px">
				</td>
				
				<td style="border:none">派车人：${scheduler.name }</td>
			</tr>
			<tr>
				<td style="border:none">　</td>
				<td style="border:none" colspan="5">　</td>
			</tr>
			<tr>
				<td style="border:none">　</td>
				<td style="border:none" colspan="5">　</td>
			</tr>
			<tr>
				<td style="border:none">　</td>
				<td style="border:none" colspan="5">
				<div class="ttt">注：白联</div>
				<div class="ttt">部门，红联</div>
				<div class="ttt">部门，篮联</div>
				<div class="ttt">部门，黄联</div>
				<div class="ttt">部门</div>
				</td>
			</tr>
		</tbody>
		<tfoot>
			<tr >
         	<td style="border:none" colspan="2" class="noprint">
             	<input class="inputButton" type="button" value="打印订单" onclick=javascript:window.print() >
              	<a class="p15" href="javascript:history.go(-1);">返回</a>
         	</td>
    		</tr>
    	</tfoot>
</table>
        </div>
    </div>
    <script type="text/javascript" src="<%=basePath %>js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>js/common.js"></script>
    <script type="text/javascript">
    $(function(){
		 
		var obj= $("#signatureId");
		if(obj.val() != null&&obj.val() != 0){
			$("#imgId").show();
		}
	 });
    $(function(){
    	//获取当前日期
    	today = new Date();  
		centry="";
		if  (today.getFullYear()<2000 )  
		    centry = "19" ; 
		date = centry + (today.getFullYear())+ "年" + 
		    		(today.getMonth() + 1 ) + "月" + 
		    		today.getDate() + "日 "; 
		$("#tableId tr:eq(19) td:eq(0)").text(date);
		//计算订单执行时间
    	var date1 = $("#tableId tr:eq(17) td:eq(1)").text();
    	var date2 = $("#tableId tr:eq(17) td:eq(3)").text();
    	
    	var y1 = parseInt(date1.split('-')[0]);
    	var m1 = parseInt(date1.split('-')[1]);
    	var d1 = parseInt(date1.split('-')[2]);
    	var h1 = parseInt(date1.split('-')[2].split(' ')[1]);
    	var min1 = parseInt(date1.split('-')[2].split(' ')[1].split(':')[1]);
    	
    	var y2 = parseInt(date2.split('-')[0]);
    	var m2 = parseInt(date2.split('-')[1]);
    	var d2 = parseInt(date2.split('-')[2]);
    	var h2 = parseInt(date2.split('-')[2].split(' ')[1]);
    	var min2 = parseInt(date2.split('-')[2].split(' ')[1].split(':')[1]);
    	
    	var day1 = new Date(Date.UTC(y1,m1,d1,h1,min1));
    	var day2 = new Date(Date.UTC(y2,m2,d2,h2,min2));

    	var days = (day2 - day1)/1000/60/60;
    	$("#tableId tr:eq(17) td:eq(5)").text(days.toFixed(1));
    	
    });
    </script>
</body>
</html>
