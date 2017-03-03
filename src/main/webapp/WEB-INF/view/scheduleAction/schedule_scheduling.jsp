<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>skins/jquery.autocomplete.css">
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
													<input type="radio" name="radio" value="days" checked /><s:property value="tr.getText('order.ChargeModeEnum.DAY')" />
												</label>
												<label class="mr10">
													<input type="radio" name="radio" value="protocol" /><s:property value="tr.getText('order.ChargeModeEnum.PROTOCOL')" />
												</label>												
												<label class="mr10">
													<input type="radio" name="radio" value="plane"/><s:property value="tr.getText('order.ChargeModeEnum.PLANE')" />
												</label>												
												<label class="mr10">
													<input type="radio" name="radio" value="mileage"/><s:property value="tr.getText('order.ChargeModeEnum.MILE')" />
												</label>
											</div>
										</td>
									</tr>
									<tr>
										<th>起止时间<span class="required">*</span></th>
										<td><input class="Wdate half" type="text"
											id="planBeginDate" name="planBeginDate"
											onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" /> <span
											class="filter_days filter_protocol"> - <input
												class="Wdate half" type="text" id="planEndDate"
												name="planEndDate"
												onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
										</span></td>
									</tr>
									<tr>
										<th>用车类型<span class="required">*</span></th>
										<td>
											<cqu:serviceTypeSelector name="serviceType" />
										</td>
									</tr>
									<tr>
										<th>上车地点<span class="required">*</span></th>
										<td>
											<s:textfield class="inputText" type="text" name="fromAddress" id="fromAddress"/>
										</td>
									</tr>
									<tr class="filter_mileage filter_plane">
										<th>下车地点<span class="required">*</span></th>
										<td>
											<s:textfield class="inputText" type="text" name="toAddress" id="toAddress" />
										</td>
									</tr>	
									<tr class="filter_protocol">
										<th>协议价格<span class="required">*</span></th>
										<td>
											<s:textfield class="inputText" type="text" name="protocolMoney" id="protocolMoney" />
										</td>
									</tr>			
									<tr class="filter_protocol">
										<th>收款周期</th>
										<td>
											<s:select class="SelectStyle" name="payPeriodId" list="#{'0':'每月','1':'每季度' ,'2':'每年' ,'3':'一次性'}" style="width:84px;"/>
											&nbsp;首次收款日期<span class="required">*</span>
											<s:textfield class="Wdate half" type="text" name="firstPayDate" id="firstPayDate" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
										</td>
									</tr>	
									<tr class="filter_protocol">
										<th>收款金额<span class="required">*</span></th>
										<td>
											<s:textfield class="inputText" type="text" name="moneyForPeriodPay" id="moneyForPeriodPay" />
										</td>
									</tr>					
									<tr>
										<th>业务员</th>
										<td><cqu:userSelector name="saler" departments="运营科;技术保障科"/></td>
									</tr>
									<tr>
										<th></th>
										<td>
											<s:if test="!fromUpdate">
												<label><input type="checkbox" class="m10" id="needCopy" name="needCopy"/>复制该订单</label>
												<input class="inputText" type="text" style="width: 50px;" id="copyNumber" name="copyNumber"	placeholder="数量" /> 份
											</s:if>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</td>
					<td class="col-right">
						<!-- 右侧列表 -->
						<div class="editBlock search Align tool">
							<input class="inputButton floatR" type="button" value="推荐车辆" name="getDriver" />
							<table>
								<tbody>
								<tr>
									<th>车牌号</th>
									<%-- <td><cqu:carSelector name="queryCar"/> --%>
									<td><cqu:carPlateNumber name="queryCar"/>
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
											<th>车牌号</th>
											<th>司机</th>
											<th>联系方式</th>
										</tr>
									</thead>
									<tbody class="tableHover">
									</tbody>
								</table>
							</div>
						</div>
						<div class="editBlock search">
							<table style="margin-top:10px;">
								<tbody>
								<tr>
									<th>调度的车</th>
									<td><cqu:carPlateNumber name="selectedCar"/></td> 
									<th>调度的司机</th>
									<td><cqu:driverName name="selectedDriver"/></td> 
									
									<%-- <th>调度的车</th>
									 <td><cqu:carSelector name="selectedCar" synchDriver="selectedDriver"/></td> 
									<th>调度的司机</th>
									<td><cqu:userSelector name="selectedDriver"/></td> --%>
								</tr>
								</tbody>
							</table>                             
						</div> 
						<textarea class="w" name="memo" placeholder="请输入备注信息"></textarea>
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
	<script type="text/javascript" src="<%=basePath%>js/jquery.autocomplete.js"></script>
	<script type="text/javascript">		
		function initFromQueue(){
			if("filter_mileage"=="${chargeMode}"){
				$("input[type=radio][value=mileage]").attr("checked", "checked");
				$("input[type=radio][value=mileage]").click();
			}else if("filter_plane"=="${chargeMode}"){
				$("input[type=radio][value=plane]").attr("checked", "checked");
				$("input[type=radio][value=plane]").click();
			}else if("filter_days"=="${chargeMode}"){
				$("input[type=radio][value=days]").attr("checked", "checked");
				$("input[type=radio][value=days]").click();
			}else if("filter_protocol"=="${chargeMode}"){
				$("input[type=radio][value=protocol]").attr("checked", "checked");
				$("#protocolMoney").val("${protocolMoney}");
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
			
			$("#fromAddress").val("${fromAddress}");
			$("#toAddress").val("${toAddress}");
			
			if("filter_days"=="${chargeMode}" || "filter_protocol"=="${chargeMode}"){
				formatDateField1($("#planBeginDate"));
				formatDateField1($("#planEndDate"));
				formatDateField1($("#firstPayDate"));
			}
			
			$("input[type=button][value=进入队列]").hide();
		}
		
		function initFromUpdate(){
			if("filter_mileage"=="${chargeMode}"){
				$("input[type=radio][value=mileage]").attr("checked", "checked");
				$("input[type=radio][value=mileage]").click();
			}else if("filter_plane"=="${chargeMode}"){
				$("input[type=radio][value=plane]").attr("checked", "checked");
				$("input[type=radio][value=plane]").click();
			}else if("filter_days"=="${chargeMode}"){
				$("input[type=radio][value=days]").attr("checked", "checked");
				$("input[type=radio][value=days]").click();
			}else if("filter_protocol"=="${chargeMode}"){
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
			
			$("#fromAddress").val("${fromAddress}");
			$("#toAddress").val("${toAddress}");
			
			$("#schedule").val("确认修改");
			$("#inQueue").hide();
			$("#reset").hide();
			
			if("filter_days"=="${chargeMode}" || "filter_protocol"=="${chargeMode}"){
				formatDateField1($("#planBeginDate"));
				formatDateField1($("#planEndDate"));
				formatDateField1($("#firstPayDate"));
			}
		}
	
	
		function taskClick(orderDate,carId){
			popup("订单详情","order_info.action?carId="+carId+"&orderDate="+orderDate,650,460,"orderDetail");
			coverHidden();
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
			if ($("#planBeginDate").val() == "") {
				alert("上车时间不能为空！");
				return false;
			}
			if($("#serviceType").val() == "") {
				alert("用车类型不能为空！");
				return false;
			}
			if ($("#fromAddress").val() == "") {
				alert("上车地点不能为空！");
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

			if ($("#chargeMode").val() == "filter_mileage" || $("#chargeMode").val() == "filter_plane") {
				if ($("#toAddress").val() == "") {
					alert("下车地点不能为空!");
					return false;
				}
			}
			
			if ($("#chargeMode").val() == "filter_protocol") {
				if ($("#protocolMoney").val() == "") {
					alert("协议价格不能为空！");
					return false;
				}else if(isNaN($("#protocolMoney").val())){
					alert("协议价格填写错误！");
					return false;
				}
				if($("#firstPayDate").val() == ""){
					alert("第一次收款日期不能为空！");
					return false;
				}
				if ($("#moneyForPeriodPay").val() == "") {
					alert("收款金额不能为空！");
					return false;
				}else if(isNaN($("#moneyForPeriodPay").val())){
					alert("收款金额填写错误！");
					return false;
				}
			}
			
			return true;
		}

		function checkDriver() {
			if($("#chargeMode").val() == "filter_protocol"){
				if($("#selectedCar").val()=="" && $("#selectedDriver").val()==""){
					alert("协议订单中也需要指定车或者司机！");
					return false;
				}
			}else{
				if($("#selectedCar").val()==""){
					alert("还没有选择调度的车辆！");
					return false;
				}
				if($("#selectedDriver").val()==""){
					alert("还没有选择调度的司机！");
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
			if($("#queryCar").val()==""){
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
			if($("#serviceType").val() == "") {
				alert("用车类型不能为空！");
				return false;
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
					}else{
						$("#fromAddress").val("电话联系");
						$("#toAddress").val("电话联系");
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
						if (checkMain()){
							coverShow();
							$("#myForm").attr("action","schedule_inQueue.action").submit();
						}
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
						//alert($("#carPlate").val());
						if (checkMain() && checkDriver()){
							var chargeMode = $("#chargeMode").val();
							var planBeginDate = $("#planBeginDate").val();
							var planEndDate = $("#planEndDate").val();
							var selectedCar = $("#selectedCar").val();
							var selectedDriver = $("#selectedDriver").val();
							var serviceType = $("#serviceType").val();
							var scheduleFromUpdateOrderId = $("#scheduleFromUpdateOrderId").val();
							$.ajax({
								url : 'schedule_isCarAndDriverAvailable.action',// 跳转到 action  
								data : {
								chargeMode : chargeMode,
								planBeginDate : planBeginDate,
								planEndDate : planEndDate,
								serviceType : serviceType,
								selectedCar : selectedCar,
								selectedDriver : selectedDriver,
								scheduleFromUpdateOrderId : scheduleFromUpdateOrderId
								},
								type : 'post',
								dataType : 'json',
								success : function(data) {
									if (data.result == "OK") {
										coverShow();
										$("#myForm").attr("action","schedule_startSchedule.action").submit();
									} else
										alert(data.result);
								},
								error : function(msg) {  
							    	console.log("异常"+msg);  
							    } 
							});
							
						}
					});
			$('input[name="getDriver"]').click(
					function(x) {
						if (!checkRecommend())
							return;
						var chargeMode = $("#chargeMode").val();
						var serviceType = $("#serviceType").val();
						var planBeginDate = $("#planBeginDate").val();
						var planEndDate = $("#planEndDate").val();
						$.ajax({
							url : 'schedule_getRecommandDriver.action',// 跳转到 action  
							data : {
										chargeMode : chargeMode,
										serviceType : serviceType,
										planBeginDate : planBeginDate,
										planEndDate : planEndDate
									},
							type : 'post',
							dataType : 'json',
							success : function(data) {
								var header = $("#carInfoHeader");
								$(".dataGrid").find("thead").find('th[class="alignCenter"]').remove();
								$(".tableHover").find("td").remove();
								
								if(data!=null && data.length>0){
									//填充表头
					    			jQuery.each(data[0].carInfo,function(i, val){
										$(".dataGrid").find("thead").find("tr").append('<th class="alignCenter">'+ i+ '</th>');
									});
									//填充数据项
									for (var i = 0; i < data.length; i++) {
										$(".tableHover").append('<tr>');
										$(".tableHover").append('<td class="alignCenter"><input type="radio"  name="radio2" value='+data[i].id+' /></td>');
										if (typeof (data[i].carNumber) != "undefined") {
											$(".tableHover").append('<td>'+ data[i].carNumber+ '</td>');
										} else {
											$(".tableHover").append('<td></td>');
										}
										if (typeof (data[i].driverName) != "undefined") {
											$(".tableHover").append("<td>"+ data[i].driverName + "<input type='hidden' value=';"+data[i].driverId+";'></input></td>");
										}else{
											$(".tableHover").append("<td><input type='hidden'></input></td>");
										}
										if (typeof (data[i].phone) != "undefined") {
											$(".tableHover").append('<td>'+ data[i].phone+ '</td>');
										} else {
											$(".tableHover").append('<td></td>');
										}
										jQuery.each(data[i].carInfo,function(date,val) {
											if (val == 0) {
												$(".tableHover").append('<td class="alignCenter"><a href="javascript:taskClick(\''+date+'\','+data[i].id+');"><i class="icon-car"></i>有任务</a></td>');
											} else {
												$(".tableHover").append('<td class="alignCenter orang"><i class="icon-car gray"></i>空闲</td>');
											}
										});
										$(".tableHover").append('</tr>');
									}
								}
								console.log(data);
							},
							error : function(data) {
								console.log(data);
							}
						});
			});
			$('.tableHover').find("input:radio").live('click',function(x) {
				var carId = x.target.value;
				var plateNumber = x.target.parentNode.nextElementSibling.innerText;
				var driverName = x.target.parentNode.nextElementSibling.nextElementSibling.innerText;
				var driverId = x.target.parentNode.nextElementSibling.nextElementSibling.innerHTML;
				driverId=driverId.split(";")[1];
				$("#selectedCarLabel").val(plateNumber);
				//$("#selectedCar").val(carId);
				$("#selectedCar").val(plateNumber);
				$("#selectedDriverLabel").val(driverName);
				//$("#selectedDriver").val(driverId);
				$("#selectedDriver").val(driverName);
			});

			$(".inputButton[id=findCar]").click(function() {
				if(!checkQueryCar())
					return;
				var chargeMode = $("#chargeMode").val();
				var planBeginDate = $("#planBeginDate").val();
				var planEndDate = $("#planEndDate").val();
				var queryCar = $("#queryCar").val();
				$.ajax({
					url : 'schedule_getCar.action',// 跳转到 action  
					data : {
								chargeMode : chargeMode,
								planBeginDate : planBeginDate,
								planEndDate : planEndDate,
								queryCar : queryCar
							},
					type : 'post',
					dataType : 'json',
					success : function(data) {
						var header = $("#carInfoHeader");
						$(".dataGrid").find("thead").find('th[class="alignCenter"]').remove();
						$(".tableHover").find("td").remove();
						if(data!=null && data.length>0){
							//填充表头
							jQuery.each(data[0].carInfo,function(i, val) {
								$(".dataGrid").find("thead").find("tr").append('<th class="alignCenter">'+ i+ '</th>');
							});
							//填充数据项
							for (var i = 0; i < data.length; i++) {
								$(".tableHover").append('<tr>');
								$(".tableHover").append('<td class="alignCenter"><input type="radio"  name="radio2" value='+data[i].id+' /></td>');
								if (typeof (data[i].carNumber) != "undefined") {
									$(".tableHover").append('<td>'+ data[i].carNumber+ '</td>');
								} else {
									$(".tableHover").append('<td></td>');
								}
								if (typeof (data[i].driverName) != "undefined") {
									$(".tableHover").append("<td>"+ data[i].driverName + "<input type='hidden' value=';"+data[i].driverId+";'></input></td>");
								}else{
									$(".tableHover").append("<td><input type='hidden'></input></td>");
								}							
								if (typeof (data[i].phone) != "undefined") {
									$(".tableHover").append('<td>'+ data[i].phone+ '</td>');
								} else {
									$(".tableHover").append('<td></td>');
								}
								jQuery.each(data[i].carInfo,function(date,val) {
									if (val == 0) {
										$(".tableHover").append('<td class="alignCenter"><a href="javascript:taskClick(\''+date+'\','+data[i].id+');"><i class="icon-car"></i>有任务</a></td>');
									} else {
										$(".tableHover").append('<td class="alignCenter orang"><i class="icon-car gray"></i>空闲</td>');
									}
								});
								$(".tableHover").append('</tr>');
							}
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
</cqu:border>