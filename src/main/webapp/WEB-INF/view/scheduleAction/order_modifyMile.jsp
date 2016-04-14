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
        <!-- 标题 -->
        <div class="title">
            <h1>修改订单里程</h1>
        </div>
        <div class="editBlock detail p30">
        	<s:form action="order_modifyMileDo" id="myForm">
        	<s:hidden name="orderId"/>
            <table>
                <tbody>
                    <tr>
                        <th width="15%">订单号：</th>
                        <td width="35%">${sn }</td>
                        <th width="15%">单位：</th>
                        <td width="35%">${customerOrganization.abbreviation }</td>
                    </tr>
                    <tr>
                        <th>姓名：</th>
                        <td>${customer.name}</td>
                        <th>联系方式：</th>
                        <td>${phone }</td>
                    </tr>
                    <tr>
                    	<th>计费方式</th>
                    	<td>${chargeModeString }</td>
                        <th>用车人数：</th>
                        <td>${passengerNumber }人</td>
                    </tr>
                    <tr>
                        <th>车型：</th>
                        <td>${serviceType.title }</td>
                        <th>订单状态：</th>
                        <td>${statusString }</td>
                    </tr>
                    <tr>
                        <th>计划起止时间：</th>
                        <td>${planDateString }</td>
                        <th>实际起止时间：</th>
                        <td>${actualDateString }</td>
                    </tr>                    
                    <tr>
                        <th>始发地：</th>
                        <td>${fromAddress.description }（${fromAddress.detail }）</td>
                        <th>目的地：</th>
                        <td>
                        	<s:if test="toAddress!=null">
                        		${toAddress.description }（${toAddress.detail }）
                        	</s:if>
                        </td>
                    </tr>
                    <tr>
                        <th>司机：</th>
                        <td>
                        	<s:if test="driver!=null">
                        		${driver.name }（${driver.phoneNumber}）
                        	</s:if>
                        </td>
                        <th>车牌：</th>
                        <td>
                        	<s:if test="car!=null">
                        		${car.plateNumber }
                        	</s:if>
						</td>
                    </tr>
                    <tr>
                        <th>进队列时间：</th>
                        <td><s:date name="queueTime" format="yyyy-MM-dd HH:mm:ss"/></td>
                        <th>调度时间：</th>
                        <td><s:date name="scheduleTime" format="yyyy-MM-dd HH:mm:ss"/></td>
                    </tr>
                    <tr>
                        <th>调度员：</th>
                        <td>${scheduler.name}</td>
                        <th>接受时间：</th>
                        <td><s:date name="acceptedTime" format="yyyy-MM-dd HH:mm:ss"/></td>
                    </tr>
                    <tr>
                        <th>实际里程：</th>
                        <td>${actualMile }KM</td>
                        <th>订单里程：<span class="required">*</span></th>
                        <td>
                        	<s:textfield name="orderMileString" type="text" class="inputText" style="width: 60px;"/>&nbsp;KM
                        </td>
                    </tr>
                    <tr>
                        <th>实际金额：</th>
                        <td>${actualMoneyString}元</td>
                        <th>订单金额：</th>
                        <td>${orderMoneyString}元</td>
                    </tr>
                    <tr>
                        <th>备注：</th>
                        <td>${memo}</td>
                        <th></th>
                        <td></td>
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
                        	<s:submit value="确定修改" class="inputButton"></s:submit>
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
		$("#myForm").validate({
			onfocusout: function(element) { $(element).valid(); },
			rules:{				
				orderMileString:{
					required:true,
					number:true
				},
			}
		});
	});
    </script>
</body>
</html>
