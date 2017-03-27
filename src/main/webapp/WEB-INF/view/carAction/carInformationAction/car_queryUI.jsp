<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>详细查询</h1>
		</div>
		<div class="editBlock detail p30">
			<s:form action="car_moreQuery" id="pageForm">
			<table style="width:90%"> 
				<tbody>
				<tr>
					<th><s:property value="tr.getText('car.Car.plateNumber')" /></th>
					<td><s:textfield id="platenumber" name="plateNumber" class="inputText inputChoose" type="text" /></td>
					<th><s:property value="tr.getText('car.Car.brand')" /></th>
					<td><s:textfield class="inputText" name="brand" /></td>
				</tr>
				<tr>
					<th><s:property value="tr.getText('car.Car.model')" /></th>
					<td><s:textfield class="inputText" name="model" /></td>
					<th><s:property value="tr.getText('car.Car.VIN')" /></th>
					<td><s:textfield class="inputText" name="VIN" /></td>
				</tr>
				<tr>
					<th><s:property value="tr.getText('car.Car.EngineSN')" /></th>
					<td><s:textfield class="inputText" name="EngineSN" /></td>
					<th><s:property value="tr.getText('car.CarServiceType.title')" /></th>
					<td><s:select name="serviceType.title" cssClass="SelectStyle"
                        		list="carServiceTypeList" listKey="title" listValue="title"
                        		headerKey="" headerValue="选择服务类型"
                        		/>
                     </td>
				</tr>
				<tr>
					<th><s:property value="tr.getText('car.Car.driver')" /></th>
					<td>
						<cqu:userAutocompleteSelector name="driver"/>
					</td>
					<th><s:property value="tr.getText('car.ServicePoint.name')" /></th>
					<td><s:select name="servicePoint.name" cssClass="SelectStyle"
                        		list="servicePointList" listKey="name" listValue="name"
                        		headerKey="" headerValue="选择驻车点"
                        		/>
                    </td>
				</tr>
				<tr>
					<th><s:property value="tr.getText('car.Car.device')" /></th>
					<td><s:textfield class="inputText" name="device.SN" /></td>
					<th><s:property value="tr.getText('car.Car.tollChargeSN')" /></th>
					<td><s:textfield class="inputText" name="tollChargeSN" /></td>
				</tr>
				<tr>
					<th><s:property value="tr.getText('car.Car.status')" /></th>
					<td><s:select name="statusLabel" list="#{0:'正常',1:'报废'}" cssClass="SelectStyle"  listKey="value" listValue="value"  headerKey="" headerValue="选择车辆状态" /></td>
					<th><s:property value="tr.getText('car.Car.plateType')" /></th>
					<td><s:select name="plateTypeLabel" list="#{0:'蓝牌',1:'黄牌'}" cssClass="SelectStyle"  listKey="value" listValue="value"  headerKey="" headerValue="选择车牌类型" /></td>
				</tr>
				<tr>
					<th><s:property value="tr.getText('car.Car.registDate')" /></th>
					<td>
						<s:textfield name="registDate1" id="registDate1" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
						<s:textfield name="registDate2" id="registDate2" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<th><s:property value="tr.getText('car.Car.enrollDate')" /></th>
					<td>
						<s:textfield name="enrollDate1" id="enrollDate1" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
						<s:textfield name="enrollDate2" id="enrollDate2" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
				</tr>
				<tr>
					<th><s:property value="tr.getText('car.Car.mileage')" /></th>
					<td><s:textfield id="mileage1" class="inputText half" name="mileage1" />&nbsp;<s:textfield id="mileage2" class="inputText half" name="mileage2" /></td>
					<th><s:property value="tr.getText('car.Car.seatNumber')" /></th>
					<td><s:textfield id="seatNumber1" class="inputText half" name="seatNumber1" />&nbsp;<s:textfield id="seatNumber2" class="inputText half" name="seatNumber2" /></td>
				</tr>
				<tr>
					<th><s:property value="tr.getText('car.Car.transmissionType')" /></th>
					<td><s:select name="transmissionTypeLabel" list="#{0:'自动',1:'手动',2:'不确定'}" cssClass="SelectStyle"  listKey="value" listValue="value"  headerKey="" headerValue="选择变速箱类型" /></td>	
					<th><s:property value="tr.getText('car.Car.careExpired')" /></th>
					<td><s:select name="careExpiredLabel" list="#{0:'否',1:'是'}" cssClass="SelectStyle"  listKey="value" listValue="value"  headerKey="" headerValue="" /></td>
				</tr>
				<tr>
					<th><s:property value="tr.getText('car.Car.insuranceExpired')" /></th>
					<td><s:select name="insuranceExpiredLabel" list="#{0:'否',1:'是'}" cssClass="SelectStyle"  listKey="value" listValue="value"  headerKey="" headerValue="" /></td>
					<th><s:property value="tr.getText('car.Car.examineExpired')" /></th>
					<td><s:select name="examineExpiredLabel" list="#{0:'否',1:'是'}" cssClass="SelectStyle"  listKey="value" listValue="value"  headerKey="" headerValue="" /></td>
				</tr>
				<tr>
					<th><s:property value="tr.getText('car.Car.tollChargeExpired')" /></th>
					<td><s:select name="tollChargeExpiredLabel" list="#{0:'否',1:'是'}" cssClass="SelectStyle"  listKey="value" listValue="value"  headerKey="" headerValue="" /></td>
					<th><s:property value="tr.getText('car.Car.standbyCar')" /></th>
					<td><s:select name="standbyCarLabel" list="#{0:'否',1:'是'}" cssClass="SelectStyle"  listKey="value" listValue="value"  headerKey="" headerValue="" /></td>
				</tr>
				<tr>
					<th><s:property value="tr.getText('car.Car.borrowed')" /></th>
					<td><s:select name="borrowedLabel" list="#{0:'否',1:'是'}" cssClass="SelectStyle"  listKey="value" listValue="value"  headerKey="" headerValue="" /></td>
					<th><s:property value="tr.getText('car.Car.memo')" /></th>
					<td><s:textfield class="inputText" name="memo" /></td>
				</tr>
				</tbody>
				<tfoot>
					<tr>
						<td>
							<input type="submit" id="btn" class="inputButton coverOff" value="确定"/>
		                	<a class="p15" href="javascript:history.go(-1);">返回</a>
	                	</td>
		            </tr>
				</tfoot>
			</table>
			</s:form>
		</div>
		
	</div>
	<script type="text/javascript">
		
		if($("#mileage1").val() == 0){
			$("#mileage1").val("");
		}
		if($("#mileage2").val() == 0){
			$("#mileage2").val("");
		}
		if($("#seatNumber1").val() == 0){
			$("#seatNumber1").val("");
		}
		if($("#seatNumber2").val() == 0){
			$("#seatNumber2").val("");
		}
		formatDateField2($("#registDate1"));
		formatDateField2($("#registDate2"));
		formatDateField2($("#enrollDate1"));
		formatDateField2($("#enrollDate2"));
	</script>
</cqu:border>