<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border bodyClass="null">			
	  <div class="editBlock p20 pt10"><br>
		        <s:form id="timeForm" name="myForm" action="#">
		        <table class="showInfo">	           
		            <tr>
		                <th >
		                		 <h4>导出年月</h4>			                		
		                </th>       
		                <td>
								<s:textfield class="Wdate half" name="gDate" id="gDate" onfocus="new WdatePicker({dateFmt:'yyyy-MM'})" />
						</td>
		            </tr>
		            
		            <tfoot>
		                <tr>
		                    <td></td>
		                    <td>
		                        <input id="btnOil" type="submit" class="inputButton" value="确定" />
		                        <a class="p15" href="#" onClick="popdown('timeModify')">取消</a>
		                    </td>
		                </tr>
		            </tfoot>
		        </table>
		        </s:form>
    </div>
	<script type="text/javascript">	
	 	$("#btnOil").click(function(){
	 		if($("#gDate").val()==""){
	 			alert("年月不能为空！");
	 		}else{
				$("#timeForm").attr("action","carRefuel_OilReport.action");
				$("#timeForm").submit();
	 		}
		}); 
	</script>
</cqu:border>