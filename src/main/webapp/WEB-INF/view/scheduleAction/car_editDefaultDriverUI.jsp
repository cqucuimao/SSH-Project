<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>修改默认司机</h1>
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
         <s:form action="car_editDefaultDriver" name="pageForm" id="pageForm">
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
                        <th>车牌号<span class="required">*</span></th>
                        	<td>
                        		<%-- <s:textfield cssClass="inputText" name="plateNumberEDD"/> --%>
                        		<cqu:carAutocompleteSelector name="car" synchDriver="newDriver"/>
                        		<!--<input name="car" id="car" value="{}">  -->
                        	</td>
                    </tr>
                    <tr>
                        <th>默认司机</th>
                        <td>
                        	<cqu:userAutocompleteSelector name="newDriver"/>
						</td>
                    </tr>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                            <input class="inputButton coverOff" id="editdefaultDriver" type="submit" value="确定" />
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
					car:{
						required:true,
					},
					driver:{
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

