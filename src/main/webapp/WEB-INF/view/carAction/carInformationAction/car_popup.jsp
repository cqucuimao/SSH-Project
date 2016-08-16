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
<body style="overflow-x:hidden">
    <div class="space">
    	<div class="editBlock search">
			<s:form id="pageForm" action="car_popup">
			<table>
				<tr>
					<th>车牌号:</th>
					<td><s:textfield cssClass="inputText" name="plateNumber" type="text" /></td>
					<td>						
						<input type="hidden" name="selectorName" value="${selectorName}"/>
						<input type="hidden" name="synchDriver" value="${synchDriver}"/>
						<input class="inputButton" type="submit" value="查询"/>
					</td>
				</tr>					
				<tr>
					<th>司机姓名:</th>
					<td><s:textfield cssClass="inputText" name="driverName" type="text" /></td>
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
        <!-- 填充div，防止部分数据被遮盖 -->
        <div style="width:245px;height:60px">  
        </div>
        <div class="bottomBar alignCenter" style="position:fixed;bottom:0;background-color:white;width:245px;height:60px">
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
                for(var i=0;i<nodes.length;i++){
                   	if(!nodes[i].isParent) {
                   		newVal = nodes[i].name;
                   		newId = nodes[i].id;
                   		if(nodes[i].param){
                   			synchDriverName=nodes[i].param.driverName;
                   			synchDriverId=nodes[i].param.driverId;
                   		}else{
                   			synchDriverName=false;
                   			synchDriverId=false;
                   		}
                   	}
                }
            	var origin = artDialog.open.origin;
            	var input = origin.document.getElementById("${selectorName}Label");
            	var inputId = origin.document.getElementById("${selectorName}");
            	input.value = newVal;
            	inputId.value = newId;
            	input.select();

            	if("${synchDriver}"!="null"){
            		driverName=origin.document.getElementById("${synchDriver}Label");
            		driverId=origin.document.getElementById("${synchDriver}");
					if(synchDriverName){       			
            			driverName.value=synchDriverName;
            			driverId.value=synchDriverId;
					}else{
						driverName.value="";
						driverId.value="";
					}
            	}            	
            	art.dialog.close();
            })
            
            $("#clear").click(function(){
            	var origin = artDialog.open.origin;
            	var input = origin.document.getElementById("${selectorName}Label");
            	var inputId = origin.document.getElementById("${selectorName}");
            	input.value = "";
            	inputId.value ="";
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
