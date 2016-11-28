<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>

<style>
	.tdDiv{width:100px;text-align:center;float:left;margin-left:20px}
    .tdDiv1{width:150px;text-align:center;float:left;margin-left:35px}
    .tdDiv2{width:120px;text-align:center;float:left;margin-left:33px}
	.tdInput{background: #FFFFFF;border: 1px solid #bbb;height: 33px; line-height: 33px;font-size: 14px;font-weight:normal;padding: 0 4px 0 6px;vertical-align: middle;width:130px;}
    .tdInput:focus{ background:#f0fff3;border: 1px solid #65bd77;color: #65bd77;}
    .tdInput1{background: #FFFFFF;border: 1px solid #bbb;height: 33px; line-height: 33px;font-size: 14px;font-weight:normal;padding: 0 4px 0 6px;vertical-align: middle;width:132px;}
	</style>

	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>添加商业保险</h1>
		</div>
		<div class="editBlock detail p30">
		<s:form action="carInsurance_addCommercialInsurance" id="pageForm">
		<s:hidden name="id"></s:hidden>
				<table>
					<colgroup>
					<col width="80"></col>
					<col></col>
					<col></col>
					<col></col>
					<col></col>
					<col width="120"></col>
					</colgroup>
					<tbody class="tableHover" id="tab"> 
							<tr>
                    			<th width="100"><s:property value="tr.getText('car.Car.plateNumber')" />：</th>
                    			<td>${car.plateNumber}</td>
                			</tr>
                			<tr>
                    			<th><s:property value="tr.getText('car.CarInsurance.insureCompany')" />：</th>
                    			<td>${insureCompany}</td>
                			</tr>
                			<tr>
                    			<th><s:property value="tr.getText('car.CarInsurance.compulsoryPolicyNumber')" />：</th>
                    			<td>${compulsoryPolicyNumber}</td>
                			</tr>
                			<tr>
                    			<th><s:property value="tr.getText('car.CarInsurance.commercialPolicyNumber')" />：</th>
                    			<td>${commercialPolicyNumber}</td>
                			</tr>
                			<tr>
		                    	<th></th>
		                    	<th>
		                    			<div class="tdDiv">类型</div>
		                    			<div class="tdDiv1">生效日期</div>
		                    		    <div class="tdDiv1"> 截止日期</div>
		                    		    <div class="tdDiv2"> 承保金额（元）</div>
		                    		    <div class="tdDiv2">金额（元）</div>
		                    		    <div class="tdDiv2">备注</div>
		                    	</th>
		                    </tr>  
                			<tr>
                    			<th>交强险：</th>
                    			<td>
                    			<div class="tdDiv">——</div>
                    			<div class="tdDiv1"><s:date name="compulsoryBeginDate" format="yyyy-MM-dd"/></div>			
		                    	<div class="tdDiv1"><s:date name="compulsoryEndDate" format="yyyy-MM-dd"/></div>   			
		                    	<div class="tdDiv2">——</div>		
		                    	<div class="tdDiv2" id="compulsoryMoney"> ${compulsoryMoney }</div>
		                    	<div class="tdDiv2">——</div>
                    			</td>
                			</tr>
                			<tr>
                    			<th>车船税：</th>
                    			<td>
                    			<div class="tdDiv">——</div>
                    			<div class="tdDiv1"><s:date name="vehicleTaxBeginDate" format="yyyy-MM-dd"/></div>
		                    	<div class="tdDiv1"><s:date name="vehicleTaxEndDate" format="yyyy-MM-dd"/></div>
		                    	<div class="tdDiv2">——</div>
		                    	<div class="tdDiv2" id="vehicleTaxMoney">${vehicleTaxMoney }</div>
		                    	<div class="tdDiv2">——</div>
                    			</td>
                			</tr>
                			<s:iterator value="commercialInsurances">
                			<tr>
                				<th>商业保险：</th>
                				<td>
                				<div class="tdDiv">${commercialInsuranceType.name }</div>
                				<div class="tdDiv1"><s:date name="commercialInsuranceBeginDate" format="yyyy-MM-dd"/></div>
                				<div class="tdDiv1"><s:date name="commercialInsuranceEndDate" format="yyyy-MM-dd"/></div>
                				<div class="tdDiv2">${commercialInsuranceCoverageMoney }</div>
                				<div class="tdDiv2"><div class="commercialInsuranceMoney">${commercialInsuranceMoney }</div></div>
                				<div class="tdDiv2">${commercialInsuranceMemo}</div>
                				</td>
                			</tr>
                			</s:iterator>       
                			 <tr class="trClass">
		                    	<th>商业保险<span class="required">*</span></th>
		                    	<td>
		                    		<s:select name="commercialInsuranceType" class="selectClass"
		                        		list="commercialInsuranceTypes" listKey="id" listValue="name"
		                        		headerKey="" headerValue="请选择类型" style="width:142px;"
		                        		/>
		                    		<input class="Wdate half" name="commercialInsuranceBeginDate" type="text" onblur="getBeginDate()" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
									<input class="Wdate half" name="commercialInsuranceEndDate" type="text" onblur="getEndDate()" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
									<input class="tdInput" type="text" name="commercialInsuranceCoverageMoney"/>&nbsp;
									<input class="tdInput" type="text" name="commercialInsuranceMoney" onblur="getAllMoney()"/>&nbsp;
									<input class="tdInput" type="text" name="commercialInsuranceMemo"/>&nbsp;&nbsp;
									<input class="btn" id="btn" type="button" value="点击新增" />
								</td>
		                    </tr>           			
                			<tr>
                    			<th>保险起止时间：</th>
                    			<td><s:textfield class="Wdate half" id="fromDate" name="fromDate" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
                    					<s:textfield class="Wdate half" id="toDate" name="toDate" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
                    					<div id="date1" style="display:none"><s:date name="fromDate" format="yyyy-MM-dd"/></div>
                    					<div id="date2" style="display:none"><s:date name="toDate" format="yyyy-MM-dd"/></div>
                    			</td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarInsurance.money')" />(元)：</th>
                				<td><s:textfield class="inputText"  name="money" readonly="true"/></td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarInsurance.memo')" />：</th>
                				<td>${memo}</td>
                			</tr>
					</tbody>
					<tfoot>
                		<tr>
                    	<td colspan="2">
                    		<input type="hidden" name="inputRows"/>
                    		<input type="submit" name="sub" class="inputButton" value="确定"/>
                        	<a class="p15" href="javascript:history.go(-1);">返回</a>
                    	</td>
                		</tr>
            		</tfoot>
				</table>
				</s:form>
		</div>
	</div>
	<script type="text/javascript">
	function checkEndTime(startTime, endTime) {
		var start = new Date(startTime.replace("-", "/").replace("-", "/"));
		var end = new Date(endTime.replace("-", "/").replace("-", "/"));
		if (end < start) {
			return false;
		}
		return true;
	}
	
	//计算保险金额（总金额）  实现数字相加，Number(a1)+Number(a2);	
	 function getAllMoney(){    		
  		var compulsoryMoney = $("#compulsoryMoney").text();
  		var vehicleTaxMoney = $("#vehicleTaxMoney").text();
  		//var commercialInsuranceMoney =$("#commercialInsuranceMoney").text();
  		var money = Number(compulsoryMoney) + Number(vehicleTaxMoney);
  		$(".commercialInsuranceMoney").each(function(){
 			 var val =$(this).text();
 			 money=Number(money)+Number(val);
 		 }); 
  		$("input[name=commercialInsuranceMoney]").each(function(){
  			 var value =$(this).val();
  			 money=Number(money)+Number(value);
  		 }); 
  		$("input[name=money]").val(money.toFixed(2)); 
	 }	
	 
	 //计算保险开始时间
	 function getBeginDate(){
		 var maxBeginDate = $("#date1").text();
		$("input[name=commercialInsuranceBeginDate]").each(function(){
   			var value = $(this).val();
   			
			if(checkEndTime(maxBeginDate,value)){
				maxBeginDate = value;
   			}
   		 }); 
		$("input[name=fromDate]").val(maxBeginDate);
	 }
	 
	 //计算保险截止时间
	 function getEndDate(){
		 //alert("11");
		 var minEndDate = $("#date2").text();
		$("input[name=commercialInsuranceEndDate]").each(function(){
   			var value = $(this).val();
			if(checkEndTime(value,minEndDate)){
				minEndDate = value;
   			}
   		 }); 
		$("input[name=toDate]").val(minEndDate);
	 }
	
	 //点击新增一条商业保险
	 $("#btn").click(function(){
		 	var row = $("#tab").find('tr').length-4;	//获取table行数，减去5得到增加行的位置
	 		var row1 = row+1;	 
			$($('#tab').find('tr')[row]).after('<tr class="trClass"><th>&nbsp;&nbsp;&nbsp;&nbsp;<span class="required"></span></th>'+
				'<td><select name="commercialInsuranceType" id=select'+row1+' style="width:142px"></select>&nbsp;'+							
      			'<input  class="Wdate half" name="commercialInsuranceBeginDate" type="text" onblur="getBeginDate()" onfocus="new WdatePicker({dateFmt:\'yyyy-MM-dd\'})" />&nbsp;'+
				'<input  class="Wdate half" name="commercialInsuranceEndDate" type="text" onblur="getEndDate()" onfocus="new WdatePicker({dateFmt:\'yyyy-MM-dd\'})" />&nbsp;'+
				'<input  class="tdInput" type="text" name="commercialInsuranceCoverageMoney">&nbsp;&nbsp;'+
				'<input  class="tdInput" type="text" name="commercialInsuranceMoney" onblur="getAllMoney()"/>&nbsp;&nbsp;'+
				'<input class="tdInput" type="text" name="commercialInsuranceMemo"/>&nbsp;&nbsp;&nbsp;'+
				'<a href="#" class="deleteTr"><i class="icon-operate-delete" title="删除"></i></a></td></tr>'); 			
			$(".selectClass option").each(function(){
		 		$("#select"+row1).append('<option value='+$(this).val()+'>'+$(this).text()+'</option>');
		 	});	    
			//点击删除一条商业保险
			$(".deleteTr").click(function(){
   		 	$(this).parent('td').parent('tr').empty();
   		 	getAllMoney();
   		 	getBeginDate();
   		 	getEndDate();
   		}); 
	 })
	 
	  $("input[name=sub]").click(function(){
				 
		    		 if(!checkEndTime($("input[name=fromDate]").val(),$("input[name=toDate]").val())){
		    			 alert("开始时间晚于了截止时间！");
		    			 return false;
		    		 }
				 	
		    		 //提交时对商业保险中的字段进行判断
		    		 result=true;
		    		 $("select[name=commercialInsuranceType] option:selected").each(function(){
		    			 var value =$(this).attr("value");
	   					 if(value==""){
	   						 alert("请选择商业保险类型！");
	   						 result=false;
	   					 }
		    		 }); 
		    		 
		    		$("input[name=commercialInsuranceBeginDate]").each(function(){
		    			 var value =$(this).val();
		    			 if(value==""){
	   						 alert("请输入商业保险生效日期！");
	   						 result=false;
	   					 }
		    		 }); 
		    		
		    		$("input[name=commercialInsuranceEndDate]").each(function(){
		    			 var value =$(this).val();
		    			 if(value==""){
	  						 alert("请输入商业保险截止日期！");
	   						 result=false;
	  					 }
		    		 }); 
		    		
		    		$("input[name=commercialInsuranceCoverageMoney]").each(function(){
		    			 var value =$(this).val();
		    			 if(value==""){
	  						 alert("请输入商业保险承保金额！");
	   						 result=false;
	  					 }
		    			 if(isNaN(value)){
		    				 alert("请输入合法数字！");
	   						 result=false;
		    			 }
		    		 }); 
		    		
		    		$("input[name=commercialInsuranceMoney]").each(function(){
		    			 var value =$(this).val();
		    			 if(value==""){
	 						 alert("请输入商业保险金额！");
	   						 result=false;
	 					 }
		    		 }); 
		    		//获取商业保险的条数，方便在action中处理数据
					 var inputRows = 0;
		    		 $('tr.trClass').each(function(){
		    			 if($(this).text() != ""){
		    				 inputRows++;
		    			 }
		    		 }) 
		    		 $("input[name=inputRows]").val(inputRows); 
		    		 return result;
		    		
	    	 });
	 
	 formatDateField1($("#fromDate"));
	 formatDateField1($("#toDate"));
	</script>
</cqu:border>
