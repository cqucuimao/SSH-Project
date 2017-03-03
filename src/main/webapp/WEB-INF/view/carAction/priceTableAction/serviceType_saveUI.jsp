<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>车型信息</h1>
		</div>
		<div class="editBlock detail p30">
			<s:form action="priceTable_%{id == null ? 'addServiceType' : 'editServiceType'}" id="pageForm" enctype="multipart/form-data" method="post">
	        <s:hidden name="id"></s:hidden>
	        <s:textfield type="hidden" name="carServiceSuperTypeId"/>
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
					<tr>
						<th>车类<span class="required">*</span></th>
						<td>
								<s:textfield cssClass="inputText" name="superTitle"/>
						</td>
					</tr>
					<s:iterator value="carServiceTypes">
						<tr>
							<th>车型</th>
							<td>
								<s:textfield cssClass="inputText" name="title" readonly="true"/>
								<s:if test="canDeleteServiceType">
									<s:a action="priceTable_deleteCarServiceType?carServiceTypeId=%{id}&carServiceSuperTypeId=%{carServiceSuperTypeId}" onclick="return confirm('确认要删除吗？'); if(!result) coverHidden(); return result;"><i class="icon-operate-delete" title="删除"></i></s:a>
								</s:if>
							</td>
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
	<script type="text/javascript">
	if($("input[name=actionFlag]").val() == "edit"){
		$("input[name=superTitle]").attr("readonly",true);
	}
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
				},
				invalidHandler: function(form, validator){
					 coverHidden();
				}
			});
		});
	 
	 
	 //点击新增按钮
	 $("#btn").click(function(){
		 var row = $("#tb").find('tr').length - 1;
		 $($('#tb').find('tr')[row]).after('<tr class="trClass"><th></th><td><s:textfield cssClass="inputText" name="typeTitle" />'+
						'&nbsp;&nbsp;&nbsp;<a href="#" class="deleteTr"><i class="icon-operate-delete" title="删除"></i></a></td></tr>');
		 
		//点击删除一条
		$(".deleteTr").click(function(){
		 	$(this).parent('td').parent('tr').empty();
 		}); 
	 })
	 
	 $("input[name=sub]").click(function(){
		 
		 $("input[name=typeTitle]").each(function(){
			 var value =$(this).val();
			 if(value==""){
					 alert("请输入车型！");
					 result=false;
				 }
		 }); 
		 
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
</cqu:border>