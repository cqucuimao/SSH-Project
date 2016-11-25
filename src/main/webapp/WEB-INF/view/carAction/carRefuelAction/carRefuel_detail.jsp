<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>车辆加油详细信息</h1>
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
                    			<th width="90"><s:property value="tr.getText('car.CarRefuel.sn')" />：</th>
                    			<td>${sn}</td>
                			</tr>
							<tr>
                    			<th><s:property value="tr.getText('car.Car.plateNumber')" />：</th>
                    			<td>${car.plateNumber}</td>
                			</tr>
							<tr>
                    			<th><s:property value="tr.getText('car.CarRefuel.driver')" />：</th>
                    			<td>${driver.name}</td>
                			</tr>
                			<tr>
                    			<th><s:property value="tr.getText('car.CarRefuel.date')" />：</th>
                    			<td><s:date name="date" format="yyyy-MM-dd"/></td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarRefuel.volume')" />(L)：</th>
                				<td>${volume}</td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarRefuel.money')" />(元)：</th>
                				<td><fmt:formatNumber value="${money}" pattern="#0"/></td>
                			</tr>
                			<tr>
	                			<th><s:property value="tr.getText('car.CarRefuel.outSource')" /></th>
	                			<s:if test="outSource"><td>是</td></s:if>
								<s:else><td>否</td></s:else>
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