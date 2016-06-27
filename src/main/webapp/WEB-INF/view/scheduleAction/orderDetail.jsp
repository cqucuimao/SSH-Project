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
                    <th width="70">订单号：</th>
                    <td>${sn }</td>
                </tr>
                <tr>
                    <th width="70">状态：</th>
                    <td>${status.string }</td>
                </tr>
                <tr>
                    <th width="70">单位：</th>
                    <td>${customerOrganization.name }</td>
                </tr>
                <tr>
                    <th>姓名：</th>
                    <td>${customer.name }</td>
                </tr>
                <s:if test="callForOther">
                <tr>
                	<th>实际乘车人：</th>
                	<td>${otherPassengerName}（${otherPhoneNumber}）</td>
                </tr>
                </s:if>
                <tr>
                    <th>联系方式：</th>
                    <td>${phone }</td>
                </tr>
                <tr>
                	<th>计费方式：</th>
                	<td>${chargeModeString}</td>
                </tr>
                <tr>
                    <th>用车人数：</th>
                    <td>${passengerNumber }</td>
                </tr>
                <tr>
                    <th>车型：</th>
                    <td>${serviceType.title }</td>
                </tr>
                <tr>
                    <th><s:property value="tr.getText('order.Order.planBeginDate')"/>：</th>
                    <td><s:date name="planBeginDate" format="yyyy-MM-dd"/></td>
                </tr>
                <tr>
                    <th>始发地：</th>
                    <td>${fromAddress}&nbsp;</td>
                </tr>
                <tr>
                    <th>目的地：</th>
                    <td>${toAddress}&nbsp;</td>
                </tr>                
                <tr>
                    <th>车牌：</th>
                    <td>${car.plateNumber}</td>
                </tr>
                <tr>
                    <th>司机：</th>
                    <td>${driver.name }</td>
                </tr>
                <tr>
                    <th>备注：</th>
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
