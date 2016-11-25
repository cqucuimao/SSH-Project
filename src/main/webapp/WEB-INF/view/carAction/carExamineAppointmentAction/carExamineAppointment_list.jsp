<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>车辆年审记录</h1>
		</div>		
		<div class="tab_next style2">
			<table>
				<tr>
				    <td class="on"><a href="#" class="coverOff"><span>预约车辆年审</span></a></td>
					<td><s:a action="carExamine_list"><span>车辆年审记录</span></s:a></td>
				</tr>
			</table>
		</div>
		<br/>
		<div class="editBlock search">
			<s:form id="pageForm" action="carExamineAppointment_queryList">
			<table>
				<tr>
					<td>
						<s:a cssClass="buttonA" action="carExamineAppointment_addUI">年审预约</s:a>
					</td>
					<th><s:property value="tr.getText('car.CarExamine.car')" /></th>
					<td><cqu:carSelector name="car"/></td>
					<th><s:property value="tr.getText('car.CarExamine.driver')" /></th>
					<td><cqu:userSelector name="driver"/></td>
					<th>从</th>
					<td>
						<s:textfield name="date1" id="date1" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<th>到</th>
					<td>
						<s:textfield name="date2" id="date2" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<th>是否完成</th>
					<td>
						<s:select name="doneOrUndone" cssClass="selectClass" list="#{1:'是',2:'所有'}" listKey="key" listValue="value"  headerKey="0" headerValue="否"></s:select>
					</td>
					<td>
						<input class="inputButton" type="submit" value="查询"/>
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
							<th><s:property value="tr.getText('car.CarExamineAppointment.car')" /></th>
							<th><s:property value="tr.getText('car.CarExamineAppointment.driver')" /></th>
              				<th><s:property value="tr.getText('car.CarExamineAppointment.date')" /></th>
              				<th><s:property value="tr.getText('car.CarExamineAppointment.done')" /></th>
                			<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td><cqu:carDetailList id="${car.id}" /> </td>
							<td>${driver.name}</td>
							<td><s:date name="date" format="yyyy-MM-dd"/></td>
							<td>
								<s:if test="done==true">
								<s:text name="是"></s:text>
								</s:if>
								<s:else>
								<s:text name="否"></s:text>
								</s:else>
							</td>
							<td>
                				<s:if test="done==false">
                    			<s:a action="carExamineAppointment_delete?id=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
                    			<s:a action="carExamineAppointment_editUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
                    			</s:if>
								<s:else>
								</s:else>
                			</td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="carExamineAppointment_freshList">
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
