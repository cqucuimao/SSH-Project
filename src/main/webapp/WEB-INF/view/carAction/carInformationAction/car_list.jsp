<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>基本信息</h1>
		</div>
		<div class="editBlock search">
			<s:form action="car_queryList">
			<table>
				<tr>
					<td>
						<s:a cssClass="buttonA" action="car_addUI.action?actionFlag=register">车辆登记</s:a>
					</td>
					<td>
						<s:a cssClass="buttonA" action="car_borrowedUI">外调车登记</s:a>
					</td>
					<th><s:property value="tr.getText('car.Car.plateNumber')" /></th>
					<td>
						<%-- <cqu:carSelector name="car"/> --%>
						<cqu:carPlateNumber name="car"/>
					</td>
					<th><s:property value="tr.getText('car.CarServiceType.title')" /></th>
					<td><s:select name="serviceType.title" cssClass="SelectStyle"
                        		list="carServiceTypeList" listKey="title" listValue="title"
                        		headerKey="" headerValue="选择服务类型"
                        		/>
                    </td>
					<th><s:property value="tr.getText('car.Car.driver')" /></th>
					<td>
						<cqu:driverName name="selectedDriver"/>
						<%-- <cqu:userSelector name="driver" driverOnly="true"/> --%>
					<td>
						<input class="inputButton" type="submit" value="查询" name="submit" />
					</td>
					<td>
						<s:a cssClass="buttonA" action="car_queryUI">详细查询</s:a>
					</td>
					<td>
					<cqu:diskFileSelector name="uploadedDiskFiles" uploadLimit="false"/>
					</td>
				</tr>
			</table>
			</s:form>
		</div>
		<div class="dataGrid">
			<div class="tableWrap">
				<table>
					
					<thead>
						<tr>
							
							<th><s:property value="tr.getText('car.Car.plateNumber')" /></th>
							<th><s:property value="tr.getText('car.CarServiceType.title')" /></th>
                			<th><s:property value="tr.getText('car.Car.model')" /></th>
                			<th><s:property value="tr.getText('car.Car.transmissionType')" /></th>
                			<th><s:property value="tr.getText('car.Car.registDate')" /></th>
                			<th><s:property value="tr.getText('car.Car.driver')" /></th>
                			<th><s:property value="tr.getText('car.ServicePoint.name')" /></th>
			                <th>相关操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>
							<%-- <s:a action="car_carDetail?id=%{id}">${plateNumber }</s:a>  --%>
							   <cqu:carDetailList id="${id}" /> 
							</td>
							<td>${serviceType.title }</td>
							<td>${model }</td>
							<td>${transmissionType.label }</td>
							<td><s:date name="registDate" format="yyyy-MM-dd"/></td>
							<td>${driver.name }</td>
                			<td>${servicePoint.name }</td>							
							<td>							
			                    <s:a action="car_editUI?id=%{id}&actionFlag=edit"><i class="icon-operate-edit" title="修改"></i></s:a>
			                    <s:a action="car_detail?id=%{id}"><i class="icon-operate-detail" title="GPS定位器"></i></s:a>	 
			                    <s:if test="canDeleteCar">
			                	<s:a action="car_delete?id=%{id}" onclick="result=confirm('确认要删除吗？'); if(!result) coverHidden(); return result;"><i class="icon-operate-delete" title="删除"></i></s:a>
			                	</s:if>
			                	<s:else></s:else>            
			                </td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="car_freshList">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %> 
			</s:form>
		</div>
	</div>	
	<script>
	</script>
</cqu:border>
