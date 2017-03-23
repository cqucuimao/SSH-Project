<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>修改默认司机</h1>
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
            <table>
            	<colgroup>
					<col width="80"></col>
					<col width="120"></col>
					<col></col>
					<col></col>
					<col></col>
					<col></col>
				</colgroup>
                <tbody>
                	<tr>
                        	<th>车牌号<span class="required">*</span></th>
                        	<td>
                        		<%-- <s:textfield cssClass="inputText" name="plateNumberEDD"/> --%>
                        		<cqu:carPlateNumber name="car"/>
                        		<!--<input name="car" id="car" value="{}">  -->
                        	</td>
                        	<td>
                        	<span style="width:20px;"></span>
                        	</td>
                        	<td>
                        		<input class="inputButton" type="submit" id="findCar" value="查询" name="button" />
                            	<a class="p15" href="javascript:history.go(-1);">返回</a>
                        	</td>
                    </tr>
                    
                    
                    <tr id="defaultDriver" style="display:none;">
                        <th>默认司机</th>
                        <td>
                        	<cqu:userSelector name="defaultDriverEDD"/>
						</td>
						<td>
                        	<span style="width:20px;"></span>
                        	</td>
						 <td>
                            <input class="inputButton coverOff" type="submit" value="确定" />
                        </td>
                    </tr>
                </tbody>
            </table>
        
        </div>
    </div>
    <script type="text/javascript">
	    $(function(){
			$("#pageForm").validate({
				submitout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					customerOrganizationLabel:{
						required:true,
					},
					name:{
						required:true,
					},
					phonesStr:{
						required:true,
					}
				}
			});
		});
	    
	    $(".inputButton[id=findCar]").click(function() {
	    	$("#defaultDriver").show();
	    	var plateNumber = $("#car").val();
// 			$.get("car_queryDefaultDriver.action?plateNumberEDD="+encodeURI(plateNumber),function(data){
// 				alert("ture");
// 				//$("#defaultDriverEDD").val(data);
// 				coverHidden();
// 			});
			$.ajax({
				url:'car_queryDefaultDriver.action',
				type:'post',
				data:{
					plateNumberEDD:plateNumber
				},
				dataType:'json',
				success : function(data){
					alert(data.driverName);
					$("#defaultDriverEDD").val(data.driverName);
					coverHidden();
				},
				error:function(){
					alert("error");
					coverHidden();
				}
			});
			
	})
    </script>
</cqu:border>

