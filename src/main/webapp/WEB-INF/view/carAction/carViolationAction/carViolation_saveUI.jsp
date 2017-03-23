<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>登记违章信息</h1>
		</div>
		<div class="editBlock detail p30">
			<s:form action="carViolation_%{id == null ? 'save' : 'edit'}" id="pageForm">
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
						<th><s:property value="tr.getText('car.CarViolation.car')" /><span class="required">*</span></th>
						<td>
						 	<cqu:carAutocompleteSelector name="car" synchDriver="driver"/>
						</td>
					</tr>
					<tr>
                        <th><s:property value="tr.getText('car.CarViolation.driver')" /></th>
                        <td>
                        	<cqu:userAutocompleteSelector name="driver"/>
						</td>
					<td>
                    </tr>
					<tr>
						<th><s:property value="tr.getText('car.CarViolation.date')" /><span class="required">*</span></th>
						<td>
							<s:textfield cssClass="inputText" type="text" class="Wdate half" name="date" id="date"  onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  />
						
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.CarViolation.place')" /><span class="required">*</span></th>
						<td>
								<s:textfield cssClass="inputText" name="place" id="place" />
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.CarViolation.description')" /><span class="required">*</span></th>
						<td>
								<s:textfield cssClass="inputText" name="description" id="description" />
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.CarViolation.penaltyPoint')" /></th>
						<td>
								<s:textfield cssClass="inputText" name="penaltyPoint" id="penaltyPoint" />
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.CarViolation.penaltyMoney')" />（元）</th>
						<td>
								<s:textfield cssClass="inputText"  name="penaltyMoney" id="penaltyMoney" />
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.CarViolation.dealt')" /></th>
						<td>
							<s:checkbox class="m10" id="dealt" name="dealt"/>
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.CarViolation.dealtDate')" /></th>
						<td>
								<s:textfield class="Wdate half" name="dealtDate" id="dealtDate" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"  />
						</td>
					</tr>
					
					<tr>
	                <td colspan="2">
		                	<input class="inputButton coverOff" type="submit" id="submit" value="提交" />
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
					carLabel:{
						required:true,
					},
					date:{
						required:true,
					},
					place:{
						required:true,				
					},
					description:{
						required:true,
					},
					penaltyPoint:{
						digits:true,
						 min:1
					},
					penaltyMoney:{
						number:true,
						min:1
					},
				}
			});
			   formatDateField3($("#date"));
			   formatDateField1($("#dealtDate"));
			  if($("#penaltyPoint").val()==0)
				   {
				   $("#penaltyPoint").val("");
				   }
			   $("#submit").click(function (){
				   if(!($("#dealt").is(':checked'))&& $("#dealtDate").val().length!=0)
				   {  
					  alert("请先填写已经处理");
					  return false;
				   }
				   
				   if($("#dealt").is(':checked') && $("#dealtDate").val().length==0)
				   {  
					  alert("提交前请填写处理日期");
					  return false;
				   }
				   
				});
			   
			   });
	  
	</script>
</cqu:border>