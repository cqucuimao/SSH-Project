<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>

<style>
	  .tdDiv{width:150px;text-align:left;float:left;margin-left:0px;}
    .tdDiv1{width:110px;text-align:center;float:left;margin-left:0px;}
    .tdDiv2{width:110px;text-align:center;float:left;margin-left:33px;}
    .tdDiv3{width:110px;text-align:center;float:left;margin-left:33px;}
	</style>

	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>车辆保险详细信息</h1>
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
					<tbody class="tableHover">
							<tr>
                    			<th width="100"><s:property value="tr.getText('car.Car.plateNumber')" />：</th>
                    			<td><cqu:carDetailList id="${car.id}" /></td>
                			</tr>
                			<tr>
                    			<th><s:property value="tr.getText('car.CarInsurance.insureCompany')" />：</th>
                    			<td>${insureCompany}</td>
                			</tr>
                			<tr>
                    			<th><s:property value="tr.getText('car.CarInsurance.compulsoryPolicyNumber')" />：</th>
                    			<td>${compulsoryPolicyNumber}</td>
                			</tr>
                			<tr>
                    			<th><s:property value="tr.getText('car.CarInsurance.commercialPolicyNumber')" />：</th>
                    			<td>${commercialPolicyNumber}</td>
                			</tr>
                			<tr>
		                    	<th></th>
		                    	<th>
		                    			<div class="tdDiv">类型</div>
		                    			<div class="tdDiv1">生效日期</div>
		                    		    <div class="tdDiv2"> 截止日期</div>
		                    		    <div class="tdDiv3"> 承保金额（元）</div>
		                    		    <div class="tdDiv3">金额（元）</div>
		                    		    <div class="tdDiv3">备注</div>
		                    	</th>
		                    </tr>  
                			<tr>
                    			<th>交强险：</th>
                    			<td>
                    			<div class="tdDiv">——</div>
                    			<div class="tdDiv1"><s:date name="compulsoryBeginDate" format="yyyy-MM-dd"/></div>      			
		                    	<div class="tdDiv2"><s:date name="compulsoryEndDate" format="yyyy-MM-dd"/></div>	                    			
		                    	<div class="tdDiv3">——</div>
		                    	<div class="tdDiv3"> ${compulsoryMoney }</div>
		                    	<div class="tdDiv3">——</div>
                    			</td>
                			</tr>
                			<tr>
                    			<th>车船税：</th>
                    			<td>
                    			<div class="tdDiv">——</div>
                    			<div class="tdDiv1"><s:date name="vehicleTaxBeginDate" format="yyyy-MM-dd"/></div>
		                    	<div class="tdDiv2"><s:date name="vehicleTaxEndDate" format="yyyy-MM-dd"/></div>
		                    	<div class="tdDiv3">——</div>
		                    	<div class="tdDiv3">${vehicleTaxMoney }</div>
		                    	<div class="tdDiv3">——</div>
                    			</td>
                			</tr>
                			<s:iterator value="commercialInsurances">
                			<tr>
                				<th>商业保险：</th>
                				<td>
                				<div class="tdDiv">${commercialInsuranceType.name }</div>
                				<div class="tdDiv1"><s:date name="commercialInsuranceBeginDate" format="yyyy-MM-dd"/></div>
                				<div class="tdDiv2"><s:date name="commercialInsuranceEndDate" format="yyyy-MM-dd"/></div>
                				<div class="tdDiv3">${commercialInsuranceCoverageMoney }</div>
                				<div class="tdDiv3">${commercialInsuranceMoney }</div>
                				<div class="tdDiv3">${commercialInsuranceMemo }</div>
                				</td>
                			</tr>
                			</s:iterator>             			
                			<tr>
                    			<th>保险起止时间：</th>
                    			<td><s:date name="fromDate" format="yyyy-MM-dd"/>&nbsp;&nbsp;—&nbsp;&nbsp;<s:date name="toDate" format="yyyy-MM-dd"/></td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarInsurance.money')" />(元)：</th>
                				<td><fmt:formatNumber value="${money}" pattern="#0.00"/></td>
                			</tr>
                			<tr>
                				<th><s:property value="tr.getText('car.CarInsurance.memo')" />：</th>
                				<td>${memo}</td>
                			</tr>
					</tbody>
					<tfoot>
                		<tr>
                    	<td colspan="2">
                        	<a class="p15" href="javascript:history.go(-1);">返回</a>
                    	</td>
                		</tr>
            		</tfoot>
				</table>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			
	    })
	</script>
</cqu:border>
