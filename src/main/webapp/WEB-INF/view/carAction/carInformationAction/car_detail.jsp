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
            <h1>车辆详细信息</h1>
        </div>
        <div class="editBlock detail p30">
            <table>
                <tbody>
                    <tr>
                        <th width="15%"><s:property value="tr.getText('car.Car.plateNumber')" />：</th>
                        <td width="35%" id="carId">${plateNumber}</td>
                        <th width="15%"><s:property value="tr.getText('car.CarServiceType.title')" />：</th>
                        <td width="35%">${serviceType.title}</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.Car.brand')" />：</th>
                        <td>${brand}</td>
                        <th><s:property value="tr.getText('car.Car.model')" />：</th>
                        <td>${model }</td>
                    </tr>
                    
                    <tr>
                        <th><s:property value="tr.getText('car.Car.VIN')" />：</th>
                        <td>${VIN}</td>
                        <th><s:property value="tr.getText('car.Car.EngineSN')" />：</th>
                        <td>${EngineSN }</td>
                    </tr>
                    
                    <tr>
                        <th><s:property value="tr.getText('car.Car.tollChargeSN')" />：</th>
                        <td>${tollChargeSN}</td>
                        <th><s:property value="tr.getText('car.Car.plateType')" />：</th>
                        <td>${plateType.label}</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.Car.seatNumber')" />：</th>
                        <td>${seatNumber}</td>
                        <th><s:property value="tr.getText('car.Car.transmissionType')" />：</th>
                        <td>${transmissionType.label}</td>
                    </tr>
                    
                    <tr>
                        <th><s:property value="tr.getText('car.Car.registDate')" />：</th>
                        <td>
                        <s:date name="registDate" format="yyyy-MM-dd "/>
                        </td>
                        <th><s:property value="tr.getText('car.Car.enrollDate')" />：</th>
                        <td>
                        <s:date name="enrollDate" format="yyyy-MM-dd "/>
                        </td>
                    </tr>
                   <tr>
                        <th><s:property value="tr.getText('car.Car.driver')" />：</th>
                        <td>${driver.name}</td>
                        <th><s:property value="tr.getText('car.Car.servicePoint')" />：</th>
                        <td>${servicePoint.name }</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.Car.standbyCar')" />：</th>
                        <td>
                        <s:if test="standbyCar">是</s:if><s:else>否</s:else>
                        </td>
                        <th><s:property value="tr.getText('car.Car.standingGarage')" />：</th>
                        <td>
						<s:if test="standingGarage">是</s:if><s:else>否</s:else>
						</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.Car.status')" />：</th>
                        <td>${status.label }</td>
                        <th><s:property value="tr.getText('car.Car.mileage')" />：</th>
                        <td>${mileage}</td>
                    </tr>
                    <tr>
                    	<th>&nbsp;</th>
                    	<td>&nbsp;</td>
                    	<th>&nbsp;</th>
                    	<td>&nbsp;</td>
                    </tr>
                    <!-- 车辆运行情况 **********************************-->
                    <tr>
                        <th>保险到期：</th>
						<td>
							<s:if test="insuranceExpiredDate==null">< 未知日期 ></s:if>
							<s:else><s:date name="insuranceExpiredDate" format="yyyy-MM-dd"/></s:else>
						   	<s:if test="insuranceExpired"><font color="red">（脱保）</font></s:if>
						   	&nbsp;
						   	<s:a action="carInsurance_list?carId=%{id}" >
						   		 <img width=16 height=16 src="skins/images/info.jpg" title="保险记录"/>
						   	</s:a>
						</td>						
                        <th>&nbsp;</th>
                        <td>&nbsp;</td>
                    </tr>         
                    <tr>
                    	<th>年审到期：</th>
						<td>
							<s:if test="nextExaminateDate==null">< 未知日期 ></s:if>
							<s:else><s:date name="nextExaminateDate" format="yyyy-MM-dd"/></s:else>
						   	<s:if test="examineExpired"><font color="red">（过期）</font></s:if>
						   	&nbsp;
						   	<s:if test="unDoneAppointExamine!=null">
						   		<font color="red">预约（<s:date name="unDoneAppointExamine.date" format="yyyy-MM-dd"/>）</font>
						   		&nbsp;
						   	</s:if>
						   	<s:a action="carExamine_list?carId=%{id}" >
						   		 <img width=16 height=16 src="skins/images/info.jpg" title="年审记录"/>
						   	</s:a>
						</td>						
                        <th>&nbsp;</th>
                        <td>&nbsp;</td>
                    </tr>   
                    <tr>
                    	<th>路桥费到期：</th>
						<td>
							<s:if test="nextTollChargeDate==null">< 未知日期 ></s:if>
							<s:else><s:date name="nextTollChargeDate" format="yyyy-MM-dd"/></s:else>
						   	<s:if test="tollChargeExpired"><font color="red">（过期）</font></s:if>
						   	&nbsp;
						   	<s:a action="tollCharge_list?carId=%{id}" >
						   		 <img width=16 height=16 src="skins/images/info.jpg" title="路桥费记录"/>
						   	</s:a>
						</td>						
                        <th>&nbsp;</th>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                    	<th>保养到期：</th>
						<td>
							<s:if test="nextCareMile==0">< 未知里程 ></s:if>
							<s:else>${nextCareMile}</s:else>
						   	<s:if test="careExpired"><font color="red">（过期）</font></s:if>
						   	&nbsp;
						   	<s:if test="unDoneAppointCare!=null">
						   		<font color="red">预约（<s:date name="unDoneAppointCare.date" format="yyyy-MM-dd"/>）</font>
						   		&nbsp;
						   	</s:if>
						   	<s:a action="carCare_list?carId=%{id}" >
						   		 <img width=16 height=16 src="skins/images/info.jpg" title="保养记录"/>
						   	</s:a>
						</td>						
                        <th>&nbsp;</th>
                        <td>&nbsp;</td>
                    </tr>
                   	<tr>                    
                        <th>违章情况：</th>
                        <td>
                        	<s:if test="violationExist"><font color="red">有</font></s:if><s:else>无</s:else>
                        	&nbsp;
                        	<s:a action="carViolation_list?carId=%{id}">
						   		 <img width=16 height=16 src="skins/images/info.jpg" title="违章记录"/>
						  	</s:a>
                        </td>			
                        <th>&nbsp;</th>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <th>维修情况：</th>
                        <td>
						   	<s:if test="unDoneAppointRepair!=null">
						   		<font color="red">预约（<s:date name="unDoneAppointRepair.fromDate" format="yyyy-MM-dd"/> 到 <s:date name="unDoneAppointRepair.toDate" format="yyyy-MM-dd"/>）</font>
						   		&nbsp;
						   	</s:if>
						   	<s:a action="carRepair_list?carId=%{id}" >
						   		 <img width=16 height=16 src="skins/images/info.jpg" title="维修记录"/>
						   	</s:a>
                        </td>			
                        <th>&nbsp;</th>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <th>加油情况：</th>
						<td>
                        	<s:a action="carRefuel_list?carId=%{id} " >
						   		 <img width=16 height=16 src="skins/images/info.jpg" title="加油记录"/>
                        	</s:a>
                        </td>
                        <th>&nbsp;</th>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <th>洗车情况：</th>
						<td>
                        	<s:a action="carWash_list?carId=%{id} " >
						   		 <img width=16 height=16 src="skins/images/info.jpg" title="洗车记录"/>
                        	</s:a>
                        </td>
                        <th>&nbsp;</th>
                        <td>&nbsp;</td>
                    </tr> 
                    <tr>
                        <th>物品领用情况：</th>
						<td>
                        	<s:a action="materialReceive_list?carId=%{id} " >
						   		 <img width=16 height=16 src="skins/images/info.jpg" title="物品领用记录"/>
                        	</s:a>
                        </td>
                        <th>&nbsp;</th>
                        <td>&nbsp;</td>
                    </tr>                    
                </tbody>                
                 <tfoot> 
                    <tr>
                    <td>
                         <a class="p15" href="javascript:history.go(-1);">返回</a>
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
