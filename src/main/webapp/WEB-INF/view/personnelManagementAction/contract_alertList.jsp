<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>员工合同列表</h1>
		</div>		
		<div class="tab_next style2">
			<table>
				<tr>
				    <td><s:a action="contract_list"><span>合同管理</span></s:a></td>
					<td class="on"><a href="#" class="coverOff"><span>合同到期提醒</span></a></td>
				</tr>
			</table>
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
							<th>到期日期</th>
							<th>查看</th>
						</tr>
					</thead>
					<tbody class="tableHover" id="htcList">
					<s:iterator value="recordList"> 
					<tr>
					    <td>${user.name}</td>
						<td><s:date name="toDate" format="yyyy-MM-dd"/></td>
						<td>
						<s:a action="contract_editUI?id=%{id}&actionFlag=editFromAlert"><i class="icon-operate-detail" title="查看"></i></s:a>
						</td>
					</tr>
					</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="contract_alertFreshList">
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
