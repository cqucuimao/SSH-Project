<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link href="skins/main.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-1.7.2.js"></script>
<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
<script type="text/javascript" src="<%=basePath%>js/common.js"></script>
<style >
 tr td:first-child 
   {
        border: 1px solid #eaeaea !important;
    }

</style>
</head>
<body>
 <div class="space">
    <div class="dataGrid">
			<div class="tableWrap">
				<table >
					<thead>
						<tr>
							<th style="text-align:center">车类</th>
							<th style="text-align:center">选择</th>
							<th style="text-align:center">车型</th>
							<th style="text-align:center">8小时<br/>（100公里内）</th>
							<th style="text-align:center">4小时<br/>（50公里内</th>
							<th style="text-align:center">超公里<br/>（每公里）</th>
							<th style="text-align:center">超时<br/>（每小时）</th>
							<th style="text-align:center">机场接送机<br/>（50公里/2小时）</th>
						</tr>
					</thead>
					<tbody class="tableHover">
								<s:iterator value="#session.mapList" id="column"> 
								<s:set name="total" value="#column.value.size"/> 
								<s:iterator value="#column.value" status="s" id="list1">
								<tr> 
									<s:if test="#s.first">
									<td class="td" rowspan="${total}">
										<s:property value="#column.key"/>
									</td>
									</s:if> 
									<td class="alignCenter">
                            			<input type="radio" name="id" value="${list1[0]}" data="${list1[1]}" />
                                	</td>
									<td>  
								 		<s:property value="#list1[1]"/>
								 	</td>
									<td>
										<s:property value="#list1[2]"/>
									</td> 
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
        <div class="bottomBar alignCenter" style="position:fixed;bottom:0;background-color:white;width:100%;height:60px">
            <input id="sure" class="inputButton" type="button" value="确定" />
            <input id="clear" class="inputButton" type="button" value="清空" />
            <input id="close" class="inputButton" type="button" value="关闭" />
        </div>
    </div>
    <script type="text/javascript">
        $(function(){
       	       var selectorName=art.dialog.data("selectorName");
       	       var origin = artDialog.open.origin;
       	       var input_id_value = origin.document.getElementById(selectorName).value;
       	       if(input_id_value!=null)
    	       {
    				$("input[type=radio][name='id'][value='"+input_id_value+"']").attr("checked",'checked');
    	       } 
            $("#sure").click(function(){
            	var selectorName=art.dialog.data("selectorName");
            	var newVal=$('input:radio[name="id"]:checked').attr('data');
            	var id=$('input:radio[name="id"]:checked').attr('value');

            	var origin = artDialog.open.origin;
            	var input = origin.document.getElementById(selectorName+"Label");
            	var input_id = origin.document.getElementById(selectorName); 
            	if(newVal != null)
            	input.value = newVal;
            	input.select();
            	
            	input_id.value = id;
            	input.select(); 
            	art.dialog.close();
            })
            
            $("#clear").click(function(){
            	var selectorName=art.dialog.data("selectorName");
            	var origin = artDialog.open.origin;
            	var input = origin.document.getElementById(selectorName+"Label");
            	var input_id = origin.document.getElementById(selectorName); 

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
