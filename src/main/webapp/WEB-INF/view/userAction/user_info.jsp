<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link href="skins/main.css" rel="stylesheet" type="text/css" />
</head>
<body>
    <div class="tab_next">
        <table>
            <tr>
            
                <td id="tab1" <s:if test='#tabid == "1"'>class="on"</s:if>  ><a href="#"><span>修改密码</span></a></td>
                <td id="tab3" <s:if test='#tabid == "2"'>class="on"</s:if> ><a href="#"><span>修改手机</span></a></td>
                <td id="tab2"><a href="#"><span>个人信息</span></a></td>
            </tr>
        </table>
    </div>
    <div class="tab_next_con pt_10">
        <div class="editBlock" id="tab1_c">
        <s:form action="user_changePassword">
            <table>
                <tbody>
                    <tr>
                        <th width="80"><span class="orang">*</span>旧密码</th>
                        <td><input name="oldPassword" type="password" class="inputText" /></td>
                    </tr>
                    <tr>
                        <th><span class="orang">*</span>新密码</th>
                        <td>
                            <input name="newPassword" type="password" class="inputText" />
                        </td>
                    </tr>
                    <tr>
                        <th><span class="orang">*</span>确认密码</th>
                        <td><input name="confirmPassword" name="textfield" type="password" class="inputText" id="textfield" /></td>
                    </tr>
                   	<tr>
                   		<th></th>
                   		<td style="color:red;"><s:property value="pass_msg"/></td>
                   	</tr>
                </tbody>
                <tfoot>
                    <tr>
                        <td></td>
                        <td>
                            <input type="submit" class="inputButton" value="确定" />
                            <a class="p15" href="#">取消</a>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </s:form>
        </div>
        <div class="editBlock" id="tab3_c">
        <s:form action="user_changePhoneNumber">
            <table>
            
                <tbody>
                    <tr>
                        <th width="80"><span class="orang">*</span>手机号码</th>
                        <td><s:textfield name="phoneNumber" type="text" class="inputText" /></td>
                    </tr>
      
                   	<tr>
                   		<th></th>
                   		<td style="color:red;"><s:property value="phone_msg"/></td>
                   	</tr>
                </tbody>
                <tfoot>
                    <tr>
                        <td></td>
                        <td>
                            <input type="submit" class="inputButton" value="确定" />
                            <a class="p15" href="#">取消</a>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </s:form>
        </div>
        <div class="editBlock" id="tab2_c">
            <table>
           		<tr>
                    <th width="80">登录名称</th>
                    <td>${loginName}</td>
                </tr>
                <tr>
                    <th width="80">姓名</th>
                    <td>${name}</td>
                </tr>
                <tr>
                    <th>部门</th>
                    <td>${department}</td>
                </tr>
                <tr>
                    <th>电话</th>
                    <td>${phoneNumber}</td>
                </tr>
                <tfoot>
                    <tr>
                        <td></td>
                        <!-- <td>
                            <input type="submit" class="inputButton" value="确定" />
                            <a class="p15" href="#">取消</a>
                        </td> -->
                    </tr>
                </tfoot>
            </table>
        </div>
        
        
    </div>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <s:debug></s:debug>
</body>
</html>

