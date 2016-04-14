$(function(){
	menuInit();
});
function menuInit(){
	var Menu = $("#menu");
	Menu.find("dl").not(".on").find("dd").hide();
	Menu.find("dl.on").find("dd").show();
	Menu.find("dl").not(".on").find(".group-collapsed").removeClass("expanded");
	Menu.find("dl.on").find(".group-collapsed").addClass("expanded");
	Menu.find("dt").click(function(){ //一级菜单点击
		var this_ = $(this),
			dl = this_.closest("dl");
		if(dl.find("dd").eq(0).is(":visible")){
			dl.find("dd").hide();
			dl.find(".group-collapsed").removeClass("expanded");
		}else {
			dl.find(".group-collapsed").addClass("expanded");
			dl.find("dd").show();
			Menu.find("dl").not(dl).find("dd").hide();
			Menu.find("dl").not(dl).find(".group-collapsed").removeClass("expanded");
		}
	});
	Menu.find("dd").click(function(){ //二级菜单点击
		var this_ = $(this),
			dl = this_.closest("dl");
		Menu.find("dd.focus").not(this_).removeClass("focus");
		this_.addClass("focus");
		Menu.find("dl.on").not(dl).removeClass("on");
		dl.addClass("on");
		Menu.find("dl").not(dl).find("dd").hide();
	});
}




