<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border bodyClass="null">
    <div class="space">
			
		<div class="editBlock search">
        <s:form id="pageForm" action="customerOrganization_popup">
			<table>
				<tr>
					<th><s:property value="tr.getText('order.CustomerOrganization.name')" /></th>
					<td><s:textfield cssClass="inputText" name="customerOrganizationName" type="text" /></td>
					<td>
						<input class="inputButton" type="submit" value="查询"/>
					</td>
				</tr>
			</table>
		</s:form>
		</div>
        
        <!-- 数据列表 -->
        <div class="dataGrid">
            <div class="tableWrap fixW fixed-height">
                <table>
                    <colgroup>
                        <col width="20px"></col>
                        <col width="100px"></col>
                        <col width="240px"></col>
                    </colgroup>
                    <thead>
                        <tr>
                            <th class="alignCenter"></th>
                            <th><s:property value="tr.getText('order.CustomerOrganization.name')" /></th>
                            <th>单位地址</th>
                        </tr>
                    </thead>
                    <tbody class="tableHover">
                        <s:iterator value="recordList">
                        <tr>
                            <td class="alignCenter">
                            	<input type="radio" name="id" value="${id}" data="${name}" />
                            </td>
                            <td>${name}</td>
                            <td>${address.detail}</td>
                        </tr>
                        </s:iterator>
                    </tbody>
                </table>
            </div>
        </div>
			<s:form id="pageForm">
        			<%@ include file="/WEB-INF/view/public/pageView.jspf" %>
        	</s:form>
        <br/>
        <!-- 填充div，防止部分数据被遮盖 -->
        <div style="width:300px;height:60px">  
        </div>
        <div class="bottomBar alignCenter" style="position:fixed;bottom:0;background-color:white;width:300px;height:60px">
            <input id="sure" class="inputButton" type="button" value="确定" />
            <input id="clear" class="inputButton" type="button" value="清空" />
            <input id="close" class="inputButton" type="button" value="关闭" />
        </div>
    </div>
    <script type="text/javascript">
        $(function(){
        	   var selectorName=art.dialog.data("selectorName");
    	       var origin = artDialog.open.origin;
    	       var input_id_value = origin.document.getElementById(selectorName).value;
    	       if(input_id_value!=null)
 	       {
 				$("input[type=radio][name='id'][value='"+input_id_value+"']").attr("checked",'checked');
 	       } 
        	
            $("#sure").click(function(){
            	var newVal=$('input:radio[name="id"]:checked').attr('data');
            	var id=$('input:radio[name="id"]:checked').attr('value');
            	var selectorName=art.dialog.data("selectorName");
            	var origin = artDialog.open.origin;
            	var input = origin.document.getElementById(selectorName+"Label");
            	var input_id = origin.document.getElementById(selectorName);
            	if(newVal != null)
            	input.value = newVal;
            	input.select();
            	
            	input_id.value = id;
            	input.select();
            	art.dialog.close();
            })
            
            $("#clear").click(function(){
            	var origin = artDialog.open.origin;
            	var input = origin.document.getElementById(selectorName+"Label");
            	var input_id = origin.document.getElementById(selectorName);

            	input.value = "";
            	input.select();
            	input_id.value= "";
            	input.select();
            	art.dialog.close();
            })
            
            $("#close").click(function(){
            	art.dialog.close();
            })
        })
    </script>
</cqu:border>
