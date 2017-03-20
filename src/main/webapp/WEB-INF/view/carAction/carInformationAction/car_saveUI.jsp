<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>车辆登记</h1>
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
			<s:form action="car_%{id == null ? 'add' : 'edit'}" id="pageForm">
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
						<th><s:property value="tr.getText('car.Car.plateNumber')" /><span class="required">*</span></th>
						<td>
								<s:textfield cssClass="inputText" name="plateNumber"/>
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.CarServiceType.title')" /><span class="required">*</span></th>
						<td>
								<s:select name="carServiceTypeId" cssClass="SelectStyle"
                        		list="carServiceTypeList" listKey="id" listValue="title"
                        		headerKey="" headerValue="选择服务类型"
                        		/>
						
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.Car.brand')" /><span class="required">*</span></th>
						<td>

								<s:textfield cssClass="inputText" name="brand"/>
						
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.Car.model')" /><span class="required">*</span></th>
						<td>
								<s:textfield cssClass="inputText" name="model"/>
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.Car.VIN')" /><span class="required">*</span></th>
						<td>

								<s:textfield cssClass="inputText" name="VIN"/>
							
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.Car.EngineSN')" /><span class="required">*</span></th>
						<td>
								<s:textfield cssClass="inputText" name="EngineSN"/>
						
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.Car.tollChargeSN')" /></th>
						<td>
								<s:textfield cssClass="inputText" name="tollChargeSN"/>
						
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.Car.plateType')" /><span class="required">*</span></th>
						<td>
								<s:radio list="#{'0':'蓝牌','1':'黄牌'}" name="plateTypeId" value="plateType.id" class="{required:true}"/>
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.Car.seatNumber')" /><span class="required">*</span></th>
						<td>
								<s:textfield cssClass="inputText" id="seatNumber" name="seatNumber"/>
						
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.Car.transmissionType')" /><span class="required">*</span></th>
						<td>
								<s:radio list="#{'0':'自动','1':'手动'}" name="transmissionTypeId" value="transmissionType.id" class="{required:true}"/>
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.Car.registDate')" /><span class="required">*</span></th>
						<td >
								<s:textfield class="Wdate half" id="registDate" name="registDate" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
						</td>
					</tr>	
					<tr>
						<th><s:property value="tr.getText('car.Car.enrollDate')" /><span class="required">*</span></th>
						<td >
								<s:textfield class="Wdate half" name="enrollDate" id="enrollDateId" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
						</td>
					</tr>				
					<tr>
						<th><s:property value="tr.getText('car.Car.driver')" /></th>
						<td>
							<cqu:userAutocompleteSelector name="driver" driverOnly="true"/>				
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.ServicePoint.name')" /><span class="required">*</span></th>
						<td>
								<s:select name="servicePointId" cssClass="SelectStyle"
                        		list="servicePointList" listKey="id" listValue="name"
                        		headerKey="" headerValue="选择驻车点"
                        		/>
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.Car.standbyCar')" /></th>
						<td>
							<s:checkbox class="m10" id="standbyCar" name="standbyCar"/>
						</td>
					</tr>
					<tr>
	                <td colspan="2">	    
	                		<input name="actionFlag" type="hidden" value="${actionFlag }">            		 
		                	<input type="submit" id="btn" class="inputButton coverOff" value="确定"/>
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
					plateNumber:{
						required:true,
					},
					carServiceTypeId:{
						required:true,
					},
					brand:{
						required:true,
					},
					VIN:{
						required:true,
					},
					EngineSN:{
						required:true,
					},
					plateType:{
						required:true,
					},
					enrollDate:{
						required:true,
					},
					model:{
						required:true,
					},
					servicePointId:{
						required:true,
					},
					plateTypeId:{
						required:true,
					},
					transmissionTypeId:{
						required:true,
					},
					seatNumber:{
						required:true,
						digits:true  ,
					},
				}
			});
			
			$("#btn").click(function(){
				if(!$("#standbyCar").is(":checked") && $("#driverId").val()==""){
					alert("非备用车的情况下必须指定司机！");
					return false;
				}
		 	}); 
			
			//登记车辆的时候获取当前日期
			var actionFlag = $("input[name=actionFlag]").val();
			if(actionFlag == "register"){
				today = new Date();  
				centry="";
				if  (today.getFullYear()<2000 )  
				    centry = "19" ; 
				date = centry + (today.getFullYear())+ "-" + 
				    		(today.getMonth() + 1 ) + "-" + 
				    		today.getDate(); 
				$("#enrollDateId").val(date);
				if($("#seatNumber").val() == 0){
					$("#seatNumber").val("");
				}
			}
			
			formatDateField1($("#registDate"));
			formatDateField1($("#enrollDateId"));
		});
	</script>
</cqu:border>