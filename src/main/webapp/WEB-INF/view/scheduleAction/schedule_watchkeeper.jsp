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
<link href="<%=basePath %>skins/main.css" rel="stylesheet" type="text/css" />
</head>
<body class="minW">
    <div class="space">
        <div class="title">
            <h1>设置值班模式</h1>
        </div>
        <div class="editBlock detail p30">
        	<s:form action="schedule_configWatchKeeper.action" id="myForm">
            <table>
                <tbody>
                    <tr>
                        <th width="100px;">值班模式：<span class="required">*</span></th>
                        <td><s:checkbox type="checkbox" class="m10" id="onDuty" name="onDuty"/>开启值班模式</td>
                    </tr>
                    <tr>
                        <th>值班人员：<span class="required">*</span></th>
                        <td>
                        	<s:textfield class="inputText" id="driverName" type="text" name="driverName"/>
							<s:textfield id="driverId" name="driverId" type="hidden"/>
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
                        	<s:submit value="确定" class="inputButton"></s:submit>
                        	<a class="p15" href="javascript:history.go(-1);">返回</a>
                        </td>
                    </tr>
                </tfoot>
            </table>
            </s:form>
          </div>
    </div>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
    <script type="text/javascript" src="js/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="js/validate/messages_cn.js"></script>
	<script type="text/javascript">
    $(function(){    	
		$("#myForm").validate({
			onfocusout: function(element) { $(element).valid(); },
			rules:{				
				driverName:{
					required:true
				},
			}
		});
	});
    </script>
</body>
</html>
