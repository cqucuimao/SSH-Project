<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border bodyClass="null">
    <div id="msg" >
        <div class="msgFalse" >
            <dl>
                <dt>对不起，由于超时，你正在调度的订单被剥夺！</dt>
                <dd>
                </dd>
                <dd>
                    	您可以返回
                    	&nbsp;<s:a action="schedule_queue">待调度队列</s:a>&nbsp;
                    	重新选取订单
                </dd>
            </dl>
        </div>
    </div>
</cqu:border>
