<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border exceptJMin="jquery">
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>路桥费</h1>
		</div>
		<div class="editBlock search">
			<s:form id="queryForm" action="tollCharge_queryForm">
			<table>
				<tr>
					<td>
						<s:a action="tollCharge_saveUI"><input id="register" class="inputButton" type="button" value="路桥费缴纳登记"/></s:a>
					</td>
					<th><s:property value="tr.getText('car.Car.plateNumber')" /></th>
					<td>
						<s:textfield id="car_platenumber" cssClass="carSelector inputText inputChoose" onfocus="this.blur();" name="car.plateNumber" type="text" />
					</td>
					<th>从</th>
					<td>
						<s:textfield name="beginDate" id="beginDate" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<th>到</th>
					<td>
						<s:textfield name="endDate" id="endDate" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<td>
						<input class="inputButton" type="submit" onClick="registerClick()" value="查询"/>
						<s:a action="tollCharge_test" type="button">請求</s:a>
						<s:a action="tollCharge_initTest" type="button">回值</s:a>
						<input id="remind" class="inputButton" type="button" value="缴纳提醒"/>
						<s:if test="Ture">
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
							<th><s:property value="tr.getText('car.TollCharge.car')" /></th>
              				<th><s:property value="tr.getText('car.TollCharge.payDate')" /></th>
                			<th><s:property value="tr.getText('car.TollCharge.money')" /></th>
                			<th><s:property value="tr.getText('car.TollCharge.overdueFine')" /></th>
                			<th><s:property value="tr.getText('car.TollCharge.moneyForCardReplace')" /></th>
                			<th><s:property value="tr.getText('car.TollCharge.nextPayDate')" /></th>
                			<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
				        
						<tr>
							<td>${car.plateNumber}</td>
							<td style="text-align:right"><s:date name="payDate" format="yyyy-MM-dd"/></td>
							<td style="text-align:right"><fmt:formatNumber value="${money}" pattern="#0"/></td>
							<td style="text-align:right"><fmt:formatNumber value="${overdueFine}" pattern="#0"/></td>
							<td style="text-align:right"><fmt:formatNumber value="${moneyForCardReplace}" pattern="#0"/></td>
							<td style="text-align:right"><s:date name="nextPayDate" format="yyyy-MM-dd"/></td>
							<td>
                    			<s:a action="tollCharge_delete?id=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
                    			<s:a action="tollCharge_editUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
          					</td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="tollCharge_freshList">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>	
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
	<script type="text/javascript">
		$(function(){
	        $("#remind").click(function(){
	            self.location.href='tollCharge_remind.action';
	        });  
			formatDateField2($("#beginDate"));
			formatDateField2($("#endDate"));
	    })
	</script>
</cqu:border>
