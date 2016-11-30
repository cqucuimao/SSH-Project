<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>分组信息</h1>
        </div>
        <div class="editBlock detail p30">
        <s:form action="realtime_%{monitorGroupId == null ? 'monitorGroupAdd' : 'monitorGroupEdit'}" id="pageForm">
        	<s:hidden name="monitorGroupId"></s:hidden>
            <table>
                <tbody>
                	<tr>
                        <th>分组名称<span class="required">*</span></th>
                        <td>
                        	<s:textfield cssClass="inputText" name="monitorGroupTitle"/>
                        </td>
                    </tr>
                    <tr>
                    	<th>分组车辆</th>
                    	<td style="background-color:;width:350px">
	                        <s:select name="listCarIds" multiple="true" cssClass="SelectStyle" 
	          				list="carsList" listKey="id" listValue="plateNumber" ondblclick="moveOption(document.pageForm.listCarIds, document.pageForm.selectedCarIds)"
	          				style="height:350px"/>
          				</td>
          				<td align="center" style="background-color:;width:200px">
          					<input type="button" value="全部添加" onclick="moveAllOption(document.pageForm.listCarIds, document.pageForm.selectedCarIds)"><br/> 
							<br/>
							<input type="button" value="添加" onclick="moveOption(document.pageForm.listCarIds, document.pageForm.selectedCarIds)"><br/> 
							<br/> 
							<input type="button" value="移除" onclick="moveOption(document.pageForm.selectedCarIds, document.pageForm.listCarIds)"><br/> 
							<br/> 
							<input type="button" value="全部移除" onclick="moveAllOption(document.pageForm.selectedCarIds, document.pageForm.listCarIds)"> 
							<br>
							<s:textfield type="hidden" name="carString" />
						</td>
          				<td>
          					<s:select name="selectedCarIds" multiple="true" cssClass="SelectStyle"
          					 list="selectedList" listKey="id" listValue="plateNumber" ondblclick="moveOption(document.pageForm.selectedCarIds, document.pageForm.listCarIds)"
          					 style="height:350px"/>
          				</td>
                    </tr>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                            <input class="inputButton" type="submit" value="确定" />
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
    <script src="js/jquery-1.7.2.js"></script>
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
    <script type="text/javascript" src="js/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="js/validate/messages_cn.js"></script>
    <script type="text/javascript">
	    $(function(){
			$("#pageForm").validate({
				submitout: function(element) { $(element).valid(); },
				rules:{
					// 配置具体的验证规则
					monitorGroupTitle:{
						required:true,
					},
				}
			});
		});
	    
	    <!--操作全部-->
	    function moveAllOption(e1, e2){ 
	     var fromObjOptions=e1.options; 
	      for(var i=0;i<fromObjOptions.length;i++){ 
	       fromObjOptions[0].selected=true; 
	       e2.appendChild(fromObjOptions[i]); 
	       i--; 
	      } 
	     document.pageForm.carString.value=getvalue(document.pageForm.selectedCarIds); 
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
	     document.pageForm.carString.value=getvalue(document.pageForm.selectedCarIds); 
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
	    } 
	    else if(index==1){ 
	    if (obj.selectedIndex<obj.options.length-1){ 
	    obj.options(obj.selectedIndex).swapNode(obj.options(obj.selectedIndex+1)) 
	    } 
	    } 
	    } 
    </script>
</cqu:border>
