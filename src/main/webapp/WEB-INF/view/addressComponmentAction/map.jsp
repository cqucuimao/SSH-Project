<%@ page language="java" pageEncoding="utf-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<link href="<%=basePath %>skins/Pager.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<%=basePath %>skins/main.css">
<script language="javascript" src="<%=basePath %>js/jquery1.13.js"></script>
<script language="javascript" src="<%=basePath %>js/jquery.pager.js"></script>
<script type="text/javascript" src="<%=basePath %>js/common.js"></script>

<script type="text/javascript">
	$(document).ready(function() {

		var id = window.parent.activeId;
		$("#location").attr("id", id + "location");
		$("#trigger8").attr("id", id + "trigger8");
		$("#list").attr("id", id + "list");
		$("#pager").attr("id", id + "pager");
		$("#detailLocation").attr("id", id + "detailLocation");
	});
	function closePage(){
		popdown("adressSelect");
	}
</script>
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=hxTHFQNXApPMBdYM8CIfoS9x"></script>
<title>地图demo</title>
</head>
<body>
<br/>
<div class="editBlock">
<table>
	<colgroup>
		<col width="80"/><col/>
		<col/><col/>
	</colgroup>
<tbody>
<tr>
<th>&nbsp;&nbsp;地址名称<span class="required">*</span></th>
<td>
<div class="adressText input-group mb5" name="address111">
	<!-- 
	<span class="adress-btn input-group-addon">
		<i class="icon-favorite"></i>
	</span> 
	 -->
	<input class="inputText" cssClass="inputText" id="${param.id}location" type="text" text="请输入地址信息" onkeyup=tttt(this); />
	<ul class="list" id="${param.id}list"></ul>
</div>
</td>
</tr>
<tr>
<th>&nbsp;&nbsp;地址详情<span class="required">*</span></th>
<td>
<div class="input-group">
	<input class="inputText" cssClass="inputText" id="${param.id}detailLocation" type="text" text="地址详细信息" />
	<input value="确定"  type="button" onclick="closePage()" class="inputButton floatR" style="text-decoration: none;margin-left:5px;height:36px"/>
</div>
</td>
</tr>
</tbody>
</table>
</div>
<div id="allmap"></div>
<script type="text/javascript" src="<%=basePath %>js/map.js"></script>
</body>
</html>

