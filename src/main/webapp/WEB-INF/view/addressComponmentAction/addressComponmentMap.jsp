<%@ page language="java" pageEncoding="utf-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<script language="javascript" src="<%=basePath %>js/jquery1.13.js"></script>
<script language="javascript" src="<%=basePath %>js/jquery.pager.js"></script>
<script language="javascript" src="<%=basePath %>js/popwin.js"></script>
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=hxTHFQNXApPMBdYM8CIfoS9x"></script>

<div>
<div class="adressText input-group mb5" name="address${param.id}">
	<span class="adress-btn input-group-addon" id="${param.id}historyAddress">
		<i class="icon-favorite" id="${param.id}"></i>
	</span>
	<input class="inputText" id="${param.id}location" name="address[${param.id}].description" type="text" placeholder="请输入地址信息" onkeyup=tttt(this); />
	<ul class="list" id="${param.id}list"></ul>
	<div id="${param.id}pager" class="page"></div>
</div>
<br>
<div class="input-group">
	<span class="input-group-addon">
	<i class="icon-map" id="${param.id}"></i>
	</span> 
	<input class="inputText" id="${param.id}detailLocation" name="address[${param.id}].detail" type="text" placeholder="地址详细信息" />
	<input type="hidden" name="address[${param.id}].location.longitude" id="${param.id}lng" /> 
	<input  type="hidden" name="address[${param.id}].location.latitude" id="${param.id}lat" /> 
</div>
</div>
<div id="allmap" style="display: none"></div>
<script type="text/javascript">
	$(document).ready(function() {
		if(""!="${address[param.id].description}")
			$("#${param.id}location").val("${address[param.id].description}");
		if(""!="${address[param.id].description}")
			$("#${param.id}detailLocation").val("${address[param.id].detail}");
		if(""!="${address[param.id].location.longitude}")
			$("#${param.id}lng").val("${address[param.id].location.longitude}");
		if(""!="${address[param.id].location.latitude}")
			$("#${param.id}lat").val("${address[param.id].location.latitude}");
		if("true"!="${param.showHistory}")
			$("#${param.id}historyAddress").hide();
	});
</script>
<script type="text/javascript" src="<%=basePath %>js/map.js"></script>

