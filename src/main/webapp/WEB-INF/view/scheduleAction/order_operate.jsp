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
            <h1>编辑司机动作</h1>
        </div>
        <div class="editBlock detail p30">
            <table border="1">
                <tbody>
                    <tr>
                        <th width="15%">订单号：</th>
                        <td width="35%">${sn }</td>
                        <th width="15%">用车单位：</th>
                        <td width="35%">${customerOrganization.abbreviation }</td>
                    </tr>
                    <tr>
                        <th>联系电话：</th>
                        <td>${customer.name}：${phone }</td>
                        <th>定车时间：</th>
                    	<td>${planDateString }</td>
                    </tr>
                    <tr>               	
                        <th>上车地点：</th>
                        <td>${fromAddress }</td>
                        <th>车型：</th>
                        <td>${serviceType.title }</td>
                    </tr>
                    <tr>               	
                    	<th>车牌号：</th>
                        <td>
                        	<s:if test="car!=null">
                        		${car.plateNumber }
                        	</s:if>
						</td>
						<th>驾驶员/电话：</th>
                        <td>
                        	<s:if test="driver!=null">
                        		${driver.name }：${driver.phoneNumber}
                        	</s:if>
                        </td> 
                    </tr>
                    <tr>    
                        <th>目的地：</th>
                        <td>
                        	<s:if test="toAddress!=null">
                        		${toAddress }（${toAddress.detail }）
                        	</s:if>
                        </td>
                        <td></td><td></td>
                    </tr>            
                </tbody>
             </table>
             <table>
             	<tbody>
             		<s:iterator value="driverActionVOList">
             		<tr>
             			<td>${status }</td>
             			<td>${date }</td>
             		</tr>
             		</s:iterator>
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
                        		<s:if test="canAddAcceptAction">
                        			<s:a action="order_addAcceptAction?scheduleFromUpdateOrderId=%{id}">
                        				<input type="button" class="inputButton" value="接受订单"/>
                        			</s:a>
                        		</s:if>  
                        		<s:if test="canAddBeginAction">                      		
                        			<s:a action="order_addBeginAction?orderId=%{id}">
                        				<input type="button" class="inputButton" value="开始订单"/>
                        			</s:a>
                        		</s:if>
                        		<s:if test="canAddGetonAction"> 
                        			<s:a action="order_addGetonAction?orderId=%{id}">
                        				<input type="button" class="inputButton" value="客户上车"/>
                        			</s:a>
                        		</s:if>
                        		<s:if test="canAddGetoffAction"> 
                        			<s:a action="order_addGetoffAction?orderId=%{id}">
                        				<input type="button" class="inputButton" value="客户下车"/>
                        			</s:a>
                        		</s:if>
                        		<s:if test="canAddEndAction"> 
                        			<s:a action="order_addEndAction?orderId=%{id}">
                        				<input type="button" class="inputButton" value="结束订单"/>
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
