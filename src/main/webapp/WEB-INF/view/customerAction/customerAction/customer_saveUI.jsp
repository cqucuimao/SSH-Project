<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>客户信息</h1>
        </div>
        <div class="editBlock detail p30">
        <s:form action="customer_%{id == null ? 'add' : 'edit'}" id="pageForm">
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
                        	<cqu:customerOrganizationSelector name="customerOrganization"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('order.Customer.name')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="name"/>
						</td>
                    </tr>
                    <tr>
                    	<th><s:property value="tr.getText('order.Customer.gender')" /><span class="required">*</span></th>
                    	<td>
                    		<s:radio name="gender" list="%{#{'true':'男性','false':'女性'}}" value="name==null ? 'true' : gender"></s:radio>
                    	</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('order.Customer.phones')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="phonesStr"/>&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">(多个号码之间用英文逗号隔开)</span>
                        	<s:fielderror style="color:red"></s:fielderror>
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
					customerOrganizationLabel:{
						required:true,
					},
					name:{
						required:true,
					},
					phonesStr:{
						required:true,
					}
				}
			});
		});
    </script>
</cqu:border>
