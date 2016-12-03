<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>车型信息</h1>
		</div>
		<div class="editBlock detail p30">
			<s:form action="" name="pageForm" id="pageForm">
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
						<th>车类<span class="required">*</span></th>
						<td>
								<%-- <s:textfield cssClass="inputText" name="superTitle"/> --%>
								<s:select id="carServiceSuperTypeId" name="carServiceSuperTypeId"  cssClass="SelectStyle"
                        		list="superTypeList" listKey="id" listValue="superTitle"
                        		headerKey="" headerValue="选择车类" onchange="getCarServiceTypeOption()"
                        		/>	
						</td>
					</tr>
					<tr>
						<th>车型<span class="required">*</span></th>
						<td class="editSelect">
							<s:select name="carServiceTypeId" id="carServiceTypeId" cssClass="SelectStyle"
                        		list="typeList" listKey="id" listValue="title"
                        		headerKey="" headerValue="选择车型"
                        		/>	
						</td><%-- 
						<td class="addSelect">
							<s:select id="typeTitleOption" name="typeTitleOption" cssClass="SelectStyle" 
							list="typeList" headerValue="选择车型"/>
							
						</td> --%>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.Price.perDay')" /><span class="required">*</span></th>
						<td>
							<s:textfield cssClass="inputText" name="perDay"/>
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.Price.perHalfDay')" /><span class="required">*</span></th>
						<td>
							<s:textfield cssClass="inputText" name="perHalfDay"/>
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.Price.perMileAfterLimit')" /><span class="required">*</span></th>
						<td>
							<s:textfield cssClass="inputText" name="perMileAfterLimit"/>
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.Price.perHourAfterLimit')" /><span class="required">*</span></th>
						<td>
							<s:textfield cssClass="inputText" name="perHourAfterLimit"/>
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.Price.perPlaneTime')" /><span class="required">*</span></th>
						<td>
							<s:textfield cssClass="inputText" name="perPlaneTime"/>
						</td>
					</tr>
				</tbody>
				<tfoot>	
					<tr>
		                <td colspan="2">
		                		<input name="priceTableId" type="hidden" value="${priceTableId }"> 
                        		<input name="actionFlag" type="hidden" value="${actionFlag }">    
			                	<input type="button" id="sub" class="inputButton" value="确定" />
			                	<a class="p15" href="javascript:history.go(-1);">返回</a>
		                </td>
	            	</tr>
	            </tfoot>
				</tbody>
			</table>
			</s:form>
		</div>
		
	</div>
	<script type="text/javascript">
	 
	var actionFlag = $("input[name=actionFlag]").val();
	var priceTableId = $("input[name=priceTableId]").val();
	var carServiceTypeId = $("#carServiceTypeId").val();
	
	//通过js为提交操作指定action
	if (actionFlag == "add"){				//新建
		
		$("#sub").click(function(){
			$('#pageForm').attr("action", "priceTable_add.action").submit();
		});
	}
	if (actionFlag == "edit"){				//修改
		$("#sub").click(function(){
			$('#pageForm').attr("action", "priceTable_edit.action?carServiceTypeId="+carServiceTypeId).submit();
		});
	}
	
	//修改时，车类和车型置为只读
	if(actionFlag == "edit"){
			$("#carServiceSuperTypeId").attr("disabled",true);
			$("#carServiceTypeId").attr("disabled",true);
	}
	 
	//选中车类时，获取不同的车型列表
	function getCarServiceTypeOption(){
		var carServiceSuperTypeId = $("select[name=carServiceSuperTypeId]").val();
		$("#carServiceTypeId").empty();
		$.get("priceTable_getCarServiceTypeOption.action?carServiceSuperTypeId="+carServiceSuperTypeId,function(data){
			var size = data.lists.length;
			for(var i=0;i<size;i++){
				var array = data.lists[i].split(",");
				$("#carServiceTypeId").append('<option value='+array[0]+'>'+array[1]+'</option>');
			}
		})
	}
	
	
	 $(function(){    	
			$("#pageForm").validate({
				submitout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					carServiceSuperTypeId:{
						required:true,
					},
					carServiceTypeId:{
						required:true,
					},
					perDay:{
						required:true,
						number:true,
					},
					perHalfDay:{
						required:true,
						number:true,
					},
					perMileAfterLimit:{
						required:true,
						number:true,
					},
					perHourAfterLimit:{
						required:true,
						number:true,
					},
					perPlaneTime:{
						required:true,
						number:true,
					},
				}
			});
		});
		 
	</script>
</cqu:border>