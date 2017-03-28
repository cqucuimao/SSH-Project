<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
        <div class="title">
            <h1>年审预约信息</h1>
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
		<s:form action="carExamineAppointment_%{id == null ? 'add' : 'edit'}?actionFlag=register" id="pageForm">
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
                		<th><s:property value="tr.getText('car.CarExamine.car')" /><span class="required">*</span></th>
						<td>
							<cqu:carAutocompleteSelector name="car" synchDriver="driver"/>
						</td>
                    </tr>
                    <tr>
						<th><s:property value="tr.getText('car.CarExamine.driver')" /><span class="required">*</span></th>
						<td>
							<cqu:userAutocompleteSelector name="driver"/>
						</td>
					</tr>
                    <tr>
                    	<th>预约时间<span class="required">*</span></th>
                        <td>
							<s:textfield cssClass="inputText" name="date" id="date" class="Wdate half" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
						</td>
                    </tr>
                    <s:if test="id!=null">                    
					<tr>
						<th>是否完成年审</th>
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
					driverLabel:{
						required:true,
					},
					date:{
						required:true,
					},
				}
			});
			formatDateField2($("#date"));
		});
    </script>
</cqu:border>