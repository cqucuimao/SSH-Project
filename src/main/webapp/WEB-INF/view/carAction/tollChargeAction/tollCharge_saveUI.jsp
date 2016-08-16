<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border  exceptJquery="jquery">
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
							<cqu:carSelector name="car"/>
						</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('car.TollCharge.payDate')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="payDate" id="payDate" class="Wdate half" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.TollCharge.money')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="money"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.TollCharge.overdueFine')" /></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="overdueFine"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.TollCharge.moneyForCardReplace')" /></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="moneyForCardReplace"/>
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
			$("#pageForm").validate({
				submitout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					carLabel:{
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
			formatDateField1($("#payDate"));
			formatDateField1($("#nextPayDate"));
		});
   </script>
</cqu:border>
