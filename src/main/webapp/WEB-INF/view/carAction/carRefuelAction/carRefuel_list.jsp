<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>加油信息</h1>
		</div>
		<div class="editBlock search">
			<s:form id="pageForm" action="carRefuel_queryList">
			<table>
				<tr>
					<td>
						<s:a cssClass="buttonA" action="carRefuel_addUI">加油登记</s:a>
					</td>
					<th><s:property value="tr.getText('car.CarRefuel.car')" /></th>
					<td>
						<cqu:carAutocompleteSelector name="car"/>
					</td>
					<th>从</th>
					<td>
						<s:textfield name="date1" id="date1" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<th>到</th>
					<td>
						<s:textfield name="date2" id="date2" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<td>
						<input class="inputButton" type="submit" value="查询"/>
					</td>
					<td>
						<s:a cssClass="buttonA" action="carRefuel_excel">导入加油信息</s:a>
					</td>
					<td>
					<s:a action="carRefuel_outPutOil_time" href="#" class="modify" onclick="modify();coverHidden();">
					    <input class="inputButton" id="outPutOil" type="button" value="导出加油信息月报表"/>
					</s:a>
					<s:if test="carId!=null">
						<a class="p15" href="javascript:history.go(-1);">返回</a>
					</s:if>
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
							<th><s:property value="tr.getText('car.CarRefuel.sn')" /></th>
							<th><s:property value="tr.getText('car.Car.plateNumber')" /></th>
							<th><s:property value="tr.getText('car.CarRefuel.driver')" /></th>
                			<th><s:property value="tr.getText('car.CarRefuel.date')" /></th>
                			<th><s:property value="tr.getText('car.CarRefuel.volume')" />(L)</th>
                			<th><s:property value="tr.getText('car.CarRefuel.money')" />(元)</th>
                			<th><s:property value="tr.getText('car.CarRefuel.outSource')" /></th>
                			<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
						<tr>
							<td>${sn}</td>
							<td><s:a action="carRefuel_detail?id=%{id}">${car.plateNumber}</s:a></td>
							<td>${driver.name}</td>
							<td style="text-align:right"><s:date name="date" format="yyyy-MM-dd"/></td>
                			<td style="text-align:right"><fmt:formatNumber value="${volume}" pattern="#0"/></td>
							<td style="text-align:right"><fmt:formatNumber value="${money}" pattern="#0"/></td>
							<s:if test="outSource"><td>是</td></s:if>
							<s:else><td>否</td></s:else>
							<td>
                    			<s:a action="carRefuel_delete?id=%{id}" onclick="result=confirm('确认要删除吗？'); if(!result) coverHidden(); return result;"><i class="icon-operate-delete" title="删除"></i></s:a>
                    			<s:a action="carRefuel_editUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
          					</td> 
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<s:form id="pageForm" action="carRefuel_freshList">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
<script type="text/javascript">
	    function modify(){
	        	url="carRefuel_outPutOil_time.action";
	        	art.dialog.open(url,{
	                id: "timeModify",
	                title: "选择时间",
	                width: 330,
	                height: 200,
	                padding: 0,
	                lock: true
	            });
	        }
	        
	    formatDateField2($("#date1"));
		formatDateField2($("#date2"));
	</script>
</cqu:border>
