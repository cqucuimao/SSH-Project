<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
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
                        <th>单位名称<span class="required">*</span></th>
                        <td>
						 	<s:textfield id="customer_organization_name" name="customerOrganization.name" cssClass="inputText" type="text"/>
						 	<input id="customer_organization_id" type="hidden">
                        </td>
                    </tr>
                    <tr>
                        <th>姓名<span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="name"/>
						</td>
                    </tr>
                    <tr>
                    	<th>性别<span class="required">*</span></th>
                    	<td>
                    		<s:radio name="gender" list="%{#{'true':'男性','false':'女性'}}" value="name==null ? 'true' : gender"></s:radio>
                    	</td>
                    </tr>
                    <tr>
                        <th>联系方式<span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="phonesStr"/>&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:gray">(多个号码之间用英文逗号隔开)</span>
                        	<s:fielderror style="color:red"></s:fielderror>
						</td>
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
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    
    <script src="js/jquery-1.7.2.js"></script>
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
    
    
    <script type="text/javascript" src="js/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="js/validate/messages_cn.js"></script>
    <script type="text/javascript">
	    $(function(){
			$("#pageForm").validate({
				onfocusout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					'customerOrganization.name':{
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
</body>
</html>
