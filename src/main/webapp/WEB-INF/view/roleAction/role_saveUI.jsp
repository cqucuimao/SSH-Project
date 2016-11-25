<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>角色信息</h1>
        </div>
        <div class="editBlock detail p30">
        <s:form action="role_%{id == null ? 'add' : 'edit'}" id="pageForm">
        	<s:hidden name="id"></s:hidden>
            <table>
                <tbody>
                	<tr>
                        <th width="100"><s:property value="tr.getText('privilege.Role.name')" /><span class="required">*</span></th>
                        <td><s:textfield cssClass="inputText"  name="name" /> </td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('privilege.Role.description')" /></th>
                        <td><s:textarea cssClass="inputText" style="height:100px" name="description" /></td>
                    </tr>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                            <input class="inputButton" type="submit" value="确定" />
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
					required:true
				},
			},
		});
	});
    </script>
</cqu:border>
