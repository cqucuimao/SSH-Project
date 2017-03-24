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
         <%-- <s:form action="car_editDefaultDriver" name="pageForm" id="pageForm"> --%>
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
                        		<%-- <s:textfield cssClass="inputText" name="plateNumberEDD"/> --%>
                        		<cqu:carAutocompleteSelector name="car"/>
                        		<!--<input name="car" id="car" value="{}">  -->
                        	</td>
                    </tr>
                    <tr>
                        <th>默认司机</th>
                        <td>
                        	<cqu:userAutocompleteSelector name="user"/>
						</td>
                    </tr>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                            <input class="inputButton coverOff" type="submit" value="确定" id="editdefaultDriver"/>
                            <a class="p15" href="javascript:history.go(-1);">返回</a>
                        </td>
                    </tr>
                </tfoot>
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
	    
	 /*    $(".inputButton[id=findCar]").click(function() {
	    	/* if ($("#car").val() == "") {
	    		$("#defaultDriver").hide();
				alert("车牌号不能为空！");
				coverHidden();
			}else{ */
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
						if(data.errorMessage=="")
							$("#user").val(data.driverName);
						else
							{
								alert(data.errorMessage);
								$("#defaultDriver").hide();
							}
						coverHidden();
					},
					error:function(){
						coverHidden();
					}
				});
			
		}) */
	
	   $(".inputButton[id=editdefaultDriver]").click(function() {
	    	var plateNumber = $("#car").val();
	    	var defaultDriver = $("#user").val();
			$.ajax({
				url:'car_editDefaultDriver.action',
				type:'post',
				data:{
					plateNumberEDD:plateNumber,
					defaultDriverEDD:defaultDriver
				},
				dataType:'json',
				success : function(data){
					if(data.errorMessage=="")
						{
						alert("修改成功！");
						}
					else{
						alert(data.errorMessage);
						}
					coverHidden();
					}
				},
				error:function(){
					alert("error");
					coverHidden();
				}
			});
	})
    </script>
</cqu:border>

