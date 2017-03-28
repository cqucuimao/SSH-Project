<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>保养信息</h1>
        </div>        
		<div class="tab_next style2">
			<table>
				<tr>
				    <td><s:a action="carCareAppointment_list"><span>预约车辆保养</span></s:a></td>
					<td class="on"><a href="#" class="coverOff"><span>车辆保养记录</span></a></td>
				</tr>
			</table>			
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
		<br/>
        <div class="editBlock detail p30">
        <s:form action="carCare_%{id == null ? 'add' : 'edit'}" id="pageForm">
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
                        <th><s:property value="tr.getText('car.CarCare.car')" /><span class="required">*</span></th>
						<td>
							<cqu:carAutocompleteSelector name="car" synchDriver="driver"/>
						</td>
                    </tr>
                    <tr>
						<th><s:property value="tr.getText('car.CarCare.driver')" /><span class="required">*</span></th>
						<td>
							<cqu:userAutocompleteSelector name="driver"/>
						</td>
					</tr>
                	<tr>
                        <th><s:property value="tr.getText('car.CarCare.date')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="date" id="date" class="Wdate half" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
                        </td>
                    </tr>
                    <tr>
                    	<th><s:property value="tr.getText('car.CarCare.careDepo')" /><span class="required">*</span></th>
                    	<td>
                    		<s:textfield cssClass="inputText" name="careDepo" />
                    	</td>
                    </tr>
                    <tr>
                    	<th><s:property value="tr.getText('car.CarCare.careMiles')" /></th>
                    	<td>
                    		<s:textfield cssClass="inputText" name="careMiles" />
                    	</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarCare.money')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="money"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarCare.memo')" /><span class="required">*</span></th>
                        <td>
                        	<s:textarea cssClass="inputText" style="height:100px" id="memo" name="memo"></s:textarea>
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
    	var miles = $("input[name=careMiles]").val();
    	if(miles <= 0 ){
    		$("input[name=careMiles]").val("");
    	}
    		
	    //编辑时的权限处理
		//alert($("#editNormalInfo").val());
		//alert($("#editKeyInfo").val());
		if($("#editNormalInfo").val() == "true" && $("#editKeyInfo").val() == "false" ){
    		$("input[name=money]").attr("readonly",true);
    		$("input[name=date]").attr("readonly",true);
    		$("input[name=date]").removeAttr("onfocus"); 
    	}
    	if($("#editKeyInfo").val() == "true" && $("#editNormalInfo").val() == "false"){
    		$("#carLable").removeAttr("onclick");
	    	$("#carLable").attr("readonly", true); 
	    	$("#driverLabel").removeAttr("onclick");
	    	$("#driverLabel").attr("readonly", true); 
    		$("#car").removeAttr("onclick");
	    	$("#car").attr("readonly", true); 
	    	$("#driver").removeAttr("onclick");
	    	$("#driver").attr("readonly", true); 
	    	$("input[name=careDepo]").attr("readonly",true);
	    	$("input[name=careMiles]").attr("readonly",true);
	    	$("input[name=mileInterval]").attr("readonly",true);
	    	$("#memo").attr("readonly",true);
    	}
    
	    $(function(){
			$("#pageForm").validate({
				submitout: function(element) { $(element).valid(); },
				rules:{
					car:{
						required:true,
					},
					driverLabel:{
						required:true,
					},
					date:{
						required:true,
					},
					careDepo:{
						required:true,
					},
					mileInterval:{
						required:true,
						digits:true,
						min:1
					},
					money:{
						required:true,
						digits:true,
						min:1
					},
					memo:{
						required:true,
					},
				}
			});
			formatDateField2($("#date"));
		});
    </script>
</cqu:border>
