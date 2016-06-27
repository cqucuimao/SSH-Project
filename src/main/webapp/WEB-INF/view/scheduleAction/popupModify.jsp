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
								<input class="Wdate half" name="inputTime" id="timeId" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
								<input name="actionId" type="hidden"  value="${actionId }"/>
								<input name="time" type="hidden"  value="${time }"/>
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
		var actionId = $("input[name=actionId]").val();
		var id = actionId.substr(2,1);	
		
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
		
		var time = $("input[name=time]").val();
		var beforeTime = $("input[name=beforeTime]").val();
		var afterTime = $("input[name=afterTime]").val();
		//alert("beforeTime="+beforeTime);
		//alert("afterTime="+afterTime);
		$("#timeId").val(time.substr(0,19));
		
		$("#btn").click(function(){
			var inputTime = $("#timeId").val();	
			//alert("inputTime="+inputTime+"beforeTime="+beforeTime+"afterTime="+afterTime);
			if(Date.parse(inputTime)-Date.parse(beforeTime)<0){
				alert("时间不合法！");
				return false;
			}
			if(Date.parse(inputTime)-Date.parse(afterTime)>0){
				alert("时间不合法！");
				return false;
			}
			alert("chen");
			alert(artDialog.open.origin.location);
			$("#timeForm").attr("action","order_editDriverActionTime.action");
			alert(artDialog.open.origin.location);
			artDialog.open.origin.location.reload();
			art.dialog.close();
			//top.location.href="order_editDriverAction.action";
			//popdown("popupModify");
		}) 
	</script>
</body>
</html>