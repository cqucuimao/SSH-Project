<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>修改驾照过期时间</h1>
        </div>
        <div class="editBlock detail p30">
        <s:form action="driverLicense_edit" id="pageForm">
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
                        <th>员工姓名</th>
                        <td>
                        	${name}
						</td>
                    </tr>
                    <tr>
                        <th>驾驶号码</th>
                        <td>
                        	${driverLicense.licenseID}
						</td>
                    </tr>
                    <tr>
                        <th>到期日期<span class="required">*</span></th>
                        <td>
                        	<s:textfield class="Wdate half" style="width:150px;" type="text" 
                        		name="driverLicense.expireDate" id="startTime" 
                        		onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" >
						  		<s:param name="value">
						  			<s:date name="driverLicense.expireDate" format="yyyy-MM-dd"/>
						  		</s:param>
							</s:textfield>
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
					expireDate:{
						required:true,
					}
				}
			});
		});
    </script>
</cqu:border>
