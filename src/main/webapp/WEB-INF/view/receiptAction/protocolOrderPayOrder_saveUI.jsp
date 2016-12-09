<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>协议订单分期收款单信息</h1>
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
        <s:form action="protocolOrderPayOrder_%{id == null ? 'add' : 'edit'}" id="pageForm">
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
                        <th>协议订单号<span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="protocolOrderPayOrderSn"/>&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">
                        </td>
                    </tr>
                    <tr>
                        <th>计费起止日期<span class="required">*</span></th>
                        <td>
                        	<s:textfield class="Wdate half" style="width:150px;" type="text" 
                        		name="fromDate" id="startTime" 
                        		onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" >
						  		<s:param name="value">
						  			<s:date name="fromDate" format="yyyy-MM-dd"/>
						  		</s:param>
							</s:textfield>
						&nbsp;&nbsp;- &nbsp;&nbsp;
						<s:textfield class="Wdate half" style="width:150px;" type="text" 
							name="toDate" id="endTime" 
							onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" >
						  	<s:param name="value">
						  		<s:date name="toDate" format="yyyy-MM-dd"/>
						  	</s:param>
						</s:textfield>
						</td>
                    </tr>
                    <tr>
                    	<th>金额<span class="required">*</span></th>
                    	<td>
                    		<s:textfield cssClass="inputText" name="money"/>&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">
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
					protocolOrderPayOrderSn:{
						required:true,
					}
				}
			});
		});
    </script>
</cqu:border>
