<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
	<link href="skins/main.css" rel="stylesheet" type="text/css" />
</head>
<body class="minW">
    <div class="space">
        <!-- 标题 -->
        <div class="title">
            <h1>导入保养信息</h1>
        </div>
        <div class="editBlock detail p30">
        <s:form action="carCare_importExcelFile" id="pageForm" enctype="multipart/form-data">
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
							<a href="file/保养信息.xls">保养信息模板</a>
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
								<font color="red">请参考保养信息模板，目前仅支持xls格式！</font>
						</td>
                    </tr>
                	
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                            <input id="submit" class="inputButton" type="submit" value="确定" />
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
    <script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
    <script type="text/javascript" src="js/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="js/validate/messages_cn.js"></script>
    <script type="text/javascript">
	    $("#submit").click(function(){
	    	
	    	if($("#file").val() == null || $("#file").val()== ""){
	    		alert("请选择文件！");
	    		return false;
	    	}
	    });
    </script>
</body>
</html>
