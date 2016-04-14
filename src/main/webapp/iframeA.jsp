<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>test</title>
	<script src="js/jquery-1.7.2.js"></script>
	<script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>
</head>

<body style="margin:0">
<input style="width:15em; padding:4px;" id="aInput" value=""> 
<br />
<button id="exit">确定</button>
<button id="clear">清空</button>
<button id="close">关闭</button>

</div>
<script>
if (art.dialog.data('test')) {
	//$('#aInput').val(art.dialog.data('test'));// 获取由主页面传递过来的数据
};



// 关闭并返回数据到主页面
document.getElementById('exit').onclick = function () {
	var origin = artDialog.open.origin;
	var aValue = $('#aInput').val();
	var input = origin.document.getElementById('data1');
	input.value = aValue;
	input.select();
	art.dialog.close();
};

//关闭并返回数据到主页面
document.getElementById('clear').onclick = function () {
	var origin = artDialog.open.origin;
	var aValue = '';
	var input = origin.document.getElementById('data1');
	input.value = aValue;
	input.select();
	art.dialog.close();
};

//关闭并返回数据到主页面
document.getElementById('close').onclick = function () {
	art.dialog.close();
};



</script>
</body>
</html>


