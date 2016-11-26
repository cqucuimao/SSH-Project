<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cqu" uri="//WEB-INF/tlds/cqu.tld" %>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link href="skins/main.css" rel="stylesheet" type="text/css" />
<style>
    	
    </style>
</head>
<body class="minW">
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>扩充常备车库申请</h1>
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
			<s:form action="reserveCarApplyOrder_%{id == null ? 'add' : 'edit'}" id="pageForm">
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
				<tbody>
					<!-- 新建时填写的内容 -->
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.proposer')" /><span class="required">*</span></th>
						<td>
								<s:select id="proposerId" name="proposerId" cssClass="SelectStyle"
                        		list="applyUserList" listKey="id" listValue="name"
                        		headerKey="" headerValue="选择申请人"
                        		/>	
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.carCount')" /><span class="required">*</span></th>
						<td>

								<s:textfield cssClass="inputText" id="carCount" name="carCount"/>
						
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.fromDate')" /><span class="required">*</span></th>
						<td>
								<s:textfield class="Wdate half" id="fromDate" name="fromDate" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.toDate')" /><span class="required">*</span></th>
						<td>
								<s:textfield class="Wdate half" id="toDate" name="toDate" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.reason')" /><span class="required">*</span></th>
						<td>
								<s:textarea class="inputText" style="height:100px" id="reason" name="reason"></s:textarea>
						</td>
					</tr>	
					<!-- 公司领导审核的内容 -->
					<tr class="approveTr" style="display:none;">
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.approveUser')" /><span class="required">*</span></th>
						<td>
								<s:select name="approveUserId" cssClass="SelectStyle"
                        		list="approveUserList" listKey="id" listValue="name"
                        		headerKey="" headerValue="选择审核人"
                        		/>	
						</td>
					</tr>
					<tr class="approveTr" style="display:none;">
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.approveMemo')" /></th>
						<td>
								<s:textarea class="inputText" style="height:100px" id="approveMemo" name="approveMemo"></s:textarea>
						</td>
					</tr>
					<tr class="approveTr" style="display:none;">
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.approved')" /></th>
						<td>
							<s:checkbox class="m10" id="approved" name="approved"/>
						</td>
					</tr>
					<tr>
	                <td colspan="2">	    
	                		<input name="actionFlag" type="hidden" value="${actionFlag }">            		 
		                	<input type="submit" id="btn" class="inputButton" value="保存"/>
		                	<input type="button" id="sub" class="inputButton" value="提交"/>
		                	<a class="p15" href="javascript:history.go(-1);">返回</a>
		                
	                </td>
	            </tr>
				</tbody>
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
	
	var actionFlag = $("input[name=actionFlag]").val();
	
	//新建时carCount为0，处理为空
	if(actionFlag == "add"){
		if($("#carCount").val() == 0){
			$("#carCount").val("");
		}
	}
	
	//通过js为提交操作指定action
	if (actionFlag == "add"){				//新建
		$("#sub").click(function(){
			$('#pageForm').attr("action", "reserveCarApplyOrder_submitForNew.action").submit();
		});
	}
	if (actionFlag == "edit"){				//修改
		$("#sub").click(function(){
			$('#pageForm').attr("action", "reserveCarApplyOrder_submitForEdit.action").submit();
		});
	}
	
	//审核
	if(actionFlag == "approve"){
		//其他项设置为只读
    	$("#proposerId").attr("disabled", true); 
    	$("#carCount").attr("disabled",true);
    	$("#fromDate").removeAttr("onfocus"); 
		$("#fromDate").attr("disabled", true); 
		$("#toDate").removeAttr("onfocus"); 
		$("#toDate").attr("disabled", true); 
    	$("#reason").attr("disabled",true);
    	//保存按钮不显示
    	$("#btn").hide();
    	//显示是否审核通过
		$(".approveTr").show();
    	//提交审核
		$("#sub").click(function(){
			$('#pageForm').attr("action", "reserveCarApplyOrder_approve.action").submit();
		});
	}

	// 配置具体的验证规则
	$(function(){    	
		$("#pageForm").validate({
			submitout: function(element) { $(element).valid(); },
			rules:{
				proposerId:{
					required:true,
				},
				fromDate:{
					required:true,
				},
				toDate:{
					required:true,
				},
				reason:{
					required:true,
				},
				carCount:{
					required:true,
					digits:true ,
				},
				approveUserId:{
					required:true,
				},
			}
	  });
			
	formatDateField1($("#fromDate"));
	formatDateField1($("#toDate"));
	});
	
	</script>
</body>
</html>