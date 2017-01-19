<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>驾照列表</h1>
		</div>	
		<br/>
		<div class="dataGrid">
			<div class="tableWrap">
				<table>
					<colgroup>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
					</colgroup>
					<thead>
						<tr>
							<th>员工姓名</th>
							<th>驾照号码</th>
							<th>到期日期</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover" id="htcList">
					<s:iterator value="recordList"> 
					<tr>
					    <td>${name}</td>
						<td>${driverLicense.licenseID}</td>
						<td><s:date name="driverLicense.expireDate" format="yyyy-MM-dd" /></td>
						<td>
						<s:a action="driverLicense_editUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
						</td>
					</tr>
					</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="driverLicense_freshList">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
	
	
	<script type="text/javascript">
		$(function(){
			formatDateField2($("#date1"));
			formatDateField2($("#date2"));
	    })
	</script>
</cqu:border>
