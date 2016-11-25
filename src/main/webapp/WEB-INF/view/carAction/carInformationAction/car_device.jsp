<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>车载设备信息</h1>
		</div>
		<div class="editBlock detail p30">
			<s:form action="car_editDevice" id="pageForm">
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
						<th width="12%"><s:property value="tr.getText('car.Car.plateNumber')" />：</th>
						<td>${plateNumber}</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.CarServiceType.title')" />：</th>
						<td>${serviceType.title }</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.Car.brand')" />：</th>
						<td>${brand }</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.Car.model')" />：</th>
						<td>${model }</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.Car.VIN')" />：</th>
						<td>${VIN }</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.Car.EngineSN')" />：</th>
						<td>${EngineSN }</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.Car.registDate')" />：</th>
						<td><s:date name="registDate" format="yyyy-MM-dd"/></td>
					</tr>				
					<tr>
						<th><s:property value="tr.getText('car.Car.driver')" />：</th>
						<td>${driver.name }</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('car.ServicePoint.name')" />：</th>
						<td>${servicePoint.name }</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('monitor.Device.SN')" /><span class="required">*</span></th>
						<td>
						<s:textfield cssClass="inputText" name="device.SN" />
						
						</td>
						
					</tr>
					<tr>
	                <td colspan="2">	                		 
		                	<input type="submit" class="inputButton coverOff" value="确定"/>
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
					'device.SN':{
						required:true,
					}
				}
			});
		});
	</script>
</cqu:border>