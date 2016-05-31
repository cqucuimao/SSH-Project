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
		                <th>XXX</th>
		                <td >
								<input class="Wdate half" name="registDate" id="registDateId" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
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
	
	</script>
</body>
</html>