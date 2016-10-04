<%@ page import="org.apache.poi.ss.usermodel.Picture"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

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
			<h1>车型信息</h1>
		</div>
		<div class="editBlock detail p30">
			<s:form action="priceTable_%{id == null ? 'addServiceType' : 'editServiceType'}" id="pageForm" enctype="multipart/form-data" method="post">
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
				<tbody id="tb">
					<s:if test="id==null">
						<tr>
							<th>车类<span class="required">*</span></th>
							<td>
									<s:textfield cssClass="inputText" name="superTypeTitle"/>
							</td>
						</tr>
					</s:if>
					<s:else>
						<tr>
							<th>车类</th>
							<td>
									<s:textfield cssClass="inputText" name="superTypeTitle" readonly="true"/>
							</td>
						</tr>
					</s:else>
					<s:iterator value="carServiceTypes">
						<tr>
							<th>车型</th>
							<td><s:textfield cssClass="inputText" name="title" readonly="true"/></td>
						</tr>
					</s:iterator>
					<tr class="trClass">
						<th>车型<span class="required">*</span></th>
						<td>
							<s:textfield cssClass="inputText" name="typeTitle" />
							&nbsp;&nbsp;<input class="btn" id="btn" type="button" value="点击新增" />
						</td>
					</tr>
				<tfoot>	
					<tr>
		                <td colspan="2">
                        		<input name="actionFlag" type="hidden" value="${actionFlag }">    
		                		<input type="hidden" name="inputRows"/>
			                	<input type="submit" name="sub" class="inputButton" value="确定" />
			                	<a class="p15" href="javascript:history.go(-1);">返回</a>
		                </td>
	            	</tr>
	            </tfoot>
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
	if($("input[name=actionFlag]").val() == "edit"){
		$("input[name=superTypeTitle]").attr("readonly",true);
	}
	$("input[name=date]").attr("readonly",true);
	 $(function(){    	
			$("#pageForm").validate({
				submitout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					superTypeTitle:{
						required:true,
					},
					typeTitle:{
						required:true,
					},
					pricePerKM:{
						required:true,
						number:true,
						min:1
					},
					pricePerDay:{
						required:true,
						number:true,
						min:1				
					},
				}
			});
		});
	 //点击新增按钮
	 $("#btn").click(function(){
		 var row = $("#tb").find('tr').length - 1;
		 $($('#tb').find('tr')[row]).after('<tr class="trClass"><th>车型</th><td><s:textfield cssClass="inputText" name="typeTitle" />'+
						'&nbsp;&nbsp;&nbsp;<a href="#" class="deleteTr"><i class="icon-operate-delete" title="删除"></i></a></td></tr>');
		 
		//点击删除一条
		$(".deleteTr").click(function(){
		 	$(this).parent('td').parent('tr').empty();
 		}); 
	 })
	 
	 $("input[name=sub]").click(function(){
		 
		//获取车型条数，方便在action中处理数据
		 var inputRows = 0;
		 $('tr.trClass').each(function(){
			 if($(this).text() != ""){
				 inputRows++;
			 }
		 }) 
		 $("input[name=inputRows]").val(inputRows); 
	 })
	 
	</script>
</body>
</html>