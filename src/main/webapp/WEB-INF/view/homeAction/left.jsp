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
	<link rel="stylesheet" type="text/css" href="<%=basePath %>skins/left.css">
</head>
<base target="rightFrame" />
<body>
	
	<div class="header">
	<input id="logoName" type="hidden" value="${session.companyLogoName}"/>	
	</div>
	
	<div id="menu">
		<dl class="on">
			<dt>
				<i class="icon-menu-home"></i>
				<s:a action="home_welcome">首页</s:a>
			</dt>
		</dl>
		
		<s:if test="#session.user.hasPrivilegeByUrl('/privilege')">
		<dl>
			<dt>
				<i class="icon-menu-rights"></i>
				权限管理
				<div class="group-collapsed"></div>
			</dt>
			<s:if test=" #session.user.hasPrivilegeByUrl('/user_list') ">
			<dd>
				<s:a action="user_list">用户管理</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/department_list') ">
			<dd>
				<s:a action="department_list">部门管理</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/role_list') ">
			<dd>
				<s:a action="role_list">角色管理</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>		
			<s:if test=" #session.user.hasPrivilegeByUrl('/user_dispatchUserList') ">
			<dd>
				<s:a action="user_dispatchUserList">外派员工管理</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>				
		</dl>
		</s:if>
		
		<s:if test=" #session.user.hasPrivilegeByUrl('/schedule') ">
		<dl>
			<dt>
				<i class="icon-menu-scheduling"></i>
				车辆调度
				<div class="group-collapsed"></div>
			</dt>
			<s:if test=" #session.user.hasPrivilegeByUrl('/schedule_scheduling') ">
			<dd>
				<s:a action="schedule_scheduling">调度</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/schedule_queue') ">
			<dd>
				<s:a action="schedule_queue">待调度队列</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/order_orderManager') ">
			<dd>
				<s:a action="order_orderManager">订单管理</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/driver_taskList') ">
			<dd>
				<s:a action="driver_taskList">动态任务表</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
		</dl>
		</s:if>
		
		<s:if test=" #session.user.hasPrivilegeByUrl('/car') ">
		<dl>
			<dt>
				<i class="icon-menu-manage"></i>
				车辆管理
				<div class="group-collapsed"></div>
			</dt>
			<s:if test=" #session.user.hasPrivilegeByUrl('/car_list') ">
			<dd>
				<s:a action="car_list">基本信息</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/priceTable_list') ">
			<dd>
				<s:a action="priceTable_list">价格表管理</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/carViolation_list') ">
			<dd>
				<s:a action="carViolation_list">违章信息</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/servicePoint_list') ">
			<dd>
				<s:a action="servicePoint_list">驻车点管理</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/carCareAppointment_list') || #session.user.hasPrivilegeByUrl('/carCare_list')">
			<dd>
				<s:if test="#session.user.hasPrivilegeByUrl('/carCareAppointment_list')">
				<s:a action="carCareAppointment_list">车辆保养</s:a>
				</s:if>
				<s:else>
				<s:a action="carCare_list">车辆保养</s:a>
				</s:else>				
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/carInsurance_list') ">
			<dd>
				<s:a action="carInsurance_list">车辆保险</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/carRepairAppointment_list') || #session.user.hasPrivilegeByUrl('/carRepair_list') ">
			<dd>
			     <s:if test="#session.user.hasPrivilegeByUrl('/carRepairAppointment_list')">
				<s:a action="carRepairAppointment_list">车辆维修</s:a>
				</s:if>
				<s:else>
				<s:a action="carRepair_list">车辆维修</s:a>
				</s:else>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/carRefuel_list') ">
			<dd>
				<s:a action="carRefuel_list">加油信息</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/carExamineAppointment_list') || #session.user.hasPrivilegeByUrl('/carExamine_list') ">
			<dd>
			    <s:if test="#session.user.hasPrivilegeByUrl('/carExamineAppointment_list')">
				<s:a action="carExamineAppointment_list">车辆年审</s:a>
				</s:if>
				<s:else>
				<s:a action="carExamine_list">车辆年审</s:a>
				</s:else>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/tollCharge_list') ">
			<dd>
				<s:a action="tollCharge_list">路桥费</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/materialReceive_list') ">
			<dd>
				<s:a action="materialReceive_list">物品领用</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/carWash_list') ">
			<dd>
				<s:a action="carWash_list">洗车登记</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
		</dl>
		</s:if>
				
		<s:if test=" #session.user.hasPrivilegeByUrl('/monitor') ">
		<dl>
			<dt>
				<i class="icon-menu-monitor"></i>
				车辆监控
				<div class="group-collapsed"></div>
			</dt>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/realtime_home') ">
			<dd>
				<s:a action="realtime_home">实时监控</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			<s:if test=" #session.user.hasPrivilegeByUrl('/replay_home') ">
			<dd>
				<s:a action="replay_home">轨迹回放</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/alarm_home') ">
			<dd>
				<s:a action="alarm_home">历史报警</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
		</dl>
		</s:if>
		
		<s:if test=" #session.user.hasPrivilegeByUrl('/receipt') ">
		<dl>
			<dt>
				<i class="icon-menu-pay"></i>
				收款管理
				<div class="group-collapsed"></div>
			</dt>
			<s:if test=" #session.user.hasPrivilegeByUrl('/unpaidOrder_home') ">
			<dd>
				<s:a action="unpaidOrder_home">未收款订单</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/orderStatement_newList') ">
			<dd>
				<s:a action="orderStatement_newList">对账单</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
		</dl>
		</s:if>
				
		<s:if test=" #session.user.hasPrivilegeByUrl('/customer') ">
		<dl>
			<dt>
				<i class="icon-menu-customer"></i>
				客户管理
				<div class="group-collapsed"></div>
			</dt>
			<s:if test=" #session.user.hasPrivilegeByUrl('/customerOrganization_list') ">
			<dd>
				<s:a action="customerOrganization_list">单位信息</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			<s:if test=" #session.user.hasPrivilegeByUrl('/customer_list') ">
			<dd>
				<s:a action="customer_list">客户信息</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			</s:if>
		</dl>
		</s:if>
		
		<s:if test=" #session.user.hasPrivilegeByUrl('/statistic') ">
		<dl>
			<dt>
				<i class="icon-menu-statistics"></i>
				查询统计
				<div class="group-collapsed"></div>
			</dt>
			<s:if test=" #session.user.hasPrivilegeByUrl('/completedOrderStatistic_list') ">
			<dd>
				<s:a action="completedOrderStatistic_list">已完成订单</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/accountsReceivableStatistic_list') ">
			<dd>
				<s:a action="accountsReceivableStatistic_list">已收款对账单</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/mileageStatistic_list') ">
			<dd>
				<s:a action="mileageStatistic_list">里程数据</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/refuelStatistic_list') ">
			<dd>
				<s:a action="refuelStatistic_list">加油数据</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/fuelConsumptionStatistic_list') ">
			<dd>
				<s:a action="fuelConsumptionStatistic_list">平均油耗</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/insuranceStatistic_list') ">
			<dd>
				<s:a action="insuranceStatistic_list">保险记录</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/careStatistic_list') ">
			<dd>
				<s:a action="careStatistic_list">保养记录</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
			<s:if test=" #session.user.hasPrivilegeByUrl('/repairStatistic_list') ">
			<dd>
				<s:a action="repairStatistic_list">维修记录</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
		</dl>
		</s:if>
		
		<s:if test=" #session.user.hasPrivilegeByUrl('/businessParameter') ">
		<dl>
			<dt>
				<i class="icon-menu-customer"></i>
				配置
				<div class="group-collapsed"></div>
			</dt>
			<s:if test=" #session.user.hasPrivilegeByUrl('/businessParameter_employees') ">
			<dd>
				<s:a action="businessParameter_employees">配置</s:a>
				<i class="icon-menu-arrow"></i>
			</dd>
			</s:if>
		</dl>
		</s:if>
	</div>
	<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="<%=basePath %>js/left.js"></script>
	<script type="text/javascript">
	    var logo=$("#logoName").val();
	    logo="skins/images/"+logo
	    //console.log(logo);
		$(".header").css("background","url("+logo+") no-repeat center 8px");
	</script>
</body>
</html>