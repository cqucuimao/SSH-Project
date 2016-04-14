<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择用户</title>
<link rel="stylesheet" href="js/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
<link href="skins/main.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/ztree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="js/ztree/jquery.ztree.excheck-3.5.js"></script>

<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
</head>
<body>
    <div class="space">
    	<div class="editBlock search">
			<s:form id="pageForm" action="user_popup">
			<table>
				<tr>
					<th>用户名:</th>
					<td><s:textfield cssClass="inputText" name="name" type="text" /></td>
					<td>
						<input class="inputButton" type="submit" value="查询"/>
					</td>
				</tr>
			</table>
			</s:form>
		</div>
        <!-- 数据列表 -->
        <div class="content_wrap">
	        <div class="dataGrid" >
	            <div class="tableWrap fixW fixed-height">
	                <div class="zTreeDemoBackground left">
						<ul id="treeDemo" class="ztree"></ul>
					</div>
	            </div>
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
        	var setting = {
        			check: {
        				enable: true,
        				chkStyle: "radio",
        				radioType: "all"
        			}
        	};

        	var zNodes = <s:property value="#nodes" escape="false"/>;

        	$(document).ready(function(){
        		$.fn.zTree.init($("#treeDemo"), setting, zNodes);
        	});
        	
        	$("#sure").click(function(){
            	
                var treeObj=$.fn.zTree.getZTreeObj("treeDemo"),
        		nodes = treeObj.getCheckedNodes(true),
            	newVal = "";  
                newId="";
                for(var i=0;i<nodes.length;i++){
                   	if(!nodes[i].isParent) {
                   		newVal = nodes[i].name;
                   		newId = nodes[i].id;
                   	}
                }
            	var origin = artDialog.open.origin;
            	var driverName = origin.document.getElementById('driverName');
            	var driverId = origin.document.getElementById('driverId');
		
            	if(newVal != null && newVal !="")
                	driverName.value = newVal;
            	driverName.select();
            	
            	driverId.value=newId;
            	driverId.select();
            	art.dialog.close();
            })
            
            $("#clear").click(function(){
            	var origin = artDialog.open.origin;
            	var input = origin.document.getElementById('driverName');
            	var driverId = origin.document.getElementById('driverId');
            	
            	driverId.value="";
            	driverId.select();
            	
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
