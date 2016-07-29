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
		        <s:form id="timeForm" name="myForm" action="#">
		        <table class="showInfo">	           
		            <tr>
		                <th >
		                		 <h4>导出年月</h4>			                		
		                </th>       
		                <td>
								<s:textfield class="Wdate half" name="gDate" id="gDate" onfocus="new WdatePicker({dateFmt:'yyyy-MM'})" />
						</td>
		            </tr>
		            
		            <tfoot>
		                <tr>
		                    <td></td>
		                    <td>
		                        <input id="btnOil" type="submit" class="inputButton" value="确定" />
		                        <a class="p15" href="#" onClick="popdown('timeModify')">取消</a>
		                    </td>
		                </tr>
		            </tfoot>
		        </table>
		        </s:form>
    </div>
		
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>	
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
    <script type="text/javascript" src="js/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="js/validate/messages_cn.js"></script>
	
	<script type="text/javascript">	
	 	$("#btnOil").click(function(){
	 		if($("#gDate").val()==""){
	 			alert("年月不能为空！");
	 		}else{
				$("#timeForm").attr("action","carRefuel_OilReport.action");
				$("#timeForm").submit();
	 		}
		}); 
	</script>
</body>
</html>