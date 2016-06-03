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
		        <table class="showInfo">	           
		            <tr>
		                <th class="driverAction">
		                		<input name="actionId" type="hidden"  value="${actionId }"/>	
		                		
		                </th>       
		                <td >
								<input class="Wdate half" name="actionTime" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
								<input name="time" type="hidden"  value="${time }"/>
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
    </div>
		
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>	
	<script type="text/javascript">
		var time = $("input[name=time]").val();
		var actionTime = time.substr(0,19);
		$("input[name=actionTime]").attr("value",actionTime);
	
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
	</script>
</body>
</html>