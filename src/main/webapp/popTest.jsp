<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
    <title></title>
	<script src="js/jquery-1.7.2.js"></script>
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>

	
</head>
<body>
    <input id="department_name" type="text" value="部门名称" style="padding:4px; width:16em; margin-right:10px">
	<button id="btn1">部门选择</button>
	
	<br><br>
	<input id="driverName" name="driverName" type="text" value="张菲菲" style="padding:4px; width:16em; margin-right:10px">
	<input id="driverId" name="driverId" type="hidden">
	<button id="btn2">司机选择</button>
	
	<br><br>
	<input id="customer_organization_name" name="customer_organization_name" type="text" value="张菲菲" style="padding:4px; width:16em; margin-right:10px">
	<input id="customer_organization_id" name="customer_organization_id" type="hidden">
	<button id="btn3">单位选择</button>
	
	<script type="text/javascript">
		$(function(){
	        $("#btn1").click(function(){
	            art.dialog.data('test', $("#department_name").val()); // 存储数据  
	            art.dialog.open('department_popup.action',{
					title: '部门选择', 
					width: 380, 
					height: 530
				});
	        });
	        
	        $("#btn2").click(function(){
	            art.dialog.data('driverName', $("#driverName").val()); // 存储数据  
	            art.dialog.open('user_popup.action?popupDriverOnly=false',{
					title: '司机选择', 
					width: 300, 
					height: 530
				});
	        });
	        
	        $("#btn3").click(function(){
	            art.dialog.data('customer_organization_name', $("#customer_organization_name").val()); // 存储数据  
	            art.dialog.open('driver_popupCustomerOrganization.action',{
					title: '单位选择', 
					width: 420, 
					height: 530
				});
	        });
	    })
	</script>
</body>
</html>
