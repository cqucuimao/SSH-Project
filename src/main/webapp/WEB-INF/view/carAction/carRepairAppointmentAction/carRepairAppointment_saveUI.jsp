<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
        <div class="title">
            <h1>维修预约信息</h1>
        </div>
		<div class="tab_next style2">
			<table>
				<tr>
				    <td class="on"><a href="#" class="coverOff"><span>预约车辆维修</span></a></td>
					<td><s:a action="carRepair_list"><span>车辆维修</span></s:a></td>
				</tr>
			</table>
		</div>
		<br/>		
            <p style="color: red">
				<s:if test="hasFieldErrors()">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<s:property />
						</s:iterator>
					</s:iterator>
				</s:if>
			</p>
		<!--显示表单内容-->
		<div class="editBlock detail p30">
		<s:form action="carRepairAppointment_%{id == null ? 'add' : 'edit'}" id="pageForm">
        	<s:hidden name="id"></s:hidden>
            <table>
            	<colgroup>
					<col width="80"></col>
					<col></col>
					<col></col>
					<col width="120"></col>
				</colgroup>
            	<tbody>
                	<tr>
                		<th><s:property value="tr.getText('car.CarRepair.car')" /><span class="required">*</span></th>
						<td>
							<cqu:carAutocompleteSelector name="car" synchDriver="driver"/>
						</td>
                    </tr>
                    <tr>
						<th><s:property value="tr.getText('car.CarRepair.driver')" /><span class="required">*</span></th>
						<td>
							<cqu:userAutocompleteSelector name="driver"/>
						</td>
					</tr>
                    <tr>
                    	<th>预约时间<span class="required">*</span></th>
                        <td>
							<s:textfield cssClass="inputText" class="Wdate half" name="fromDate" id="fromDate" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<s:textfield cssClass="inputText" class="Wdate half" name="toDate" id="toDate" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
						</td>
                    </tr>
                    <s:if test="id!=null">
					<tr>
						<th>是否完成维修</th>
						<td>
							<s:checkbox class="m10" id="done" name="done"/>
						</td>
					</tr>
					</s:if>
				</tbody>
				<tfoot>
					<tr>
                        <td colspan="2">
                        	<input class="inputButton coverOff" type="submit" value="确定" />
                        	<a class="p15" href="javascript:history.go(-1);">返回</a>
                        </td>
                    </tr>
                    <tr>
                    	<td>
                    		<input type="hidden" name="appointment" value="true">
                    	</td>
                    </tr>
				</tfoot>
        	</table>
    	</s:form>
		</div>
	</div>
    <script type="text/javascript">
	    $(function(){
			$("#pageForm").validate({
				submitout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					car:{
						required:true,
					},
					driver:{
						required:true,
					},
					fromDate:{
						required:true,
					},
					toDate:{
						required:true,
					},
				}
			});
			formatDateField2($("#fromDate"));
			formatDateField2($("#toDate"));
		});
    </script>
</cqu:border>