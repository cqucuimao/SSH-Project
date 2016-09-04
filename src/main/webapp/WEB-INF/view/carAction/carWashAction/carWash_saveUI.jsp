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
                        <th><s:property value="tr.getText('car.CarWash.car')" /><span class="required">*</span></th>
						<td>
						<s:if test=" #session.user.hasPrivilegeByUrl('/carWash_editNormalInfo') ">
							<cqu:carSelector name="car" synchDriver="driver"/>
						</s:if>
						<s:else>
						<cqu:carSelector name="car" synchDriver="driver" readonly="true"/>
						</s:else>
						</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('car.CarWash.driver')" /><span class="required">*</span></th>
                        <td>
                        <s:if test=" #session.user.hasPrivilegeByUrl('/carWash_editNormalInfo') ">
                        	<cqu:userSelector name="driver" />
                        </s:if>
                        <s:else>
                        <cqu:userSelector name="driver" readonly="true"/>
                        </s:else>
                        	
                        </td>
					<td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarWash.date')" /><span class="required">*</span></th>
                        <td>
                        <s:if test=" #session.user.hasPrivilegeByUrl('/carWash_editKeyInfo') ">
						<s:textfield name="date" id="date" class="Wdate half" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					     </s:if>
					     <s:else>
					     <s:textfield readonly="true" name="date" id="date" class="Wdate half" type="text" />
					     </s:else>
					</td>
                    </tr>
                    <tr>
                    <th><s:property value="tr.getText('car.CarWash.shop')" /><span class="required">*</span></th>
						<td>
						<s:if test=" #session.user.hasPrivilegeByUrl('/carWash_editNormalInfo') ">
						    <s:select name="shop.id" cssClass="SelectStyle"
                        		list="carWashShopList" listKey="id" listValue="name"
                        		headerKey="" headerValue="选择洗车点"/>
                        </s:if>
                        <s:else>
                         <s:select id="select_list" name="shop.id" cssClass="SelectStyle" 
                        		list="carWashShopList" listKey="id" listValue="name"
                        		headerKey="" headerValue="选择洗车点" />
                        </s:else>		
						</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.CarWash.money')" /><span class="required">*</span></th>
                        <td>
                        	<s:if test=" #session.user.hasPrivilegeByUrl('/carWash_editKeyInfo') ">
                        	<s:textfield id="money" cssClass="inputText" name="money"/>
                        	</s:if>
                        	<s:else>
                        	<s:textfield id="money" readonly="true" cssClass="inputText" name="money"/>
                        	</s:else>
                        	
                        </td>
                    </tr>
                    
                    <tr>
                        <th><s:property value="tr.getText('car.CarWash.innerCleanMoney')" /></th>
                        <td>
                        <s:if test=" #session.user.hasPrivilegeByUrl('/carWash_editKeyInfo') ">
                        	<s:textfield id="innerCleanMoney" cssClass="inputText" name="innerCleanMoney"/>
                        	</s:if>
                        	<s:else>
                        	<s:textfield id="innerCleanMoney" readonly="true" cssClass="inputText" name="innerCleanMoney"/>
                        	</s:else>
                        	
                        </td>
                    </tr>
                    
                    <tr>
                        <th><s:property value="tr.getText('car.CarWash.polishingMoney')" /></th>
                        <td>
                        <s:if test=" #session.user.hasPrivilegeByUrl('/carWash_editKeyInfo') ">
                        	<s:textfield id="polishingMoney" cssClass="inputText" name="polishingMoney"/>
                        	</s:if>
                        	<s:else>
                        	<s:textfield id="polishingMoney" readonly="true" cssClass="inputText" name="polishingMoney"/>
                        	</s:else>
                        </td>
                    </tr>
                    
                    <tr>
                        <th><s:property value="tr.getText('car.CarWash.engineCleanMoney')" /></th>
                        <td>
                        <s:if test=" #session.user.hasPrivilegeByUrl('/carWash_editKeyInfo') ">
                        	<s:textfield id="engineCleanMoney" cssClass="inputText" name="engineCleanMoney"/>
                        	</s:if>
                        	<s:else>
                        	<s:textfield id="engineCleanMoney" readonly="true" cssClass="inputText" name="engineCleanMoney"/>
                        	</s:else>
                        </td>
                    </tr>
                    
                    <tr>
                        <th><s:property value="tr.getText('car.CarWash.cushionCleanMoney')" /></th>
                        <td>
                        <s:if test="#session.user.hasPrivilegeByUrl('/carWash_editKeyInfo') ">
                        	<s:textfield id="cushionCleanMoney" cssClass="inputText" name="cushionCleanMoney"/>
                        	</s:if>
                        	<s:else>
                        	<s:textfield id="cushionCleanMoney" readonly="true" cssClass="inputText" name="cushionCleanMoney"/>
                        	</s:else>
                        	
                        </td>
                    </tr>
                    
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                        	<input class="inputButton" id="submit" type="submit" value="提交" />
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
	    	$("#select_list").attr("disabled", "disabled"); 
	    	$("#submit").click(function(){
	    		$("#select_list").removeAttr("disabled"); 
	    	});
	    	
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
