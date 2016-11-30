<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
	<div class="space">
		<!-- 标题 -->
		<div class="title">
			<h1>配置${viewFlag }信息</h1>
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
			<!-- 这里不指定action，由js指定action -->
			<s:form action="" name="pageForm" id="pageForm">
	        <s:hidden name="id"></s:hidden>
			<table> 
				<tbody>
					<tr>
						<th>配置${viewFlag }</th>
						<!-- 多选  start-->
						<td style="background-color:;width:350px">
	                        <s:select name="listIds" multiple="true" cssClass="SelectStyle" 
	          				list="showList" listKey="id" listValue="name" ondblclick="moveOption(document.pageForm.listIds, document.pageForm.selectedIds)"
	          				style="height:250px"/>
          				</td>
          				<td align="center" style="background-color:;width:200px"><br/>
          					<input type="button" value="全部添加" onclick="moveAllOption(document.pageForm.listIds, document.pageForm.selectedIds)"><br/> 
							<br/>
							<input type="button" value="添加" onclick="moveOption(document.pageForm.listIds, document.pageForm.selectedIds)"><br/> 
							<br/> 
							<input type="button" value="移除" onclick="moveOption(document.pageForm.selectedIds, document.pageForm.listIds)"><br/> 
							<br/> 
							<input type="button" value="全部移除" onclick="moveAllOption(document.pageForm.selectedIds, document.pageForm.listIds)"> 
							<br>
							<s:textfield type="hidden" name="idString" />
						</td>
          				<td>
          					<s:select name="selectedIds" multiple="true" cssClass="SelectStyle"
          					 list="selectedList" listKey="id" listValue="name" ondblclick="moveOption(document.pageForm.selectedIds, document.pageForm.listIds)"
          					 style="height:250px"/>
          				</td>
          				<!-- 多选  end-->
					</tr>
					<tr>
	                <td colspan="2">
	        				<input name="actionFlag" type="hidden" value="${actionFlag }">  	    
		                	<input type="button" id="sub" class="inputButton" value="确定"/>
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
	
	// 配置具体的验证规则
	$(function(){    	
		$("#pageForm").validate({
			submitout: function(element) { $(element).valid(); },
			rules:{
				selectedIds:{
					required:true,
				},
			}
	  });
	});
	
	var actionFlag = $("input[name=actionFlag]").val();
	//通过js为提交操作指定action
	if (actionFlag == "for4S"){
		$("#sub").click(function(){
			$('#pageForm').attr("action", "businessParameter_addEmployeesFor4S.action").submit();
		});
	}
	
	if (actionFlag == "forCarCare"){
		$("#sub").click(function(){
			$('#pageForm').attr("action", "businessParameter_addEmployeesForCarCare.action").submit();
		});
	}
	
	if (actionFlag == "approveUser"){
		$("#sub").click(function(){
			$('#pageForm').attr("action", "businessParameter_addReserveCarApplyOrderApproveUser.action").submit();
		});
	}
	
	if (actionFlag == "applyUser"){
		$("#sub").click(function(){
			$('#pageForm').attr("action", "businessParameter_addReserveCarApplyOrderApplyUser.action").submit();
		});
	}
	
	if (actionFlag == "carApproveUser"){
		$("#sub").click(function(){
			$('#pageForm').attr("action", "businessParameter_addReserveCarApplyOrderCarApproveUser.action").submit();
		});
	}
	
	if (actionFlag == "driverApproveUser"){
		$("#sub").click(function(){
			$('#pageForm').attr("action", "businessParameter_addReserveCarApplyOrderDriverApproveUser.action").submit();
		});
	}
	<!--操作全部-->
    function moveAllOption(e1, e2){ 
	      var fromObjOptions=e1.options; 
	      for(var i=0;i<fromObjOptions.length;i++){ 
	       fromObjOptions[0].selected=true; 
	       e2.appendChild(fromObjOptions[i]); 
	       i--; 
      	} 
     	document.pageForm.idString.value=getvalue(document.pageForm.selectedIds); 
    }

    <!--操作单个-->
    function moveOption(e1, e2){ 
        var fromObjOptions=e1.options; 
        for(var i=0;i<fromObjOptions.length;i++){ 
	        if(fromObjOptions[i].selected){ 
		        e2.appendChild(fromObjOptions[i]); 
		        i--; 
       		} 
    	} 
     	document.pageForm.idString.value=getvalue(document.pageForm.selectedIds); 
    } 

    function getvalue(geto){ 
	    var allvalue = ""; 
	    for(var i=0;i<geto.options.length;i++){ 
    		allvalue +=geto.options[i].value + ","; 
    	} 
    	return allvalue; 
    } 

    function changepos1111(obj,index) 
    { 
    	if(index==-1){ 
    		if (obj.selectedIndex>0){ 
    			obj.options(obj.selectedIndex).swapNode(obj.options(obj.selectedIndex-1)) 
    		} 
    	}else if(index==1){ 
    				if (obj.selectedIndex<obj.options.length-1){ 
    						obj.options(obj.selectedIndex).swapNode(obj.options(obj.selectedIndex+1)) 
    				} 
    	} 
    } 
	
	</script>
</cqu:border>