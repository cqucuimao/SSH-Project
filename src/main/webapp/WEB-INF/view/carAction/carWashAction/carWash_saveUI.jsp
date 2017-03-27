<%@ page import="com.yuqincar.domain.privilege.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>洗车登记信息</h1>
            <!-- 检验错误信息提示 -->
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
        <div class="editBlock detail p30">
        <s:form action="carWash_%{id == null ? 'save' : 'edit'}" id="pageForm">
        	<s:hidden name="id"></s:hidden>
            <table>
            	<colgroup>
					<col width="80"></col>
					<col></col>
					<col></col>
					<col></col>
					<col></col>
					<col></col>
					<col></col>
					<col></col>
					<col width="120"></col>
				</colgroup>
                <tbody>
                	<tr>
                        <th><s:property value="tr.getText('car.CarWash.car')" /><span class="required">*</span></th>
						<td>
						    <input id="editNormalInfo" type="hidden" value="${editNormalInfo}" />
							<input id="editKeyInfo" type="hidden" value="${editKeyInfo}" />
							<s:textfield id="type" type="hidden" value="%{type}" />
							<cqu:carAutocompleteSelector  name="car" synchDriver="driver"/>
						</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('car.CarWash.driver')" /><span class="required">*</span></th>
                        <td>
                        	<cqu:userAutocompleteSelector  name="driver" />
                        </td>
					<td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarWash.date')" /><span class="required">*</span></th>
                        <td>
						<s:textfield name="date" id="date" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
                    </tr>
                    <tr>
                    <th><s:property value="tr.getText('car.CarWash.shop')" /><span class="required">*</span></th>
						<td>
						    <s:select id="select_list" name="shop.id" cssClass="SelectStyle"
                        		list="carWashShopList" listKey="id" listValue="name"
                        		headerKey="" headerValue="选择洗车点"/>
						</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarWash.money')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield id="money" cssClass="inputText" name="money"/>
                        </td>
                    </tr>
                    
                    <tr>
                        <th><s:property value="tr.getText('car.CarWash.innerCleanMoney')" /></th>
                        <td>
                        	<s:textfield id="innerCleanMoney" cssClass="inputText" name="innerCleanMoney"/>
                        </td>
                    </tr>
                    
                    <tr>
                        <th><s:property value="tr.getText('car.CarWash.polishingMoney')" /></th>
                        <td>
                        	<s:textfield id="polishingMoney" cssClass="inputText" name="polishingMoney"/>
                        </td>
                    </tr>
                    
                    <tr>
                        <th><s:property value="tr.getText('car.CarWash.engineCleanMoney')" /></th>
                        <td>
                        	<s:textfield id="engineCleanMoney" cssClass="inputText" name="engineCleanMoney"/>
                        </td>
                    </tr>
                    
                    <tr>
                        <th><s:property value="tr.getText('car.CarWash.cushionCleanMoney')" /></th>
                        <td>
                        	<s:textfield id="cushionCleanMoney" cssClass="inputText" name="cushionCleanMoney"/>
                        </td>
                    </tr>
                    
                    <tr>
                        <th><s:property value="tr.getText('car.CarWash.pitchCleanMoney')" /></th>
                        <td>
                        	<s:textfield id="pitchCleanMoney" cssClass="inputText" name="pitchCleanMoney"/>
                        </td>
                    </tr>
                    
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                        	<input class="inputButton coverOff" id="submit" type="submit" value="提交" />
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
	    	
	    	if($("#type").val()=="edit")
	    	{
	    		if(!($("#editNormalInfo").val()=="true" && $("#editKeyInfo").val()=="true"))
	    		{
	    		if($("#editNormalInfo").val()=="true")
	    		{
	    			$("#date").removeAttr("onfocus"); 
	    			$("#date").attr("readonly", true); 
			    	$("#money").attr("readonly", true); 
			    	$("#innerCleanMoney").attr("readonly", true); 
			    	$("#polishingMoney").attr("readonly", true); 
			    	$("#engineCleanMoney").attr("readonly", true); 
			    	$("#cushionCleanMoney").attr("readonly", true); 
			    	$("#pitchCleanMoney").attr("readonly", true); 
	    		}
	    		
	    		if($("#editKeyInfo").val()=="true")
		    	{  
	    			$("#select_list").attr("disabled", "disabled"); 
			    	$("#submit").click(function(){
			    		$("#select_list").removeAttr("disabled"); 
			    	});
			    	$("#car").removeAttr("onclick");
			    	$("#car").attr("readonly", true); 
			    	$("#driver").removeAttr("onclick");
			    	$("#driver").attr("readonly", true); 
		    	}
	    	   }
	    	}
	    	
	    	
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
					date:{
						required:true,
					},
					money:{
						required:true,
						number:true,
						min:0
					},
					innerCleanMoney:{
						number:true,
						min:0
					},
					polishingMoney:{
						number:true,
						min:0
					},
					engineCleanMoney:{
						number:true,
						min:0
					},
					cushionCleanMoney:{
						number:true,
						min:0
					},
					pitchCleanMoney:{
						number:true,
						min:0
					},
					'shop.id':{
						required:true,
					}
				}
			});
			formatDateField1($("#date"));
		});
    </script>
</cqu:border>
