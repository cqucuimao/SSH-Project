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
            <h1>订单信息</h1>
        </div>
        <div class="editBlock detail p30">
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
                    	<th>为他人叫车：</th>
                    	<td><s:if test="callForOther">是</s:if><s:else>否</s:else></td>
                    	<td>乘车人信息：</td>
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
                        <td>${fromAddress }（${fromAddress.detail }）</td>
                        <th>目的地：</th>
                        <td>
                        	<s:if test="toAddress!=null">
                        		${toAddress }（${toAddress.detail }）
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
                        <th>订单里程：</th>
                        <td>${orderMile}KM
                        	<s:if test="chargeMode==@com.yuqincar.domain.order.ChargeModeEnum@MILE">
                        		&nbsp;&nbsp;&nbsp;<s:a action="order_modifyMile?orderId=%{id}">[修改]</s:a>
                        	</s:if>
                        </td>
                    </tr>
                    <tr>
                        <th>实际金额：</th>
                        <td>${actualMoneyString}元</td>
                        <th>订单金额：</th>
                        <td>${orderMoneyString}元
                        	<s:if test="chargeMode!=@com.yuqincar.domain.order.ChargeModeEnum@MILE">
                        		&nbsp;&nbsp;&nbsp;<s:a action="order_modifyMoney?orderId=%{id}">[修改]</s:a>
                        	</s:if>
                        </td>
                    </tr>
                    <tr>
                        <th>备注：</th>
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
          	<table>
                <tfoot>                    
                    <tr>
                        <th></th>
                        <td></td>
                        <th></th>
                        <td></td>
                    </tr>
                    <tr>
                        <td colspan="4">
                        	<form id="myForm">
                        		<s:if test="canUpdateOrder">
                        			<s:a action="schedule_updateOrder?scheduleFromUpdateOrderId=%{id}">
                        				<input type="button" class="inputButton" value="修改订单"/>
                        			</s:a>
                        		</s:if>
                        		<s:if test="canPushBackOrder">
                        			<s:a action="order_postpone?orderId=%{id}">
                        				<input type="button" class="inputButton" value="延后订单"/>
                        			</s:a>
                        		</s:if>
                        		<s:if test="canRescheduleOrder">
                        			<s:a action="order_reschedule?orderId=%{id}">
                        				<input type="button" class="inputButton" value="重新调度"/>
                        			</s:a>
                        		</s:if>
                        		<s:if test="canCancelOrder">
                        			<s:a action="order_cancel?orderId=%{id}">
                        				<input type="button" class="inputButton" value="取消订单"/>
                        			</s:a>
                        		</s:if>
                            	<a class="p15" href="javascript:history.go(-1);">返回</a>
                            </form>
                        </td>
                    </tr>
                </tfoot>
            </table>
          </div>
    </div>
    <script type="text/javascript" src="<%=basePath %>js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>js/common.js"></script>
</body>
</html>
