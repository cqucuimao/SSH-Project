<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>  
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border >
<s:form  action="test_test">
					<cqu:diskFileSelector name="uDiskFiles" uploadLimit="false"/>
					<input class="inputButton" type="submit" value="发送"/>	
</s:form>
</cqu:border>   


