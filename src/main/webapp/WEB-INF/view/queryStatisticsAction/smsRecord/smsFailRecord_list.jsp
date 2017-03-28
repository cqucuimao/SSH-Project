<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>短信发送失败记录</h1>
			<p>&nbsp;&nbsp;</p>
		</div>
		<div class="tab_next style2">
			<table>
				<tr>
					<td><s:a action="smsRecord_list"><span>短信发送成功记录</span></s:a></td>
				    <td  class="on"><a href="#" class="coverOff"><span>短信发送失败记录</span></a></td>
				</tr>
			</table>
		</div>
		<br/>
		<div class="editBlock search">
		<s:form action="smsFailRecord_queryList">
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
							<th>出错信息</th>
						</tr>
					</thead>
					<tbody class="tableHover" id="htcList">
					<s:iterator value="recordList"> 
					<tr>
						<td><s:date name="date" format="yyyy-MM-dd HH:mm:ss"/></td>
					    <td>${phoneNumber}</td>
					    <td>${sendName}</td>
					    <td width="48%">${content}</td>
					    <td width="25%">${errorMemo}</td>
					</tr>
					</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="smsFailRecord_freshList">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
	   </div>
	</div>
	<script type="text/javascript">
	</script>
</cqu:border>
