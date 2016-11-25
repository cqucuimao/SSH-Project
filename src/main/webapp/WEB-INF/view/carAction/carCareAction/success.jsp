<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div id="msg" >
        <!--成功提示-->
        <div class="msgSuccess" >
            <dl>
                <dt>恭喜您，成功导入&nbsp;${result }&nbsp;条信息！</dt>
                <dd class="msg_time">
                </dd>
                <dd>
                    <a href="#" onclick="location.href='carCare_list.action';">返回</a>
                </dd>
            </dl>
        </div>
    </div>
<script type="text/javascript">
</script>
</cqu:border>
