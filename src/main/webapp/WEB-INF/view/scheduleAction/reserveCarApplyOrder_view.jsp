<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cqu" uri="//WEB-INF/tlds/cqu.tld" %>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link href="skins/main.css" rel="stylesheet" type="text/css" />
<style>
    	
    </style>
</head>
<body class="minW">
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>扩充常备车库申请</h1>
		</div>
		<div class="editBlock detail p30">
			<table> 
				<colgroup>
					<col width="80"></col>
					<col></col>
					<col></col>
					<col></col>
					<col></col>
					<col width="120"></col>
				</colgroup>
				<tbody>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.proposer')" />：</th>
						<td>${proposer.name }</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.carCount')" />：</th>
						<td>${carCount }</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.fromDate')" />：</th>
						<td><s:date name="fromDate" format="yyyy-MM-dd"/></td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.toDate')" />：</th>
						<td>
								<s:date name="toDate" format="yyyy-MM-dd"/>
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.newTime')" />：</th>
						<td>
								<s:date name="newTime" format="yyyy-MM-dd HH:mm"/>
						</td>
					</tr>
					<s:if test="status.id != 0">
						<tr>
							<th><s:property value="tr.getText('order.ReserveCarApplyOrder.submittedTime')" />：</th>
							<td>
									<s:date name="submittedTime" format="yyyy-MM-dd HH:mm"/>
							</td>
						</tr>
					</s:if>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.reason')" />：</th>
						<td>
								<s:textarea class="inputText" disabled="true" style="height:100px" name="reason"></s:textarea>
						</td>
					</tr>	
					<tr>
					<!-- 公司领导审核信息-->
					<s:if test="approveUser != null">
						<tr>
							<th><s:property value="tr.getText('order.ReserveCarApplyOrder.approveUser')" />：</th>
							<td>${approveUser.name }</td>
						</tr>
						<tr>
							<th><s:property value="tr.getText('order.ReserveCarApplyOrder.approveMemo')" />：</th>
							<td>
									<s:textarea class="inputText" style="height:100px" disabled="true" name="approveMemo"></s:textarea>
							</td>
						</tr>
						<tr>
							<th><s:property value="tr.getText('order.ReserveCarApplyOrder.approved')" />：</th>
							<td>
								<s:if test="approved == true">是</s:if>
								<s:else><font color="red">否</font></s:else>
							</td>
						</tr>
						<s:if test="approved == true">
							<tr>
								<th><s:property value="tr.getText('order.ReserveCarApplyOrder.approvedTime')" />：</th>
								<td>
										<s:date name="approvedTime" format="yyyy-MM-dd HH:mm"/>
								</td>
							</tr>
						</s:if>
						<s:else>
							<tr>
								<th><s:property value="tr.getText('order.ReserveCarApplyOrder.rejectedTime')" />：</th>
								<td>
										<s:date name="rejectedTime" format="yyyy-MM-dd HH:mm"/>
								</td>
							</tr>
						</s:else>
					</s:if>
					<!-- 车辆审批信息 -->
					<s:if test="carApproveUser != null">
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.carApproveUser')" />：</th>
						<td>	${carApproveUser.name }</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.carApproveUserMemo')" />：</th>
						<td>
								<s:textarea class="inputText" style="height:100px" disabled="true" name="carApproveUserMemo"></s:textarea>
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.carApproved')" />：</th>
						<td>
							<s:if test="carApproved == true">是</s:if>
							<s:else>
									<font color="red">否</font>
							</s:else>
						</td>
					</tr>
					</s:if>
					<!-- 当车辆审批为true时，显示审批车辆 -->
					<s:if test="carApproved == true">
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.cars')" />：</th>
						<td>
								<s:iterator value="cars">
                					${plateNumber}&nbsp;
                				</s:iterator></td>
					</tr>		
					</s:if>
					
					<!-- 司机审批信息-->
					<s:if test="driverApproveUser != null">
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.driverApproveUser')" />：</th>
						<td>	${driverApproveUser.name }</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.driverApproveUserMemo')" />：</th>
						<td>
								<s:textarea class="inputText" style="height:100px" disabled="true" name="driverApproveUserMemo"></s:textarea>
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.driverApproved')" />：</th>
						<td>
							<s:if test="driverApproved == true">是</s:if>
							<s:else><font color="red">否</font></s:else>
						</td>
					</tr>
					</s:if>
					<!-- 当车辆审批为true时，显示审批车辆 -->
					<s:if test="driverApproved == true">
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.drivers')" />：</th>
						<td>
								<s:iterator value="drivers">
                					${name}&nbsp;
                				</s:iterator></td>
					</tr>		
					</s:if>
					
					<!-- 显示配置完成时间-->
					<s:if test="status.id == 5">
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.configuredTime')" />：</th>
						<td>
								<s:date name="configuredTime" format="yyyy-MM-dd HH:mm"/>
						</td>
					</tr>		
					</s:if>
					
	                <td colspan="2">	    
	                		<input name="actionFlag" type="hidden" value="${actionFlag }">     
		                	<a class="p15" href="javascript:history.go(-1);">返回</a>
		                
	                </td>
	            </tr>
				</tbody>
			</table>
		</div>
		
	</div>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>	
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
    <script type="text/javascript" src="js/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="js/validate/messages_cn.js"></script>
	<script type="text/javascript">
	</script>
</body>
</html>