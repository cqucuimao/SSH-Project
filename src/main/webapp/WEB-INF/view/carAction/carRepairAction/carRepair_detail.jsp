<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>车辆维修详细信息</h1>
		</div>	
		<div class="tab_next style2">
			<table>
				<tr>
				    <td><s:a action="carRepairAppoint_list"><span>预约车辆维修</span></s:a></td>
					<td class="on"><a href="#"><span>车辆维修</span></a></td>
				</tr>
			</table>
		</div>
		<br/>
		<div class="editBlock detail p30">
				<table>
					<colgroup>
					<col width="80"></col>
					<col></col>
					<col></col>
					<col></col>
					<col></col>
					<col width="120"></col>
					</colgroup>
					<tbody class="tableHover">
							<tr>
                    			<th width="90"><s:property value="tr.getText('car.Car.plateNumber')" />：</th>
                    			<td>${car.plateNumber}</td>
                			</tr>
							<tr>
                    			<th><s:property value="tr.getText('car.CarCare.driver')" />：</th>
                    			<td>${driver.name}</td>
                			</tr>
                			<tr>
                    			<th>维修时间：</th>
                    			<td><s:date name="fromDate" format="yyyy-MM-dd"/>&nbsp;&nbsp;-&nbsp;&nbsp;<s:date name="toDate" format="yyyy-MM-dd"/></td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarRepair.repairLocation')" />：</th>
                				<td>${repairLocation}</td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarRepair.money')" />(元)：</th>
                				<td><fmt:formatNumber value="${money}" pattern="#0"/></td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarRepair.moneyNoGuaranteed')" />(元)：</th>
                				<td><fmt:formatNumber value="${moneyNoGuaranteed}" pattern="#0"/></td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarRepair.reason')" />：</th>
                				<td>${reason}</td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarRepair.memo')" />：</th>
                				<td>${memo}</td>
                			</tr>
					</tbody>
					<tfoot>
                		<tr>
                    	<td colspan="2">
                        	<a class="p15" href="javascript:history.go(-1);">返回</a>
                    	</td>
                		</tr>
            		</tfoot>
				</table>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			
	    })
	</script>
</cqu:border>
