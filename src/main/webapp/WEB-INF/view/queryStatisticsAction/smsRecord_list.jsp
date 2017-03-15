<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>短信记录</h1>
			<p>&nbsp;&nbsp;</p>
		</div>
		<br/>
		<div class="editBlock search">
		<s:form action="smsRecord_queryList">
			<table>
				<tr>
					<th>手机号码</th>
					<td>
						<s:textfield cssClass="inputText" name="phoneNumberQ" type="text" />
					</td>
					<th>发送日期</th>
					<td><s:textfield class="Wdate half" style="width:120px;"
								type="text" name="fromDateQ" id="startTime"
								onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})">
								<s:param name="value">
									<s:date name="fromDateQ" format="yyyy-MM-dd" />
								</s:param>
							</s:textfield> - <s:textfield class="Wdate half" style="width:120px;"
								type="text" name="toDateQ" id="endTime"
								onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})">
								<s:param name="value">
									<s:date name="toDateQ" format="yyyy-MM-dd" />
								</s:param>
							</s:textfield></td>
					<th>内容</th>
					<td>
						<s:textfield cssClass="inputText" name="contentQ" type="text" />
					</td>
					<td>
						<input class="inputButton" type="submit" value="查询" name="button" />
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
					</colgroup>
					<thead>
						<tr>
							<th>发送时间</th>
							<th>手机号码</th>
							<th>接收人</th>
							<th>短信内容</th>
						</tr>
					</thead>
					<tbody class="tableHover" id="htcList">
					<s:iterator value="recordList"> 
					<tr>
						<td><s:date name="date" /></td>
					    <td>${phoneNumber}</td>
					    <td>${sendName}</td>
					    <td width="60%">${content}</td>
					</tr>
					</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="smsRecord_freshList">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
	   </div>
	</div>
	<script type="text/javascript">
	</script>
</cqu:border>
