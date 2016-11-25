<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>客户单位信息</h1>
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
        <s:form action="customerOrganization_%{id == null ? 'add' : 'edit'}" id="pageForm">
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
                        <th><s:property value="tr.getText('order.CustomerOrganization.name')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="name"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('order.CustomerOrganization.abbreviation')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="abbreviation"/>
						</td>
                    </tr>
					<tr>
                        <th><s:property value="tr.getText('order.CustomerOrganization.manager')" /></th>
                        <td>
                        	<s:select name="customerId" cssClass="SelectStyle"
                        		list="customerList" listKey="id" listValue="name"
                        		headerKey="" headerValue="选择管理员" 
                        		/>
						</td>
                    </tr>
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
					name:{
						required:true,
					},
					abbreviation:{
						required:true,
					}
				}
			});
		});
    </script>
</cqu:border>
