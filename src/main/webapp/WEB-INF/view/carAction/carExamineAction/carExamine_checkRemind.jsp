<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>车辆年审提醒信息列表</h1>
		</div>      		
		<div class="tab_next style2">
			<table>
				<tr>
				    <td><s:a action="carExamineAppointment_list"><span>预约车辆年审</span></s:a></td>
					<td class="on"><a href="#" class="coverOff"><span>车辆年审记录</span></a></td>
				</tr>
			</table>
		</div>
		<br/>
		<div class="editBlock search">
			<s:form id="pageForm" action="carExamine_queryRemind">
			<table>
				<tr>
					<td><s:a action="carExamine_exportRemind" cssClass="buttonA coverOff">导出</s:a>
					</td>
					<td>
						<s:textfield name="yearMonth" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM'})" />
					</td>
					<td>
						<input class="inputButton" type="submit" value="查询"/>
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
							<th><s:property value="tr.getText('car.Car.nextExaminateDate')" /></th>
							<th>违章数量</th>
							<th>协议用车</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${plateNumber}</td>
							<td>${driver.name}</td>
							<td>${driver.phoneNumber}</td>
							<td style="text-align:right"><s:date name="nextExaminateDate" format="yyyy-MM-dd"/></td>
							<td style="text-align:right"><s:a action="carViolation_list?carId=%{id}">${carViolationCount }</s:a></td>
							<td>${protocolInfo }</td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="carExamine_freshRemind">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
	<!-- <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>	 -->
	
	<script type="text/javascript">
		$(function(){
			
	    })
	</script>
</cqu:border>
