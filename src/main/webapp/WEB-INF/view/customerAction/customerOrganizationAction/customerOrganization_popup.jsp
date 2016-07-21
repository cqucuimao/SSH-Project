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
			
		<div class="editBlock search">
        <s:form id="pageForm" action="customerOrganization_popup">
			<table>
				<tr>
					<th><s:property value="tr.getText('order.CustomerOrganization.name')" /></th>
					<td><s:textfield cssClass="inputText" name="customerOrganizationName" type="text" /></td>
					<td>
						<input class="inputButton" type="submit" value="查询"/>
					</td>
				</tr>
			</table>
		</s:form>
		</div>
        
        <!-- 数据列表 -->
        <div class="dataGrid">
            <div class="tableWrap fixW fixed-height">
                <table>
                    <colgroup>
                        <col width="20px"></col>
                        <col width="100px"></col>
                        <col width="240px"></col>
                    </colgroup>
                    <thead>
                        <tr>
                            <th class="alignCenter"></th>
                            <th><s:property value="tr.getText('order.CustomerOrganization.name')" /></th>
                            <th>单位地址</th>
                        </tr>
                    </thead>
                    <tbody class="tableHover">
                        <s:iterator value="recordList">
                        <tr>
                            <td class="alignCenter">
                            	<input type="radio" name="id" value="${id}" data="${name}" />
                            </td>
                            <td>${name}</td>
                            <td>${address.detail}</td>
                        </tr>
                        </s:iterator>
                    </tbody>
                </table>
            </div>
        </div>
			<s:form id="pageForm">
        			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
        	</s:form>
        <br/>
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
            	var input = origin.document.getElementById('customer_organization_name');
            	var input_id = origin.document.getElementById('customer_organization_id');
            	if(newVal != null)
            		input.value = newVal;
            	input.select();
            	
            	input.id.value = id;
            	input.select();
            	art.dialog.close();
            })
            
            $("#clear").click(function(){
            	var origin = artDialog.open.origin;
            	var input = origin.document.getElementById('customer_organization_name');
            	var input_id = origin.document.getElementById('customer_organization_id');

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
