<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>

<style >
	.ttt tr td:first-child 
   {
        border: 1px solid #eaeaea !important;
    }

</style>

	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>车型管理</h1>
		</div>
		<div class="editBlock search">
			<table style="border:none">
				<tr>
					<td>
						<s:a cssClass="buttonA" action="priceTable_addServiceTypeUI">新增车类</s:a>
					</td>					
				</tr>
			</table>
		</div>
		<div class="dataGrid">
			<div class="tableWrap">
			
				<table class="ttt">
					<thead>
						<tr>
							<th>车类</th>
							<th>车型</th>
							<th class="alignCenter" colspan="2">操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
						<s:iterator value="#session.mapList" id="column">
						<s:set name="total" value="#column.value.size"/> 
						<s:iterator value="#column.value" status="s" id="list">
						<tr>
							<s:if test="#s.first">
								<td class="td" rowspan="${total }">
									<s:property value="#column.key"/>
								</td>
							</s:if>
							<td><s:property value="#list"/></td>
							<td class="alignCenter">
								<s:a action="priceTable_editServiceTypeUI" ><i class="icon-operate-delete" title="删除"></i></s:a>
							</td>
							<s:if test="#s.first">
							<td class="alignCenter" rowspan="${total }">
								<a href="priceTable_editServiceTypeUI.action?actionFlag=edit&superTypeTitle=<s:property value="#column.key"/>" ><i class="icon-operate-edit" title="编辑"></i></a>
								<s:if test="canDeleteSuperServiceType">
									<s:a action="priceTable_editServiceTypeUI" ><i class="icon-operate-delete" title="删除"></i></s:a>
								</s:if>
							</td>
							</s:if>
						</tr>
						</s:iterator>
						</s:iterator> 
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	</script>
</cqu:border>
