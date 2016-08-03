<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link href="skins/main.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-1.7.2.js"></script>
<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
</head>
<body>
 <div class="space">
    <div class="dataGrid">
			<div class="tableWrap">
				<table>
					<thead>
						<tr>
							<th>车类</th>
							<th class="alignCenter"></th>
							<th>车型</th>
							<th>8小时（100公里内）</th>
							<th>4小时（50公里内</th>
							<th>超公里（每公里）</th>
							<th>超时（每小时</th>
							<th>机场接送机（50公里/2小时）</th>
						</tr>
					</thead>
					<tbody class="tableHover">
								<s:iterator value="#session.mapList" id="column"> 
								<s:set name="total" value="#column.value.size"/> 
								<s:iterator value="#column.value" status="s" id="list1">
								<tr> 
								<s:if test="#s.first"><td rowspan="${total}"><s:property value="#column.key"/></td></s:if> 
								<td class="alignCenter">
                            	<input type="radio" name="id" value="${list1[0]}" data="${list1[1]}" />
                                </td>
								<td>  
								 <s:property value="#list1[1]"/>
								 </td>
								<td><s:text name="format.number"> 
								<s:property value="#list1[2]"/>
								</s:text></td> 
								<td><s:property value="#list1[3]"/></td> 
								<td><s:property value="#list1[4]"/></td> 
								<td><s:property value="#list1[5]"/></td> 
								<td><s:property value="#list1[6]"/></td> 
								</tr> 
								</s:iterator> 
						 		</s:iterator>
					</tbody>
				</table>
	      </div>
     </div>
           
            <div class="bottomBar alignCenter" style="position:fixed;bottom:0;background-color:white;width:300px;height:60px">
            <input id="sure" class="inputButton" type="button" value="确定" />
            <input id="clear" class="inputButton" type="button" value="清空" />
            <input id="close" class="inputButton" type="button" value="关闭" />
        </div>
    </div>
    <script type="text/javascript">
        $(function(){
            $("#sure").click(function(){
            	
            	var newVal=$('input:radio[name="id"]:checked').attr('data');
            	var id=$('input:radio[name="id"]:checked').attr('value');

            	var origin = artDialog.open.origin;
            	var input = origin.document.getElementById('serviceType');
            	var input_id = origin.document.getElementById('serviceType_id');
            	if(newVal != null)
            		input.value = newVal;
            	input.select();
            	
            	input.id.value = id;
            	input.select();
            	art.dialog.close();
            })
            
            $("#clear").click(function(){
            	var origin = artDialog.open.origin;
            	var input = origin.document.getElementById('serviceType');
            	var input_id = origin.document.getElementById('serviceType_id');

            	input.value = "";
            	input.select();
            	input_id.value= "";
            	input.select();
            	art.dialog.close();
            })
            
            $("#close").click(function(){
            	art.dialog.close();
            }) 
        })
    </script>
</body>
</html>
