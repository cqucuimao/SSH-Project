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
			<s:form action="carServiceType_%{id == null ? 'add' : 'edit'}" id="pageForm" enctype="multipart/form-data" method="post">
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
					<tr>
						<th><s:property value="tr.getText('car.CarServiceType.title')" /><span class="required">*</span></th>
						<td>
								<s:textfield cssClass="inputText" name="title" />
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.CarServiceType.pricePerKM')" />（元/KM）<span class="required">*</span></th>
						<td>
								<s:textfield cssClass="inputText" name="pricePerKM"/>
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.CarServiceType.pricePerDay')" />（元/天）<span class="required">*</span></th>
						<td>
								<s:textfield cssClass="inputText" name="pricePerDay" />
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.CarServiceType.personLimit')" /></th>
						<td>
								<s:textfield cssClass="inputText" name="personLimit"/>（人）
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.CarServiceType.priceDescription')" /></th>
						<td>
								<s:textarea style="height:100px" cssClass="inputText" name="priceDescription"></s:textarea>
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.CarServiceType.picture')" /></th>
						<td>
								<s:fielderror/>							
									<s:textfield name="fileTitle" type="hidden"/>
									<s:hidden name="orderId" value="1"></s:hidden>
									<s:file name="upload" label="上传图片"/>
									
							
						</td>
					</tr>
					<tr id="showInEditImage" style="display:none">
						<th></th>
						<td>
								<img id="imgId" width="362" height="200" src=carServiceType_getImage.action?imageId=<s:property value="imageId" /> />		
						</td>
					</tr>
					<tr>
	                <td colspan="2">
		                	<input type="submit" class="inputButton" value="确定" />
		                	<a class="p15" href="javascript:history.go(-1);">返回</a>
	                </td>
	            </tr>
				</tbody>
			</table>
			</s:form>
			<s:hidden id="flagId" name="flag"/>
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
			$("#pageForm").validate({
				onfocusout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					title:{
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
	 
	 $(function(){
		 
		var obj= $("#flagId");
		if(obj.val()=="edit"){
			$("#showInEditImage").show();
		}
	 });
	 
	</script>
</body>
</html>