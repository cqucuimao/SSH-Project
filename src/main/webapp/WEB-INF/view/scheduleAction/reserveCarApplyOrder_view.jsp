<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
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
						<td colspan="2">		
							<div class="title">
             					<br/>
            					<h2>申请信息</h2>
        					</div>
        				</td>
        			</tr>
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
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.status')" />：</th>
						<td>
								${status.label}
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
						<td colspan="2">		
							<div class="title">
             					<br/>
             					<br/>
            					<h2>审核信息</h2>
        					</div>
        				</td>
        			</tr>
						<tr>
							<th><s:property value="tr.getText('order.ReserveCarApplyOrder.approveUser')" />：</th>
							<td>${approveUser.name }</td>
						</tr>
						<tr>
							<th><s:property value="tr.getText('order.ReserveCarApplyOrder.approved')" />：</th>
							<td>
								<s:if test="approved == true">是</s:if>
								<s:else><font color="red">否</font></s:else>
							</td>
						</tr>
						<tr>
							<th><s:property value="tr.getText('order.ReserveCarApplyOrder.approveMemo')" />：</th>
							<td>
									<s:textarea class="inputText" style="height:100px" disabled="true" name="approveMemo"></s:textarea>
							</td>
						</tr>
						<tr>
							<th><s:property value="tr.getText('order.ReserveCarApplyOrder.approvedTime')" />：</th>
							<td>
								<s:date name="approvedTime" format="yyyy-MM-dd HH:mm"/>
							</td>
						</tr>
					</s:if>	
					<!-- 车辆审批信息 -->					
					<s:if test="carApproveUser != null">									
					<tr>
						<td colspan="2">		
							<div class="title">
             					<br/>
             					<br/>
            					<h2>车辆配置信息</h2>
        					</div>
        				</td>
        			</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.carApproveUser')" />：</th>
						<td>	${carApproveUser.name }</td>
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
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.cars')" />：</th>
						<td>
							<s:select name="cars" multiple="true" cssClass="SelectStyle" list="cars" listKey="id" listValue="plateNumber" style="height:100px"/>
                		</td>	
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.carApproveUserMemo')" />：</th>
						<td>
								<s:textarea class="inputText" style="height:100px" disabled="true" name="carApproveUserMemo"></s:textarea>
						</td>
					</tr>	
					</s:if>	
					<s:if test="driverApproveUser != null">								
					<tr>
						<td colspan="2">		
							<div class="title">
             					<br/>
             					<br/>
            					<h2>司机配置信息</h2>
        					</div>
        				</td>
        			</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.driverApproveUser')" />：</th>
						<td>	${driverApproveUser.name }</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.driverApproved')" />：</th>
						<td>
							<s:if test="driverApproved == true">是</s:if>
							<s:else><font color="red">否</font></s:else>
						</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.drivers')" />：</th>
						<td>
							<s:select name="drivers" multiple="true" cssClass="SelectStyle" list="drivers" listKey="id" listValue="name" style="height:100px"/>
                		</td>
					</tr>
					<tr>
						<th><s:property value="tr.getText('order.ReserveCarApplyOrder.driverApproveUserMemo')" />：</th>
						<td>
								<s:textarea class="inputText" style="height:100px" disabled="true" name="driverApproveUserMemo"></s:textarea>
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
</cqu:border>