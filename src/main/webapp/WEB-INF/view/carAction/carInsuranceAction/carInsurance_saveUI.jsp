<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>登记保险信息</h1>
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
        <div class="editBlock detail p30">
        <s:form action="carInsurance_%{id == null ? 'add' : 'edit'}" id="pageForm">
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
                <tbody id="tab">
                	<tr>
                        <th><s:property value="tr.getText('car.CarInsurance.car')" /><span class="required">*</span></th>
						<td>
							<cqu:carSelector name="car"/>						
						</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('car.CarInsurance.insureCompany')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield class="inputText" type="text" name="insureCompany"/>
						</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarInsurance.compulsoryPolicyNumber')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield class="inputText" type="text" name="compulsoryPolicyNumber"/>
						</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarInsurance.commercialPolicyNumber')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield class="inputText" type="text" name="commercialPolicyNumber"/>
						</td>
                    </tr>
                    <tr>
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
                    	<th>交强险<span class="required">*</span></th>
                    	<td>
                    		<input class="tdInput1" type="text" name="" value="——" readonly style="border:0;text-align:center;"/>
                    		<s:textfield class="Wdate half" name="compulsoryBeginDate" type="text" onblur="getBeginDate()" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<s:textfield class="Wdate half" name="compulsoryEndDate" type="text" onblur="getEndDate()" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<input class="tdInput1" type="text" name="" value="——" readonly style="border:0;text-align:center;"/>&nbsp;
							<s:textfield class="tdInput" type="text" name="compulsoryMoney" onblur="getAllMoney()"/>
							<input class="tdInput1" type="text" name="" value="——" readonly style="border:0;text-align:center;"/>
						</td>
                    </tr>
                     <tr>
                    	<th>车船税<span class="required">*</span></th>
                    	<td>
                    		<input class="tdInput1" type="text" name="" value="——" readonly style="border:0;text-align:center;"/>
                    		<s:textfield class="Wdate half" name="vehicleTaxBeginDate" type="text" onblur="getBeginDate()" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<s:textfield class="Wdate half" name="vehicleTaxEndDate" type="text" onblur="getEndDate()" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<input class="tdInput1" type="text" name="" value="——" readonly style="border:0;text-align:center;"/>&nbsp;
							<s:textfield class="tdInput" type="text" name="vehicleTaxMoney" onblur="getAllMoney()"/>
							<input class="tdInput1" type="text" name="" value="——" readonly style="border:0;text-align:center;"/>
						</td>
                    </tr>      
                    <!-- 新增保险 -->
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
                    <!-- 修改保险信息 -->      
                    <s:iterator value="commercialInsurances">
                    	<tr>
                    	<th>商业保险<span class="required">*</span></th>
                    	<td>
                    		<input type="text" class="tdInput1" readonly name="commercialInsuranceTypeName" value="${commercialInsuranceType.name }">
                    		<s:textfield class="Wdate half" name="commercialInsuranceBeginDate" type="text" onblur="getBeginDate()" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<s:textfield class="Wdate half" name="commercialInsuranceEndDate" type="text" onblur="getEndDate()" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<s:textfield class="tdInput" type="text" name="commercialInsuranceCoverageMoney"/>&nbsp;
							<s:textfield class="tdInput" type="text" name="commercialInsuranceMoney" onblur="getAllMoney()"/>&nbsp;
							<s:textfield class="tdInput" type="text" name="commercialInsuranceMemo"/>
						</td>
                    </tr>  
                    </s:iterator>                            
                    <tr>
                        <th>保险起止时间<span class="required">*</span></th>
                        <td>
							<s:textfield class="Wdate half" name="fromDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
							<s:textfield class="Wdate half" name="toDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />						
						</td>
                    </tr>
                    <tr><th><s:property value="tr.getText('car.CarInsurance.money')" />(元)<span class="required">*</span></th>
                    	<td>
                        	<s:textfield class="inputText" type="text" name="money" readonly="true"/>
						</td>
                    </tr>     
                     <tr><th><s:property value="tr.getText('car.CarInsurance.memo')" /></th>
                    	<td>
                        	<s:textarea class="inputText" style="height:100px" name="memo"></s:textarea>
                        </td>
                    </tr>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                        	<input type="hidden" name="inputRows"/>
                        	<input type="hidden" name="actionFlag" value="${actionFlag }">
                            <input class="inputButton coverOff" name="sub" type="submit" value="确定"/>
                             <a class="p15" href="javascript:history.go(-1);">返回</a>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </s:form>
        
        </div>
    </div>
    <script type="text/javascript">
    
    	//编辑时的权限处理
    	//alert($("#editNormalInfo").val());
    	//alert($("#editKeyInfo").val());
    	if($("#editNormalInfo").val() == "true" && $("#editKeyInfo").val() == "false" ){
    		$("input[name=compulsoryMoney]").attr("readonly",true);
    		$("input[name=vehicleTaxMoney]").attr("readonly",true);
    		$("input[name=commercialInsuranceMoney]").each(function(){
	    		$(this).attr("readonly",true);
	    	});
    	}
    	if($("#editKeyInfo").val() == "true" && $("#editNormalInfo").val() == "false"){
    		$("#carLabel").removeAttr("onclick");
	    	$("#carLabel").attr("readonly", true); 
	    	$("input[name=insureCompany]").attr("readonly",true);
	    	$("input[name=compulsoryPolicyNumber]").attr("readonly",true);
	    	$("input[name=commercialPolicyNumber]").attr("readonly",true);
	    	$("input[name=compulsoryBeginDate]").attr("readonly",true);
	    	$("input[name=compulsoryBeginDate]").removeAttr("onfocus"); 
	    	$("input[name=compulsoryEndDate]").attr("readonly",true);
	    	$("input[name=compulsoryEndDate]").removeAttr("onfocus"); 
	    	$("input[name=vehicleTaxBeginDate]").attr("readonly",true);
	    	$("input[name=vehicleTaxBeginDate]").removeAttr("onfocus"); 
	    	$("input[name=vehicleTaxEndDate]").attr("readonly",true);
	    	$("input[name=vehicleTaxEndDate]").removeAttr("onfocus"); 
	    	$("input[name=fromDate]").attr("readonly",true);
	    	$("input[name=fromDate]").removeAttr("onfocus"); 
	    	$("input[name=toDate]").attr("readonly",true);
	    	$("input[name=toDate]").removeAttr("onfocus"); 
	    	
	    	$("input[name=commercialInsuranceBeginDate]").each(function(){
	    		$(this).attr("readonly",true);
	    		$(this).removeAttr("onfocus");
	    	});
	    	$("input[name=commercialInsuranceEndDate]").each(function(){
	    		$(this).attr("readonly",true);
	    		$(this).removeAttr("onfocus");
	    	});
	    	$("input[name=commercialInsuranceMemo]").each(function(){
	    		$(this).attr("readonly",true);
	    	});
	    	
    	}
        //处理不合法的日期格式
    	formatDateField1($("input[name=compulsoryBeginDate]"));
    	formatDateField1($("input[name=compulsoryEndDate]"));
    	formatDateField1($("input[name=vehicleTaxBeginDate]"));
    	formatDateField1($("input[name=vehicleTaxEndDate]"));
    	$("input[name=commercialInsuranceBeginDate]").each(function(){
    		formatDateField1($(this));
    	});
    	$("input[name=commercialInsuranceEndDate]").each(function(){
    		formatDateField1($(this));
    	});
    	formatDateField1($("input[name=fromDate]"));
    	formatDateField1($("input[name=toDate]"));
    	
    	
    	var actionFlag = $("input[name=actionFlag]").val();
    	if(actionFlag == "edit"){
    		$(".trClass").remove();	/* 
    		$("input[name=commercialInsuranceTypeName]").each(function(){
    			
    		});
    		var commercialInsuranceTypeName = $("input[name=commercialInsuranceTypeName]").val();  */
    		/* 
    		$(".selectClass option").each(function(){
		 		$(".selector").append('<option value='+$(this).val()+'>'+$(this).text()+'</option>');
		 	});	 */
    	}
    
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
	   		var compulsoryMoney = $("input[name=compulsoryMoney]").val();
	   		var vehicleTaxMoney = $("input[name=vehicleTaxMoney]").val();
	   		var money = Number(compulsoryMoney) + Number(vehicleTaxMoney);
	   		$("input[name=commercialInsuranceMoney]").each(function(){
	   			 var value =$(this).val();
	   			 money=Number(money)+Number(value);
	   		 }); 
	   		$("input[name=money]").val(money.toFixed(2)); 
		 }	
    
		 
		 //计算保险开始时间
		 function getBeginDate(){
			 var maxBeginDate = "";
			 var compulsoryBeginDate = $("input[name=compulsoryBeginDate]").val();
			 var vehicleTaxBeginDate = $("input[name=vehicleTaxBeginDate]").val();
			 if(vehicleTaxBeginDate == "" && compulsoryBeginDate != ""){
				 maxBeginDate = compulsoryBeginDate;
			 }
			 if(vehicleTaxBeginDate != "" && compulsoryBeginDate == ""){
				 maxBeginDate = vehicleTaxBeginDate;
			 }
			 if(vehicleTaxBeginDate != "" && compulsoryBeginDate != ""){
				 if(checkEndTime(compulsoryBeginDate,vehicleTaxBeginDate)){
					 maxBeginDate = vehicleTaxBeginDate;
				 }else{
					 maxBeginDate = compulsoryBeginDate;
				 }
			 } 
			 var maxValue = "1970-01-01";
			$("input[name=commercialInsuranceBeginDate]").each(function(){
	   			var value = $(this).val();
	   			
   				if(checkEndTime(maxValue,value)){
   					maxValue = value;
	   			}
	   		 }); 
			if(maxBeginDate == ""){
				$("input[name=fromDate]").val(maxValue);
			}else{
				if(checkEndTime(maxValue,maxBeginDate)){
					$("input[name=fromDate]").val(maxBeginDate);
				}else{
					$("input[name=fromDate]").val(maxValue);
				}
			}
		 }
		 
		//计算保险截止时间
		 function getEndDate(){
			 var minEndDate = "";
			 var compulsoryEndDate = $("input[name=compulsoryEndDate]").val();
			 var vehicleTaxEndDate = $("input[name=vehicleTaxEndDate]").val();
			 if(vehicleTaxEndDate == "" && compulsoryEndDate != ""){
				 minEndDate = compulsoryEndDate;
			 }
			 if(vehicleTaxEndDate != "" && compulsoryEndDate == ""){
				 minEndDate = vehicleTaxEndDate;
			 }
			 if(vehicleTaxEndDate != "" && compulsoryEndDate != ""){
				 if(checkEndTime(compulsoryEndDate,vehicleTaxEndDate)){
					 minEndDate = compulsoryEndDate;
				 }else{
					 minEndDate = vehicleTaxEndDate;
				 }
			 } 
			var minValue = "2050-01-01";
			$("input[name=commercialInsuranceEndDate]").each(function(){
	   			var value = $(this).val();
	   			//alert("value="+value);
	   			if(value == ""){
	   				;
	   			}else{
	   				if(checkEndTime(value,minValue)){
	   					minValue = value;
		   			}
	   			}
   				
	   		 }); 
			if(minEndDate == ""){
				if(minValue == "2050-01-01"){
					$("input[name=toDate]").val("");
				}else{
					$("input[name=toDate]").val(minValue);
				}
				
			}else{
				if(checkEndTime(minValue,minEndDate)){
					$("input[name=toDate]").val(minValue);
				}else{
					$("input[name=toDate]").val(minEndDate);
				}
			}
		 }
		 
	    $(function(){
	    	
	    	 $("#pageForm").validate({
	    		 	onsubmit: function(element) { $(element).valid(); },
					rules:{
						// 配置具体的验证规则
						'car.plateNumber':{
							required:true,
						},
						insureCompany:{
							required:true,
						},
						compulsoryPolicyNumber:{
							required:true,
						},
						commercialPolicyNumber:{
							required:true,
						},
						compulsoryBeginDate:{
							required:true,
						},
						compulsoryEndDate:{
							required:true,
						},
						compulsoryMoney:{
							required:true,
							number:true,
							min:0,
						},
						vehicleTaxBeginDate:{
							required:true,
						},
						vehicleTaxEndDate:{
							required:true,
						},
						vehicleTaxMoney:{
							required:true,
							number:true,
							min:0,
						},
						fromDate:{
							required:true,
						},
						toDate:{
							required:true,
						},
						money:{
							required:true,
							number:true,
							min:0,
						},
						payDate:{
							required:true,
						},
					}
				});
	    	
	    	
	    	 
	    	 //点击新增一条商业保险
	    	 $("#btn").click(function(){
	    		 	var row = $("#tab").find('tr').length-4;	//获取table行数，减去4得到增加行的位置
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
				 
					 if(!checkEndTime($("input[name=compulsoryBeginDate]").val(),$("input[name=compulsoryEndDate]").val())){
		    			 alert("交强险的生效时间晚于了截止时间！");
		    			 return false;
		    		 }	
					 
					 if(!checkEndTime($("input[name=vehicleTaxBeginDate]").val(),$("input[name=vehicleTaxEndDate]").val())){
		    			 alert("车船税的生效时间晚于了截止时间！");
		    			 return false;
		    		 }	
				 
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
	    	 
	    	 
	    	
	    	
			
		});
	    
	   
    </script>
</cqu:border>
