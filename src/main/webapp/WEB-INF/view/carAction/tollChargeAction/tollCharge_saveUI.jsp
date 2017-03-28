<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>路桥费缴纳信息</h1>
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
        <s:form action="tollCharge_%{id == null ? 'save' : 'edit'}" id="pageForm">
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
                        <th><s:property value="tr.getText('car.TollCharge.car')" /><span class="required">*</span></th>
						<td>
							<input id="editNormalInfo" type="hidden" value="${editNormalInfo}" />
							<input id="editKeyInfo" type="hidden" value="${editKeyInfo}" />
							<s:textfield id="type" type="hidden" value="%{type}" />
							<cqu:carAutocompleteSelector name="car"/>
						</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('car.TollCharge.payDate')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield  type="text" name="payDate" id="payDate" class="Wdate half"  onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" onchange="autoDate(this.id)"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.TollCharge.money')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="money" id="money"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.TollCharge.overdueFine')" /></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="overdueFine" id="overdueFine"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.TollCharge.moneyForCardReplace')" /></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="moneyForCardReplace" id="moneyForCardReplace"/>
                        </td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('car.TollCharge.nextPayDate')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="nextPayDate" id="nextPayDate" class="Wdate half" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
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
    <!-- <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script> -->
    
    <script type="text/javascript">
	formatDateField1($("#nextPayDate"));
	formatDateField1($("#payDate"));
      function autoDate(x)
       {
	    	var date=document.getElementById(x).value;
	    	var year=date.substring(0,4);
	    	var day=date.substring(4,date.length);
	    	var newYear=Number(year)+1;
	    	$("#nextPayDate").attr("value",newYear+day);
       }  
    
    $(function(){
    	if($("#type").val()=="edit")
    	{
    		if(!($("#editNormalInfo").val()=="true" && $("#editKeyInfo").val()=="true"))
    		{
    		if($("#editNormalInfo").val()=="true")
    		{
    			$("#payDate").removeAttr("onfocus"); 
    			$("#payDate").attr("readonly", true); 
		    	$("#overdueFine").attr("readonly", true); 
		    	$("#moneyForCardReplace").attr("readonly", true); 
		    	$("#money").attr("readonly", true); 
    		}
    		
    		if($("#editKeyInfo").val()=="true")
	    	{  
    			$("#nextPayDate").removeAttr("onfocus"); 
    			$("#nextPayDate").attr("readonly", true); 
		    	$("#car").removeAttr("onclick");
		    	$("#car").attr("readonly", true); 
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
					payDate:{
						required:true,
					},
					money:{
						required:true,
						number:true,
						min:0
					},
					overdueFine:{
						number:true,
						min:0
					},
					moneyForCardReplace:{
						number:true,
						min:0
					},
					nextPayDate:{
						required:true,
					},
				}
			});
		});
   </script>
</cqu:border>
