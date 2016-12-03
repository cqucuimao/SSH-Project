<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>车辆保险</h1>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="carInsurance_queryList">
			<table>
				<tr>
					<td>
						<s:a cssClass="buttonA" action="carInsurance_addUI">保险登记</s:a>
					</td>
					<th><s:property value="tr.getText('car.CarInsurance.car')" /></th>
					<td>
						<cqu:carSelector name="car"/>
					</td>
					<th>从</th>
					<td>
						<s:textfield name="date1" id="date1" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<th>到</th>
					<td>
						<s:textfield name="date2" id="date2" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<td>
						<input class="inputButton" type="submit" value="查询"/>
					</td>
					<td>
						<s:a cssClass="buttonA" action="carInsurance_remind">保险到期提醒</s:a>
					</td>
					<td>
						<s:a cssClass="buttonA" action="commercialInsuranceType_list">商业保险类型管理</s:a>
					</td>
					<td>
					   	<s:if test="carId!=null">
							<a class="p15" href="javascript:history.go(-1);">返回</a>
						</s:if>
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
                			<th><s:property value="tr.getText('car.CarInsurance.compulsoryPolicyNumber')" /></th>
                			<th><s:property value="tr.getText('car.CarInsurance.commercialPolicyNumber')" /></th>
              				<th><s:property value="tr.getText('car.CarInsurance.insureCompany')" /></th>
                			<th>起止时间</th>
                			<th><s:property value="tr.getText('car.CarInsurance.money')" />(元)</th>
                			<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td><s:a action="carInsurance_detail?id=%{id}">${car.plateNumber}</s:a></td>
                			<td>${compulsoryPolicyNumber}</td>
                			<td>${commercialPolicyNumber}</td>
							<td>${insureCompany}</td>
							<td style="text-align:right"><s:date name="fromDate" format="yyyy-MM-dd"/>&nbsp;&nbsp;-&nbsp;&nbsp;<s:date name="toDate" format="yyyy-MM-dd"/></td>
							<td style="text-align:right"><fmt:formatNumber value="${money}" pattern="#0.00"/></td>
							<td>
									<s:a action="carInsurance_editUI?id=%{id}&actionFlag=edit"><i class="icon-operate-edit" title="修改"></i></s:a>
									<s:a action="carInsurance_addCommercialInsuranceUI?id=%{id}"><i class="icon-operate-password" title="添加商业保险"></i></s:a>
							</td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="carInsurance_freshList">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			formatDateField2($("#date1"));
			formatDateField2($("#date2"));
	    })
	</script>
</cqu:border>
