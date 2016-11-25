<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div id="msg" >
        <!--失败提示-->
        <div class="msgFalse" style="width:220px">
            <dl>
                <dt>导入失败，第&nbsp;${result}&nbsp;条加油信息出错。<br>原因:&nbsp;${unknownCarOrDriver }&nbsp;。</dt>
                <dd>
                    <a href="#" onclick="location.href='carRefuel_list.action';">返回</a>
                </dd>
            </dl>
        </div>
    </div>
<script type="text/javascript">
</script>
</cqu:border>
