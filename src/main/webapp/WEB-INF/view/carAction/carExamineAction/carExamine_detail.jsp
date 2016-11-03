<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>车辆年审详细信息</h1>
		</div>      		
		<div class="tab_next style2">
			<table>
				<tr>
				    <td><s:a action="carExamineAppoint_list"><span>预约车辆年审</span></s:a></td>
					<td class="on"><a href="#"><span>车辆年审记录</span></a></td>
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
                    			<th width="100"><s:property value="tr.getText('car.Car.plateNumber')" />：</th>
                    			<td>${car.plateNumber}</td>
                			</tr>
							<tr>
                    			<th><s:property value="tr.getText('car.CarCare.driver')" />：</th>
                    			<td>${driver.name}</td>
                			</tr>
                			<tr>
                    			<th><s:property value="tr.getText('car.CarExamine.date')" />：</th>
                    			<td><s:date name="date" format="yyyy-MM-dd"/></td>
                			</tr>
                			<tr>
                    			<th><s:property value="tr.getText('car.CarExamine.nextExamineDate')" />：</th>
                    			<td><s:date name="nextExamineDate" format="yyyy-MM-dd"/></td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarExamine.money')" />：</th>
                				<td>${money}</td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarExamine.carPainterMoney')" />：</th>
                				<td>${carPainterMoney}</td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarExamine.memo')" />：</th>
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
	<%-- <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>	 --%>
	<script type="text/javascript">
		$(function(){
			
	    })
	</script>
</cqu:border>
