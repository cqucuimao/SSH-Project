<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
        <div class="title">
            <h1>保养预约登记</h1>
        </div> 
		<div class="tab_next style2">
			<table>
				<tr>
				    <td class="on"><s:a action="carCareAppointment_list" href="#" class="coverOff"><span>预约车辆保养</span></s:a></td>
					<td><s:a action="carCare_list"><span>车辆保养记录</span></s:a></td>
				</tr>
			</table>
		</div>
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
		<div class="editBlock">
		<s:form action="carCareAppointment_%{id == null ? 'add' : 'edit'}" id="pageForm">
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
						<th><s:property value="tr.getText('car.CarCareAppointment.car')" /><span class="required">*</span></th>
						<td>
							<cqu:carAutocompleteSelector name="car" synchDriver="driver"/>
						</td>
                    </tr>
                    <tr>
						<th><s:property value="tr.getText('car.CarCareAppointment.driver')" /><span class="required">*</span></th>
						<td>
							<cqu:userAutocompleteSelector name="driver"/>
						</td>
					</tr>
                    <tr>
                    	<th><s:property value="tr.getText('car.CarCareAppointment.date')" /><span class="required">*</span></th>
						<td>
							<s:textfield cssClass="inputText" name="date" id="date" class="Wdate half" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
						</td>
                    </tr>
                     <s:if test="id!=null">
					<tr>
						<th>保养间隔里程</th>
						<td>
							<s:textfield cssClass="inputText" name="mileInterval" id="mileInterval"/>
						</td>
					</tr>
					</s:if>
                    <s:if test="id!=null">
					<tr>
						<th><s:property value="tr.getText('car.CarCareAppointment.done')" /></th>
						<td>
							<s:checkbox class="m10" id="done" name="done"/>
						</td>
					</tr>
					</s:if>
				</tbody>
				<tfoot>
					<tr>
                        <td colspan="2">
                        	<input class="inputButton coverOff" id="btn" type="submit" value="确定" />
                        	<a class="p15" href="javascript:history.go(-1);">返回</a>
                        </td>
                    </tr>
				</tfoot>
        	</table>
    	</s:form>
		</div>
	</div>
    <script type="text/javascript">
        //当done是true时，必须填写mileInterval
	    $("#btn").click(function(){
			if($("#done").is(":checked") && $("#mileInterval").val()==""){
				alert("完成保养预约时必须指定保养间隔里程！");
				return false;
			}
	 	}); 
    	
    	//保养里程间隔默认为8000
    	$("input[name=mileInterval]").val(8000);
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
					date:{
						required:true,
					},
				}
			});
			formatDateField2($("#date"));
		});
    </script>
</cqu:border>