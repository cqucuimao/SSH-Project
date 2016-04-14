// 声明搜索结果集
var resultList;
// 声明当前活跃index
var activeId;
// 定义地图,中心为重庆大学
var map = new BMap.Map("allmap");
var point = new BMap.Point(106.476088, 29.571658);
map.centerAndZoom(point, 15);
map.enableScrollWheelZoom(true); 

// 创建标注
var pt = new BMap.Point(106.476088, 29.571658);
var myIcon = new BMap.Icon("skins/images/location.png", new BMap.Size(34, 45), {
	anchor : new BMap.Size(17, 45)
});// 图标尺寸和图像大小一致
var marker2 = new BMap.Marker(pt, {
	icon : myIcon
});

// 活跃栏位
$(document).ready(function() {

	activeId = window.parent.activeId;
});
// 测试搜索函数
var local = new BMap.LocalSearch(marker2.getPosition(), {
	renderOptions : {
		map : map,
		autoViewport : false,
		selectFirstResult : false
	},
	pageCapacity : 10,
	onSearchComplete : function(results) {
		resultList = results;
		pageList(results);
	}
});

//
map.addOverlay(marker2); // 将标注添加到地图中
marker2.enableDragging();
marker2.addEventListener("dragend", function showInfo(f) {
	var cp = marker2.getPosition();
	var geoc = new BMap.Geocoder();

	// 清空搜索结果
	$("#" + activeId + "list").empty();// 清空搜索结果
	geoc.getLocation(cp,
			function(rs) {
				var addComp = rs.addressComponents;
				var info = addComp.province + addComp.city + addComp.district
						+ addComp.street + addComp.streetNumber;

				if (rs.surroundingPois.length >= 2) {

					$("#" + activeId + "location").val(
							rs.surroundingPois[1].title);
					$("#" + activeId + "detailLocation").val(
							rs.surroundingPois[1].address);

					// 设置经纬度
					setCookie("lng", rs.surroundingPois[1].point.lng);
					setCookie("lat", rs.surroundingPois[1].point.lat);
				} else if (rs.surroundingPois.length >= 1) {

					$("#" + activeId + "location").val(
							rs.surroundingPois[0].title);
					$("#" + activeId + "detailLocation").val(
							rs.surroundingPois[0].address);

					// 设置经纬度
					setCookie("lng", rs.surroundingPois[0].point.lng);
					setCookie("lat", rs.surroundingPois[0].point.lat);
				} else {
					$("#" + activeId + "location").val(
							addComp.district + addComp.street
									+ addComp.streetNumber);
					$("#" + activeId + "detailLocation").val(info);

					// 设置经纬度
					setCookie("lng", rs.point.lng);
					setCookie("lat", rs.point.lat);
				}

				// 设置cookie
				setCookie("location", $("#" + activeId + "location").val());
				setCookie("locationDetail",
						$("#" + activeId + "detailLocation").val());

			});

});

// 文本框事件响应搜索
var sOldValue = "", oldid;
function tttt(x) {

	// 活跃id随时切换
	activeId = x.id.split('location')[0];
	vNewValue = x.value;
	if ($.trim(vNewValue).length == 0) {
		sOldValue = vNewValue;
		oldid = activeId;
		$("#" + activeId + "list").empty();// 清空搜索结果
		$("#" + activeId + "detailLocation").val("");// 地址清空
	} else if ((sOldValue != vNewValue && vNewValue != "")
			|| (oldid != activeId && sOldValue == vNewValue && vNewValue != "")) {
		sOldValue = vNewValue;
		oldid = activeId;
		local.search(sOldValue);// 搜索
		$("#" + activeId + "detailLocation").val("");// 地址清空
	}
}

// 搜索结果点击响应函数,回填数据到选择列表
function searchResult(r) {

	// 赋值
	$("#" + activeId + "location").val(resultList.wr[r].title);
	$("#" + activeId + "detailLocation").val(resultList.wr[r].address);

	// 清空数据来隐藏面板
	$("#" + activeId + "list").empty();// 清空搜索结果

	// 设置cookie
	setCookie("location", $("#" + activeId + "location").val());
	setCookie("locationDetail", $("#" + activeId + "detailLocation").val());

	// 设置经纬度
	setCookie("lng", resultList.wr[r].point.lng);
	setCookie("lat", resultList.wr[r].point.lat);

	// 值塞回去,这是处理不点开地图是经纬度赋值
	$("#" + activeId + "lng").val(resultList.wr[r].point.lng);
	$("#" + activeId + "lat").val(resultList.wr[r].point.lat);

	// 移动地图和标注
	var point = resultList.wr[r].point;
	map.setCenter(point);
	marker2.setPosition(point);

}
// 分页
function pageList(resultList) {

	var total = resultList.wr.length;
	// 没有数据,直接返回
	if (total == 0) {
		$("#" + activeId + "list").empty();// 清空搜索结果
		$("#" + activeId + "detailLocation").val("");// 地址清空
		return;
	}
	var pageclickednumber = 1;
	var ul = $("#" + activeId + "list"); // 获取UL对象
	// 先显示五条
	ul.empty();
	// 够五条
	if (total >= 5) {
		for (var i = 0; i < 5; i++) {
			ul.append("<li><a href=\"javascript:searchResult(" + i + ")\" >"
					+ resultList.wr[i].address + "-" + resultList.wr[i].title
					+ "</a></li>");
		}
	} else {// 不够五条

		for (var i = 0; i < total; i++) {
			ul.append("<li><a href=\"javascript:searchResult(" + i + ")\" >"
					+ resultList.wr[i].address + "-" + resultList.wr[i].title
					+ "</a></li>");
		}
		// 分页时响应处理数据
	}	
}

$(".demoBtn3").click(
		function() {

			popWin.showWin("840", "660", "查看地图", "mapIndex.jsp",
					$(this).prev()[0].id.split('detailLocation')[0]);
		});

// 调用父窗口关闭事件
$(".closeButton").click(function() {

	window.parent.popWin.childClose();
});
// cookie设置函数
function setCookie(name, value) {
	//var Days = 30;
	//var exp = new Date();
	//exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
	//document.cookie = name + "=" + escape(value) + ";expires="
	//		+ exp.toGMTString();
	document.cookie = name + "=" + escape(value) + ";";
}

// cookie获取函数
function getCookie(name) {
	var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
	if (arr = document.cookie.match(reg))
		return unescape(arr[2]);
	else
		return null;
}