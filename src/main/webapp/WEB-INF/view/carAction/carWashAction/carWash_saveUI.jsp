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
            <h1>洗车登记信息</h1>
            <!-- 检验错误信息提示 -->
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
        <s:form action="carWash_%{id == null ? 'save' : 'edit'}" id="pageForm">
        	<s:hidden name="id"></s:hidden>
            <table>
            	<colgroup>
					<col width="80"></col>
					<col></col>
					<col></col>
					<col></col>
					<col></col>
					<col></col>
					<col></col>
					<col></col>
					<col width="120"></col>
				</colgroup>
                <tbody>
                	<tr>
                        <th>车辆<span class="required">*</span></th>
						<td>
							<s:textfield id="car_platenumber" cssClass="carSelector inputText inputChoose" onfocus="this.blur();" name="car.plateNumber"
									 type="text" synchDriverName="driver" synchDriverId="driverId"/>
						</td>
                    </tr>
                	<tr>
                        <th>司机<span class="required">*</span></th>
                        <td><s:textfield class="userSelector inputText" id="driver" type="text" name="driver.name" driverOnly="true"/>
							<s:textfield id="driverId" name="driver.id" type="hidden" /></td>
					<td>
                    </tr>
                    <tr>
                        <th>洗车日期<span class="required">*</span></th>
                        <td>
						<s:textfield name="date" id="date" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</td>
                    </tr>
                    <tr>
                    <th>洗车点<span class="required">*</span></th>
						<td>
						    <s:select name="shop.id" cssClass="SelectStyle"
                        		list="carWashShopList" listKey="id" listValue="name"
                        		headerKey="" headerValue="选择洗车点"/>
                        		
						</td>
                    </tr>
                    <tr>
                        <th>金额<span class="required">*</span></th>
                        <td>
                        	<s:textfield id="money" cssClass="inputText" name="money"/>
                        </td>
                    </tr>
                    
                    <tr>
                        <th>内饰清洁金额</th>
                        <td>
                        	<s:textfield id="innerCleanMoney" cssClass="inputText" name="innerCleanMoney"/>
                        </td>
                    </tr>
                    
                    <tr>
                        <th>抛光打蜡金额</th>
                        <td>
                        	<s:textfield id="polishingMoney" cssClass="inputText" name="polishingMoney"/>
                        </td>
                    </tr>
                    
                    <tr>
                        <th>清洗发动机金额</th>
                        <td>
                        	<s:textfield id="engineCleanMoney" cssClass="inputText" name="engineCleanMoney"/>
                        </td>
                    </tr>
                    
                    <tr>
                        <th>座套清洗金额</th>
                        <td>
                        	<s:textfield id="cushionCleanMoney" cssClass="inputText" name="cushionCleanMoney"/>
                        </td>
                    </tr>
                    
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                        	<input class="inputButton" type="submit" value="提交" />
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
				submitout: function(element) { $(element).valid(); },
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
						number:true,
						min:0
					},
					innerCleanMoney:{
						number:true,
						min:0
					},
					polishingMoney:{
						number:true,
						min:0
					},
					engineCleanMoney:{
						number:true,
						min:0
					},
					cushionCleanMoney:{
						number:true,
						min:0
					},
					'shop.id':{
						required:true,
					}
				}
			});
			formatDateField1($("#date"));
		});
    </script>
</body>
</html>
