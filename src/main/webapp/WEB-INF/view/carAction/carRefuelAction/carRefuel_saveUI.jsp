<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>加油信息</h1>
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
        <s:form action="carRefuel_%{id == null ? 'add' : 'edit'}" id="pageForm">
        	<s:hidden name="id"></s:hidden>
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
                        <th><s:property value="tr.getText('car.CarRefuel.sn')" /></th>
						<td>
							<input id="editNormalInfo" type="hidden" value="${editNormalInfo}" />
							<input id="editKeyInfo" type="hidden" value="${editKeyInfo}" />
							<s:textfield id="type" type="hidden" value="%{type}" />
							<s:textfield class="inputText" type="text" name="sn" id="sn"/>
						</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('car.CarRefuel.car')" /><span class="required">*</span></th>
						<td>
							<cqu:carAutocompleteSelector name="car" synchDriver="driver"/>
						</td>
                    </tr>
                    <tr>
						<th><s:property value="tr.getText('car.CarRefuel.driver')" /><span class="required">*</span></th>
						<td>
							<cqu:userAutocompleteSelector name="driver"/>
						</td>
					</tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarRefuel.date')" /><span class="required">*</span></th>
						<td>
							<s:textfield name="date" id="date" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
						</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarRefuel.volume')" />(L)<span class="required">*</span></th>
                        <td>
                        	<s:textfield class="inputText" type="text" name="volume" id="volume"/>
						</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarRefuel.money')" />(元)<span class="required">*</span></th>
                        <td>
                        	<s:textfield class="inputText" type="text" name="money" id="money"/>
						</td>
                    </tr>
                    <tr>
						<th><s:property value="tr.getText('car.CarRefuel.outSource')" /></th>
						<td>
							<s:checkbox class="m10" name="outSource" id="outSource"/>
						</td>
					</tr>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                            <input class="inputButton coverOff" type="submit" value="确定 " />
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
	    	formatDateField1($("#date"));
	    	if($("#type").val()=="edit")
	    	{   
	    		if(!($("#editNormalInfo").val()=="true" && $("#editKeyInfo").val()=="true"))
	    		{
		    		if($("#editNormalInfo").val()=="true")
		    		{
		    			$("#date").removeAttr("onfocus"); 
		    			$("#date").attr("readonly", true); 
				    	$("#volume").attr("readonly", true); 
				    	$("#money").attr("readonly", true); 
		    		}
		    		
		    		if($("#editKeyInfo").val()=="true")
			    	{  
		    			$("#outSource").attr("disabled", "disabled"); 
				    	$("#submit").click(function(){
				    		$("#outSource").removeAttr("disabled"); 
				    	});
		    			$("#sn").attr("readonly", true);
				    	$("#carLable").removeAttr("onclick");
				    	$("#carLable").attr("readonly", true); 
				    	$("#driverLabel").removeAttr("onclick");
				    	$("#driverLabel").attr("readonly", true); 
			    	}
	    	   }
	    	}
	    	
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
					money:{
						required:true,
						number:true,
						min:0,
					},
					volume:{
						required:true,
						number:true,
						min:0,
					},
				}
			});
		});
    </script>
</cqu:border>
