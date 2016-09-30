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
</head>
<body class="minW">
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>订单信息</h1>
        </div>
        <div class="editBlock detail p30">
            <table>
                <tbody>
                    <tr>
                        <th width="15%"><s:property value="tr.getText('order.Order.sn')" />：</th>
                        <td width="35%">${sn }</td>
                        <th width="15%"><s:property value="tr.getText('order.Order.customerOrganization')" />：</th>
                        <td width="35%">${customerOrganization.name }</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('order.Order.customer')" />：</th>
                        <td>${customer.name}</td>
                        <th><s:property value="tr.getText('order.Order.phone')" />：</th>
                        <td>${phone }</td>
                    </tr>
                    <tr>
                    	<th><s:property value="tr.getText('order.Order.callForOther')" />：</th>
                    	<td><s:if test="callForOther">是</s:if><s:else>否</s:else></td>
                    	<td><s:property value="tr.getText('order.Order.otherPassengerName')" />：</td>
                    	<td>
                    		<s:if test="callForOther">
                    			${otherPassengerName}（${otherPhoneNumber}，<s:if test="callForOtherSendSMS">发送提醒短信</s:if><s:else>不发送提醒短信</s:else>）
							</s:if>
							<s:else>
								&nbsp;
							</s:else>
						</td>
                    </tr>
                    <tr>
                    	<th><s:property value="tr.getText('order.Order.chargeMode')" />：</th>
                    	<td>${chargeMode.label }</td>
                    	<th><s:property value="tr.getText('order.Order.serviceType')" />：</th>
                        <td>${serviceType.title }</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('order.Order.car')" />：</th>
                        <td>
                        	<s:if test="car!=null">
                        		${car.plateNumber }
                        	</s:if>
						</td>
                        <th><s:property value="tr.getText('order.Order.driver')" />：</th>
                        <td>
                        	<s:if test="driver!=null">
                        		${driver.name }（${driver.phoneNumber}）
                        	</s:if>
                        </td>
                    </tr> 
                    <tr>    
                        <th><s:property value="tr.getText('order.Order.saler')" />：</th>
                        <td>
                        	${saler.name }
                        </td>                    
                        <th><s:property value="tr.getText('order.Order.status')" />：</th>
                        <td>${status.label }</td>
                    </tr> 
                    <tr>
                        <th>计划起止时间：</th>
                        <td>${planDateString }</td>
                        <th>实际起止时间：</th>
                        <td>${actualDateString }</td>
                    </tr>                    
                    <tr>
                        <th><s:property value="tr.getText('order.Order.fromAddress')" />：</th>
                        <td>${fromAddress }</td>
                        <th><s:property value="tr.getText('order.Order.toAddress')" />：</th>
                        <td>
                        	<s:if test="toAddress!=null">
                        		${toAddress }
                        	</s:if>
                        </td>
                    </tr>                  
                    <tr>
                        <th><s:property value="tr.getText('order.Order.queueTime')" />：</th>
                        <td>
                        	<s:if test="queueTime!=null">
                        		<s:date name="queueTime" format="yyyy-MM-dd HH:mm:ss"/>
                        	</s:if>
                        </td>
                        <th><s:property value="tr.getText('order.Order.scheduleTime')" />：</th>
                        <td>
                        	<s:if test="scheduleTime!=null">
                        		<s:date name="scheduleTime" format="yyyy-MM-dd HH:mm:ss"/>
                        	</s:if>
                        </td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('order.Order.scheduler')" />：</th>
                        <td>${scheduler.name}</td>
                        <th><s:property value="tr.getText('order.Order.acceptedTime')" />：</th>
                        <td>
                        	<s:if test="acceptedTime!=null">
                        		<s:date name="acceptedTime" format="yyyy-MM-dd HH:mm:ss"/>
                        	</s:if>
                        </td>
                    </tr>
                    <tr>
                        <th>实际路码：</th>
                        <td>${endMile-beginMile}KM</td>
                        <th><s:property value="tr.getText('order.Order.totalChargeMile')" />：</th>
                        <td>${totalChargeMile}KM</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('order.Order.orderMoney')" />：</th>
                        <td>${orderMoney}元</td>
                        <th><s:property value="tr.getText('order.Order.actualMoney')" />：</th>
                        <td>${actualMoney}元</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('order.Order.memo')" />：</th>
                        <td>${memo}</td>
                        <th></th>
                        <td></td>
                    </tr>
                </tbody>
             </table>
             <s:iterator value="operationRecord" status="status">
             	<div class="title">
             		<br/>
            		<h2>第<s:property value="#status.index+1"/>条操作记录</h2>
        		</div>
             	<table>
             		<tbody>
             			<tr>
             				<th width="15%">操作类型</th>
             				<td>${typeString }</td>
             			</tr>
             			<tr>
             				<th>操作日期</th>
             				<td><s:date name="date" format="yyyy-MM-dd HH:mm:ss"/></td>
             			</tr>
             			<tr>
             				<th>操作人</th>
             				<td>${user.name }</td>
             			</tr>
             			<tr>
             				<th>操作内容</th>
             				<td>${description }</td>
             			</tr>
             		</tbody>
             	</table>
             </s:iterator>
             </div>
             
		<div class="editBlock search">
          	<table style="margin-left:15px">
          			<tr>
	          			<td></td>
	          			<td></td>
	          			<td></td>
	          			<td></td>
	          			<td></td>
          			</tr>
                    <tr>
                        <td height="50px">
                        		<s:if test="canUpdateOrder">
                        			<s:a cssClass="buttonB" action="schedule_updateOrder?scheduleFromUpdateOrderId=%{id}">修改订单</s:a>
                        		</s:if>
                        </td>
                        <td>
                        		<s:if test="canPushBackOrder">
                        			<s:a cssClass="buttonB" action="order_postpone?orderId=%{id}">延后订单</s:a>
                        		</s:if>
                        </td>
                        <td>
                        		<s:if test="canRescheduleOrder">
                        			<s:a cssClass="buttonB" action="order_reschedule?orderId=%{id}">重新调度</s:a>
                        		</s:if>
                        </td>
                        <td>
                        		<s:if test="canCancelOrder">
                        			<s:a cssClass="buttonB" action="order_cancel?orderId=%{id}">取消订单</s:a>
                        		</s:if>
                        </td>
                        <td>
                            	<a class="p15" href="javascript:history.go(-1);">返回</a>
                           
                        </td>
                    </tr>
            </table>
            </div>
    </div>
    <script type="text/javascript" src="<%=basePath %>js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>js/common.js"></script>
</body>
</html>
