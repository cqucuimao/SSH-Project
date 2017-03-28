<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>物品领用信息</h1>
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
        <s:form action="materialReceive_%{id == null ? 'save' : 'edit'}" id="pageForm">
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
                        <th><s:property value="tr.getText('car.Material.car')"/><span class="required">*</span></th>
						<td>
							<input id="editNormalInfo" type="hidden" value="${editNormalInfo}" />
							<input id="editKeyInfo" type="hidden" value="${editKeyInfo}" />
							<s:textfield id="type" type="hidden" value="%{type}" />
							<cqu:carAutocompleteSelector name="car" synchDriver="driver"/>
						</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('car.Material.driver')"/><span class="required">*</span></th>
                        <td>
                        	<cqu:userAutocompleteSelector name="driver"/>
                        </td>
					<td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.Material.date')"/><span class="required">*</span></th>
                        <td>
						<s:textfield name="date" id="date" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.Material.content')"/><span class="required">*</span></th>
                        <td>
                        	<s:textfield id="content" cssClass="inputText" name="content"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.Material.value')"/><span class="required">*</span></th>
                        <td>
                        	<s:textfield id="vaule" cssClass="inputText" name="value"/>
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
	    $(function(){
	    	
	    	if($("#type").val()=="edit")
	    	{   
	    		if(!($("#editNormalInfo").val()=="true" && $("#editKeyInfo").val()=="true"))
	    		{
		    		if($("#editNormalInfo").val()=="true")
		    		{
		    			$("#date").removeAttr("onfocus"); 
		    			$("#date").attr("readonly", true); 
				    	$("#vaule").attr("readonly", true); 
		    		}
		    		
		    		if($("#editKeyInfo").val()=="true")
			    	{  
		    			$("#content").attr("readonly", true);
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
					car:{
						required:true,
					},
					driverLabel:{
						required:true,
					},
					date:{
						required:true,
					},
					content:{
						required:true,
					},
					value:{
						required:true,
						number:true,
						min:0
					},
				}
			});
			formatDateField1($("#date"));
		});
    </script>
</cqu:border>
