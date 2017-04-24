<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>监控报表</h1>
			<p style="color: red">
				<s:if test="hasFieldErrors()">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<s:property />
						</s:iterator>
					</s:iterator>
				</s:if>
			</p>
		</div>
		<div class="tab_next style2">
			<table>
				<tr>
					<td class="on"><a href="#" class="coverOff"><span>导出监控报表</span></a></td>
				</tr>
			</table>
		</div>
		<br/>
		<div class="editBlock search">
		<s:form action="realtimeChart_export" id="pageForm">
			<table>
					<tr>
						<th>日期</th>
						<td>
							<s:textfield cssClass="inputText" name="date" id="date" class="Wdate half" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
						</td>
	                    <td colspan="2">
	                    	<input class="inputButton coverOff" type="submit" value="导出" />
	                    </td>
	                </tr>
			</table>
		</s:form>
		</div>
	</div>
	<script type="text/javascript">
	 $(function(){
			$("#pageForm").validate({
				submitout: function(element) { $(element).valid(); },
				rules:{
					date:{
						required:true,
					},
				}
			});
			
			formatDateField1($("#date"));
		});
	</script>
</cqu:border>
