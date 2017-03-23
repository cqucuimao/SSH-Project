<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>重新调度</h1>
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
        	<s:form action="order_rescheduleDo" id="myForm">
        	<s:hidden name="orderId"/>
            <table>
                <tbody>
                    <tr>
                        <th width="15%">订单号：</th>
                        <td width="35%">${sn }</td>
                        <th width="15%">单位：</th>
                        <td width="35%">${customerOrganization.name }</td>
                    </tr>
                    <tr>
                        <th>姓名：</th>
                        <td>${customer.name}</td>
                        <th>联系方式：</th>
                        <td>${phone }</td>
                    </tr>
                    <tr>
                    	<th>计费方式</th>
                    	<td>${chargeMode.label }</td>
                        <th>车型：</th>
                        <td>${serviceType.title }</td>
                    </tr>
                    <tr>
                        <th>订单状态：</th>
                        <td>${status.label }</td>
                        <th></th>
                        <td></td>
                    </tr>
                    <tr>
                        <th>计划起止时间：</th>
                        <td>${planDateString }</td>
                        <th>实际起止时间：</th>
                        <td>${actualDateString }</td>
                    </tr>                    
                    <tr>
                        <th>始发地：</th>
                        <td>${fromAddress}</td>
                        <th>目的地：</th>
                        <td>
                        	<s:if test="toAddress!=null">
                        		${toAddress}
                        	</s:if>
                        </td>
                    </tr>
                    <tr>
                        <th>司机：</th>
                        <td>
                        	<s:if test="driver!=null">
                        		${driver.name }（${driver.phoneNumber}）
                        	</s:if>
                        </td>
                        <th>车牌：</th>
                        <td>
                        	<s:if test="car!=null">
                        		${car.plateNumber }
                        	</s:if>
						</td>
                    </tr>
                    <tr>
                        <th>实际里程：</th>
                        <td>${actualMile }KM</td>
                        <th>订单里程：</th>
                        <td>${orderMile}KM</td>
                    </tr>
                    <tr>
                        <th>实际金额：</th>
                        <td>${actualMoneyString}元</td>
                        <th>订单金额：</th>
                        <td>${orderMoneyString}元</td>
                    </tr>
                    <tr>
                        <th>备注：</th>
                        <td>${memo}</td>
                        <th></th>
                        <td></td>
                    </tr>                  
                    <tr>
                        <th></th>
                        <td></td>
                        <th></th>
                        <td></td>
                    </tr>                  
                    <tr>
                        <th>车辆：<span class="required">*</span></th>
                        <td><cqu:carAutocompleteSelector name="car" synchDriver="driver"/></td>
                        <th></th>
                        <td></td>
                    </tr>                  
                    <tr>
                        <th>司机：<span class="required">*</span></th>
                        <td><cqu:userAutocompleteSelector name="driver"/></td>
                        <th></th>
                        <td></td>
                    </tr>             
                    <tr>
                        <th>重新调度原因：<span class="required">*</span></th>
                        <td>
							<s:textarea style="height:100px;" class="w" name="rescheduleReason" placeholder="请输入重新调度原因"/>
						</td>
                        <th></th>
                        <td></td>
                    </tr>
                </tbody>
                <tfoot>                    
                    <tr>
                        <th></th>
                        <td></td>
                        <th></th>
                        <td></td>
                    </tr>
                    <tr>
                        <td colspan="4">
                        	<s:submit value="重新调度" class="inputButton coverOff"></s:submit>
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
		$("#myForm").validate({
			submitout: function(element) { $(element).valid(); },
			rules:{		
				carLabel:{
					required:true
				},
				driverLabel:{
					required:true
				},
				rescheduleReason:{
					required:true
				}
			}
		});
	});
    </script>
</cqu:border>
