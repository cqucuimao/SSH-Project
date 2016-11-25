<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cqu" uri="//WEB-INF/tlds/cqu.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
	<link rel="stylesheet" type="text/css" href="skins/main.css">
	<link rel="stylesheet" type="text/css" href="js/artDialog4.1.7/skins/blue.css">
</head>
<body class="minW">
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>配置</h1>
		</div>
		<div class="tab_next style2">
			<table>
				<tr>
				<td id="tab1" class="on"><a href="#"><span>4S员工</span></a></td>
				<td id="tab2"><a href="#"><span>保养预约管理员</span></a></td>
				</tr>
			</table>
		</div>
		<br/>
		<div class="tab_next_con pt_10">
		<!-- tab1对应显示内容 -->
			    	<div id="tab1_c">
						<div class="editBlock search">
							<table>
								<tr>
									<td>
										<s:a cssClass="buttonA" action="realtime_monitorGroupAddUI">添加4S</s:a>
									</td>
								</tr>
							</table>
						</div>
			    		<div class="dataGrid" style="background-color:blue;">
			    			<div class="tableWrap">
					            <table>
					            	<thead>
					            		<tr>
					            			<th>4S员工</th>
					            			<th class="alignCenter">操作</th>
					            		</tr>
					            	</thead>
					            	<tbody class="tableHover">	
					            		<s:iterator value="for4sUserList">
					            			<tr>
					            				<td>${name }</td>
					            				<td>
					            						<s:a action="businessParameter_delete4S?4SId=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
												</td>
					            			</tr>
					            		</s:iterator>
					            		            					    
					            	</tbody>
					            </table>
					        </div>
				        </div>
			    	</div>
			    	<!-- tab2对应显示内容 -->
			    	<div id="tab2_c">
						<div class="editBlock search">
							<table>
								<tr>
									<td>
										<s:a cssClass="buttonA" action="realtime_monitorGroupAddUI">添加保养预约管理员</s:a>
									</td>
								</tr>
							</table>
						</div>
			    		<div class="dataGrid">
			    			<div class="tableWrap">
					            <table>
					            	<thead>
					            		<tr>
					            			<th>保养预约管理员</th>
					            			<th class="alignCenter">操作</th>
					            		</tr>
					            	</thead>
					            	<tbody class="tableHover">	
					            	
					            	<s:iterator value="forCarCareUserList">
					            			<tr>
					            				<td>${name }</td>
					            				<td>
					            				<s:a action="businessParameter_deleteCarCare?carCareId=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
					            				</td>
					            			</tr>
					            		</s:iterator>				            		
					            	</tbody>
					            </table>
					        </div>
				        </div>
			    	</div>
	</div>
	</div>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="js/artDialog4.1.7/artDialog.js"></script>
	<script type="text/javascript" src="js/artDialog4.1.7/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="js/jquery.cookie.js"></script>
	<script type="text/javascript" src="js/jquery.pager.js"></script>
	<script type="text/javascript" src="js/common.js"></script>	
	<script type="text/javascript">
	</script>
</body>
</html>
