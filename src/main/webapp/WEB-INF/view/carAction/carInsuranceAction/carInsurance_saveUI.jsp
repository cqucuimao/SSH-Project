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
    .tdInput1{background: #FFFFFF;border: 1px solid #bbb;height: 33px; line-height: 33px;font-size: 14px;font-weight:normal;padding: 0 4px 0 6px;vertical-align: middle;width:130px;}
    
	</style>
</head>
<body class="minW">
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>保险信息</h1>
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
                        <th><s:property value="tr.getText('car.CarInsurance.policyNumber')" /><span class="required">*</span></th>
                        <td>
                        	<input class="inputText" type="text" name="policyNumber"/>
						</td>
                    </tr>
                    <tr>
                    	<th></th>
                    	<th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    			类型&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    			生效日期&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    		    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    		    截止日期&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    		    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    		    承保金额&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    		    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;金额（元）
                    	</th>
                    </tr>
                    <tr>
                    	<th>交强险<span class="required">*</span></th>
                    	<td>
                    		<input class="tdInput1" type="text" value="交强险" readonly style="border:0;text-align:center"/>
                    		<input class="Wdate half" name="compulsoryBeginDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<input class="Wdate half" name="compulsoryEndDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<input class="tdInput" type="text" name="compulsoryCoverageMoney"/>&nbsp;
							<input class="tdInput" type="text" name="compulsoryMoney"/>
						</td>
                    </tr>
                     <tr>
                    	<th>车船税<span class="required">*</span></th>
                    	<td>
                    		<input class="tdInput1" type="text" value="车船税" readonly style="border:0;text-align:center"/>
                    		<input class="Wdate half" name="vehicleTaxBeginDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<input class="Wdate half" name="vehicleTaxEndDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<input class="tdInput1" type="text" readonly value="——" style="border:1px solid #ffffff;text-align:center"/>&nbsp;
							<input class="tdInput" type="text" name="vehicleTaxMoney"/>
						</td>
                    </tr>                   
                    <tr>
                    	<th>商业保险<span class="required">*</span></th>
                    	<td>
                    		<s:select name="commercialInsuranceTypeId" class="selectClass"
                        		list="commercialInsuranceTypeList" listKey="id" listValue="name"
                        		headerKey="" headerValue="请选择类型" style="width:140px;"
                        		/>
                    		<input class="Wdate half" name="commercialInsuranceBeginDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<input class="Wdate half" name="commercialInsuranceEndDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<input class="tdInput" type="text" name="commercialInsuranceCoverageMoney"/>&nbsp;
							<input class="tdInput" type="text" name="commercialInsuranceMoney"/>&nbsp;&nbsp;
							<input class="" id="btn" type="button" value="新增" />
						</td>
                    </tr>              
                    <tr>
                        <th>保险起止时间<span class="required">*</span></th>
                        <td>
							<input class="Wdate half" name="fromDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<input class="Wdate half" name="toDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<s:fielderror style="color:red"></s:fielderror>
						</td>
                    </tr>
                    <tr><th><s:property value="tr.getText('car.CarInsurance.money')" />(元)<span class="required">*</span></th>
                    	<td>
                        	<input class="inputText" type="text" name="money"/>
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
                            <input class="inputButton" name="sub" type="submit" value="提交"/>
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
	    $(function(){
	    		
	    	 $("#btn").click(function(){
	    		 	var row = $("#tab").find('tr').length-5;	//获取table行数，减去5得到增加行的位置
	    		 	var row1 = row+1;
					$($('#tab').find('tr')[row]).after('<tr id='+row1+'><th>&nbsp;&nbsp;&nbsp;&nbsp;<span class="required"></span></th>'+
						'<td><select id=select'+row1+' style="width:140px"></select>&nbsp;'+							
                		'<input class="Wdate half" name="commercialInsuranceBeginDate" type="text" onfocus="new WdatePicker({dateFmt:\'yyyy-MM-dd\'})" />&nbsp;'+
						'<input class="Wdate half" name="commercialInsuranceEndDate" type="text" onfocus="new WdatePicker({dateFmt:\'yyyy-MM-dd\'})" />&nbsp;'+
						'<input class="tdInput" type="text" name="commercialInsuranceCoverageMoney"/>&nbsp;&nbsp;'+
						'<input class="tdInput" type="text" name="commercialInsuranceMoney"/>&nbsp;&nbsp;&nbsp;<input type="button" name="deleteTr" value="删除" /></td></tr>'); 
	    		 	$(".selectClass option").each(function(){
	    		 		$("#select"+row1).append('<option value='+$(this).val()+'>'+$(this).text()+'</option>');
	    		 	});
	    		 	$("input[name=deleteTr]").click(function(){
	    		 		$("#"+row1).remove();
	    		 	});
	    		 	
			 })
			
			 $("input[name=sub]").click(function(){
	    		 var ss = $("input[name=commercialInsuranceBeginDate]").val();
	    		 alert(ss);
	    	 });
	    	 
			 
			$("#pageForm").validate({
				onfocusout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					'car.plateNumber':{
						required:true,
					},
					insureCompany:{
						required:true,
					},
					policyNumber:{
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
					},
					compulsoryMoney:{
						required:true,
					},
					fromDate:{
						required:true,
					},
					toDate:{
						required:true,
					},
					money:{
						required:true,
						digits:true,
						min:1
					},
					insureType:{
						required:true,
					},
					payDate:{
						required:true,
					},
				}
			});
			
			
		});
	    
	   
    </script>
</body>
</html>
