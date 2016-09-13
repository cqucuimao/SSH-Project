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
	<link href="skins/main.css" rel="stylesheet" type="text/css" />
</head>
<body class="minW">
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>年审信息</h1>
        </div>        		
		<div class="tab_next style2">
			<table>
				<tr>
				    <td><s:a action="carExamineAppointment_list"><span>预约车辆年审</span></s:a></td>
					<td class="on"><a href="#"><span>车辆年审记录</span></a></td>
				</tr>
			</table>
		</div>
		<br/>			
            <p style="color: red">
				<s:if test="hasFieldErrors()">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<s:property />
						</s:iterator>
					</s:iterator>
				</s:if>
			</p>
        <div class="editBlock detail p30">
        <s:form action="carExamine_%{id == null ? 'add' : 'edit'}" id="pageForm">
        	<s:hidden name="id"></s:hidden>
        	<s:hidden name="editNormalInfo" id="editNormalInfo"></s:hidden>
        	<s:hidden name="editKeyInfo" id="editKeyInfo"></s:hidden>
            <table>
            	<colgroup>
					<col width="80"></col>
					<col></col>
					<col></col>
					<col></col>
					<col></col>
					<col width="120"></col>
				</colgroup>
                <tbody>
                	<tr>
                        <th><s:property value="tr.getText('car.CarExamine.car')" /><span class="required">*</span></th>
						<td>
							<cqu:carSelector name="car" synchDriver="driver"/>
						</td>
                    </tr>
                    <tr>
						<th><s:property value="tr.getText('car.CarExamine.driver')" /><span class="required">*</span></th>
						<td>
							<cqu:userSelector name="driver"/>
						</td>
					</tr>
                    <tr>
                    	<th><s:property value="tr.getText('car.CarExamine.date')" /><span class="required">*</span></th>
                        <td>
							<s:textfield cssClass="inputText" id="date" name="date" class="Wdate half" onchange="getDate()" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
						</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('car.CarExamine.nextExamineDate')" /><span class="required">*</span></th>
                        <td>
							<s:textfield cssClass="inputText" id="nextExamineDate" name="nextExamineDate" class="Wdate half" onchange="getDate1()" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
							<input class="btn" type="button" name="getNextExamineDate" value="点击获取推荐日期">
						</td>
                    </tr>  
                    <tr>
                        <th><s:property value="tr.getText('car.CarExamine.carPainterMoney')" /></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="carPainterMoney" id="carPainterMoney"/>
                        </td>
                    </tr>                  
                    <tr>
                        <th><s:property value="tr.getText('car.CarExamine.money')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="money" id="money"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarExamine.otherFee')" /></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="otherFee"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarExamine.memo')" /></th>
                        <td>
                        	<s:textarea cssClass="inputText" style="height:100px" name="memo" id="memo"></s:textarea>
                        </td>
                    </tr>             
                </tbody>
            </table>
            <div class="toll" style="display:none">
            <div class="title">
			<br/><br/>
			<h2>路桥费</h2>
			</div>
            <table>
            	<colgroup>
					<col width="80"></col>
					<col></col>
					<col></col>
					<col></col>
					<col></col>
					<col width="120"></col>
				</colgroup>
            		<tbody>
                	<tr>
                        <th><s:property value="tr.getText('car.TollCharge.payDate')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="payDate" id="payDate" class="Wdate half" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.TollCharge.money')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="tollChargeMoney"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.TollCharge.overdueFine')" /></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="overdueFine"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.TollCharge.moneyForCardReplace')" /></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="moneyForCardReplace"/>
                        </td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('car.TollCharge.nextPayDate')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="nextPayDate" id="nextPayDate" class="Wdate half" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
                        </td>
                    </tr>
            	</tbody>
            	  
            </table>
            </div>
            <table>
            <tfoot>
                    <tr>
                        <td colspan="2">
                        	<input name="actionFlag" type="hidden" value="${actionFlag }">       
                            <input class="inputButton" type="submit" value="确定" />
                             <a class="p15" href="javascript:history.go(-1);">返回</a>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </s:form>
        </div>
    </div>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
    <script type="text/javascript" src="js/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="js/validate/messages_cn.js"></script>
    <script type="text/javascript">
    

		var date = $("#date").val();
		$("#payDate").val(date);
    
    	function getDate(){
    		var date = $("#date").val();
    		$("#payDate").val(date);
    	}	
    	
    	function getDate1(){
    		var date = $("#nextExamineDate").val();
    		$("#nextPayDate").val(date);
    	}	
    
    	//登记时显示路桥费信息，编辑时不显示
    	var actionFlag = $("input[name=actionFlag]").val();
    	if(actionFlag == "register"){
    		$(".toll").show();
    	}
    
    	//编辑时的权限处理
		if($("#editNormalInfo").val() == "true" && $("#editKeyInfo").val() == "false" ){
			$("input[name=date]").attr("readonly",true);
			$("input[name=date]").removeAttr("onfocus"); 
			$("input[name=carPainterMoney]").attr("readonly",true);
			$("input[name=money]").attr("readonly",true);
		}
		if($("#editKeyInfo").val() == "true" && $("#editNormalInfo").val() == "false"){
			$("#carLabel").removeAttr("onclick");
	    	$("#carLabel").attr("readonly", true); 
	    	$("#driverLabel").removeAttr("onclick");
	    	$("#driverLabel").attr("readonly", true); 
			$("input[name=nextExamineDate]").attr("readonly",true);
			$("input[name=nextExamineDate]").removeAttr("onfocus"); 
	    	$("input[name=otherFee]").attr("readonly",true);
	    	$("#memo").attr("readonly",true);
		}
    	
	    $(function(){
			$("#pageForm").validate({
				submitout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					carLabel:{
						required:true,
					},
					driverLabel:{
						required:true,
					},
					nextExamineDate:{
						required:true,
					},
					date:{
						required:true,
					},
					money:{
						required:true,
						number:true,
					},
					examineIntervalYear:{
						required:true,
					},
					carPainterMoney:{
						number:true,
						min:1
					},
					payDate:{
						required:true,
					},
					tollChargeMoney:{
						required:true,
						number:true,
						min:0
					},
					overdueFine:{
						number:true,
						min:0
					},
					moneyForCardReplace:{
						number:true,
						min:0
					},
					nextPayDate:{
						required:true,
					},
				}
			});
			
			$("input[name=getNextExamineDate]").click(function(){
				var carId = $("#car").val();
				var recentExamineDate = $("#date").val();
				if(carId != "" && recentExamineDate != ""){
					$.ajax({
						url : 'carExamine_getNextExamineDate.action', 
						data : {
							carId:carId,
							recentExamineDate:recentExamineDate
						},
						type : 'post',
						dataType : 'json',
						success : function(data) {
							var time = new Date(data.nextExamineDate);
							var month = time.getMonth()+1;
							var day = time.getDate();
							if(month<10){
								if(day<10){
									var time1 = time.getFullYear()+"-0"+month+"-0"+day;
								}else{
									var time1 = time.getFullYear()+"-0"+month+"-"+day;
								}				
							}else{
								if(day<10){
									var time1 = time.getFullYear()+"-"+month+"-0"+day;
								}else{
									var time1 = time.getFullYear()+"-"+month+"-"+day;
								}	
							}							
							$("#nextExamineDate").val(time1);
							$("#nextPayDate").val(time1);
						},
						error : function(msg) {  			         
					    	console.log("异常"+msg);  
					    } 
					});
				}
			});
			
			formatDateField2($("#date"));
			formatDateField2($("#nextExamineDate"));
			formatDateField1($("#payDate"));
			formatDateField1($("#nextPayDate"));
		});
    </script>
</body>
</html>
