<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>车辆保险提醒信息列表</h1>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="carInsurance_remind">
			<table>
				<tr>
					<td>
						<s:a action="carInsurance_exportRemind" cssClass="buttonA coverOff">导出</s:a>
					</td>
					<td>
						<a class="p15" href="javascript:history.go(-1);">返回</a>
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
							<th><s:property value="tr.getText('car.Car.driver')" /></th>
							<th><s:property value="tr.getText('privilege.User.phoneNumber')" /></th>
							<th><s:property value="tr.getText('car.Car.insuranceExpiredDate')" /></th>
							<th><s:property value="tr.getText('car.Car.insuranceExpired')" /></th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${plateNumber}</td>
							<td>${driver.name}</td>
							<td>${driver.phoneNumber}</td>
							<td style="text-align:right"><s:date name="insuranceExpiredDate" format="yyyy-MM-dd"/></td>
							<s:if test="insuranceExpired">
								<td><font color="red">是</font></td>
							</s:if>
							<s:else>
								<td>否</td>
							</s:else>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			
	    })
	</script>
</cqu:border>
