<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link href="skins/main.css" rel="stylesheet" type="text/css" />
<style>
    	
    </style>
</head>
<body class="minW">
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>用户信息</h1>
        </div>
        <div class="editBlock detail p30">
        <s:form action="user_%{id == null ? 'add' : 'edit'}" name="pageForm" id="pageForm">
        	<s:hidden name="id"></s:hidden>
            <table>
                <tbody>
                	<tr>
                        <th><s:property value="tr.getText('privilege.User.loginName')" /><span class="required">*</span></th>
                        <td><s:textfield cssClass="inputText" name="loginName"/> </td>
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
                    <th><s:property value="tr.getText('privilege.User.userType')" /><span class="required">*</span></th>
						<td>
							   <s:radio list="#{'0':'办公室员工','1':'司机员工'}" name="userTypeId" value="userType.id"/>
						</td>
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
	                        <s:select name="roleIds" multiple="true" size="10" cssClass="SelectStyle" 
	          				list="roleList" listKey="id" listValue="name"
	          				/>
          				</td>
                    </tr>
                    <tr class="userStatus" style="display:none">
                    	<th><s:property value="tr.getText('privilege.User.status')" /></th>
						<td>
							   <s:radio list="#{'0':'正常','1':'锁定'}" name="statusId" value="status.id"/>
						</td>
                    </tr>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                        	<input name="actionFlag" type="hidden" value="${actionFlag }">  
                            <input class="inputButton" type="submit" value="确定" />
                             <a class="p15" href="javascript:history.go(-1);">返回</a>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </s:form>
        </div>
    </div>
    <script type="text/javascript" src="js/jquery-1.7.2.js"></script>
    <script type="text/javascript" src="js/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="js/validate/messages_cn.js"></script>
    <script type="text/javascript" src="js/common.js"></script>	
    <script type="text/javascript">
   		 formatDateField1($("input[name=expireDate]"));
    	var actionFlag = $("input[name=actionFlag]").val();
    	//如果是新加用户，用户类型默认选中办公室员工
    	if(actionFlag == "add"){
    		document.getElementsByName('userTypeId')[0].checked=true;
    	}	
    	//修改用户是显示用户状态
    	if(actionFlag =="edit"){
    		$(".userStatus").show();
    	}
    	if($("input[name=userTypeId]:checked").val() == 1){
    		$(".license").show();
    	}
    
    	$("input[name=userTypeId]").click(function(){
    		if($("input[name=userTypeId]:checked").val() == 1){
        		$(".license").show();
        	}if($("input[name=userTypeId]:checked").val() == 0){
        		$(".license").hide();
        	}	
    	});  	
    
	    $(function(){
			$("#pageForm").validate({
				onfocusout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					loginName:{
						required:true
					},
					name:{
						required:true
					},
					phoneNumber:{
						required:true
					},
					licenseID:{
						required:true
					},
					expireDate:{
						required:true
					},
				}
			});
		});
    </script>
</body>
</html>
