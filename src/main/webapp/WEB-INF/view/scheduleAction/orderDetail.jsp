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
${order }
<body class="p20 pt5">
    <div class="editBlock detailM">
    	<s:iterator  value="orderList" status="iteratorStatus">
        <table>
            <tbody>
            	<s:if test="#orderList.size>1">
            	<tr>
            		<th colspan="2" align="center"><h2>第<s:property value="#iteratorStatus.index+1"/>个订单</h2></th>
            	</tr>
            	</s:if>
                <tr>
                    <th width="70"><s:property value="tr.getText('order.Order.sn')" />：</th>
                    <td>${sn }</td>
                </tr>
                <tr>
                    <th width="70"><s:property value="tr.getText('order.Order.status')" />：</th>
                    <td>${status.string }</td>
                </tr>
                <tr>
                    <th width="70"><s:property value="tr.getText('order.Order.customerOrganization')" />：</th>
                    <td>${customerOrganization.name }</td>
                </tr>
                <tr>
                    <th><s:property value="tr.getText('order.Order.customer')" />：</th>
                    <td>${customer.name }</td>
                </tr>
                <s:if test="callForOther">
                <tr>
                	<th><s:property value="tr.getText('order.Order.otherPassengerName')" />：</th>
                	<td>${otherPassengerName}（${otherPhoneNumber}）</td>
                </tr>
                </s:if>
                <tr>
                    <th><s:property value="tr.getText('order.Order.phone')" />：</th>
                    <td>${phone }</td>
                </tr>
                <tr>
                	<th><s:property value="tr.getText('order.Order.chargeMode')" />：</th>
                	<td>${chargeModeString}</td>
                </tr>
                <tr>
                    <th><s:property value="tr.getText('order.Order.serviceType')" />：</th>
                    <td>${serviceType.title }</td>
                </tr>
                <tr>
                    <th><s:property value="tr.getText('order.Order.planBeginDate')"/>：</th>
                    <td><s:date name="planBeginDate" format="yyyy-MM-dd"/></td>
                </tr>
                <tr>
                    <th><s:property value="tr.getText('order.Order.fromAddress')" />：</th>
                    <td>${fromAddress}&nbsp;</td>
                </tr>
                <tr>
                    <th><s:property value="tr.getText('order.Order.toAddress')" />：</th>
                    <td>${toAddress}&nbsp;</td>
                </tr>                
                <tr>
                    <th><s:property value="tr.getText('order.Order.car')" />：</th>
                    <td>${car.plateNumber}</td>
                </tr>
                <tr>
                    <th><s:property value="tr.getText('order.Order.driver')" />：</th>
                    <td>${driver.name }</td>
                </tr>
                <tr>
                    <th><s:property value="tr.getText('order.Order.saler')" />：</th>
                    <td>${saler.name }</td>
                </tr>
                <tr>
                    <th><s:property value="tr.getText('order.Order.memo')" />：</th>
                    <td>${memo }</td>
                </tr>
            </tbody>
        </table>
        <s:if test="!#iteratorStatus.last">
        	</br></br>
        </s:if>
        </s:iterator>
        <table>        	
            <tfoot>
                <tr>
                    <td colspan="2">
                        <div class="bottomBar borderT alignCenter"><input class="inputButton" type="button" value="关闭" onclick="popdown('orderDetail');" /></div>
                    </td>
                </tr>
            </tfoot>
        </table>
    </div>
    <script type="text/javascript" src="<%=basePath %>js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>js/common.js"></script>
    <script type="text/javascript">
        $(function(){
            
        })
    </script>
</body>
</html>
