<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
<style>
	@media print 
	{
		.noprint{display:none;}
	}
	td{
		height:30px;
		padding-left:1px;
		padding-right:5px;
	}
	.ttt{
		float:left;
		width:100px;
		height:30px;
	}
	input{
		height:20px
	}
	.inputStyle{
		width:100%;
		height:80%;
	}
	.inputStyle1{
		width:55%;
		height:80%;
	}
</style>

    <div class="space">
        <div class="">
        <s:form id="myForm" action="">
        <input type="hidden" name="orderId" value="${orderId }"> 
        <table style="border-color:black;width:84%;">
        <tr>
			<table border="1" style="border-color:black;width:84%;">
	        		<tr>
						<th colspan="4" style="font-size:20px;border:none">重庆市渝勤汽车服务有限公司派车单</th>
					</tr>
					<tr>
						<td style="border:none" colspan="3"></td>
						<td style="border:none" class="alignCenter">渝勤运${sn }</td>
					</tr>
	        		<tr>
	        			<td class="alignCenter" width="14%">用车单位</td>
	        			<td class="alignCenter" width="28%">${customerOrganization.name }</td>
	        			<td class="alignCenter" width="14%">联系人电话</td>
	        			<td class="alignCenter" width="28%">${customer.name}：${phone }</td>
	        		</tr>
	        		<tr>
	        			<td class="alignCenter">定车时间</td>
	        			<td class="alignCenter"><s:date name="scheduleTime" format="yyyy-MM-dd"/></td>
	        			<td class="alignCenter">上车地点</td>
	        			<td class="alignCenter">${fromAddress}</td>
	        		</tr>
	        		<tr>
	        			<td class="alignCenter">车型</td>
	        			<td class="alignCenter">${serviceType.title }</td>
	        			<td class="alignCenter">车牌号</td>
	        			<td class="alignCenter">${car.plateNumber }</td>
	        		</tr>
	        		<tr>
	        			<td class="alignCenter" style="border-bottom:none">驾驶员/电话</td>
	        			<td class="alignCenter" style="border-bottom:none">${driver.name }：${driver.phoneNumber }</td>
	        			<td class="alignCenter" style="border-bottom:none">目的地</td>
	        			<td class="alignCenter" width="10%" style="border-top:none">
	        				<s:textfield class="inputStyle" name="destination" />
	        			</td>
	        		</tr>
	        	</table>      	
        </tr>
        <tr>
              <td>
              	<table border="1" style="border-color:black;width:84%;">
	        		<tr>
	        			<td class="alignCenter" width="8%">日期</td>
	        			<td class="alignCenter" width="10%">上车时间</td>
	        			<td class="alignCenter" width="10%">下车时间</td>
	        			<td class="alignCenter" width="36%">经过地点摘要</td>
	        			<td class="alignCenter" width="10%">实际公里</td>
	        			<td class="alignCenter" width="10%">收费公里</td>
	        		</tr>	        
					<!-- //TODO 临时措施 目前设备有问题 -->		
					<tr>
						<td class="alignCenter">实际开始时间</td>
						<td class="alignCenter"><s:textfield class="Wdate1 half" id="date" name="actualBeginDate" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
						<td class="alignCenter">&nbsp;</td>
						<td class="alignCenter">&nbsp;</td>
						<td class="alignCenter">&nbsp;</td>
						<td class="alignCenter">&nbsp;</td>
						<td style="display:none">&nbsp;</td>
					</tr>
					<!-- /////////////////////////////-->
	        		<s:iterator value="abstractTrackList" status="track" >
					<tr>
						<td class="alignCenter">
							<s:a action="order_deleteDayOrderDetail?orderId=%{orderId}&dodIndex=%{#track.index}" onclick="result=confirm('确认要删除吗？'); if(!result) coverHidden(); return result;"><i class="icon-operate-delete" title="删除"></i></s:a>
							<s:date name="getonDate" format="yyyy-MM-dd"/>
						</td>
						<td class="alignCenter"><s:textfield class="Wdate1 half" id="date" name="%{'dayDetails['+#track.index+'].getonDate'}" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
						<td class="alignCenter"><s:textfield class="Wdate1 half" id="date" name="%{'dayDetails['+#track.index+'].getoffDate'}" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
						<td class="alignCenter"><s:textfield class="inputStyle" name="%{'dayDetails['+#track.index+'].pathAbstract'}" /></td>
						<td class="alignCenter"><s:textfield class="inputStyle" name="%{'dayDetails['+#track.index+'].actualMile'}"/></td>
						<td class="alignCenter"><s:textfield class="inputStyle" name="%{'dayDetails['+#track.index+'].chargeMile'}" /></td>
						<td style="display:none"><input class="chargeMoney" type="text" value="${chargeMoney }"></td>
					</tr>
					</s:iterator>	
					<!-- //TODO 临时措施 目前设备有问题 -->	
					<tr>
						<td class="alignCenter">
							<s:a action="order_addDayOrderDetail?orderId=%{orderId}"><i class="icon-operate-edit" title="增加"></i></s:a>
						</td>
						<td class="alignCenter">&nbsp;</td>
						<td class="alignCenter">&nbsp;</td>
						<td class="alignCenter">&nbsp;</td>
						<td class="alignCenter">&nbsp;</td>
						<td class="alignCenter">&nbsp;</td>
						<td style="display:none">&nbsp;</td>
					</tr>				        		
					<tr>
						<td class="alignCenter">实际结束时间</td>
						<td class="alignCenter"><s:textfield class="Wdate1 half" id="date" name="actualEndDate" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
						<td class="alignCenter">&nbsp;</td>
						<td class="alignCenter">&nbsp;</td>
						<td class="alignCenter">&nbsp;</td>
						<td class="alignCenter">&nbsp;</td>
						<td style="display:none">&nbsp;</td>
					</tr>
					<!-- /////////////////////////////-->
					<s:iterator value="nullAbstractTrackList" >
					<tr>
						<td class="alignCenter"></td>
						<td class="alignCenter"></td>
						<td class="alignCenter"></td>
						<td class="alignCenter"></td>
						<td class="alignCenter"></td>
						<td class="alignCenter"></td>
					</tr>
					</s:iterator>
        		</table>
              </td>
         </tr>
        <tr>        	
        	<td>
        		<table id="tableId" border="1" style="border-color:black;width:84%;">
	        		<tr>
	        			<td class="alignCenter" width="11%" style="border-top:none">出库路码</td>
	        			<td class="alignCenter" width="10%" style="border-top:none"><s:textfield class="inputStyle" name="beginMile" /></td>
	        			<td class="alignCenter" width="11%" style="border-top:none">客户上车路码</td>
	        			<td class="alignCenter" width="10%" style="border-top:none"><s:textfield class="inputStyle" name="customerGetonMile" /></td>
	        			<td class="alignCenter" width="11%" style="border-top:none">客户下车路码</td>
	        			<td class="alignCenter" width="10%" style="border-top:none"><s:textfield class="inputStyle" name="customerGetoffMile" /></td>
	        			<td class="alignCenter" width="11%" style="border-top:none">回库路码</td>
	        			<td class="alignCenter" width="10%" style="border-top:none"><s:textfield class="inputStyle" name="endMile" /></td>
	        		</tr>
	        		<tr>
	        			<td class="alignCenter">油费</td>	        			
						<td class="alignCenter">
							<table border="0">
								<tr>
									<td width="50%"><s:textfield class="inputStyle" name="refuelMoney"  onblur="getTax();getMoney()"/></td>
									<td width="20%" align="right"><s:checkbox class="m10" name="refuelMoneyAccount" onchange="getTax();getMoney()"/></td>
									<td width="30%" align="left">入账</td>
								</tr>
							</table>
						</td>
						<td class="alignCenter">洗车费</td>
						<td class="alignCenter">
							<table border="0">
								<tr>
									<td width="50%"><s:textfield class="inputStyle" name="washingFee"  onblur="getTax();getMoney()"/></td>
									<td width="20%" align="right"><s:checkbox class="m10" name="washingFeeAccount" onchange="getTax();getMoney()"/></td>
									<td width="30%" align="left">入账</td>
								</tr>
							</table>
						</td>
						<td class="alignCenter">停车费</td>
						<td class="alignCenter">
							<table border="0">
								<tr>
									<td width="50%"><s:textfield class="inputStyle" name="parkingFee"  onblur="getTax();getMoney()"/></td>
									<td width="20%" align="right"><s:checkbox class="m10" name="parkingFeeAccount" onchange="getTax();getMoney()"/></td>
									<td width="30%" align="left">入账</td>
								</tr>
							</table>
						</td>
						<td class="alignCenter">计费路码</td>
						<td class="alignCenter"><s:textfield class="inputStyle" name="totalChargeMile" /></td>
	        		</tr>
	        		<tr>
	        			<td class="alignCenter">过路费（客户自理）</td>
						<td class="alignCenter"><s:textfield class="inputStyle" name="toll" onblur="getTax();getMoney()"/></td>
						<td class="alignCenter">食宿</td>
						<td class="alignCenter"><s:textfield class="inputStyle" name="roomAndBoardFee" onblur="getTax();getMoney()"/></td>
						<td class="alignCenter">其他费用</td>
						<td class="alignCenter">
							<table border="0">
								<tr>
									<td width="50%"><s:textfield class="inputStyle" name="otherFee" onblur="getTax();getMoney()"/></td>
									<td width="20%" align="right"><s:checkbox class="m10" name="otherFeeAccount" onchange="getTax();getMoney()"/></td>
									<td width="30%" align="left">入账</td>
								</tr>
							</table>
						</td>
						<td class="alignCenter">税费</td>
						<td class="alignCenter"><s:textfield class="inputStyle" name="tax" onblur="getMoney()"/></td>						
	        		</tr>
	        		<tr>
	        			<td class="alignCenter">核算金额</td>
						<td class="alignCenter" colspan="3"><s:textfield class="inputStyle" name="orderMoney" readonly="true"/></td>
						<td class="alignCenter">实收金额</td>
						<td class="alignCenter" colspan="3"><s:textfield class="inputStyle" name="actualMoney" /></td>
	        		</tr>
	        		<tr>
	        			<td colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;请为本次服务评价：&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="grade" value="4"/>非常满意&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="grade" value="3"/>满意&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="grade" value="2"/>一般满意&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="grade" value="1"/>不满意&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input id="gradeId" type="hidden" name="" value="${grade }">
						</td>
	        		</tr>
	        		<tr>
	        			<td class="alignCenter" style="height:60px">驾驶员签字</td>
						<td colspan="2"></td>
						<td class="alignCenter" colspan="2">用车人签字及电话</td>
						<td style="padding-left:25px" colspan="3">
						<img id="imgId" width="150" height="50" style="display:none" src="order_getSignature.action?imageId=${signature.id}" />
						<input id="signatureId" value="${signature.id }" style="display:none"/>
						</td>
	        		</tr>
	        		<tr>
	        			<td class="alignCenter">意见及建议</td>
						<td class="alignCenter" colspan="7"><s:textfield class="inputStyle" name="options"/></td>
	        		</tr>
	        		<tr>
	        			<td style="border:none;"></td>
						<td colspan="5" style="border:none;padding-left:50px" id="dateLabel"></td>			
						<td colspan="2" style="border:none">派车人：${scheduler.name }</td>
	        		</tr>
	        		<tr>
	        			<td style="border:none" colspan="2">用车电话：63219797</td>
						<td style="border:none"></td>
						<td style="border:none" colspan="2">夜间用车：60391610</td>
						<td style="border:none"></td>
						<td style="border:none" colspan="2">服务监督电话：60391609</td>
	        		</tr>
	        		<tr>
	        			<td style="border:none">　</td>
						<td style="border:none" colspan="7">　</td>
	        		</tr>
	        		<tr>
	        			<td style="border:none">　</td>
						<td style="border:none" colspan="7">
						<div class="ttt">注：白联</div>
						<div class="ttt">财务联，红联</div>
						<div class="ttt">运营联，篮联</div>
						<div class="ttt">存根联，黄联</div>
						<div class="ttt">客户联</div>
						</td>
	        		</tr>
	        		<tfoot>
						<tr >
			         	<td style="border:none" colspan="2" class="noprint">
			             	<input class="inputButton" id="sure" type="submit" value="确定" >
			             	<input class="inputButton" id="sureAndCheck" type="submit" value="确定并结算" >
			              	<a class="p15" href="javascript:history.go(-1);">返回</a>
			         	</td>
			    		</tr>
			    	</tfoot>
        		</table>
        	</td>
        </tr>
        </table>
