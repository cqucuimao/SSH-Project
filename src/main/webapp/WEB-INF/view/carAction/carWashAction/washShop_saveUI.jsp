<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>洗车点信息</h1>
		</div>
		<div class="editBlock detail p30">
		<s:form action="carWashShop_%{id == null ? 'add' : 'edit'}" id="pageForm">
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
						<th>洗车点名称<span class="required">*</span></th>
						<td>
								<s:textfield cssClass="inputText" name = "name" />
						</td>
					</tr>
					<tr>
	                <td colspan="2">	             
		                	<input type="submit" class="inputButton coverOff" value="确定" />
		                	<a class="p15" href="javascript:history.go(-1);">返回</a>
	                </td>
	            	</tr>
				</tbody>
			</table>
			</s:form>
		</div>
		
	</div>
	<script type="text/javascript">
	 $(function(){    	
			$("#pageForm").validate({
				submitout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					name:{
						required:true,
					},				
				}
			});
		});
	</script>
</cqu:border>