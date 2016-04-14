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
        <!-- 数据列表 -->
        <div class="dataGrid">
            <div class="tableWrap fixW fixed-height">
                <table>
                    <colgroup>
                        <col width="60px"></col>
                        <col width="300px"></col>
                    </colgroup>
                    <thead>
                        <tr>
                            <th class="alignCenter"></th>
                            <th>部门名称</th>
                        </tr>
                    </thead>
                    <tbody class="tableHover">
                        <s:iterator value="depts">
                        <tr>
                            <td class="alignCenter">
                            	<input type="radio" name="id" value="${id}" data="${name}" />
                            </td>
                            <td>${name}</td>
                        </tr>
                        </s:iterator>
                         
                    </tbody>
                </table>
            </div>
        </div>
        <div class="bottomBar alignCenter">
            <input id="sure" class="inputButton" type="button" value="确定" />
            <input id="clear" class="inputButton" type="button" value="清空" />
            <input id="close" class="inputButton" type="button" value="关闭" />
        </div>
    </div>
    <script type="text/javascript">
        $(function(){
            $("#sure").click(function(){
            	
            	var newVal=$('input:radio[name="id"]:checked').attr('data');
            	var origin = artDialog.open.origin;
            	var input = origin.document.getElementById('department_name');
            	if(newVal != null)
            		input.value = newVal;
            	input.select();
            	art.dialog.close();
            })
            
            $("#clear").click(function(){
            	var origin = artDialog.open.origin;
            	var input = origin.document.getElementById('department_name');
            	input.value = "";
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
