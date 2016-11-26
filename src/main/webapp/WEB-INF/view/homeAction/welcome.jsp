<%@page import="com.yuqincar.domain.privilege.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
<div class="space"> 
  <!-- 标题 -->
  <div class="title">
    <h1>欢迎光临${sessionScope.company.name }综合业务管理系统</h1>
    <s:property value="%{session.user}"/> 
    <p></p>
  </div>
  <div class="homeInfo"> <img width="80" height="80" src="skins/images/icon_userInfo.png" />
    <div class="form-group"> <span class="label">Name:</span> <span><s:property value="#session.user.name"/></span> </div>
    <div class="form-group"> <span class="label">手机号:</span> <span><s:property value="#session.user.phoneNumber"/></span> </div>
    <div class="form-group"> <span class="label"></div>
  </div>
  <br/><br/><br/>
  <ul class="float homeLink">
  	<s:if test="canShowScheduleBlock">
    <li>
      <div class="inside">
        <div class="homeLink1">
          	<h1>车辆调度</h1>
		</div>
        <p><s:a action="schedule_queue">目前有&nbsp;&nbsp;${orderCountInQueue}&nbsp;&nbsp;个订单等待调度</s:a></p>
      </div>
    </li>
    </s:if>
  	<s:if test="canShowWarningBlock">
    <li>
      <div class="inside">
        <div class="homeLink2">
          <h1>车辆监控</h1>
        </div>
        <p><s:a action="realtime_home">目前有&nbsp;&nbsp;${warningCount}&nbsp;&nbsp;个监控报警</s:a></p>
      </div>
    </li>
    </s:if>
    <!-- 临时扩充常备车库申请 -->
    <s:if test="canShowReserveCarApplyOrderBlock">
    <li>
      <div class="inside">
        <div class="homeLink2">
          <h1>临时扩充常备车库申请</h1>
        </div>
         <!-- 提醒申请人 -->
        <s:if test="canShowApply">
	        <p>
	        	  <s:a action="reserveCarApplyOrder_list">目前有&nbsp;&nbsp;${approveCount}&nbsp;&nbsp;个待审核的申请</s:a>&nbsp;&nbsp;
	        	  <s:a action="reserveCarApplyOrder_list">目前有&nbsp;&nbsp;${rejectedCount}&nbsp;&nbsp;个被驳回的申请</s:a>
	        </p>
        </s:if>
        <!-- 提醒审核人 -->
        <s:if test="canShowApprove">
	        <p>
	        	  <s:a action="reserveCarApplyOrder_list">目前有&nbsp;&nbsp;${approveCount}&nbsp;&nbsp;个待审核的申请</s:a>
	        </p>
        </s:if>
        <!-- 提醒车辆配置人 -->
        <s:if test="canShowCarApprove">
        <p>
        	  <s:a action="reserveCarApplyOrder_list">目前有&nbsp;&nbsp;${configureCarCount}&nbsp;&nbsp;个待配置车辆的申请</s:a>
        </p>
        </s:if>
    	<!-- 提醒司机配置人 -->
        <s:if test="canShowDriverApprove">
        <p>
        	  <s:a action="reserveCarApplyOrder_list">目前有&nbsp;&nbsp;${configureDriverCount}&nbsp;&nbsp;个待配置司机的申请</s:a>
        </p>
        </s:if>
      </div>
    </li>
    </s:if>
  </ul>
</div>
</cqu:border>