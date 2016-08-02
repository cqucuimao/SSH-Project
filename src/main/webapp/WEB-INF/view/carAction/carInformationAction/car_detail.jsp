<%@page import="com.yuqincar.domain.car.Car"%>
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
                        <s:if test="#session.plate=='blue' ">
                        	<td>蓝牌 </td>
                        </s:if>
                        <s:else>
                        	<td>黄牌</td>
                        </s:else>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.Car.seatNumber')" />：</th>
                        <td>${seatNumber}</td>
                        <th><s:property value="tr.getText('car.Car.transmissionType')" />：</th>
                        <s:if test="#session.tt=='AUTO' ">
                        <td>自动</td>
                        </s:if>
                        <s:elseif test="#session.tt=='MANNUAL' ">
                        <td>手动</td>
                        </s:elseif>
                        <s:elseif test="#session.tt=='unsure'">
                        <td>不确定</td>
                        </s:elseif>
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
                        <th><s:property value="tr.getText('privilege.User.name')" />：</th>
                        <td>${driver.name}</td>
                        <th><s:property value="tr.getText('car.ServicePoint.name')" />：</th>
                        <td>${servicePoint.name }</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('car.Car.standbyCar')" />：</th>
                        <td>
                        <s:if test="standbyCar">
                        		 
                        		 是
                        </s:if>
                        <s:else>
                               	否
                        </s:else>
                       
                        </td>
                        <th><s:property value="tr.getText('car.Car.status')" />：</th>
                        <td>${status }</td>
                    </tr>
                    
                    <!-- 车辆链接 **********************************-->
                    <!-- 脱保 -->
                    <tr>
                        <th>脱保日期：</th>
						<s:if test="insuranceExpired">
						   <td><font color="red">脱保</font></td>
					    </s:if>
						<s:else>
							 <td ><s:date name="insuranceExpiredDate" format="yyyy-MM-dd"/></td>
						</s:else>
						
                        <th>保险情况：</th>
                        <td>
                        <s:a action="carInsurance_list?outId=%{plateNumber}" >
                        	查看本台车的保险记录
                        </s:a>
                        </td>
                    </tr>      
                     <!-- 年审 -->            
                    <tr>
                        <th><s:property value="tr.getText('car.CarExamine.date')" />：</th>
						<s:if test="#session.isOUtExamine=='true' ">
						   <td><font color="red">未年审</font></td>
					    </s:if>
						<s:else>
							 <td ><s:date name="nextExaminateDate" format="yyyy-MM-dd"/></td>
						</s:else>
						
                        <th>年审情况：</th>
                        <td>
                        <s:a action="carExamine_list?outId=%{plateNumber} " >
                        	查看本台车的年审记录
                        </s:a>
                        </td>
                    </tr>      
                  <!-- 路桥费 -->
                   <tr>
                        <th>路桥费缴纳日期：</th>
						<s:if test="#session.isOUtTollDate=='true' ">
						   <td><font color="red">未缴纳</font></td>
					    </s:if>
						<s:else>
							 <td ><s:date name="nextTollChargeDate" format="yyyy-MM-dd"/></td>
						</s:else>
						
                        <th>路桥费情况：</th>
                        <td>
                        <s:a action="tollCharge_list?outId=%{plateNumber} " >
                        	查看本台车的路桥费记录
                        </s:a>
                        </td>
                    </tr>      
                   
                   <tr>
                        <th><s:property value="tr.getText('car.CarCare.date')" />：</th>
						<s:if test="#session.isOutCare=='true' ">
						   <td><font color="red">未保养</font></td>
					    </s:if>
						<s:else>
							 <td > ${carCaredate}</td>
						</s:else>
						
                        <th>保养情况：</th>
                        <td>
                        <s:a action="carCare_list?outId=%{plateNumber} " >
                        	查看本台车的保养记录
                        </s:a>
                        </td>
                    </tr>   
                    
                    <tr>
                        <th>维修情况：</th>
                        <td>
                        <s:a action="carRepair_list?outId=%{plateNumber} " >
                        	查看本台车的维修记录
                        </s:a>
                        </td>
                        <th>违章情况：</th>
						<s:if test="#session.isTrue=='true' ">
						   <td>
						   <s:a action="carViolation_list?outId=%{plateNumber} " >
						       <font color="red"> 有</font>
						  </s:a>
						  </td>
					    </s:if>
						<s:else>
							 <td >无</td>
						</s:else>
                    </tr>         
                   
                   <tr>
                        <th>加油情况：</th>
						 <td>
                        <s:a action="carRefuel_list?outId=%{plateNumber} " >
                        	查看这辆车的加油信息
                        </s:a>
                        </td>
                        <th>洗车情况：</th>
                        <td>
                        <s:a action="carWash_list?outId=%{plateNumber} " >
                        	查看这辆车的洗车情况
                        </s:a>
                        </td>
                    </tr> 
                    
                     <tr>
                        <th>物品领用情况：</th>
                        <td>
                        <s:a action="materialReceive_list?outId=%{plateNumber} " >
                        	查看这辆车的物品领用情况
                        </s:a>
                        </td>
                        
                        <th></th>
					     <td > </td>
						
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
