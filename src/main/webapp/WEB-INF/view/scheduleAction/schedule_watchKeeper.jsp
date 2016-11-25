<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <div class="title">
            <h1>设置值班模式</h1>
        </div>
        <div class="editBlock detail p30">
        	<s:form action="schedule_configWatchKeeper.action" id="myForm">
            <table>
                <tbody>
                    <tr>
                        <th width="100px;"><s:property value="tr.getText('order.WatchKeeper.onDuty')" />：<span class="required">*</span></th>
                        <td><s:checkbox type="checkbox" class="m10" id="onDuty" name="onDuty"/>开启值班模式</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('order.WatchKeeper.keeper')" />：<span class="required">*</span></th>
                        <td>
                        	<cqu:userSelector name="keeper"/>
						</td>
                    </tr>
                </tbody>
                <tfoot>                    
                    <tr>
                        <th></th>
                        <td></td>
                        <th></th>
                        <td></td>
                    </tr>
                    <tr>
                        <td colspan="4">
                        	<s:submit value="确定" class="inputButton coverOff"></s:submit>
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
		$("#myForm").validate({
			submitout: function(element) { $(element).valid(); },
			rules:{				
				driverName:{
					required:true
				},
			}
		});
	});
    </script>
</cqu:border>
