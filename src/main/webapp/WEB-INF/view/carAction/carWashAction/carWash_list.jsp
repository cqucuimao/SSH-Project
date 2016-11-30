<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/template/taglibs.jsp" %>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
<cqu:overwrite name="content">
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>洗车登记</h1>
		</div>
		<div class="editBlock search">
			<s:form id="queryForm" action="carWash_queryForm">
			<table>
				<tr>
					<td>
						<s:a cssClass="buttonA" action="carWash_saveUI">洗车登记</s:a>
					</td>
					<th><s:property value="tr.getText('car.CarWash.car')" /></th>
					<td>
						<cqu:carSelector name="car"/>
					</td>
					<th>从</th>
					<td>
						<s:textfield name="beginDate" id="beginDate" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<th>到</th>
					<td>
						<s:textfield name="endDate" id="endDate" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
					<td>
						<input class="inputButton" type="submit" value="查询"/>
					</td>
					<td>
						<s:a cssClass="buttonA" action="carWashShop_list">洗车点管理</s:a> 
					</td>
					<td>
						<s:a cssClass="buttonA" action="carWash_excel">洗车信息导入</s:a>
						
					</td>
					<%-- <td>
					<s:a cssClass="buttonA" action="carWash_test">测试</s:a>  
					</td>  --%>
					<td>
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
							<th><s:property value="tr.getText('car.CarWash.car')" /></th>
              				<th><s:property value="tr.getText('car.CarWash.driver')" /></th>
                			<th><s:property value="tr.getText('car.CarWash.date')" /></th>
                			<th><s:property value="tr.getText('car.CarWash.shop')" /></th>
                			<th><s:property value="tr.getText('car.CarWash.money')" /></th>
                			<th><s:property value="tr.getText('car.CarWash.innerCleanMoney')" /></th>
                			<th><s:property value="tr.getText('car.CarWash.polishingMoney')" /></th>
                			<th><s:property value="tr.getText('car.CarWash.engineCleanMoney')" /></th>
                			<th><s:property value="tr.getText('car.CarWash.cushionCleanMoney')" /></th>
                			<th>操作</th>
						</tr>
					</thead>
					<tbody class="tableHover">
				        <s:iterator value="recordList">
				        
						<tr>
							<td><cqu:carDetailList id="${car.id}" /> </td>
							<td>${driver.name}</td>
							<td ><s:date name="date" format="yyyy-MM-dd"/></td>
							<td>${shop.name}</td>
							<td >${money }</td>
							<td >${innerCleanMoney }</td>
							<td >${polishingMoney }</td>
							<td >${engineCleanMoney }</td>
							<td >${cushionCleanMoney }</td>
							
							<td>
                    			<s:a action="carWash_delete?id=%{id}" onclick="result=confirm('确认要删除吗？'); if(!result) coverHidden(); return result;"><i class="icon-operate-delete" title="删除"></i></s:a>
                    			<s:a action="carWash_editUI?id=%{id}"><i class="icon-operate-edit" title="修改"></i></s:a>
          					</td> 
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
			<!-- //底部页面显示 -->
			<s:form id="pageForm" action="carWash_freshList">
			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
			</s:form>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			formatDateField2($("#beginDate"));
			formatDateField2($("#endDate"));
	    })
	</script>
	
</cqu:overwrite>
</cqu:border>
 <jsp:include page="/WEB-INF/template/JspTemplateBlockName.jsp"></jsp:include>  