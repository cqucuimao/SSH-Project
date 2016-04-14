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
                        <col width="20px"></col>
                        <col width="340px"></col>
                    </colgroup>
                    <thead>
                        <tr>
                            <th class="alignCenter"></th>
                            <th>常用地址</th>
                        </tr>
                    </thead>
                    <tbody class="tableHover">
                        <s:iterator value="recordList">
                        <tr>
                            <td class="alignCenter">
                            	<input type="radio" name="id" value="${id}" data="${description}&&${detail}&&${location.longitude}&&${location.latitude}" />
                            </td>
                            <td>${description}（${detail}）</td>
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
        <div class="bottomBar alignCenter">
            <input id="sure" class="inputButton" type="button" value="确定" />
            <input id="close" class="inputButton" type="button" value="关闭" />
        </div>
    </div>
    <script type="text/javascript">
        $(function(){
            $("#sure").click(function(){
            	var newVal=$('input:radio[name="id"]:checked').attr('data');
            	var values=newVal.split("&&");

            	var origin = artDialog.open.origin;
            	origin.document.getElementById("${componentId}location").value=values[0];
            	origin.document.getElementById("${componentId}detailLocation").value=values[1];
            	origin.document.getElementById("${componentId}lng").value=values[2];
            	origin.document.getElementById("${componentId}lat").value=values[3];
            	
            	art.dialog.close();
            })
                        
            $("#close").click(function(){
            	art.dialog.close();
            })
        })
    </script>
    
</body>
</html>
