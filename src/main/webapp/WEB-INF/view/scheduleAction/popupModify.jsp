<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link href="skins/main.css" rel="stylesheet" type="text/css" />
<style>
    	
    </style>
</head>
<body>
			
	  <div class="editBlock p20 pt10"><br>
		        <form id="timeForm" name="myForm" action="">
		        <table class="showInfo">	           
		            <tr>
		                <th class="driverAction">
		                		<input name="actionId" type="hidden"  value="${actionId }"/>			                		
		                </th>       
		                <td>
								<s:textfield class="Wdate half" name="time" id="timeId" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
								<input name="actionId" type="hidden"  value="${actionId }"/>
								<input name="beforeTime" type="hidden"  value="${beforeTime }"/>
								<input name="afterTime" type="hidden"  value="${afterTime }"/>
						</td>
		            </tr>
		            
		            <tfoot>
		                <tr>
		                    <td></td>
		                    <td>
		                        <input id="btn" type="submit" class="inputButton" value="确定" />
		                        <a class="p15" href="#" onClick="popdown('popupModify')">取消</a>
		                    </td>
		                </tr>
		            </tfoot>
		        </table>
		        </form>
    </div>
		
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>	
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
	
	<script type="text/javascript">
		if($("#timeId").val().length < 28){
			formatDateField3($("#timeId"));
		}
		//将Wed Jul 13 22:40:00 CST 2016转换为 2016-07-13 22:40:00
		if($("#timeId").val().length == 28){
			formatDateField4($("#timeId"));
		}
		var actionId = $("input[name=actionId]").val();
		var id = actionId.split("-")[1];	
		
		if(id == 0){
			$(".driverAction").text("在队列");
		}else if(id == 1){
			$(".driverAction").text("已调度");
		}else if(id == 2){
			$(".driverAction").text("已接受");
		}else if(id == 3){
			$(".driverAction").text("已开始");
		}else if(id == 4){
			$(".driverAction").text("客户已上车");
		}else if(id == 5){
			$(".driverAction").text("客户已下车");
		}else if(id == 6){
			$(".driverAction").text("已结束");
		}else if(id == 7){
			$(".driverAction").text("已付费");
		}else if(id == 8){
			$(".driverAction").text("已取消");
		}
		
		var beforeTime = $("input[name=beforeTime]").val().replace('-','/').replace('-','/').substr(0,19);
		var afterTime = $("input[name=afterTime]").val().replace('-','/').replace('-','/').substr(0,19);
		$("#btn").click(function(){
			var inputTime = $("#timeId").val().replace('-','/').replace('-','/').substr(0,19);	
			if(Date.parse(inputTime)-Date.parse(beforeTime)<0){
				alert("时间不合法！");
				return false;
			}
			if(Date.parse(inputTime)-Date.parse(afterTime)>0){
				alert("时间不合法！");
				return false;
			}
			$("#timeForm").attr("action","order_modifyDriverActionTime.action");
			$("#timeForm").submit();
			var win=artDialog.open.origin;
			art.dialog.close();
			win.location.reload();
		}) 
	</script>
</body>
</html>