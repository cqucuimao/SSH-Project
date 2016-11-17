<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>车辆保养详细信息</h1>
		</div>
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
                    			<th width="120"><s:property value="tr.getText('car.CarCare.car')" />：</th>
                    			<td>${car.plateNumber}</td>
                			</tr>
							<tr>
                    			<th><s:property value="tr.getText('car.CarCare.driver')" />：</th>
                    			<td>${driver.name}</td>
                			</tr>
                			<tr>
                    			<th><s:property value="tr.getText('car.CarCare.date')" />：</th>
                    			<td><s:date name="date" format="yyyy-MM-dd"/></td>
                			</tr>
							<tr>
                    			<th><s:property value="tr.getText('car.CarCare.careDepo')" />：</th>
                    			<td>${careDepo}</td>
                			</tr>
							<tr>
                    			<th><s:property value="tr.getText('car.CarCare.careMiles')" />：</th>
                    			<td>${careMiles}</td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarCare.money')" />(元)：</th>
                				<td><fmt:formatNumber value="${money}" pattern="#0"/></td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarCare.memo')" />：</th>
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
