<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/common.jsp" %>
<cqu:border>
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>导入维修信息</h1>
        </div>	
		<div class="tab_next style2">
			<table>
				<tr>
				    <td><s:a action="carRepairAppointment_list"><span>预约车辆维修</span></s:a></td>
					<td class="on"><a href="#" class="coverOff"><span>车辆维修</span></a></td>
				</tr>
			</table>
		</div>
		<br/>
        <div class="editBlock detail p30">
        <s:form action="carRepair_importExcelFile" id="pageForm" enctype="multipart/form-data">
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
                	<tr><th>模板下载</th>
	                	<td>
							<a href="excelModel/维修信息.xls" class="coverOff">维修信息模板</a>
						</td>
					</tr>
                	<tr>
                        <th>添加文件</th>
						<td>									
									<s:file name="upload" id="file" label="上传文件"/>
						</td>
                    </tr>
                    <tr>
                        <th></th>
						<td>
								<font color="red">请参考维修信息模板，目前仅支持xls格式！</font>
						</td>
                    </tr>
                	
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                            <input id="submit" class="inputButton coverOff" type="submit" value="确定" />
                             <a class="p15" href="javascript:history.go(-1);">返回</a>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </s:form>
        </div>
    </div>
    <script type="text/javascript">
	    $("#submit").click(function(){
	    	
	    	if($("#file").val() == null || $("#file").val()== ""){
	    		alert("请选择文件！");
	    		coverSwitcherOff();
	    		return false;
	    	}
	    });
    </script>
</cqu:border>
