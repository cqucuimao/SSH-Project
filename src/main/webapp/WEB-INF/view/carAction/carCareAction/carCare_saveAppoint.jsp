<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cqu" uri="//WEB-INF/tlds/cqu.tld" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
	<link href="skins/main.css" rel="stylesheet" type="text/css">
</head>
<body class="minW">
	<div class="space">
		<!-- 标题 -->
        <div class="title">
            <h1>保养预约登记</h1>
        </div>        
		<div class="tab_next style2">
			<table>
				<tr>
				    <td class="on"><a href="#"><span>预约车辆保养</span></a></td>
					<td><s:a action="carCare_list"><span>车辆保养记录</span></s:a></td>
				</tr>
			</table>
		</div>		
            <p style="color: red">
				<s:if test="hasFieldErrors()">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<s:property />
						</s:iterator>
					</s:iterator>
				</s:if>
			</p>
		<!--显示表单内容-->
		<div class="editBlock">
		<s:form action="carCare_%{id == null ? 'save' : 'edit'}Appointment" id="pageForm">
        	<s:hidden name="id"></s:hidden>
            <table>
            	<colgroup>
					<col width="80"></col>
					<col></col>
					<col></col>
					<col width="120"></col>
				</colgroup>
            	<tbody>
                	<tr>
						<th><s:property value="tr.getText('car.CarCare.car')" /><span class="required">*</span></th>
						<td>
							<cqu:carSelector name="car" synchDriver="driver"/>
						</td>
                    </tr>
                    <tr>
						<th><s:property value="tr.getText('car.CarCare.driver')" /><span class="required">*</span></th>
						<td>
							<cqu:userSelector name="driver"/>
						</td>
					</tr>
                    <tr>
                    	<th><s:property value="tr.getText('car.CarCare.date')" /><span class="required">*</span></th>
						<td>
							<s:textfield cssClass="inputText" name="date" id="date" class="Wdate half" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
						</td>
                    </tr>
                    <s:if test="id!=null">
					<tr>
						<th><s:property value="tr.getText('car.CarCare.done')" /></th>
						<td>
							<s:checkbox class="m10" id="done" name="done"/>
						</td>
					</tr>
					</s:if>
				</tbody>
				<tfoot>
					<tr>
                        <td colspan="2">
                        	<input class="inputButton" type="submit" value="确定" />
                        	<a class="p15" href="javascript:history.go(-1);">返回</a>
                        </td>
                    </tr>
                    <tr>
                    	<td>
                    		<input type="hidden" name="mileInterval" value="5000">
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
    <script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
    <script type="text/javascript" src="js/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="js/validate/messages_cn.js"></script>
    <script type="text/javascript">
	    $(function(){
			$("#pageForm").validate({
				submitout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					carLabel:{
						required:true,
					},
					driverLabel:{
						required:true,
					},
					date:{
						required:true,
					},
				}
			});
			formatDateField2($("#date"));
		});
    </script>
</body>
</html>