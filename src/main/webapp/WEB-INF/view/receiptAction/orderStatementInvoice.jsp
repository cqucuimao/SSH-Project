<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>开发票</h1>
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
        <s:form action="orderStatement_invoiceDo" id="pageForm">
        	<s:hidden name="orderStatementId"/>
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
                        <th><s:property value="tr.getText('order.OrderStatement.name')" />：</th>
						<td>${name}</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('order.OrderStatement.customerOrganization')" />：</th>
						<td>${customerOrganization.name}</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('order.CustomerOrganization.financialDemand')" />：</th>
						<td>${customerOrganization.financialDemand}</td>
                    </tr>
                	<tr>
                        <th>起止时间：</th>
						<td><s:date name="fromDate" format="yyyy-MM-dd"/> &nbsp;&nbsp;-&nbsp;&nbsp; <s:date name="toDate" format="yyyy-MM-dd"/></td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('order.OrderStatement.orderNum')" />：</th>
						<td>${orderNum}</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('order.OrderStatement.totalMoney')" />：</th>
						<td>${totalMoney}</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('order.OrderStatement.status')" />：</th>
						<td>${status.label}</td>
                    </tr>
                    <tr>
						<th>不开票：</th>
						<td>
							<s:checkbox class="m10"  id="isInvoice" name="isInvoice"/>
						</td>
					</tr>
                    <tr>
						<th><s:property value="tr.getText('order.OrderStatement.invoiceNumber')" /><span class="required">*</span>：</th>
						<td>
                    		<s:textfield cssClass="inputText" id="invoiceNumber" name="invoiceNumber"/>
						</td>
					</tr>
                    <tr>
						<th><s:property value="tr.getText('order.OrderStatement.invoiceMoney')" /><span class="required">*</span>：</th>
						<td>
                    		<s:textfield cssClass="inputText" name="invoiceMoney" />
						</td>
					</tr>
                	<tr>
                        <th><s:property value="tr.getText('order.OrderStatement.invoiceDate')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText"  name="invoiceDate" class="Wdate half" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
                        </td>
                    </tr>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                        	<input class="inputButton" type="submit" value="提交" />
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
	    	
	    	/* $("#pageForm").validate({
	    		   onclick:false
	    		}) */
	    	
	    		$("#isInvoice").change(function(){
	    		if($("#isInvoice").is(':checked'))
	    			{
	    			    
							return true;
	    			}else{
	    				
	    				return false;
	    			}
	    		
	    	      });
	    	
					$("#pageForm").validate({
						submitout: function(element) { $(element).valid(); },
						rules:{
							// 配置具体的验证规则
							invoiceNumber:{								
							    required:"#isInvoice::unchecked",
							},
							invoiceMoney:{
								required:"#isInvoice::unchecked",
								number:true,
								min:0
							},
							invoiceDate:{
								required:"#isInvoice::unchecked",
							},
						}
					});
		});
    </script>
</cqu:border>