</s:form>
        </div>
    </div>
    <script type="text/javascript">
    
    $("#sure").click(function(){ 
    	flag=1;
    	$("input[id=date]").each(function(){
    		if($(this).val().length==0)
    		{
    			flag=0;
    		}
    	});
    	if(flag==1)
   			$("#myForm").attr("action","order_editOrderBill.action");
    	else{
    		alert("出入库时间、上下车时间不能均不能为空！");
    		coverSwitcherOff();
    		return false;
    	}
   	})
   	
    $("#sureAndCheck").click(function(){   
    	flag=1;
    	$("input[id=date]").each(function(){
    		if($(this).val().length==0)
    		{
    			flag=0;
    		}
    	});
    	if(flag==1)
       		$("#myForm").attr("action","order_editAndCheckOrderBill.action");
    	else{
    		alert("出入库时间、上下车时间不能均不能为空！");
    		coverSwitcherOff();
    		return false;
    	}
   	})
    
    $(function(){
    	
    	$("input[id=date]").each(function(){
    		formatDateField3($(this));
    		//alert($(this).val());
    	})
    	
		var obj= $("#signatureId");
		if(obj.val() != null&&obj.val() != 0){
			$("#imgId").show();
		}
		
		var grade = $("#gradeId").val();
		if(grade == null || grade == 0){
			$("input[name=grade]").attr("checked",false);
		}if(grade == 1){
			$("input[name=grade][value=1]").attr("checked",true);
		}if(grade == 2){
			$("input[name=grade][value=2]").attr("checked",true);
		}if(grade == 3){
			$("input[name=grade][value=3]").attr("checked",true);
		}if(grade == 4){
			$("input[name=grade][value=4]").attr("checked",true);
		}
	 });
    $(function(){
    	//获取当前日期
    	today = new Date();  
		centry="";
		if  (today.getFullYear()<2000 )  
		    centry = "19" ; 
		date = centry + (today.getFullYear())+ "年" + 
		    		(today.getMonth() + 1 ) + "月" + 
		    		today.getDate() + "日 "; 
		//$("#tableId tr:eq(7) td:eq(1)").text(date);
    	$("#dateLabel").text(date);
    });
    //保留两位小数
    function toDecimal(x) {  
        var f = parseFloat(x);  
        if (isNaN(f)) {  
            return;  
        }  
        f = Math.round(x*100)/100;  
        return f;  
    } 
  	//计算税费,它的值是过路费、食宿、其它费用之和的3.6%
	function getTax(){
  		var refuelMoney=0;
  		if($("input[name=refuelMoneyAccount]").attr("checked")=="checked")
  			refuelMoney=$("input[name=refuelMoney]").val();
  		
  		var washingFee=0;
  		if($("input[name=washingFeeAccount]").attr("checked")=="checked")
  			washingFee=$("input[name=washingFee]").val();
  		
  		var parkingFee=0;
  		if($("input[name=parkingFeeAccount]").attr("checked")=="checked")
  			parkingFee=$("input[name=parkingFee]").val();

  		var otherFee=0;
  		if($("input[name=otherFeeAccount]").attr("checked")=="checked")
  			otherFee=$("input[name=otherFee]").val();
  		
  		var toll = $("input[name=toll]").val();
  		var roomAndBoardFee = $("input[name=roomAndBoardFee]").val();
  		
  		var allMoney = Number(refuelMoney)+Number(washingFee)+Number(parkingFee)+Number(otherFee)+Number(toll)+Number(roomAndBoardFee);
  		var tax = allMoney * 0.036;
  		if(tax!=0){
  	  		$("input[name=tax]").val(toDecimal(tax)); 		
  		}else{
  	  		$("input[name=tax]").val(""); 
  		}
	 }	
  	//核算金额
  	function getMoney(){
  		var refuelMoney=0;
  		if($("input[name=refuelMoneyAccount]").attr("checked")=="checked")
  			refuelMoney=$("input[name=refuelMoney]").val();
  		
  		var washingFee=0;
  		if($("input[name=washingFeeAccount]").attr("checked")=="checked")
  			washingFee=$("input[name=washingFee]").val();
  		
  		var parkingFee=0;
  		if($("input[name=parkingFeeAccount]").attr("checked")=="checked")
  			parkingFee=$("input[name=parkingFee]").val();

  		var otherFee=0;
  		if($("input[name=otherFeeAccount]").attr("checked")=="checked")
  			otherFee=$("input[name=otherFee]").val();
  		
  		var toll = $("input[name=toll]").val();
  		var roomAndBoardFee = $("input[name=roomAndBoardFee]").val();
  		
  		var tax = $("input[name=tax]").val();
  		var orderMoney = Number(refuelMoney)+Number(washingFee)+Number(parkingFee)+Number(otherFee)+Number(toll)+Number(roomAndBoardFee)+Number(tax);
  		$(".chargeMoney").each(function(){
  			 var value =$(this).val();
  			 orderMoney = Number(orderMoney) + Number(value);
  		}); 
  		if(orderMoney!=0){
  	  		$("input[name=orderMoney]").val(toDecimal(orderMoney)); 
  	  		$("input[name=actualMoney]").val(toDecimal(orderMoney));		
  		}else{
  	  		$("input[name=orderMoney]").val(""); 
  		}
  	}
    
    </script>
</cqu:border>
