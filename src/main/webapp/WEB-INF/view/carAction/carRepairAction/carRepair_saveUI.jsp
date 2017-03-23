<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>维修信息</h1>
        </div>		
		<div class="tab_next style2">
			<table>
				<tr>
				    <td><s:a action="carRepairAppointment_list"><span>预约车辆维修</span></s:a></td>
					<td class="on"><a href="#" class="coverOff"><span>车辆维修</span></a></td>
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
        <div class="editBlock detail p30">
        <s:form action="carRepair_%{id == null ? 'add' : 'edit'}" id="pageForm">
        	<s:hidden name="id"></s:hidden>
        	<s:hidden name="editNormalInfo" id="editNormalInfo"></s:hidden>
        	<s:hidden name="editKeyInfo" id="editKeyInfo"></s:hidden>
            <table>
            	<colgroup>
					<col width="80"></col>
					<col></col>
					<col></col>
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
                        <th>维修时间<span class="required">*</span></th>
                        <td>
							<s:textfield cssClass="inputText" class="Wdate half" name="fromDate" id="fromDate" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							<s:textfield cssClass="inputText" class="Wdate half" name="toDate" id="toDate" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
						</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarRepair.repairLocation')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="repairLocation"/>
						</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarRepair.money')" />(元)<span class="required">*</span></th>
                        
                        <td>
                        	<s:textfield cssClass="inputText" name="money"/>
						</td>
                    </tr>
                    
                    <tr>
                        <th><s:property value="tr.getText('car.CarRepair.moneyNoGuaranteed')" />(元)</th>
                        
                        <td>
                        	<s:textfield cssClass="inputText" name="moneyNoGuaranteed"/>
						</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarRepair.reason')" /><span class="required">*</span></th>
                        
                        <td>
                        	<s:textarea cssClass="inputText" style="height:100px" name="reason" id="reason"></s:textarea>
						</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarRepair.memo')" /><span class="required">*</span></th>
                        <td>
                        	<s:textarea cssClass="inputText" style="height:100px" name="memo" id="memo"></s:textarea>
                        </td>
                    </tr>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                            <input class="inputButton coverOff" type="submit" value="提交" />
                             <a class="p15" href="javascript:history.go(-1);">返回</a>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </s:form>
        </div>
    </div>
    <script type="text/javascript">
    
	    //编辑时的权限处理
		if($("#editNormalInfo").val() == "true" && $("#editKeyInfo").val() == "false" ){
			$("input[name=fromDate]").attr("readonly",true);
			$("input[name=fromDate]").removeAttr("onfocus"); 
			$("input[name=toDate]").attr("readonly",true);
			$("input[name=toDate]").removeAttr("onfocus"); 
			$("input[name=money]").attr("readonly",true);
			$("input[name=moneyNoGuaranteed]").attr("readonly",true);
		}
		if($("#editKeyInfo").val() == "true" && $("#editNormalInfo").val() == "false"){
			$("#carLabel").removeAttr("onclick");
	    	$("#carLabel").attr("readonly", true); 
	    	$("#driverLabel").removeAttr("onclick");
	    	$("#driverLabel").attr("readonly", true); 
	    	$("input[name=repairLocation]").attr("readonly",true);
	    	$("#reason").attr("readonly",true);
	    	$("#memo").attr("readonly",true);
		}
	    $(function(){
			$("#pageForm").validate({
				submitout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					carLabel:{
						required:true,
					},
					driverLabel:{
						required:true,
					},
					fromDate:{
						required:true,
					},
					toDate:{
						required:true,
					},
					repairLocation:{
						required:true,
					},
					reason:{
						required:true,
					},
					money:{
						required:true,
						digits:true,
						min:1
					},
					memo:{
						required:true,
					},
					payDate:{
						required:true,
					},
				}
			});
			formatDateField2($("#fromDate"));
			formatDateField2($("#toDate"));
			formatDateField2($("#payDate"));
		});
    </script>
</cqu:border>
