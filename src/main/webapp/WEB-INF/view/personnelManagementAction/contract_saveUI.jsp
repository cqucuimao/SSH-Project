<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>合同信息</h1>
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
        <s:form action="contract_%{id == null ? 'add' : 'edit'}" id="pageForm">
        	<s:hidden name="id"></s:hidden>
        	<s:hidden name="actionFlag"></s:hidden>
        	<%-- <input value="${actionFlag }" /> --%>
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
                        <th>员工姓名<span class="required">*</span></th>
                        <td>
                        	<cqu:userAutocompleteSelector name="editUser"/>
						</td>
                    </tr>
                    <tr>
                        <th>开始日期<span class="required">*</span></th>
                        <td>
                        	<s:textfield class="Wdate half" style="width:150px;" type="text" 
                        		name="fromDate" id="fromDate" 
                        		onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})">
                        		<s:param name="fromDate">
						  			<s:date name="fromDate" format="yyyy-MM-dd"/>
						  		</s:param>
							</s:textfield>
						</td>
                    </tr>
                    <tr>
                        <th>结束日期<span class="required">*</span></th>
                        <td>
                        	<s:textfield class="Wdate half" style="width:150px;" type="text" 
                        		name="toDate" id="toDate" 
                        		onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})">
                        		<s:param name="toDate">
						  			<s:date name="toDate" format="yyyy-MM-dd"/>
						  		</s:param>
							</s:textfield>
						</td>
                    </tr>
                     <tr>
                        <th>扫描件</th>
                        <td>									
							<cqu:diskFileSelector name="uploadedDiskFiles" uploadLimit="false"/>
						</td>
                    </tr>
                    <tr>
                        <th></th>
                        <td>
                        <s:iterator value="diskFiles" >
							<s:a action="contract_downloadFile?uploadId=%{id}">${fileName}</s:a>
							<s:a action="contract_deleteFile?deletedId=%{id}" onclick="result=confirm('确认要删除吗？'); if(!result) coverHidden(); return result;">
								<i class="icon-operate-delete" title="删除"></i>
                    		</s:a>
						</div>
						</s:iterator>
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
					editUserLabel:{
						required:true,
					},
					fromDate:{
						required:true,
					},
					toDate:{
						required:true,
					},
				}
			});
		});
    </script>
</cqu:border>
