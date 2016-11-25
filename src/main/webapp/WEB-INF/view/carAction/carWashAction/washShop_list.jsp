<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>洗车点管理</h1>
		</div>
		<div class="editBlock search">
			<table>
				<tr>
					<td>
						<s:a cssClass="buttonA" action="carWashShop_addUI">洗车点登记</s:a>
					</td>
					<td>
						<a class="p15" href="javascript:history.go(-1);">返回</a>
					</td>										
				</tr>
			</table>
		</div>
		<div class="dataGrid">
			<div class="tableWrap">
				<table>
					<thead>
						<tr>
							<th class="alignCenter">洗车点</th>
							<th class="alignCenter">操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
						<s:iterator value="carWashshop">
						<tr>
							<td class="alignCenter">${name}</td>
							<td class="alignCenter">									
							<s:if test="canDeleteCarWashShop">
								<s:a action="carWashShop_delete?id=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
							</s:if>
							<s:else>
							</s:else>
							</td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
		</div>
	</div>
</cqu:border>