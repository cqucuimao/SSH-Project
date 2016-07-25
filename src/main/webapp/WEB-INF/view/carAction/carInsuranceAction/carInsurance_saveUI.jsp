<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
	<style>
	.tdInput{background: #FFFFFF;border: 1px solid #bbb;height: 33px; line-height: 33px;font-size: 14px;font-weight:normal;padding: 0 4px 0 6px;vertical-align: middle;width:130px;}
    .tdInput:focus{ background:#f0fff3;border: 1px solid #65bd77;color: #65bd77;}
    .tdInput1{background: #FFFFFF;border: 1px solid #bbb;height: 33px; line-height: 33px;font-size: 14px;font-weight:normal;padding: 0 4px 0 6px;vertical-align: middle;width:132px;}
    .tdDiv{width:100px;text-align:center;float:left;margin-left:20px}
    .tdDiv1{width:150px;text-align:center;float:left;margin-left:35px}
    .tdDiv2{width:120px;text-align:center;float:left;margin-left:33px}
	</style>
</head>
<body class="minW">
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
                        <th><s:property value="tr.getText('car.Car.plateNumber')" /><span class="required">*</span></th>
						<td>
						<s:textfield id="car_platenumber" cssClass="carSelector inputText inputChoose" onfocus="this.blur();" name="car.plateNumber" type="text" />
						
						</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('car.CarInsurance.insureCompany')" /><span class="required">*</span></th>
                        <td>
                        	<input class="inputText" type="text" name="insureCompany"/>
						</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarInsurance.compulsoryPolicyNumber')" /><span class="required">*</span></th>
                        <td>
                        	<input class="inputText" type="text" name="compulsoryPolicyNumber"/>
						</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarInsurance.commercialPolicyNumber')" /><span class="required">*</span></th>
                        <td>
                        	<input class="inputText" type="text" name="commercialPolicyNumber"/>
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
                    	</th>
                    </tr>      
                    <tr>
                    	<th>交强险<span class="required">*</span></th>
                    	<td>
                    		<!-- <input class="tdInput1" type="text" value="交强险" readonly style="border:0;text-align:center"/> -->
                    		<input class="tdInput1" type="text" name="" value="——" readonly style="border:0;text-align:center;"/>
                    		<input class="Wdate half" name="compulsoryBeginDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<input class="Wdate half" name="compulsoryEndDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<input class="tdInput" type="text" name="compulsoryCoverageMoney" />&nbsp;
							<input class="tdInput" type="text" name="compulsoryMoney" onblur="getAllMoney()"/>
						</td>
                    </tr>
                     <tr>
                    	<th>车船税<span class="required">*</span></th>
                    	<td>
                    		<!-- <input class="tdInput1" type="text" value="车船税" readonly style="border:0;text-align:center"/> -->
                    		<input class="tdInput1" type="text" name="" value="——" readonly style="border:0;text-align:center;"/>
                    		<input class="Wdate half" name="vehicleTaxBeginDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<input class="Wdate half" name="vehicleTaxEndDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<input class="tdInput1" type="text" name="" value="——" readonly style="border:0;text-align:center;"/>&nbsp;
							<input class="tdInput" type="text" name="vehicleTaxMoney" onblur="getAllMoney()"/>
						</td>
                    </tr>      
                    <tr class="trClass">
                    	<th>商业保险<span class="required">*</span></th>
                    	<td>
                    		<s:select name="commercialInsuranceType" class="selectClass"
                        		list="commercialInsuranceTypes" listKey="id" listValue="name"
                        		headerKey="" headerValue="请选择类型" style="width:142px;"
                        		/>
                    		<input class="Wdate half" name="commercialInsuranceBeginDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<input class="Wdate half" name="commercialInsuranceEndDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<input class="tdInput" type="text" name="commercialInsuranceCoverageMoney"/>&nbsp;
							<input class="tdInput" type="text" name="commercialInsuranceMoney" onblur="getAllMoney()"/>&nbsp;&nbsp;
							<input class="btn" id="btn" type="button" value="点击新增" />
						</td>
                    </tr>                                    
                    <tr>
                        <th>保险起止时间<span class="required">*</span></th>
                        <td>
							<input class="Wdate half" name="fromDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
							<input class="Wdate half" name="toDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />						
						</td>
                    </tr>
                    <tr><th><s:property value="tr.getText('car.CarInsurance.money')" />(元)<span class="required">*</span></th>
                    	<td>
                        	<input class="inputText" type="text" name="money" readonly/>
						</td>
                    </tr>     
                     <tr><th><s:property value="tr.getText('car.CarInsurance.memo')" /></th>
                    	<td>
                        	<textarea class="inputText" style="height:100px" name="memo"></textarea>
                        </td>
                    </tr>                  
                    <tr>
                    	<th><s:property value="tr.getText('car.CarInsurance.payDate')" /><span class="required">*</span></th>
                    	<td>
                    		<input class="Wdate half" type="text" name="payDate" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
                    	</td>
                    </tr>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                        	<input type="hidden" name="inputRows"/>
                            <input class="inputButton" name="sub" type="submit" value="确定"/>
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
    
	  	 //计算保险金额（总金额）  实现数字相加，Number(a1)+Number(a2);	
		 function getAllMoney(){    		
	   		var compulsoryMoney = $("input[name=compulsoryMoney]").val();
	   		var vehicleTaxMoney = $("input[name=vehicleTaxMoney]").val();
	   		var money = Number(compulsoryMoney) + Number(vehicleTaxMoney);
	   		$("input[name=commercialInsuranceMoney]").each(function(){
	   			 var value =$(this).val();
	   			 money=Number(money)+Number(value);
	   		 }); 
	   		$("input[name=money]").val(money); 
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
						compulsoryCoverageMoney:{
							required:true,
							number:true,
							min:0,
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
	    	
	    	 function checkEndTime(startTime, endTime) {
	 			var start = new Date(startTime.replace("-", "/").replace("-", "/"));
	 			var end = new Date(endTime.replace("-", "/").replace("-", "/"));
	 			if (end < start) {
	 				return false;
	 			}
	 			return true;
	 		}
	    	 
	    	 //点击新增一条商业保险
	    	 $("#btn").click(function(){
	    		 	var row = $("#tab").find('tr').length-5;	//获取table行数，减去5得到增加行的位置
	 		 		var row1 = row+1;	 
					$($('#tab').find('tr')[row]).after('<tr class="trClass"><th>&nbsp;&nbsp;&nbsp;&nbsp;<span class="required"></span></th>'+
						'<td><select name="commercialInsuranceType" id=select'+row1+' style="width:142px"></select>&nbsp;'+							
               			'<input  class="Wdate half" name="commercialInsuranceBeginDate" type="text" onfocus="new WdatePicker({dateFmt:\'yyyy-MM-dd\'})" />&nbsp;'+
						'<input  class="Wdate half" name="commercialInsuranceEndDate" type="text" onfocus="new WdatePicker({dateFmt:\'yyyy-MM-dd\'})" />&nbsp;'+
						'<input  class="tdInput" type="text" name="commercialInsuranceCoverageMoney">&nbsp;&nbsp;'+
						'<input  class="tdInput" type="text" name="commercialInsuranceMoney" onblur="getAllMoney()"/>&nbsp;&nbsp;&nbsp;<a href="#" class="deleteTr"><i class="icon-operate-delete" title="删除"></i></a></td></tr>'); 			
					$(".selectClass option").each(function(){
	    		 		$("#select"+row1).append('<option value='+$(this).val()+'>'+$(this).text()+'</option>');
	    		 	});	    
					//点击删除一条商业保险
					$(".deleteTr").click(function(){
		    		 	$(this).parent('td').parent('tr').empty();
		    		 	getAllMoney();
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
		    		 $("select[name=commercialInsuranceType] option:selected").each(function(){
		    			 var value =$(this).attr("value");
	   					 if(value==""){
	   						 alert("请选择商业保险类型！");
	   						 return false;
	   					 }
		    		 }); 
		    		 
		    		$("input[name=commercialInsuranceBeginDate]").each(function(){
		    			 var value =$(this).val();
		    			 if(value==""){
	   						 alert("请输入商业保险生效日期！");
	   						 return false;
	   					 }
		    		 }); 
		    		
		    		$("input[name=commercialInsuranceEndDate]").each(function(){
		    			 var value =$(this).val();
		    			 if(value==""){
	  						 alert("请输入商业保险截止日期！");
	  						 return false;
	  					 }
		    		 }); 
		    		
		    		$("input[name=commercialInsuranceCoverageMoney]").each(function(){
		    			 var value =$(this).val();
		    			 if(value==""){
	  						 alert("请输入商业保险承保金额！");
	  						 return false;
	  					 }
		    			 if(isNaN(value)){
		    				 alert("请输入合法数字！");
		    				 return false;
		    			 }
		    		 }); 
		    		
		    		$("input[name=commercialInsuranceMoney]").each(function(){
		    			 var value =$(this).val();
		    			 if(value==""){
	 						 alert("请输入商业保险金额！");
	 						 return false;
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
		    		
	    	 });
	    	 
	    	 
	    	
	    	
			
		});
	    
	   
    </script>
</body>
</html>
