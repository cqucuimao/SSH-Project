<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title" style="">
            <h1>编辑司机动作</h1>
        </div>
        <div class="editBlock detail p30">

            <table>
                <tbody>
                    <tr>
                        <th width="15%"><s:property value="tr.getText('order.Order.sn')" />：</th>
                        <td width="35%">${sn }</td>
                        <th width="15%"><s:property value="tr.getText('order.Order.customerOrganization')" />：</th>
                        <td width="35%">${customerOrganization.name }</td>
                    </tr>
                    <tr>
                        <th><s:property value="tr.getText('order.Order.phone')" />：</th>
                        <td>${customer.name}（${phone}）</td>
                        <th>计划起止时间：</th>
                    	<td>${planDateString }</td>
                     </tr>
                    <tr>                	
                    	<th><s:property value="tr.getText('order.Order.car')" />：</th>
                        <td>
                        	<s:if test="car!=null">
                        		${car.plateNumber }
                        	</s:if>
						</td> 
                        <th><s:property value="tr.getText('order.Order.serviceType')" />：</th>
                        <td>${serviceType.title }</td>
                    </tr>
                    <tr>             	
                        <th><s:property value="tr.getText('order.Order.fromAddress')" />：</th>
                        <td>${fromAddress }</td>
						<th><s:property value="tr.getText('order.Order.driver')" />：</th>
                        <td>
                        	<s:if test="driver!=null">
                        		${driver.name }（${driver.phoneNumber}）
                        	</s:if>
                        </td> 
					</tr>
                    <tr>    
                        <th><s:property value="tr.getText('order.Order.toAddress')" />：</th>
                        <td>
                        	<s:if test="toAddress!=null">
                        		${toAddress }
                        	</s:if>
                        </td>
                        
                    </tr>            
                </tbody>
             </table><br><br><br><br>
             <div class="title" style="">
            	<h2>已有操作</h2>
        	</div>
             <table>
             	<tbody>
           
             		<s:iterator value="driverActionVOList" status="iteratorStatus">
             		<tr>   
             			 			   			
             			<td width="15%">${status.label}</td>
             			<s:if test="#iteratorStatus.last">
             				<td width="35%" class="last" id="TD${iteratorStatus.index }"><s:date name="date" format="yyyy-MM-dd HH:mm:ss"/></td>
             			</s:if>
             			<s:else>
             				<td width="35%" id="TD${iteratorStatus.index }"><s:date name="date" format="yyyy-MM-dd HH:mm:ss"/></td>
             			</s:else>
             			<td width="15%">
             				<a href="#" class="modify" onclick="modify('${id}','${date }','${iteratorStatus.index }')"><i class="icon-operate-modify" title="修改"></i></a>
             				<!-- 当前是否为最后一个元素 -->
             				<s:if test="#iteratorStatus.last">
             					<s:a action="order_deleteDriverAction?actionId=%{id}" onclick="return confirm('确认要删除吗？');"><i class="icon-operate-delete" title="删除"></i></s:a>
             				</s:if>             				
             			</td>
             			<td width="35%"></td>
             		</tr>
             		</s:iterator>
             	</tbody>
             </table>
          	<table>
                <tfoot>                    
                    <tr>
                        <th></th>
                        <td></td>
                        <th></th>
                        <td></td>
                    </tr>
                    <tr>
                        <td colspan="4">  
                        <form id="actionForm" action="">                   
                        		操作时间：
                        		<input class="Wdate half" id="actionTime" name="actionTime" onfocus="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" /><span class="required">*</span>&nbsp;&nbsp;
                        		<input type="hidden" name="orderId" value="${id }">
                        		<s:if test="canAddAcceptAction">                       			
                        				<input type="submit" id="accept" class="inputButton coverOff needTime" value="接受订单"/>                      		
                        		</s:if>
                        		<s:else>                      			
                        				<input disabled="disabled" type="button" class="inputButton needTime" value="接受订单"/>                      			
                        		</s:else>                        		  
                        		<s:if test="canAddBeginAction">                      		                       			
                        				<input type="submit" id="begin" class="inputButton needTime" value="开始订单"/>                      			
                        		</s:if>
                        		<s:else>                      		                      			
                        				<input disabled="disabled" type="button" class="inputButton needTime" value="开始订单"/>                      		
                        		</s:else>                      		
                        		<s:if test="canAddGetonAction">                       			
                        				<input type="submit" id="geton" class="inputButton needTime" value="客户上车"/>                     			
                        		</s:if>
                        		<s:else>                        			
                        				<input disabled="disabled" type="button" class="inputButton needTime" value="客户上车"/>                      			
                        		</s:else>                      		
                        		<s:if test="canAddGetoffAction">                       			
                       				<input type="submit" id="getoff" class="inputButton needTime" value="客户下车"/>                        			
                        		</s:if>
                        		<s:else>                        			
                        				<input disabled="disabled" type="button" class="inputButton needTime" value="客户下车"/>                       			
                        		</s:else>                       		
                        		<s:if test="canAddEndAction">                       			
                        				<input type="submit" id="end" class="inputButton needTime" value="结束订单"/>                      			
                        		</s:if>
                        		<s:else>                        			
                        				<input disabled="disabled" type="button" class="inputButton needTime" value="结束订单"/>                       			
                        		</s:else>                         		                      		
                        		<s:if test="canEditOrderBill"> 
                        				<input type="submit" id="editOrderBill" class="inputButton" value="编辑派车单"/>                       			
                        		</s:if>                     		
                            	<a class="p15" href="javascript:history.go(-1);">返回</a>
                            </form>
                        </td>
                    </tr>
                </tfoot>
            </table>
          </div>
    </div>
    <script type="text/javascript">
    
   	
   	$(".needTime").click(function(){
   		var last = $(".last").text().replace('-','/').replace('-','/');
   		var actionTime = $("#actionTime").val().replace('-','/').replace('-','/');
   		if(actionTime == ""){
   	   		alert("操作时间不能为空！");
   	   		coverSwitcherOff();
   	   		return false;
   	   	}
   		if(Date.parse(last)>Date.parse(actionTime)){
   			alert("操作时间不合法！");
   			coverSwitcherOff();
   	   		return false;
   		}
   	})
   	
   	$("#accept").click(function(){   		
   		$("#actionForm").attr("action","order_addAcceptAction.action");
   	})
   	$("#begin").click(function(){   		
   		$("#actionForm").attr("action","order_addBeginAction.action");
   	})
   	$("#geton").click(function(){   		
   		$("#actionForm").attr("action","order_addGetonAction.action");
   	})
   	$("#getoff").click(function(){   		
   		$("#actionForm").attr("action","order_addGetoffAction.action");
   	})
   	$("#end").click(function(){   		
   		$("#actionForm").attr("action","order_addEndAction.action");
   	})     	
   	$("#editOrderBill").click(function(){   		
   		$("#actionForm").attr("action","order_editOrderBillUI.action?orderId=%{id}");
   	})   
    
    function modify(actionId,time,index){
   		var after = index-(-1);//计算index+1
   		var beforeTime = $("#TD"+(index-1)).text();
   		var afterTime = $("#TD"+after).text();
    	url="order_popupModify.action?actionId="+actionId+"&time="+time+"&beforeTime="+beforeTime+"&afterTime="+afterTime;
    	art.dialog.open(url,{
            id: "popupModify",
            title: "修改时间",
            width: 330,
            height: 200,
            padding: 0,
            lock: true
        });
    }
    
		
	</script>
</cqu:border>
