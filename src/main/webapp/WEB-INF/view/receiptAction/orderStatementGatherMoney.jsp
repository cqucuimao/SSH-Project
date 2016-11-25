<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>收款</h1>
            <p style="color: red">
				<s:if test="hasFieldErrors()">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<s:property />
						</s:iterator>
					</s:iterator>
				</s:if>
			</p>
        </div>
        <div class="editBlock detail p30">
        <s:form action="orderStatement_gatherMoneyDo" id="pageForm">
        	<s:hidden name="orderStatementId"/>
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
                        <th><s:property value="tr.getText('order.OrderStatement.name')" />：</th>
						<td>${name}</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('order.OrderStatement.customerOrganization')" />：</th>
						<td>${customerOrganization.name}</td>
                    </tr>
                	<tr>
                        <th>起止时间：</th>
						<td><s:date name="fromDate" format="yyyy-MM-dd"/> &nbsp;&nbsp;-&nbsp;&nbsp; <s:date name="toDate" format="yyyy-MM-dd"/></td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('order.OrderStatement.orderNum')" />：</th>
						<td>${orderNum}</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('order.OrderStatement.totalMoney')" />：</th>
						<td>${totalMoney}</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('order.OrderStatement.invoiceMoney')" />：</th>
						<td>${invoiceMoney}</td>
                    </tr>
                	<tr>
                        <th><s:property value="tr.getText('order.OrderStatement.actualTotalMoney')" />：</th>
						<td>${actualTotalMoney}</td>
                    </tr>
                	<tr>
                        <th>未收金额：</th>
						<td>${invoiceMoney-actualTotalMoney}</td>
                    </tr>
                    <tr>
						<th>收款情况</th>
						<td>
							<div class="dataGrid">
								<div class="tableWrap">
                    				<table>
										<colgroup>
											<col></col>
											<col></col>
											<col></col>
											<col></col>
										</colgroup>
									<thead>
										<tr>
											<th>金额</th>
											<th>日期</th>
											<th>备注</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody class="tableHover" id="htcList">
										<s:iterator value="gatherList">
										<tr>
					    					<td>${money}</td>
					    					<td><s:date name="date" format="yyyy-MM-dd"/></td>
					    					<td>${memo}</td>
					    					<td>&nbsp;</td>
										</tr>
										</s:iterator> 
										<tr>
											<td>
                    							<s:textfield cssClass="inputText" name="infoMoney"/>
											</td>
											<td>
                    							<s:textfield cssClass="inputText" name="infoDate" class="Wdate half" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
											</td>
											<td>
                    							<s:textfield cssClass="inputText" name="infoMemo"/>
											</td>
											<td>
												<input class="inputButton" type="submit" value="提交" />
											</td>
										</tr>
									</tbody>
									</table>
								</div>
							</div>
						</td>
					</tr>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                        	 <s:a action="orderStatement_gatherComplete" href="#">
		   					 	<input type="button" class="inputButton" id="gatherComplete" value="收款完成" />
		   					 </s:a>
                             <a class="p15" href="javascript:history.go(-1);">返回</a>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </s:form>
        </div>
    </div>
    <script type="text/javascript">
	    $(function(){ 	
			$("#pageForm").validate({
				submitout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					infoMoney:{
						required:true,
						number:true,
						min:0
					},
					infoDate:{
						required:true,
					},
				}
			});
		});
	    
	    $("#gatherComplete").click(function(){
	    	if(confirm("确定收款完成？")){
	       		self.location.href='orderStatement_gatherComplete.action?orderStatementId=${id}';
	    	}
	    });
    </script>
</cqu:border>
