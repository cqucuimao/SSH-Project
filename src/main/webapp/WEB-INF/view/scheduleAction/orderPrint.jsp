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
        <div class="">
		<table id="tableId" style="border-color:black;width:75%;" border="1" >
		<tbody>
			<tr>
				<th colspan="8" style="font-size:20px;border:none">重庆市渝勤汽车服务有限公司派车单</th>
			</tr>
			<tr>
				<td style="border:none" colspan="2" width=""></td>
				<td style="border:none" colspan="4"></td>
				<td style="border:none" class="alignRight" colspan="2" width="">渝勤运${sn }</td>
			</tr>
			<tr>
				<td class="alignCenter" colspan="2">用车单位</td>
				<td colspan="2">&nbsp;&nbsp;${customerOrganization.abbreviation }</td>
				<td class="alignCenter" colspan="2">联系人电话</td>
				<td colspan="2">&nbsp;&nbsp;${customer.name}：${phone }</td>
				
			</tr>
			<tr>
				<td class="alignCenter" colspan="2">定车时间</td>
				<td colspan="2">&nbsp;&nbsp;<s:date name="scheduleTime" format="yyyy-MM-dd"/></td>
				<td class="alignCenter" colspan="2">上车地点</td>
				<td colspan="2">&nbsp;&nbsp;${fromAddress}</td>
			</tr>
			<tr>
				<td class="alignCenter" colspan="2">车型</td>
				<td colspan="2" >&nbsp;&nbsp;${serviceType.title }</td>
				<td class="alignCenter" colspan="2">车牌号</td>
				<td colspan="2" >&nbsp;&nbsp;${car.plateNumber }</td>
			</tr>
			<tr>
				<td class="alignCenter" colspan="2">驾驶员/电话</td>
				<td colspan="2">&nbsp;&nbsp;${driver.name }：${driver.phoneNumber }</td>
				<td class="alignCenter" colspan="2">目的地</td>
				<td colspan="2">&nbsp;&nbsp;${toAddress}</td>
			</tr>
			<tr>
				<td class="alignCenter">日期</td>
				<td class="alignCenter">上车时间</td>
				<td class="alignCenter">下车时间</td>
				<td class="alignCenter" colspan="3">经过地点摘要</td>
				<td class="alignCenter">实际公里</td>
				<td class="alignCenter">收费公里</td>
			</tr>
			<s:iterator value="abstractTrackList">
			<tr>
				<td align="center"></td>
				<td align="center"></td>
				<td align="center"></td>
				<td colspan="3" align="center">${abstractAddress}</td>
				<td align="center"></td>
				<td align="center"></td>
			</tr>
			</s:iterator>
			<tr>
				<td class="alignCenter" width="150">出库路码</td>
				<td width="150">　</td>
				<td class="alignCenter" width="150">客户上车路码</td>
				<td width="150"></td>
				<td class="alignCenter" width="150">客户下车路码</td>
				<td width="150"></td>
				<td class="alignCenter" width="150">回库路码</td>
				<td width="150"></td>
			</tr>
			<tr>
				<td class="alignCenter">邮费</td>
				<td></td>
				<td class="alignCenter">洗车费</td>
				<td></td>
				<td class="alignCenter">停车费</td>
				<td></td>
				<td class="alignCenter">计费路码</td>
				<td></td>
			</tr>
			<tr>
				<td class="alignCenter" colspan="2">过路费（客户自理）</td>
				<td colspan="2"></td>
				<td class="alignCenter">食宿</td>
				<td></td>
				<td class="alignCenter">其他费用</td>
				<td style="padding-left:8px"></td>
			</tr>
			<tr>
				<td class="alignCenter">核算金额</td>
				<td colspan="3"></td>
				<td class="alignCenter">实收金额</td>
				<td colspan="3"></td>
			</tr>
			<tr>
				<td colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;请为本次服务评价：</td>
			</tr>
			<tr>
				<td class="alignCenter" style="height:60px">驾驶员签字</td>
				<td colspan="2"></td>
				<td class="alignCenter" colspan="2">用车人签字及电话</td>
				<td style="padding-left:25px" colspan="3">
				<img id="imgId" width="150" height="50" style="display:none" src="order_getSignature.action?imageId=${signature.id}" />
				<input id="signatureId" value="${signature.id }" style="display:none"/>
				</td>
			</tr>
			<tr>
				<td class="alignCenter">意见及建议</td>
				<td colspan="7"></td>
			</tr>
			<tr>
				<td style="border:none;"></td>
				<td colspan="5" style="border:none;padding-left:50px">
				</td>			
				<td colspan="2" style="border:none">派车人：${scheduler.name }</td>
			</tr>
			<tr>
				<td style="border:none" colspan="2">用车电话：63219797</td>
				<td style="border:none"></td>
				<td style="border:none" colspan="2">夜间用车：60391610</td>
				<td style="border:none"></td>
				<td style="border:none" colspan="2">服务监督电话：60391609</td>
			</tr>
			<tr>
				<td style="border:none">　</td>
				<td style="border:none" colspan="5">　</td>
			</tr>
			<tr>
				<td style="border:none">　</td>
				<td style="border:none" colspan="7">
				<div class="ttt">注：白联</div>
				<div class="ttt">财务联，红联</div>
				<div class="ttt">运营联，篮联</div>
				<div class="ttt">存根联，黄联</div>
				<div class="ttt">客户联</div>
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
		$("#tableId tr:eq(22) td:eq(1)").text(date);
		//计算订单执行时间
    	/* var date1 = $("#tableId tr:eq(17) td:eq(1)").text();
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
    	$("#tableId tr:eq(21) td:eq(2)").text(days.toFixed(1)); */
    	
    });
    </script>
</body>
</html>
