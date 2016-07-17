<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
</head>
<body class="minW">
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>未收款订单列表</h1>
			<p>&nbsp;&nbsp;</p>
		</div>
		<div class="editBlock search">
			<s:form id="queryForm" action="unpaidOrder_list">
			<table>
				<tr>
					<th>单位名称</th>
					<!--<td><s:textfield cssClass="inputText" name="customerOrganization.name" type="text" /></td>  -->
					<td>
					    <s:textfield id="customer_organization_name" name="customerOrganization.name" cssClass="inputText" type="text"/>
					    <input id="customer_organization_id" type="hidden">
					</td>
					<th>订单时间</th>
					<td>
						<s:textfield class="Wdate half" style="width:120px;" type="text" name="actualBeginDate" id="startTime" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" >
						  <s:param name="value"><s:date name="actualBeginDate" format="yyyy-MM-dd"/></s:param>
						</s:textfield>
						- 
						<s:textfield class="Wdate half" style="width:120px;" type="text" name="actualEndDate" id="endTime" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd'})" >
						  <s:param name="value"><s:date name="actualEndDate" format="yyyy-MM-dd"/></s:param>
						</s:textfield>
                    </td>
                    <td>
						<input id="queryBn" class="inputButton" type="submit" value="查询" />
					</td>
					<td>
						<input class="inputButton" type="button" id="newOrderStatement" value="新增对账单" name="button" />
						<input class="inputButton" type="button" id="addOrderStatement" value="添加到对账单" name="button" />
					</td>
                </tr>
			</table>
			</s:form>
		</div>
		<div id="totalMoneyDiv" style="display:none;text-align:right;">
		      <span style="color:red;font-size:18px;">总金额: </span>
		      <span id="selectedTotalPrice" style="color:red;font-size:18px;"></span>
		      <span style="color:red;font-size:18px;"> 元</span>
		</div>
		<div class="dataGrid">
			<div class="tableWrap">
				<table id="dataTable">
					<colgroup>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
						<col></col>
					</colgroup>
					<thead>
						<tr>
							<th><input type="checkbox" id="allChecked"/>全选</th>
							<th>单位</th>
							<th>乘车人</th>
							<th>计费方式</th>
							<th>车型</th>
							<th>起止时间</th>
							<th>起止地点</th>
							<th>里程(KM)</th>
							<th>天数(天)</th>
							<th>金额(元)</th>
						</tr>
					</thead>
					<tbody class="tableHover">
					 <s:iterator value="orderList">
						<tr>
						    <td><input type="checkbox" id="${id}" class="checkboxItems"/></td>
							<td>${customerOrganization.name}</td>
							<td>${customer.name}</td>
                			<td>${status.label}</td>
							<td>${carServiceType.title}</td>
							<td><s:date name="actualBeginDate" format="yyyy-MM-dd HH:mm"/>&nbsp;&nbsp;-&nbsp;&nbsp;<s:date name="actualEndDate" format="yyyy-MM-dd HH:mm"/></td>
							<td>${fromAddress}-${fromAddress}</td>
							<td>${actualMile}</td>
                			<td>${actualDay}</td>
							<td><fmt:formatNumber value="${actualMoney}" pattern="#.0"/></td>
						</tr>
						</s:iterator> 
					</tbody>
				</table>
			</div>
	   </div>
	   <div id="newOrderStatementDiv" class="editBlock search" style="display:none;">
	        <table>
	          <tr>
	               <td>对账单名称</td><td><input type="text" id="newOrderStatementName" class="inputText" style="width:300px;"/></td>
	          </tr>
	          <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>
	          <tr>
	               <td align="center" colspan="2"><input type="button" id="newOk" class="inputButton" value="确定"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" id="newCancel" class="inputButton" value="取消" /></td>
	          </tr>
	        </table>
	   </div>
	   <div id="addOrderStatementDiv" class="editBlock search" style="display:none;">
	        <table>
	          <tr>
	               <td>对账单名称</td><td><select id="orderStatementItems" style="width:300px;"></select></td>
	          </tr>
	          <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>
	          <tr>
	               <td align="center" colspan="2"><input type="button" id="addOk" class="inputButton" value="确定"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" id="addCancel" class="inputButton" value="取消" /></td>
	          </tr>
	        </table>
	   </div>
	</div>
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>	
	<script type="text/javascript" src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
    <script type="text/javascript" src="js/artDialog4.1.7/jquery.artDialog.source.js"></script>
    <script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>	
	<script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>	
	<script type="text/javascript">
	
	     //选中的订单的总价格
	     var selectedTotalPrice=0;
	     $(document).ready(function(){
	       //为所有复选框绑定总价格计算事件
 		   $(".checkboxItems").click(function(){
 			 //显示订单总价格
 			 $("#totalMoneyDiv").show();
 			 if($(this).is(":checked")){
			    //获取当前复选框选中的order的金额 
			    var moneyTd=$(this).parent().parent().find("td:last");
			    //获取订单金额
			    selectedTotalPrice+=parseFloat(moneyTd.text());
			 }else{
				//获取当前复选框选中的order的金额 
				var moneyTd=$(this).parent().parent().find("td:last");
				//获取订单金额
				selectedTotalPrice-=parseFloat(moneyTd.text());
			 }
 			 $("#selectedTotalPrice").html(selectedTotalPrice);
 		   }); 
	       
	       //为全选按钮绑定总价格计算事件
	       $("#allChecked").click(function(){
	    	   //显示订单总价格
	 		   $("#totalMoneyDiv").show();
	    	   //选中的订单的总价格
	  	       selectedTotalPrice=0;
	    	   if($(this).is(":checked")){
	    		    $(".checkboxItems").each(function(){
	    		      //获取当前复选框选中的order的金额 
				      var moneyTd=$(this).parent().parent().find("td:last");
				      //获取订单金额
				      selectedTotalPrice+=parseFloat(moneyTd.text());
	    		    });
	    		    $("#selectedTotalPrice").html(selectedTotalPrice);
	    	   }else{
	    		   $("#selectedTotalPrice").html(0);
	    	 }
	       });
	     });
	     
	     //用于记录选中的复选框的对应的order的id
	     var orderIds;
	     
	     //新增对账单对话框
	     var newOrderstatementDialog
	     //新增对账单按钮功能
	     $("#newOrderStatement").bind("click",function(){
	    	 //用于记录选中的复选框的对应的order的id
	    	 orderIds=new Array();
	    	 var index=0;
	    	 //用于保存第一条被选中记录的单位名称
	    	 var firstSletedOrgName;
	    	 //用于保存第一条被选中记录的起始年月
	    	 var orderDate;   
	    	 //获取表格数据中的所有复选框对象
	    	 var checkboxes=$("#dataTable").find("tr").find("td").find("input");
	    	 checkboxes.each(function() {      // 每一个复选框
	    		 if($(this).is(":checked")){
	    			  //获取第一条被选中记录的单位名称
	    			  if(index==0){
	    				 var currentTr=$(this).parent().parent(); //获取被选中的首行
	    				 var orgNameCol=currentTr.children()[1];  //获取首行的第2列
	    				 var orderDateCol=currentTr.children()[5];  //获取首行的第5列 即 起止时间
	    				 firstSletedOrgName=$(orgNameCol).text(); //获取第2列中的文本值
	    				 orderDate=$(orderDateCol).text(); //获取第2列中的文本值
	    		      }
	    			  //获取被选中复选框的对应的order的id
		    		  orderIds[index++]=$(this).prop("id"); 
	    		  }    
	    	 });
	    	 //如果没有任何订单被选中，则提示
	    	 if(index==0){
	    		 alert("未选择任何订单");
	    		 return false;
	    	 }
	    	 //获取订单开始时间,并格式化成yyyMM
             var orderStartTime=formtToYYYYMM(orderDate.substring(0,8));
	    	 //获取新增订单名
	    	 var newOrderStatementName=firstSletedOrgName+orderStartTime;
	    	 $("#newOrderStatementName").val(newOrderStatementName);
	    	 
	    	 //新增对账单对话框
	         newOrderstatementDialog = art.dialog({
                                               height:200,
                                               width:500,
 	                                           title: "新增对账单",
                                               lock: true,      //遮罩层效果
                                               drag: false,     //拖动效果
                                               content: document.getElementById("newOrderStatementDiv")
             });
	      });
	     //为新增对账单对话框绑定 确认 事件
    	 $("#newOk").bind("click",function () {
    	    //刷新对账单的名称
    		newOrderStatementName=$("#newOrderStatementName").val();
   	    	//判断订单名称是否存在，如果存在 提醒 重命名，否则 提交该表单   中文参数需要encodeURI函数转码
       	    $.get("orderStatement_isOrderStatementExist.action?orderStatementName="+encodeURI(newOrderStatementName)+"&timestamp="+new Date().getTime(),function(json){
       	    	if(json.status==1){ //表明当前对账单名称已经存在
       	    		  alert("当前对账单名称已经存在，请重新命名!");   
       	    	  }else{
       	    		  //将选中的订单记录的id序列发动到服务器端，用于后台生成相应的对账单
                      var idStr="";
         	    	  for(var i=0;i<orderIds.length;i++){
         	    		  idStr+=orderIds[i]+",";
         	    	  }
         	    	  //去掉最后一个多余的","
         	    	  idStr=idStr.substring(0,idStr.length-1);
         	    	  //请求中的中文参数需要encodeURI函数转码
         	    	  $.get("orderStatement_newOrderStatement.action?orderIds="+idStr+"&orderStatementName="+encodeURI(newOrderStatementName)+"&timestamp="+new Date().getTime(),function(json){
         	    		    if(json.status==1){
         	    		       $("#queryForm").submit();
         	    		    }
         	    	  });
       	    	   }
       	     });
           });
    	 //为新增对账单对话框绑定 取消 事件
    	 $("#newCancel").bind("click",function () {
         	 newOrderstatementDialog.close();
         });
	     
    	 //显示对账单对话框
	     var addOrderstatementDialog;
	     //添加对账单按钮功能
         $("#addOrderStatement").bind("click",function(){
        	 
        	 var selectedOrderNum=0;
        	 //获取表格数据中的所有复选框对象
	         var checkboxes=$("#dataTable").find("tr").find("td").find("input");
	         checkboxes.each(function() { // 每一个复选框
	        	  if($(this).is(":checked")){
			         selectedOrderNum++;
		          }
	         });
	         if(selectedOrderNum==0){
	        	alert("未选择任何订单");
	    		return false;
	         }
        	 //获取未付款的对账单名称列表
        	 $.get("orderStatement_orderStatementList.action?"+"timestamp="+new Date().getTime(),function(orderStatementNames){ 
        		//设置select选择菜单
       	   	     var selector=$("#orderStatementItems");     
       	         for(var i=0;i<orderStatementNames.length;i++){ 
       	             selector.append('<option value="'+i+'">'+orderStatementNames[i]+'</option>');     
       	         }
       	         
       	          //显示对账单对话框
       	    	  addOrderstatementDialog = art.dialog({
                        height:160,
                        width:500,
        	             title: "添加到对账单",
                        lock: true,      //遮罩层效果
                        drag: false,     //拖动效果
                        content: document.getElementById("addOrderStatementDiv"),
                  });
        	 });
	     });
         //为添加到对账单对话框绑定 确认 事件
	     $("#addOk").bind("click",function (){
	    		//获取被选中的下拉列表中对账单的名称
         	  var addOrderStatementName=$("#orderStatementItems option:selected").text();
         	  //用于记录选中的复选框的对应的order的id
  	    	  var orderIds=new Array();
  	    	  var index=0;
         	  //获取表格数据中的所有复选框对象
 	          var checkboxes=$("#dataTable").find("tr").find("td").find("input");
 	          checkboxes.each(function() { // 每一个复选框
 		                                   if($(this).is(":checked")){
 			                                  //获取被选中复选框的对应的order的id
	    		                                  orderIds[index++]=$(this).prop("id"); 
 		                                   }
 	          });
 	          //将选中的订单记录的id序列发动到服务器端，用于后台生成相应的对账单
              var idStr="";
              for(var i=0;i<orderIds.length;i++){
                  idStr+=orderIds[i]+",";
              }
              //去掉最后一个多余的","
              idStr=idStr.substring(0,idStr.length-1);
              $.get("orderStatement_addOrderStatement.action?orderIds="+idStr+"&orderStatementName="+encodeURI(addOrderStatementName)+"&timestamp="+new Date().getTime(),function(json){
    		        if(json.status==1){
    		        	addOrderstatementDialog.close();
    		        	$("#queryForm").submit();
    		        }
    	      });
	     });
	         
	     //为添加到对账单对话框绑定 确认 事件
	     $("#addCancel").bind("click",function (){
	    		 addOrderstatementDialog.close();
	     });
         
         //日期格式转换 yyyy-MM-dd ===> yyyyMMdd
         function formtToYYYYMM(fromDate){
        	 var yyyy=fromDate.substring(0,4);
        	 var mm=fromDate.substring(5,7);
        	 return yyyy+mm;
         }
	</script>
</body>
</html>
