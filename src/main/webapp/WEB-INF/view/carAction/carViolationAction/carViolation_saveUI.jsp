<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>违章登记</title>
	<link rel="stylesheet" type="text/css" href="skins/main.css">
</head>
<body class="minW">
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>登记违章信息</h1>
		</div>
		<div class="editBlock detail p30">
			<s:form action="carViolation_%{id == null ? 'save' : 'edit'}" id="pageForm">
	        <s:hidden name="id"></s:hidden>
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
						<th>车牌号<span class="required">*</span></th>
						<td>
						  <s:textfield id="car_platenumber" cssClass="carSelector inputText inputChoose" onfocus="this.blur();" name="car.plateNumber"
									 type="text" synchDriverName="driver" synchDriverId="driverId"/>
						</td>
					</tr>
					<tr>
                        <th>司机</th>
                        <td><s:textfield class="userSelector inputText" id="driver" type="text" name="driver.name" driverOnly="true"/>
							<s:textfield id="driverId" name="driver.id" type="hidden" /></td>
					<td>
                    </tr>
					<tr>
						<th>时间<span class="required">*</span></th>
						<td>
								<s:textfield class="Wdate half" name="date" id="date" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"  />
						</td>
					</tr>
					<tr>
						<th>地点<span class="required">*</span></th>
						<td>
								<s:textfield cssClass="inputText" name="place" />
						</td>
					</tr>
					<tr>
						<th>违章事实<span class="required">*</span></th>
						<td>
								<s:textfield cssClass="inputText" name="description" />
						</td>
					</tr>
					<tr>
						<th>罚分</th>
						<td>
								<s:textfield cssClass="inputText" name="penaltyPoint" id="penaltyPoint" />
						</td>
					</tr>
					<tr>
						<th>罚款（元）</th>
						<td>
								<s:textfield cssClass="inputText" name="penaltyMoney" id="penaltyMoney" />
						</td>
					</tr>
					<tr>
						<th>是否已经处理</th>
						<td>
							<s:checkbox class="m10" id="dealt" name="dealt"/>
						</td>
					</tr>
					<tr>
						<th>处理日期</th>
						<td>
								<s:textfield class="Wdate half" name="dealtDate" id="dealtDate" type="text" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})"  />
						</td>
					</tr>
					
					<tr>
	                <td colspan="2">
		                	<input class="inputButton" type="submit" value="提交" />
		                	<a class="p15" href="javascript:history.go(-1);">返回</a> 
	                </td>
	            </tr>
				</tbody>
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
					'car.plateNumber':{
						required:true,
					},
					date:{
						required:true,
					},
					place:{
						required:true,				
					},
					penaltyPoint:{
						digits:true,
						 min:1
					},
					penaltyMoney:{
						digits:true,
						min:1
					},
				}
			});
			   formatDateField1($("#date"));
			   formatDateField1($("#dealtDate"));
		});
	   
	</script>
</body>
</html>