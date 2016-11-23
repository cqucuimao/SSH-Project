<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cqu" uri="//WEB-INF/tlds/cqu.tld" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
	<link rel="stylesheet" type="text/css" href="skins/main.css">
</head>
<body class="minW">
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>分组列表</h1>
		</div>
		<div class="editBlock search">
			<table>
				<tr>
					<td>
						<s:a cssClass="buttonA" action="realtime_monitorGroupAddUI">新增分组</s:a>
					</td>
					<td>
						<a class="p15" href="javascript:history.go(-1);">返回</a>
					</td>
				</tr>
			</table>
		</div>
		<div class="dataGrid">
			<div class="tableWrap">
				<table>
					
					<thead>
						<tr>
							<th>分组名</th>
                			<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="monitorGroups">
						<tr>
							<td>${title }</td>
							<td>
								<s:a action="realtime_monitorGroupEditUI?monitorGroupId=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
                    			<s:a action="realtime_delete?monitorGroupId=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
                			</td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>	
	<script src="js/jquery-1.7.2.js"></script>
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
	<script type="text/javascript">
		$(function(){
	    })
	</script>
</body>
</html>
