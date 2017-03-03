<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border bodyStyle="width:100%">
<style>
	@media print 
	{
		.noprint{display:none;}
	}
	.space{font-family:"黑体";}
	td{
	font-weight: bold;
	height:45px;
	font-size:22px;
	}
	.ttt0{
	float:left;width:30px;height:30px;font-weight:bold;
	}
	.ttt1{
	float:left;margin-left:15px;width:135px;height:30px;font-weight:bold;
	}
	.ttt2{
	float:left;margin-left:60px;width:135px;height:30px;font-weight:bold;
	}
</style>
    <div class="space">
		<table id="tableId" style="border-color:black;width:100%;" border="1" >
		<tr>
			<table border="1" style="border-color:black;width:100%;">
				<tr>
					<th colspan="4" style="font-size:40px;border:none">重庆市渝勤汽车服务有限公司派车单</th>
				</tr>
				<tr>
					<td style="border:none" colspan="3"></td>
					<td style="border:none" class="alignCenter">渝勤运${sn }</td>
				</tr>
				<tr>
					<td class="alignCenter" width="15%">用车单位</td>
					<td class="alignCenter" width="35%">&nbsp;&nbsp;${customerOrganization.name }</td>
					<td class="alignCenter" width="15%">联系人电话</td>
					<td class="alignCenter" width="35%">&nbsp;&nbsp;${customer.name}：${phone }</td>
				</tr>
				<tr>
					<td class="alignCenter">定车时间</td>
					<td class="alignCenter">&nbsp;&nbsp;<s:date name="scheduleTime" format="yyyy-MM-dd"/></td>
					<td class="alignCenter">上车地点</td>
					<td class="alignCenter">&nbsp;&nbsp;${fromAddress}</td>
				</tr>
				<tr>
					<td class="alignCenter" >车型</td>
					<td class="alignCenter" >&nbsp;&nbsp;${serviceType.title }</td>
					<td class="alignCenter" >车牌号</td>
					<td class="alignCenter" >&nbsp;&nbsp;${car.plateNumber }</td>
				</tr>
				<tr>
					<td class="alignCenter" style="border-bottom:none">驾驶员/电话</td>
					<td class="alignCenter" style="border-bottom:none">&nbsp;&nbsp;${driver.name }：${driver.phoneNumber }</td>
					<td class="alignCenter" style="border-bottom:none">目的地</td>
					<td class="alignCenter" style="border-bottom:none">&nbsp;&nbsp;${toAddress}</td>
				</tr>
			</table>
		</tr>
		<tr>	
			<td>	
				<table border="1" style="border-color:black;width:100%;">
					<tr>
						<td class="alignCenter" width="10%">日期</td>
						<td class="alignCenter" width="12%">上车时间</td>
						<td class="alignCenter" width="12%">下车时间</td>
						<td class="alignCenter" width="46%">经过地点摘要</td>
						<td class="alignCenter" width="10%">实际公里</td>
						<td class="alignCenter" width="10%">收费公里</td>
					</tr>
					<s:iterator value="abstractTrackList">
					<tr>
						<td align="center"><s:date name="getonDate" format="MM-dd"/></td>
						<td align="center"><s:date name="getonDate" format="MM-dd HH:mm"/></td>
						<td align="center"><s:date name="getoffDate" format="MM-dd HH:mm"/></td>
						<td align="center">${pathAbstract}</td>
						<s:if test="actualMile > 0"><td align="center">${actualMile }</td></s:if>
						<s:else><td align="center"></td></s:else>
						<s:if test="chargeMile > 0"><td align="center">${chargeMile }</td></s:if>
						<s:else><td align="center"></td></s:else>
					</tr>
					</s:iterator>
					<s:iterator value="nullAbstractTrackList" >
					<tr>
						<td align="center"></td>
						<td align="center"></td>
						<td align="center"></td>
						<td align="center"></td>
						<td align="center"></td>
						<td align="center"></td>
					</tr>
					</s:iterator>
					</table>
				</td>
			</tr>	
			<tr>
				<td>
					<table border="1" style="border-color:black;width:100%;">
					<tr>
						<td class="alignCenter" width="15%" style="border-top:none">出库路码</td>
						<s:if test="beginMile > 0">
							<td class="alignCenter" width="10%" style="border-top:none">${beginMile }</td>
						</s:if>
						<s:else>
							<td class="alignCenter" width="10%" style="border-top:none"></td>
						</s:else>
						<td class="alignCenter" width="15%" style="border-top:none">客户上车路码</td>
						<s:if test="customerGetonMile > 0">
							<td class="alignCenter" width="10%" style="border-top:none">${customerGetonMile }</td>
						</s:if>
						<s:else>
							<td class="alignCenter" width="10%" style="border-top:none"></td>
						</s:else>
						<td class="alignCenter" width="15%" style="border-top:none">客户下车路码</td>
						<s:if test="customerGetoffMile > 0">
							<td class="alignCenter" width="10%" style="border-top:none">${customerGetoffMile }</td>
						</s:if>
						<s:else>
							<td class="alignCenter" width="10%" style="border-top:none"></td>
						</s:else>
						<td class="alignCenter" width="15%" style="border-top:none">回库路码</td>
						<s:if test="endMile > 0">
							<td class="alignCenter" width="10%" style="border-top:none">${endMile }</td>
						</s:if>
						<s:else>
							<td class="alignCenter" width="10%" style="border-top:none"></td>
						</s:else>
					</tr>
					<tr>
						<td class="alignCenter">邮费</td>
						<s:if test="refuelMoney > 0"><td class="alignCenter">${refuelMoney }</td></s:if>
						<s:else><td class="alignCenter"></td></s:else>
						<td class="alignCenter">洗车费</td>
						<s:if test="washingFee > 0"><td class="alignCenter">${washingFee }</td></s:if>
						<s:else><td class="alignCenter"></td></s:else>
						<td class="alignCenter">停车费</td>
						<s:if test="parkingFee > 0"><td class="alignCenter">${parkingFee }</td></s:if>
						<s:else><td class="alignCenter"></td></s:else>
						<td class="alignCenter">计费路码</td>
						<s:if test="totalChargeMile > 0"><td class="alignCenter">${totalChargeMile }</td></s:if>
						<s:else><td class="alignCenter"></td></s:else>
					</tr>
					<tr>
						<td class="alignCenter">过路费<br/>（客户自理）</td>
						<s:if test="toll > 0"><td class="alignCenter">${toll }</td></s:if>
						<s:else><td class="alignCenter"></td></s:else>
						<td class="alignCenter">食宿</td>
						<s:if test="roomAndBoardFee > 0"><td class="alignCenter">${roomAndBoardFee }</td></s:if>
						<s:else><td class="alignCenter"></td></s:else>
						<td class="alignCenter">其他费用</td>
						<s:if test="otherFee > 0"><td class="alignCenter" style="padding-left:8px">${otherFee }</td></s:if>
						<s:else><td class="alignCenter" style="padding-left:8px"></td></s:else>
						<td class="alignCenter">税费</td>
						<s:if test="tax > 0"><td class="alignCenter">${tax }</td></s:if>
						<s:else><td class="alignCenter"></td></s:else>
					</tr>
					<tr>
						<td class="alignCenter">核算金额</td>
						<s:if test="orderMoney > 0"><td class="alignCenter" colspan="3">${orderMoney }</td></s:if>
						<s:else><td class="alignCenter" colspan="3"></td></s:else>
						<td class="alignCenter">实收金额</td>
						<s:if test="actualMoney > 0"><td class="alignCenter" colspan="3">${actualMoney }</td></s:if>
						<s:else><td class="alignCenter" colspan="3"></td></s:else>
					</tr>
					<tr>
						<td colspan="8">&nbsp;&nbsp;请为本次服务评价：&nbsp;&nbsp;
						<input type="radio" name="grade" value="4" readonly="readonly"/>非常满意&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="grade" value="3" readonly="readonly"/>满意&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="grade" value="2" readonly="readonly"/>一般满意&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="grade" value="1" readonly="readonly"/>不满意&nbsp;&nbsp;&nbsp;&nbsp;
						<input id="gradeId" type="hidden" name="grade" value="${grade }"></td>
					</tr>
					<tr>
						<td class="alignCenter" style="height:60px">驾驶员签字</td>
						<td colspan="2"></td>
						<td class="alignCenter" colspan="2">用车人签字及电话</td>
						<td style="padding-left:25px" colspan="3">
						<img id="imgId" width="150" height="50" style="display:none" src="order_getSignature.action?imageId=${signature.id}" />
						<input id="signatureId" value="${signature.id }" style="display:none"/>${phone }
						</td>
					</tr>
					<tr>
						<td class="alignCenter">意见及建议</td>
						<td colspan="7" style="padding-left:15px">${options }</td>
					</tr>
					<tr>
						<td style="border:none;"></td>
						<td colspan="5" style="border:none;padding-left:50px">${nowDate }</td>			
						<td colspan="2" style="border:none">派车人：${scheduler.name }</td>
					</tr>
					<tr>
						<td style="border:none" colspan="2">用车电话：63219797</td>
						<td style="border:none"></td>
						<td style="border:none" colspan="2">夜间用车：60391610</td>
						<td style="border:none"></td>
						<td style="border:none" colspan="2">服务监督电话：60391609</td>
					</tr>
					<tr>
						<td style="border:none">　</td>
						<td style="border:none" colspan="5">　</td>
					</tr>
					<tr>
						<td style="border:none">　</td>
						<td style="border:none" colspan="7">
						<div class="ttt0">注：</div>
						<div class="ttt1">白联&nbsp;财务联</div>
						<div class="ttt2">红联&nbsp;运营联</div>
						<div class="ttt2">篮联&nbsp;存根联</div>
						<div class="ttt2">黄联&nbsp;客户联</div>
						</td>
					</tr>
					<tfoot>
						<tr >
			         	<td style="border:none" colspan="2" class="noprint">
			             	<input class="inputButton" type="button" value="打印订单" onclick=javascript:window.print()>
			              	<a class="p15" href="javascript:history.go(-1);">返回</a>
			         	</td>
			    		</tr>
			    	</tfoot>
			    	</table>
			    </td>
			  </tr>
		</table>
    </div>
    <script type="text/javascript">
    $(function(){
		 
		var obj= $("#signatureId");
		if(obj.val() != null&&obj.val() != 0){
			$("#imgId").show();
		}
		
		var grade = $("#gradeId").val();
		if(grade == null || grade == 0){
			$("input[name=grade]").attr("checked",false);
		}if(grade == 1){
			$("input[name=grade][value=1]").attr("checked",true);
		}if(grade == 2){
			$("input[name=grade][value=2]").attr("checked",true);
		}if(grade == 3){
			$("input[name=grade][value=3]").attr("checked",true);
		}if(grade == 4){
			$("input[name=grade][value=4]").attr("checked",true);
		}
	 });
    </script>
</cqu:border>
