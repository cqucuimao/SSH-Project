<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>用户信息</h1>
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
        <s:form action="user_%{id == null ? 'add' : 'edit'}" name="pageForm" id="pageForm">
        	<s:hidden name="id"></s:hidden>
            <table>
                <tbody>
                	<tr>
                        <th><s:property value="tr.getText('privilege.User.loginName')" /><span class="required">*</span></th>
                        <td width="370px"><s:textfield cssClass="inputText" name="loginName"/> </td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('privilege.User.name')" /><span class="required">*</span></th>
                        <td><s:textfield cssClass="inputText" name="name" /> </td>
                    </tr>
                    <tr>
                        <th width="100"><s:property value="tr.getText('privilege.Department.name')" /><span class="required">*</span></th>
                        <td><s:select name="departmentId" cssClass="SelectStyle"
                        		list="departmentList" listKey="id" listValue="name"
                        	/>
                        </td>
                    </tr>
                    <tr>
                    	<th><s:property value="tr.getText('privilege.User.gender')" /></th>
						<td><cqu:enumSelector name="gender" enumName="common.GenderEnum" notNull="true"/></td>
                    </tr>
                    <tr>
                    	<th><s:property value="tr.getText('privilege.User.birth')" /></th>
                    	<td>
                    		<s:textfield cssClass="inputText" name="birth" id="birth" class="Wdate half" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})">
								<s:param name="value">
									<s:date name="birth" format="yyyy-MM-dd" />
								</s:param>
							</s:textfield>
                        </td>
                    </tr>
                    <tr>
                    <th><s:property value="tr.getText('privilege.User.userType')" /><span class="required">*</span></th>
						<td><cqu:enumSelector name="userType" enumName="privilege.UserTypeEnum" notNull="true"/></td>
                    </tr>
                    <tr class="license" style="display:none">
                        <th><s:property value="tr.getText('privilege.User.driverLicense')" /><span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="licenseID" style="width:165px" placeholder="驾照编号"/>
                        	<s:textfield class="Wdate half" name="expireDate" style="width:170px" placeholder="驾照过期日期" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('privilege.User.phoneNumber')" /><span class="required">*</span></th>
                        <td><s:textfield cssClass="inputText" name="phoneNumber" /></td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('privilege.User.email')" /></th>
                        <td><s:textfield cssClass="inputText" name="email"></s:textfield></td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('privilege.User.description')" /></th>
                        <td><s:textarea cssClass="inputText" style="height:100px" name="description"></s:textarea></td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('privilege.User.roles')" /></th>
                        <td>
	                        <s:select name="listRoleIds" multiple="true" size="10" cssClass="SelectStyle" 
	          				list="roleList" listKey="id" listValue="name" ondblclick="moveOption(document.pageForm.listRoleIds, document.pageForm.selectedRoleIds)"
	          				/>
          				</td>
          				<td align="center">
          					<input type="button" value="全部添加" onclick="moveAllOption(document.pageForm.listRoleIds, document.pageForm.selectedRoleIds)"><br/> 
							<br/>
							<input type="button" value="添加" onclick="moveOption(document.pageForm.listRoleIds, document.pageForm.selectedRoleIds)"><br/> 
							<br/> 
							<input type="button" value="移除" onclick="moveOption(document.pageForm.selectedRoleIds, document.pageForm.listRoleIds)"><br/> 
							<br/> 
							<input type="button" value="全部移除" onclick="moveAllOption(document.pageForm.selectedRoleIds, document.pageForm.listRoleIds)"> 
							<br>
							<s:textfield type="hidden" name="roleString" />
						</td>
          				<td>
          					<s:select name="selectedRoleIds" multiple="true" size="10" cssClass="SelectStyle"
          					 list="selectedList" listKey="id" listValue="name" ondblclick="moveOption(document.pageForm.selectedRoleIds, document.pageForm.listRoleIds)">
	          				</s:select>
          				</td>
                    </tr>
                    <tr class="userStatus" style="display:none">
                    	<th><s:property value="tr.getText('privilege.User.status')" /></th>
						<td><cqu:enumSelector name="status" enumName="privilege.UserStatusEnum" notNull="true"/></td>
                    </tr>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                        	<input name="actionFlag" type="hidden" value="${actionFlag }">  
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
    
   		formatDateField1($("input[name=expireDate]"));
    	var actionFlag = $("input[name=actionFlag]").val();
    	
    	//修改用户是显示用户状态
    	if(actionFlag =="edit"){
    		$(".userStatus").show();
    	}
    	if($("#userType").find("option:selected").text()=="司机员工"){
    		$(".license").show();
    	}
    
    	$("#userType").click(function(){
    		if($("#userType").find("option:selected").text()=="司机员工"){
        		$(".license").show();
        	}else
        		$(".license").hide();        		
    	});  	
    	
    	// 手机号码验证
    	jQuery.validator.addMethod("isMobile", function(value, element) {
    	    var length = value.length;
    	    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
    	    return this.optional(element) || (length == 11 && mobile.test(value));
    	}, "请正确填写您的手机号码");
    	
    	//验证驾照号码，就是身份证号
    	jQuery.validator.addMethod("isCard", function(value, element) {
    	    var length = value.length;
    	    var card = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    	    return this.optional(element) || (length == 18 && card.test(value));
    	}, "请正确填写您的驾照号码");
    
	    $(function(){
			$("#pageForm").validate({
				submitout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					loginName:{
						required:true
					},
					name:{
						required:true
					},
					phoneNumber:{
						required:true,
						isMobile:true,
						minlength:11
					},
					licenseID:{
						required:true,
						isCard:true
					},
					expireDate:{
						required:true
					},
				}
			});
		});
	    <!--操作全部-->
	    function moveAllOption(e1, e2){ 
	     var fromObjOptions=e1.options; 
	      for(var i=0;i<fromObjOptions.length;i++){ 
	       fromObjOptions[0].selected=true; 
	       e2.appendChild(fromObjOptions[i]); 
	       i--; 
	      } 
	     document.pageForm.roleString.value=getvalue(document.pageForm.selectedRoleIds); 
	    }

	    <!--操作单个-->
	    function moveOption(e1, e2){ 
	     var fromObjOptions=e1.options; 
	      for(var i=0;i<fromObjOptions.length;i++){ 
	       if(fromObjOptions[i].selected){ 
	        e2.appendChild(fromObjOptions[i]); 
	        i--; 
	       } 
	      } 
	     document.pageForm.roleString.value=getvalue(document.pageForm.selectedRoleIds); 
	    } 

	    function getvalue(geto){ 
	    var allvalue = ""; 
	    for(var i=0;i<geto.options.length;i++){ 
	    allvalue +=geto.options[i].value + ","; 
	    } 
	    return allvalue; 
	    } 

	    function changepos1111(obj,index) 
	    { 
	    if(index==-1){ 
	    if (obj.selectedIndex>0){ 
	    obj.options(obj.selectedIndex).swapNode(obj.options(obj.selectedIndex-1)) 
	    } 
	    } 
	    else if(index==1){ 
	    if (obj.selectedIndex<obj.options.length-1){ 
	    obj.options(obj.selectedIndex).swapNode(obj.options(obj.selectedIndex+1)) 
	    } 
	    } 


	    } 
    </script>
</cqu:border>
