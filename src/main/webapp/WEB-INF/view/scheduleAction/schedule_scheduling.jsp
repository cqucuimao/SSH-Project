<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>skins/main.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>skins/jquery.autocomplete.css">
</head>
<body class="minW">
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>调度</h1>
			<p style="color: red">
				<s:if test="hasFieldErrors()">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<s:property />
						</s:iterator>
					</s:iterator>
				</s:if>
			</p>
		</div>
		<form id="myForm" method="post" action="">			
			<s:hidden id="scheduleMode" name="scheduleMode"></s:hidden>
			<s:hidden id="scheduleFromQueueOrderId" name="scheduleFromQueueOrderId"></s:hidden>
			<s:hidden id="scheduleFromUpdateOrderId" name="scheduleFromUpdateOrderId"></s:hidden>
			<table class="body-layout">
				<tr>
					<td class="col-left">
						<!-- 左侧表单 -->
						<div class="editBlock">
							<table>
								<colgroup>
									<col width="80" />
									<col />
								</colgroup>
								<tbody>
									<tr>
										<th>单位名称<span class="required">*</span></th>
										<td>
											<s:textfield class="inputText" id="customerOrganizationName" name="customerOrganizationName" type="text" placeholder="请输入单位信息" />
										</td>
									</tr>
									<tr>
										<th>联系人<span class="required">*</span></th>
										<td>
											<div class="adressText half">
												<s:textfield class="inputText" type="text" placeholder="姓名" name="customerName" id="customerName" />
											</div>
											<div class="adressText half">
												<s:textfield class="inputText half last" name="phone" id="phone" type="text" placeholder="手机号码" />
											</div>
										</td>
									</tr>
									<tr>
										<th>&nbsp;</th>
										<td><input type="checkbox" class="m10" id="callForOther" name="callForOther"/>&nbsp;为他人叫车</td>
									</tr>
									<tr id="otherInfoTD">
										<th>&nbsp;</th>
										<td>
											<s:textfield class="inputText half" type="text" placeholder="乘车人姓名" name="otherPassengerName" id="otherPassengerName" />
											<s:textfield class="inputText half last" name="otherPhoneNumber" id="otherPhoneNumber" type="text" placeholder="乘车人手机号码" />
										</td>
									</tr>
									<tr id="otherSMSTD">
										<th>&nbsp;</th>
										<td><input type="checkbox" class="m10" id="callForOtherSendSMS" name="callForOtherSendSMS"/>&nbsp;给他发短信</td>
									</tr>
									<tr>
										<th>计费方式<span class="required">*</span></th>
										<td>
											<div class="filter">
												<label class="mr10">
													<input type="radio" name="radio" value="mileage" checked /><s:property value="tr.getText('order.ChargeModeEnum.MILE')" />
												</label>
												<label class="mr10">
													<input type="radio" name="radio" value="days" /><s:property value="tr.getText('order.ChargeModeEnum.DAY')" />
												</label>
												<label class="mr10">
													<input type="radio" name="radio" value="protocol" /><s:property value="tr.getText('order.ChargeModeEnum.PROTOCOL')" />
												</label>
											</div>
										</td>
									</tr>
									<tr>
										<th>起止时间<span class="required">*</span></th>
										<td><input class="Wdate half" type="text"
											id="planBeginDate" name="planBeginDate"
											onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" /> <span
											class="filter_days filter_protocol"> - <input
												class="Wdate half" type="text" id="planEndDate"
												name="planEndDate"
												onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" />
										</span></td>
									</tr>
									<tr>
										<th>乘车人数<span class="required">*</span></th>
										<td>
											<input class="inputText" type="text" style="width: 84px;" placeholder="请输入" id="passengerNumber" name="passengerNumber" />
											<span class="th">用车类型</span>
												<s:select name="serviceType" id="serviceType" list="serviceTypes" listKey="id" listValue="title" style="width:178px;"></s:select>
											</td>
									</tr>
									<tr>
										<th>上车地点<span class="required">*</span></th>
										<td>
											<jsp:include page="../addressComponmentAction/addressComponmentMap.jsp">
												<jsp:param value="0" name="id" />
												<jsp:param value="true" name="showHistory"/>
											</jsp:include>
										</td>
									</tr>
									<tr class="filter_mileage">
										<th>下车地点<span class="required">*</span></th>
										<td>
											<jsp:include page="../addressComponmentAction/addressComponmentMap.jsp">
												<jsp:param value="1" name="id" />
												<jsp:param value="true" name="showHistory"/>
											</jsp:include>
										</td>
									</tr>
									<tr>
										<th>估算里程</th>
										<td><input type="button" class="btn" value="估算" /> <span
											class="mileage">
										</span></td>
									</tr>
									<tr>
										<th></th>
										<td>
											<s:if test="!fromUpdate">
												<label><input type="checkbox" class="m10" id="needCopy" name="needCopy"/>复制该订单</label>
												<input class="inputText" type="text" style="width: 50px;" id="copyNumber" name="copyNumber"	placeholder="数量" /> 份</td>
											</s:if>
									</tr>
								</tbody>
							</table>
						</div>
					</td>
					<td class="col-right">
						<!-- 右侧列表 -->
						<div class="editBlock search Align tool">
							<input class="inputButton floatR" type="button" value="推荐司机"
								name="getDriver" />
							<table>
								<colgroup>
									<col width="50" />
									<col />
									<col width="50" />
									<col />
								</colgroup>
								<tbody>
								<tr>
									<th>司机</th>
									<td><s:textfield class="inputText" id="driverName" type="text" name="driver.name"/>
										<s:textfield id="driverId" name="driverId" type="hidden"/>
									</td>
									<th>车牌号</th>
									<td>
										<s:textfield id="car_platenumber" class="inputText inputChoose" onfocus="this.blur();" name="plateNumber" type="text" />
										<input class="inputButton" type="button" value="查询" name="button" id="findCar" />
									</td>
								</tr>
								</tbody>
							</table>
						</div>
						<div class="dataGrid">
							<div class="tableWrap driverList fixW">
								<table>
									<colgroup>
										<col width="30"></col>
										<col></col>
										<col></col>
										<col></col>
										<col></col>
										<col></col>
										<col></col>
										<col></col>
										<col></col>
									</colgroup>
									<thead>
										<tr>
											<th></th>
											<th>姓名</th>
											<th>车牌号</th>
											<th>联系方式</th>
										</tr>
									</thead>
									<tbody class="tableHover">
										<tr>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="driverChoice">
							<div class="note" style="color: red;">
								<s:property value="errors['selectCarError']" />
							</div>
						</div> <textarea style="height:100px;" class="w" name="memo" placeholder="请输入备注信息"></textarea>
						<div class="bottomBar borderT">
							<input type="button" id="schedule" class="inputButton" value="调度" />
							<input type="button" id="inQueue" class="inputButton" value="进入队列" />
							<input type="reset" id="reset" class="inputButton" value="重置" />							
							<s:if test="fromUpdate">
                            	<a class="p15" href="javascript:history.go(-1);">返回</a>
							</s:if>
						</div>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="allmap" style="display: none"></div>
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/DatePicker/WdatePicker.js"></script>
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>	
	<script type="text/javascript" src="<%=basePath%>js/common.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/jquery.autocomplete.js"></script>
	<script type="text/javascript">
		
		function initFromQueue(){
			if("filter_mileage"=="${chargeMode}"){
				$("input[type=radio][value=mileage]").attr("checked", "checked");
				$("input[type=radio][value=mileage]").click();
			}
			else if("filter_days"=="${chargeMode}"){
				$("input[type=radio][value=days]").attr("checked", "checked");
				$("input[type=radio][value=days]").click();
			}
			else if("filter_protocol"=="${chargeMode}"){
				$("input[type=radio][value=protocol]").attr("checked", "checked");
				$("input[type=radio][value=protocol]").click();
			}
			
			if("on"=="${callForOther}"){
				$("#callForOther").attr("checked", "checked");
				$("#otherInfoTD").show();
				$("#otherSMSTD").show();
				$("#otherPassengerName").val("${otherPassengerName}");
				$("#otherPhoneNumber").val("${otherPhoneNumber}");
				if("on"=="${callForOtherSendSMS}")
					$("#callForOtherSendSMS").attr("checked", "checked");
			}
			
			$("#planBeginDate").val("${planBeginDate}");
			$("#planEndDate").val("${planEndDate}");
			$("#passengerNumber").val("${passengerNumber}");
			$("#serviceType").find("option[value='${serviceType}']").attr("selected","selected");
			
			$("input[type=button][value=进入队列]").hide();
		}
		
		function initFromUpdate(){
			if("filter_mileage"=="${chargeMode}"){
				$("input[type=radio][value=mileage]").attr("checked", "checked");
				$("input[type=radio][value=mileage]").click();
			}
			else if("filter_days"=="${chargeMode}"){
				$("input[type=radio][value=days]").attr("checked", "checked");
				$("input[type=radio][value=days]").click();
			}
			else if("filter_protocol"=="${chargeMode}"){
				$("input[type=radio][value=protocol]").attr("checked", "checked");
				$("input[type=radio][value=protocol]").click();
			}
			
			if("on"=="${callForOther}"){
				$("#callForOther").attr("checked", "checked");
				$("#otherInfoTD").show();
				$("#otherSMSTD").show();
				$("#otherPassengerName").val("${otherPassengerName}");
				$("#otherPhoneNumber").val("${otherPhoneNumber}");
				if("on"=="${callForOtherSendSMS}")
					$("#callForOtherSendSMS").attr("checked", "checked");
			}
			
			$("#planBeginDate").val("${planBeginDate}");
			$("#planEndDate").val("${planEndDate}");
			$("#passengerNumber").val("${passengerNumber}");
			$("#serviceType").find("option[value='${serviceType}']").attr("selected","selected");
			
			$("#schedule").val("确认修改");
			$("#inQueue").hide();
			$("#reset").hide();
			
			//$(".driverChoice").empty();
			$(".driverChoice").append('调度给：'
							+ '<span class="driverInfo">'
							+ '<em>'
							+ '${selectCarDriverName}'
							+ '</em>'
							+ '<em>'
							+ '${selectCarPlateNumber}'
							+ '</em>'
							+ '<em>'
							+ '${selectCarDriverPhone}'
							+ '</em>'
							+ '<input type="hidden" id="selectCarId" name="selectCarId" value="${selectCarId}" ></input>'
							+ '</span>');
		}
	
	
		function taskClick(orderDate,carId){
			popup("订单详情","order_info.action?carId="+carId+"&orderDate="+orderDate,650,460,"orderDetail");
		}
		function checkEndTime(startTime, endTime) {
			var start = new Date(startTime.replace("-", "/").replace("-", "/"));
			var end = new Date(endTime.replace("-", "/").replace("-", "/"));
			if (end < start) {
				return false;
			}
			return true;
		}

		function checkMain() {
			if ($("#customerOrganizationName").val() == "") {
				alert("客户单位不能为空！");
				return false;
			}
			if ($("#customerName").val() == "") {
				alert("联系人不能为空！");
				return false;
			}
			var phone = $("#phone").val();
			if (phone == "") {
				alert("手机号码不能为空！");
				return false;
			}
			if (phone.length!=11) {
				alert("手机号码输入有误！");
				return false;
			}
			if($("#callForOther").attr("checked")=="checked"){
				if($("#otherPassengerName").val() == ""){
					alert("乘车人姓名不能为空！")
					return false;
				}
				if($("#otherPhoneNumber").val() == ""){
					alert("乘车人手机号码不能为空！")
					return false;
				}
				if ($("#otherPhoneNumber").val().length!=11) {
					alert("乘车人手机号码输入有误！");
					return false;
				}
			}
			if ($("#passengerNumber").val() == "") {
				alert("乘车人数不能为空！");
				return false;
			}
			if(isNaN($("#passengerNumber").val())){
				alert("乘车人数填写有误！");
				return false;
			}
			if ($("#planBeginDate").val() == "") {
				alert("上车时间不能为空！");
				return false;
			}
			if ($("#0location").val() == ""
					|| $("#0detailLocation").val() == "") {
				alert("上车地点不能为空！");
				return false;
			}
			if ($("#0lng").val() == "" || $("#0lat").val() == "") {
				alert("上车地点不能手动输入，请从搜索结果中选取！")
				return false;
			}
			if ($("#chargeMode").val() == "filter_days"
					|| $("#chargeMode").val() == "filter_protocol") {
				if ($("#planEndDate").val() == "") {
					alert("下车时间不能为空！");
					return false;
				}
				if (!checkEndTime($("#planBeginDate").val(), $("#planEndDate")
						.val())) {
					alert("下车时间晚于了上车时间！")
					return false;
				}
			}
			if ($("#chargeMode").val() == "filter_mileage") {
				if (($("#1location").val() == "" || $("#1detailLocation").val() == "")) {
					alert("下车地点不能为空!");
					return;
				}
				if ($("#1lng").val() == "" || $("#1lat").val() == "") {
					alert("下车地点不能手动输入，请从搜索结果中选取！")
				}
			}
			return true;
		}

		function checkDriver() {
			if($("#selectCarId").length==0){
				alert("还没有选择调度的车辆！");
				return false;
			}
			return true;
		}

		function checkEstimate() {
			if ($("#chargeMode").val() == "filter_mileage") {
				if ($("#0location").val() == ""
						|| $("#0detailLocation").val() == "") {
					alert("上车地点不能为空！");
					return false;
				}
				if ($("#0lng").val() == "" || $("#0lat").val() == "") {
					alert("上车地点不能手动输入，请从搜索结果中选取！")
					return false;
				}
				if (($("#1location").val() == "" || $("#1detailLocation").val() == "")) {
					alert("下车地点不能为空!");
					return;
				}
				if ($("#1lng").val() == "" || $("#1lat").val() == "") {
					alert("下车地点不能手动输入，请从搜索结果中选取！")
				}
			} else if ($("#chargeMode").val() == "filter_days"
					|| $("#chargeMode").val() == "filter_protocol") {
				if ($("#planBeginDate").val() == "") {
					alert("上车时间不能为空！");
					return false;
				}
				if ($("#planEndDate").val() == "") {
					alert("下车时间不能为空！");
					return false;
				}
				if (!checkEndTime($("#planBeginDate").val(), $("#planEndDate")
						.val())) {
					alert("下车时间晚于了上车时间！")
					return false;
				}
			}
			return true;
		}
		
		function checkQueryCar(){
			if ($("#planBeginDate").val() == "") {
				alert("上车时间不能为空！");
				return false;
			}
			if ($("#chargeMode").val() == "filter_days"
				|| $("#chargeMode").val() == "filter_protocol") {
				if ($("#planEndDate").val() == "") {
					alert("下车时间不能为空！");
					return false;
				}
				if (!checkEndTime($("#planBeginDate").val(), $("#planEndDate")
						.val())) {
					alert("下车时间晚于了上车时间！");
					return false;
				}
			}
			if($("#driverName").val()=="" && $("#car_platenumber").val()==""){
				alert("必须输入至少一个查询条件！");
				return false;
			}
			return true;
		}

		function checkRecommend() {
			if ($("#planBeginDate").val() == "") {
				alert("上车时间不能为空！");
				return false;
			}
			if ($("#0location").val() == ""
					|| $("#0detailLocation").val() == "") {
				alert("上车地点不能为空！");
				return false;
			}
			if ($("#0lng").val() == "" || $("#0lat").val() == "") {
				alert("上车地点不能手动输入，请从搜索结果中选取！")
				return false;
			}
			if ($("#chargeMode").val() == "filter_days"
					|| $("#chargeMode").val() == "filter_protocol") {
				if ($("#planEndDate").val() == "") {
					alert("下车时间不能为空！");
					return false;
				}
				if (!checkEndTime($("#planBeginDate").val(), $("#planEndDate")
						.val())) {
					alert("下车时间晚于了上车时间！")
					return false;
				}
			}
			return true;
		}
		
		function checkNeedDeprive(){
			var scheduleFromQueueOrderId = $("#scheduleFromQueueOrderId").val();
			$.ajax({
				url : 'schedule_isOrderDeprived.action', 
				data : {
					scheduleFromQueueOrderId : scheduleFromQueueOrderId
				},
				type : 'post',
				dataType : 'json',
				success : function(data) {
					if(data.result=="true")
						location.href="schedule_showDeprived.action";
				},
				error : function(msg) {  			         
			    	console.log("异常"+msg);  
			    } 
			});
		}

		$(function() {
			$(document).ready(
				function(){
					if($("#scheduleMode").val()=="FROM_QUEUE"){
						initFromQueue();
						setInterval(checkNeedDeprive,10000);
					}else if($("#scheduleMode").val()=="FROM_UPDATE"){
						initFromUpdate();
					}
				}
			);
			
			//Autocomplete
			$("#customerOrganizationName" ).autocomplete("schedule_getCustomerOrganization.action",{
				extraParams : {keyWord : function(){return $("#customerOrganizationName").val();}},
                formatItem: function(item) {
               		return item;  
              	},
				parse:function(data) {
					var parsed = [];  
                	for (var i = 0; i < data.length; i++) {  
                    	parsed[parsed.length] = {  
                        	data: data[i],  
                         	value: data[i],  
                         	result: data[i]  
                         };  
                     }                       
                     return parsed;  
                 }
    			});
			
			 $("#customerName" ).autocomplete("schedule_getCustomer.action",{
				 extraParams : {
					 				keyWord : function(){return $("#customerName").val();},
					 				customerOrganizationName : function(){return $("#customerOrganizationName").val();}
				 				},
				 formatItem: function(item) {
				 	return item;  
				 },
	             minChars : 0,
	             matchSubset : false,
				 parse:function(data) {
					 var parsed = [];  
                     for (var i = 0; i < data.length; i++) {  
                         parsed[parsed.length] = {  
                         	data: data[i],  
                         	value: data[i],  
                         	result: data[i] 
                         };  
                     }                       
                     return parsed;  
                 }
    			});
			
			 $("#phone" ).autocomplete("schedule_getPhoneNumber.action",{
				 extraParams : {
					 				customerOrganizationName : function(){return $("#customerOrganizationName").val();},
					 				customerName : function(){return $("#customerName").val();}
				 				},
				 formatItem: function(item) {
				 	return item;  
				 },
	             minChars : 0,
	             matchSubset : false,
				 parse:function(data) {
					 var parsed = [];  
                     for (var i = 0; i < data.length; i++) {  
                         parsed[parsed.length] = {  
                         	data: data[i],  
                         	value: data[i],  
                         	result: data[i] 
                         };  
                     }                       
                     return parsed;  
                 }
    			});
			
			 $("#otherPassengerName" ).autocomplete("schedule_getOtherPassengers.action",{
				 extraParams : {
					 				phone : function(){return $("#phone").val();}
				 				},
				 formatItem: function(item) {
				 	return item;  
				 },
	             minChars : 0,
				 parse:function(data) {
					 var parsed = [];  
                     for (var i = 0; i < data.length; i++) {  
                         parsed[parsed.length] = {  
                         	data: data[i],  
                         	value: data[i],  
                         	result: data[i] 
                         };  
                     }                       
                     return parsed;  
                 }
    			});
			 
			 $("#otherPhoneNumber" ).autocomplete("schedule_getOtherPhoneNumbers.action",{
				 extraParams : {
					 				phone : function(){return $("#phone").val();}
				 				},
				 formatItem: function(item) {
				 	return item;  
				 },
	             minChars : 0,
				 parse:function(data) {
					 var parsed = [];  
                     for (var i = 0; i < data.length; i++) {  
                         parsed[parsed.length] = {  
                         	data: data[i],  
                         	value: data[i],  
                         	result: data[i] 
                         };  
                     }                       
                     return parsed;  
                 }
    			});
			 
			$("#callForOther").click(
					function(){
						if($("#callForOther").attr("checked")=="checked"){
							$("#otherInfoTD").show();
							$("#otherSMSTD").show();
						}else{
							$("#otherInfoTD").hide();
							$("#otherSMSTD").hide();
						}
					});
			$(".inputButton[value=进入队列]").click(
					function() {
						if (checkMain())
							$("#myForm").attr("action",
									"schedule_inQueue.action").submit();
					});
			$(".inputButton[value=重置]").click(
					function() {
						$("input").not(".inputButton").not(".btn").attr("value","");
						$(".mileage").html("");
						$(".tableHover").find("td").remove();
						$(".driverChoice").empty();
					});
			
			var msg = new Msg(); //初始化一个操作提示对象
			$("#schedule").click(
					function() {
						if (checkMain() && checkDriver()){
							var chargeMode = $("#chargeMode").val();
							var planBeginDate = $("#planBeginDate").val();
							var planEndDate = $("#planEndDate").val();
							var selectCarId = $("#selectCarId").val();
							var serviceType = $("#serviceType").val();
							var passengerNumber = $("#passengerNumber").val();
							var scheduleFromUpdateOrderId = $("#scheduleFromUpdateOrderId").val();
							$.ajax({
								url : 'schedule_isCarAvailable.action',// 跳转到 action  
								data : {
								chargeMode : chargeMode,
								planBeginDate : planBeginDate,
								planEndDate : planEndDate,
								serviceType : serviceType,
								selectCarId : selectCarId,
								passengerNumber : passengerNumber,
								scheduleFromUpdateOrderId : scheduleFromUpdateOrderId
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									if (data.result == 0) {
										$("#myForm").attr("action",
										"schedule_startSchedule.action").submit();
									} else if (data.result == 1) {
										alert("订单已经被调度");
									} else if (data.result == 2) {
										alert("车辆已经被调度");
									} else if (data.result == 3) {
										alert("车辆已报废");
									} else if (data.result == 4) {
										alert("车辆在维修");
									} else if (data.result == 5) {
										alert("车辆在年审");
									} else if (data.result == 6) {
										alert("车辆在保养");
									} else if (data.result == 7) {
										alert("车型不匹配");
									} else if (data.result == 8) {
										alert("乘车人数超过限定");
									}
								},
								error : function(msg) {  			         
							    	console.log("异常"+msg);  
							    } 
							});
							
						}
					});
			$(".btn[value=估算]").click(
					function() {
						if (!checkEstimate())
							return;
						var chargeMode = $("#chargeMode").val();
						var planBeginDate = $("#planBeginDate").val();
						var planEndDate = $("#planEndDate").val();
						var serviceType = $("#serviceType").val();
						var lng0 = $("#0lng").val();
						var lat0 = $("#0lat").val();
						var lng1 = $("#1lng").val();
						var lat1 = $("#1lat").val();
						var selectCarId=$("#selectCarId").val();
						$.ajax({
							url : 'schedule_estimate.action',// 跳转到 action  
							data : {
							chargeMode : chargeMode,
							planBeginDate : planBeginDate,
							planEndDate : planEndDate,
							serviceType : serviceType,
							lng0 : lng0,
							lat0 : lat0,
							lng1 : lng1,
							lat1 : lat1,
							selectCarId : selectCarId
							},
							type : 'post',
							dataType : 'json',
							success : function(data) {
								$(".mileage").html("&nbsp;&nbsp;&nbsp;&nbsp;￥"+data.money);
							},
							error : function(msg) {  			         
						    	console.log("异常"+msg);  
						    } 
						});
					});
			$('input[name="getDriver"]')
					.click(
							function(x) {
								if (!checkRecommend())
									return;
								var chargeMode = $("#chargeMode").val();
								var serviceType = $("#serviceType").val();
								var lng = $("#0lng").val();
								var lat = $("#0lat").val();
								var planBeginDate = $("#planBeginDate").val();
								var planEndDate = $("#planEndDate").val();
								$
										.ajax({
											url : 'schedule_getRecommandDriver.action',// 跳转到 action  
											data : {
												chargeMode : chargeMode,
												serviceType : serviceType,
												lng0 : lng,
												lat0 : lat,
												planBeginDate : planBeginDate,
												planEndDate : planEndDate
											},
											type : 'post',
											dataType : 'json',
											success : function(data) {
												var header = $("#carInfoHeader");
												//填充表头
												$(".dataGrid").find("thead").find('th[class="alignCenter"]').remove();
				    							jQuery
														.each(
																data[0].carInfo,
																function(i, val) {
																	$(
																			".dataGrid")
																			.find(
																					"thead")
																			.find(
																					"tr")
																			.append(
																					'<th class="alignCenter">'
																							+ i
																							+ '</th>');
																});
												//填充数据项
												$(".tableHover").find("td")
														.remove();
												for (var i = 0; i < data.length; i++) {
													$(".tableHover").append(
															'<tr>');
													$(".tableHover")
															.append(
																	'<td class="alignCenter"><input type="radio"  name="radio2" value='+data[i].id+' /></td>');
													if (typeof (data[i].driverName) != "undefined") {
														$(".tableHover")
																.append(
																		'<td>'
																				+ data[i].driverName
																				+ '</td>');
													} else {
														$(".tableHover")
																.append(
																		'<td></td>');
													}
													if (typeof (data[i].carNumber) != "undefined") {
														$(".tableHover")
																.append(
																		'<td>'
																				+ data[i].carNumber
																				+ '</td>');
													} else {
														$(".tableHover")
																.append(
																		'<td></td>');
													}
													if (typeof (data[i].phone) != "undefined") {
														$(".tableHover")
																.append(
																		'<td>'
																				+ data[i].phone
																				+ '</td>');
													} else {
														$(".tableHover")
																.append(
																		'<td></td>');
													}
													jQuery
															.each(
																	data[i].carInfo,
																	function(date,
																			val) {
																		if (val == 0) {
																			$(
																					".tableHover")
																					.append(
																							'<td class="alignCenter"><i class="icon-car maintenance"></i>保养中</td>');
																		} else if (val == 1) {
																			$(
																					".tableHover")
																					.append(
																							'<td class="alignCenter"><i class="icon-car repair"></i>维修中</td>');
																		} else if (val == 2) {
																			$(
																					".tableHover")
																					.append(
																							'<td class="alignCenter"><i class="icon-car check"></i>年审中</td>');
																		} else if (val == 3) {
																			$(
																					".tableHover")
																					.append(
																							'<td class="alignCenter"><a href="javascript:taskClick(\''+date+'\','+data[i].id+');"><i class="icon-car"></i>任务中</a></td>');
																		} else {
																			$(
																					".tableHover")
																					.append(
																							'<td class="alignCenter orang"><i class="icon-car gray"></i>空闲</td>');
																		}
																	});
													$(".tableHover").append(
															'</tr>');
												}
												console.log(data);
											},
											error : function(data) {
												console.log(data);
											}
										});

							});
			$('.tableHover')
					.find("input:radio")
					.live(
							'click',
							function(x) {

								$(".driverChoice").empty();
								var driverName = x.target.parentNode.nextElementSibling.innerText;
								var carNumber = x.target.parentNode.nextElementSibling.nextElementSibling.innerText;
								var phone = x.target.parentNode.nextElementSibling.nextElementSibling.nextElementSibling.innerText;
								$(".driverChoice")
										.append(
												'调度给：'
														+ '<span class="driverInfo">'
														+ '<em>'
														+ driverName
														+ '</em>'
														+ '<em>'
														+ carNumber
														+ '</em>'
														+ '<em>'
														+ phone
														+ '</em>'
														+ '<!-- <i class="icon-delete"></i> -->'
														+ '<input type="hidden" id="selectCarId" name="selectCarId" value='+x.target.value+' ></input>'
														+ '</span>');
							});

			$(".inputButton[id=findCar]")
					.click(
							function() {
								if(!checkQueryCar())
									return;
								var chargeMode = $("#chargeMode").val();
								var planBeginDate = $("#planBeginDate").val();
								var planEndDate = $("#planEndDate").val();
								var driverId = $("#driverId").val();
								var carNumber = $("#car_platenumber").val();
								$
										.ajax({
											url : 'schedule_getCar.action',// 跳转到 action  
											data : {
												chargeMode : chargeMode,
												planBeginDate : planBeginDate,
												planEndDate : planEndDate,
												driverId : driverId,
												carNumber : carNumber
											},
											type : 'post',
											dataType : 'json',
											success : function(data) {
												var header = $("#carInfoHeader");
												//填充表头
												$(".dataGrid")
														.find("thead")
														.find(
																'th[class="alignCenter"]')
														.remove();
												jQuery
														.each(
																data[0].carInfo,
																function(i, val) {
																	$(
																			".dataGrid")
																			.find(
																					"thead")
																			.find(
																					"tr")
																			.append(
																					'<th class="alignCenter">'
																							+ i
																							+ '</th>');
																});
												//填充数据项
												$(".tableHover").find("td")
														.remove();
												for (var i = 0; i < data.length; i++) {
													$(".tableHover").append(
															'<tr>');
													$(".tableHover")
															.append(
																	'<td class="alignCenter"><input type="radio"  name="radio2" value='+data[i].id+' /></td>');
													if (typeof (data[i].driverName) != "undefined") {
														$(".tableHover")
																.append(
																		'<td>'
																				+ data[i].driverName
																				+ '</td>');
													} else {
														$(".tableHover")
																.append(
																		'<td></td>');
													}
													if (typeof (data[i].carNumber) != "undefined") {
														$(".tableHover")
																.append(
																		'<td>'
																				+ data[i].carNumber
																				+ '</td>');
													} else {
														$(".tableHover")
																.append(
																		'<td></td>');
													}
													if (typeof (data[i].phone) != "undefined") {
														$(".tableHover")
																.append(
																		'<td>'
																				+ data[i].phone
																				+ '</td>');
													} else {
														$(".tableHover")
																.append(
																		'<td></td>');
													}
													jQuery
															.each(
																	data[i].carInfo,
																	function(date,
																			val) {
																		if (val == 0) {
																			$(
																					".tableHover")
																					.append(
																							'<td class="alignCenter"><i class="icon-car maintenance"></i>保养中</td>');
																		} else if (val == 1) {
																			$(
																					".tableHover")
																					.append(
																							'<td class="alignCenter"><i class="icon-car repair"></i>维修中</td>');
																		} else if (val == 2) {
																			$(
																					".tableHover")
																					.append(
																							'<td class="alignCenter"><i class="icon-car check"></i>年审中</td>');
																		} else if (val == 3) {
																			$(
																					".tableHover")
																					.append(
																							'<td class="alignCenter"><a href="javascript:taskClick(\''+date+'\','+data[i].id+');"><i class="icon-car"></i>任务中</a></td>');
																		} else {
																			$(
																					".tableHover")
																					.append(
																							'<td class="alignCenter orang"><i class="icon-car gray"></i>空闲</td>');
																		}
																	});
													$(".tableHover").append(
															'</tr>');
												}
												console.log(data);
											},
											error : function(data) {
												console.log(data);
											}
										});
							});

			filter();
			$("#otherInfoTD").hide();
			$("#otherSMSTD").hide();
		})
	</script>
</body>
</html>