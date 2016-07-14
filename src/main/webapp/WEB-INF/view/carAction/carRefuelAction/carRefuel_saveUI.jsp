<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
	<link href="skins/main.css" rel="stylesheet" type="text/css" />
</head>
<body class="minW">
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>加油信息</h1>
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
        <s:form action="carRefuel_%{id == null ? 'add' : 'edit'}" id="pageForm">
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
                        <th><s:property value="tr.getText('car.CarRefuel.sn')" /></th>
						<td>
							<input class="inputText" type="text" name="sn"/>
						</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('car.Car.plateNumber')" /><span class="required">*</span></th>
						<td>
							<s:textfield id="car_platenumber" cssClass="carSelector inputText inputChoose" onfocus="this.blur();" 
								name="car.plateNumber" type="text"  synchDriverName="driverName" synchDriverId="driverId"/>
						</td>
                    </tr>
                    <tr>
						<th><s:property value="tr.getText('car.CarRefuel.driver')" /><span class="required">*</span></th>
						<td>
							<s:textfield class="userSelector inputText inputChoose" id="driverName" name="driver.name" type="text" driverOnly="true"/>
							<s:textfield id="driverId" name="driver.id" type="hidden"/>
						</td>
					</tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarRefuel.date')" /><span class="required">*</span></th>
						<td>
							<input name="date" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
						</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarRefuel.volume')" />(L)</th>
                        <td>
                        	<input class="inputText" type="text" name="volume"/>
						</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarRefuel.money')" />(元)<span class="required">*</span></th>
                        <td>
                        	<input class="inputText" type="text" name="money"/>
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
					'car.plateNumber':{
						required:true,
					},
					'driver.name':{
						required:true,
					},
					date:{
						required:true,
					},
					money:{
						required:true,
						digits:true,
						min:0,
					},
				}
			});
		});
    </script>
</body>
</html>
