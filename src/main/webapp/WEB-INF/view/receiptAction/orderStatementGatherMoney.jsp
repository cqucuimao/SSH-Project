<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
	<link href="skins/main.css" rel="stylesheet" type="text/css" />
</head>
<body class="minW">
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
										<s:iterator value="moneyGatherInfoList">
										<tr>
					    					<td>${money}</td>
					    					<td><s:date name="date" format="yyyy-MM-dd"/></td>
					    					<td>${memo}</td>
					    					<td>&nbsp;</td>
										</tr>
										</s:iterator> 
										<tr>
											<td>
                    							<s:textfield cssClass="inputText" name="money"/>
											</td>
											<td>
                    							<s:textfield cssClass="inputText" name="date" class="Wdate half" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
											</td>
											<td>
                    							<s:textfield cssClass="inputText" name="memo"/>
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
		   					 <input type="button" class="inputButton" id="gatherComplete" value="收款完成" />
                             <a class="p15" href="javascript:history.go(-1);">返回</a>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </s:form>
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
	    $(function(){ 	
			$("#pageForm").validate({
				onfocusout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					money:{
						required:true,
						digits:true,
						min:0
					},
					date:{
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
</body>
</html>