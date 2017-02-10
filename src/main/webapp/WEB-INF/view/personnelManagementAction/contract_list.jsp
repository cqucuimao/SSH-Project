<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1> 合同列表</h1>
			<p>&nbsp;&nbsp;</p>
		</div>
		<div class="tab_next style2">
			<table>
				<tr>
					<td class="on"><a href="#" class="coverOff"><span>合同管理</span></a></td>
				    <td><s:a action="contract_alertList"><span>合同到期提醒</span></s:a></td>
				</tr>
			</table>
		</div>
		<br/>
		
		<div class="editBlock search">
		<s:form action="contract_queryList">
			<table>
				<tr>
				    <td>
						<s:a cssClass="buttonA" action="contract_addUI.action?actionFlag=register">新增</s:a>
					</td>
					<th>员工姓名</th>
					<td>
						<s:textfield cssClass="inputText" name="userName" type="text" />
					</td>
					<th>到期时间</th>
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
						<col></col>
						<col></col>
						<col></col>
						<col></col>
					</colgroup>
					<thead>
						<tr>
							<th>员工姓名</th>
							<th>开始日期</th>
							<th>结束日期</th>
							<th>扫描件</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover" id="htcList">
					<s:iterator value="recordList"> 
					<tr>
					    <td>${user.name}</td>
					   	<td><s:date name="fromDate" format="yyyy-MM-dd"/></td>
						<td><s:date name="toDate" format="yyyy-MM-dd"/></td>
						<td>
								<s:iterator value="diskFiles">
                					${fileName}
                				</s:iterator>
                		</td>
						<td>
						<s:if test="orderStatement==null">
							<s:a action="contract_editUI?id=%{id}&actionFlag=editFromList"><i class="icon-operate-edit" title="修改"></i></s:a>
                    		<s:a action="contract_delete?id=%{id}" onclick="result=confirm('确认要删除吗？'); if(!result) coverHidden(); return result;">
                    		<i class="icon-operate-delete" title="删除"></i>
                    		</s:a>
                    	</s:if>
                    	<s:else></s:else>
						</td>
					</tr>
					</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="contract_freshList">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
	   </div>
	</div>
	<script type="text/javascript">
	</script>
</cqu:border>
