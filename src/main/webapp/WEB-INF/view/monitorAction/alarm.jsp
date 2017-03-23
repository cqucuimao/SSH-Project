<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>历史报警信息</h1>
			<p>&nbsp;&nbsp;</p>
		</div>
	    <!-- 查询条件 -->
        <div class="editBlock search subtract">
        <s:form id="pageForm" action="alarm_home">
            <table id="queryInfoTB">
                <tr>
                    <th>司机名称</th>
                    <td><cqu:userAutocompleteSelector name="driver"/></td>
                    <th>车牌号</th>
                    <td><cqu:carAutocompleteSelector name="car"/></td>
                    <th>起始时间</th>
                    <td>
						<s:textfield class="Wdate half" style="width:120px;" type="text" name="beginDate" id="startTime" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" >
						  <s:param name="value"><s:date name="beginDate" format="yyyy-MM-dd"/></s:param>
						</s:textfield>
						- 
						<s:textfield class="Wdate half" style="width:120px;" type="text" name="endDate" id="endTime" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" >
						  <s:param name="value"><s:date name="endDate" format="yyyy-MM-dd"/></s:param>
						</s:textfield>
						<input class="inputButton" type="submit" id="queryBn" value="查询"/>
                    </td>
                </tr>
            </table>
        </s:form>
        </div>
		<div class="dataGrid">
			<div class="tableWrap">
				<table>
					<colgroup>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
					</colgroup>
					<thead>
						<tr>
							<th>车牌号</th>
							<th>司机</th>
							<th>联系方式</th>
							<th>报警时间</th>
							<th>报警原因</th>
						</tr>
					</thead>
					<tbody class="tableHover" id="htcList">
					<s:iterator value="recordList">
					<tr>
					    <td>${car.plateNumber}</td>
                		<td>${car.driver.name}</td>
						<td>${car.driver.phoneNumber}</td>
						<td>${date}</td>
						<td>
                			<s:if test="type.toString()==@com.yuqincar.domain.monitor.WarningMessageTypeEnum@PULLEDOUT.toString()">设备拔出</s:if>
                            <s:else>异常行驶</s:else>
                		</td>
					</tr>
					</s:iterator> 
					</tbody>
				</table>
			</div>
		</div>
		<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
	</div>
	<script type="text/javascript">
	</script>
</cqu:border>