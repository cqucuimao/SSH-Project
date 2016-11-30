<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>价格表管理</h1>
		</div>
		<div class="editBlock search">
			<table style="border:none">
				<tr>
					<td>
						<s:a cssClass="buttonA" action="priceTable_serviceTypeList">车型管理</s:a>
					</td>					
				</tr>
			</table>
		</div>
		<div class="dataGrid">
			<div class="tableWrap">
			
				<table>
					<thead>
						<tr>
							<th>名称</th>
							<th class="alignCenter">操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
						<s:iterator value="priceTableList">
						<tr>
							<td>${title}</td>
							<td class="alignCenter">
							<s:a action="priceTable_detail?priceTableId=%{id}" ><i class="icon-operate-edit" title="编辑"></i></s:a>
							</td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	</script>
</cqu:border>
