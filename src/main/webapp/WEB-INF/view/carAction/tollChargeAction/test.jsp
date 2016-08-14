	<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
	<%@ include file="/WEB-INF/view/common/common.jsp" %>
	<cqu:border>
			<s:form action="tollCharge_test">
			    <br/>
			           服务类型选择<cqu:serviceType name="myServiceType" /> 
				 司机选择:<cqu:userSelector name="myUser" />	
				
				<br/>
				 单位选择：<cqu:customerOrganizationSelector name="myCo"/> 
				 <br/>
				  车辆选择：<cqu:carSelector name="myCar"/> 
    <s:textfield class="userSelector inputText inputChoose" id="driverName" name="driver.name" type="text"/>
				<s:textfield id="driverId" name="driver.id" type="hidden"/>
						
				
			    <input id="go" class="inputButton" type="submit" value="测试"/>
			</s:form>
	</cqu:border> 
		