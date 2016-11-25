<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1></h1>
		</div>
		<div class="tab_next style2">
			<table>
				<tr>
					<td><s:a action="insuranceStatistic_list"><span>今日统计</span></s:a></td>
				    <td><s:a action="insuranceStatistic_weekList"><span>本周统计</span></s:a></td>
				    <td class="on"><s:a action="insuranceStatistic_monthList" class="coverOff"><span>本月统计</span></s:a></td>
				    <td><s:a action="insuranceStatistic_query"><span>查询</span></s:a></td>
				</tr>
			</table>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="insuranceStatistic_monthList">
			<table>
				<tr style="height:20px;">
					<td colspan="3"></td>
				</tr>
				<tr>
					<th>选择日期:</th>
					<td>
						<s:textfield name="payDate" id="payDate" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<td>
						<input class="inputButton" type="submit" value="统计" onclick="if($('#payDate').val()!='') {$('#clicks').val('clicked');}"/>
						<s:hidden name="clicks" id="clicks"></s:hidden>
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
							<th>车牌号</th>
							<th>保险公司</th>
							<th>保单号</th>
              				<th>起止时间</th>
                			<th>保险种类</th>
                			<th>保险金额(元)</th>
                			<th>缴费日期</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${car.plateNumber}</td>
							<td>${insureCompany}</td>
							<td>${policyNumber}</td>
							<td style="text-align:right"><s:date name="fromDate" format="yyyy-MM-dd"/>&nbsp;&nbsp;-&nbsp;&nbsp;<s:date name="toDate" format="yyyy-MM-dd"/></td>
							<td>${insureType}</td>
							<td style="text-align:right"><fmt:formatNumber value="${money}" pattern="#0"/></td>
							<td style="text-align:right"><s:date name="payDate" format="yyyy-MM-dd"/></td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
		</div>
	</div>
	<s:if test="canShowImage">
	<div style="text-align:center;">
		<tr id="MoMHistogram" style="display:none">
		<th></th>
		<td>
			<img width="900" height="600" src="insuranceStatistic_MoMHistogram.action?dateString=${dateString}"/>		
		</td>
		</tr>
	</div>
	<div style="text-align:center; margin-top:50px">
		<tr id="YoYHistogram" style="display:none">
		<th></th>
		<td>
			<img width="900" height="600" src="insuranceStatistic_YoYHistogram.action?dateString=${dateString}"/>		
		</td>
		</tr>
	</div>
	</s:if>
	<s:else>
	</s:else>
    <script type="text/javascript">
	     $(function(){
 			$("#pageForm").validate({
				submitout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					payDate:{
						required:true,
					},
				}
			});
	    })
	</script>
</cqu:border>
