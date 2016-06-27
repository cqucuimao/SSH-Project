window.getDriverDataResult;
window.getPlateNumberData;
window.activeId;
$(document).ready(function(){
    initForm(); //初始化表单
    initDatagrid(); //初始化列表
    tab_nextChange(); //初始化tab切换
    defaultEvent(); //页面默认事件
});

window.adressSelect = function(){
	
	//值回填            	
	$("#"+activeId+"location").val(getCookie("location"));
	$("#"+activeId+"detailLocation").val(getCookie("locationDetail"));
	$("#"+activeId+"lng").val(getCookie("lng"));
	$("#"+activeId+"lat").val(getCookie("lat"));
}
/*===页面默认事件===*/
function defaultEvent(){
	$("a[href='#']").click(function(event){
        event.preventDefault();
    });
    // 输入框占位符调用
	$(".inputText,textarea").each(function(){ 
    	var me = $(this),
    		tip = me.attr('text');
    	if(tip){
    		inputTip(me, tip);
    	}
    });
    //模拟按钮切换
    switchs();
    //弹出选择常用地址框
    $(".icon-favorite").parent().click(function(x){
    	var phone=$("#phone").val();
    	if(phone=="" || phone.length==0)
    		alert("请指定手机号码！");
    	else{
    		art.dialog.open('schedule_historyAddress.action?componentId='+x.target.id.charAt(0)+'&phone='+phone,{
    			title: '常用地址选择', 
    			width: 420, 
    			height: 550,
    			lock : true
    		});
    	}
    });
    //地址详情输入框地图弹出
    $(".icon-map").parent().click(function(x){
    	window.activeId=x.target.id;
    	popup("请选择地点","schedule_map.action",710,560,"adressSelect",true,adressSelect);
    });
    //input-group输入框宽度100%
    if($(".input-group.w").length>0){
    	$(".input-group.w").each(function(){
    		var me = $(this),
	    		inputText = me.find(".inputText"),
	    		w_all = me.parent().width(),
	    		w_addon = getOutterWidth(me.find(".input-group-addon")),
	    		w = w_all-w_addon-getIntval(inputText, 'paddingLeft')-getIntval(inputText, 'paddingRight')-getIntval(inputText, 'border-left-width')-getIntval(inputText, 'border-right-width');
    		inputText.width(w);
    	});
    } 
    //driverList列表设置固定宽度
    driverListW();
    function driverListW(){
    	if($('.tableWrap.fixW')){
	    	var this_ = $('.tableWrap.fixW'),
	    		table = this_.find("table"),
	    		w;
	    	this_.width('10px');
	    	table.width('auto');
	    	var paW = this_.parent().width(),
	    		tableW = getOutterWidth(table);
	    		tableH = getOutterHeight(table);
	    		var scorllH = this_.height()-tableH;
	    	if(scorllH<0){
	    		w = tableW+20;
	    	}else{
	    		w = tableW;
	    	}
	    	if(paW>w){
	    		this_.width('100%');
	    		table.width('100%');
	    	}else{
	    		this_.width(paW);
	    	}
	    }
    }
    
    $(window).resize(function(){
    	driverListW();
    });
}

/*===表单效果===*/
function initForm(){
    try {
        // 输入框聚焦效果
        $("textarea,.inputText,:password,:file").focus(function(){
            var rd = $(this).attr("readonly")||$(this).attr("disabled");
            if(!rd){
                $(this).addClass("onFocus");
                return false;
            }
        }).blur(function(){
            $(this).removeClass("onFocus");
        }); 
        //表单标题行收折
        $("tr .subtitle").click(function(){
            var t = $(this).closest("tr").next("tbody");
            if (t.length == 1) {
                t.toggle();
                $(this).toggleClass("subtitleClose");  
            }
        })   
    } catch (e) {} 
}
/*===列表效果===*/
function initDatagrid(){
    $(".dataGrid tbody").find("tr:odd").addClass("odd");
    $(".dataGrid tbody").find("tr:even").removeClass("odd");
    $(".tableHover").find("tr").hover(function(){
        $(this).addClass("trHover")
    }, function(){
        $(this).removeClass("trHover")
    });   
    $(".dataGrid,.widget").find("tr").find("td:last").addClass("last");
    $(".dataGrid tbody :radio").click(function(event){
    	$(this).closest("tbody").find("tr.on").removeClass("on");
    	$(this).closest("tr").addClass("on");;
    });
    DGCheckboxCtrl();
    //表头checkbox
    $(".dataGrid thead :checkbox").click(function(event){
        event.stopPropagation();
        var itemChk = $(this).closest("table").find("tbody :checkbox").not(":disabled,:hidden");
        itemChk.attr("checked", this.checked);
        DGCheckboxCtrl();
    })
    //tbody checkbox
    $(".dataGrid tbody :checkbox").click(function(event){
        event.stopPropagation();
        var $tmp=$(this).closest("tbody").find(":checkbox").not(":disabled,:hidden");
        var checked = $tmp.length==$tmp.filter(':checked').not(":disabled,:hidden").length;
        $(this).closest("table").find("thead :checkbox").attr('checked',checked);
        DGCheckboxCtrl();
    })  
}
function DGCheckboxCtrl(){
    $(".dataGrid tbody :checkbox").parents("tr").removeClass("on");
    $(".dataGrid tbody :checked").parents("tr").addClass("on");
}
/*===占位符提示===*/
function inputTip(t, tVal){
    t.val(tVal);
    if (t.val() == tVal) {
        t.css("color", "#aaaaaa")
    }
    t.focus(function(){
        if ($(this).val() == tVal) {
            $(this).val("")
            $(this).css("color", "#555555")
        }
    }).blur(function(){
        if ($(this).val() == "") {
            $(this).val(tVal);
            $(this).css("color", "#aaa")
        }else{
            $(this).css("color", "#555");
        }  
    })
}
/*===弹出窗口===*/
function popup(title, url, w, h, id, lock,closeF){
    var hh = top.window.document.documentElement.clientHeight||document.body.clientHeight;
    hh = hh-45;
    if(hh<h){
        h = hh;
    }
    if(lock==undefined){
        lock = true;
    }
    top.art.dialog.open(url,{
        id: id,
        title: title,
        width: w,
        height: h,
        padding: 0,
        lock: lock,
        close:closeF
    });
}
//关闭弹出窗口
function popdown(id){
    top.artDialog({
        id: id
    }).close();
}
/*===tab点击切换内容DIV隐藏显示===*/
function tab_nextChange(){
    $(".tab_next.fixed").next(".tab_next_con").css("marginTop","32px");
    if($(".tab_next_con").length>0){
        tabContentShow();
        $(".tab_next td").not(".noAction").bind("click",function(){
            $(this).closest(".tab_next").find(".on").removeClass('on');
            $(this).addClass("on");
            tabContentShow();
        });
    }
}
function tabContentShow(){
    $(".tab_next_con > div").hide();
    $(".tab_next .on").each(function(){
        var id = $(this).attr("id")+"_c",
            this_ = $("#"+id);
        this_.show();
    });   
}
/*===页面操作成功失败提示效果===*/
(function(tips){
    msg.defaultConfig={
        type:'success',
        text:null,
        follow:null,
        callFn:null
    }
    function msg(config){
        var self = this;
        self.config = $.extend(msg.defaultConfig, config);
        self._init();
    };
    $.extend(msg.prototype, {
        _init: function(){
            var self = this;
            self.type = self.config.type;
            self.text = self.config.text;
            self.follow = self.config.follow;
            self.callFn = self.config.callFn;
            self.t = -1;
            self.mm = 5;
            self.DOT = ".";
	        self.CSS = {
	            MSG:'msg',
	            TIME:'msg_time',
	            S:'success',
	            F:'false',
	            text_S:'恭喜您，操作成功。',
	            text_F:'很遗憾，操作失败。'
	        };
	        self._defaultEvent();
        },
        _defaultEvent:function(){
        	var self = this;
        	if($(self.DOT+self.CSS.MSG).length==0){
        		self._create();
        	}
        },
        _create:function(){
            var self = this;
            var msg = ['<div class="msg">',
	                        '<dl>',
	                            '<dt></dt>',
	                            '<dd class="msg_time">',
	                                '<span></span>秒钟后，自动关闭，您也可以手动<a href="#">关闭</a>',
	                            '</dd>',
	                        '</dl>',
	                    '</div>'
	            ].join('');
	        $("body").append(msg);
	        $(self.DOT+self.CSS.MSG).hide();
	        $(self.DOT+self.CSS.TIME).find("a").click(function(){
	            $(self.DOT+self.CSS.MSG).hide();
	            clearTimeout(self.t);
	        });
        },
        show:function(parameter){
        	var self = this,
        		tips;
        	self._freshConfig(parameter);
        	clearTimeout(self.t);
        	self.mm = 5;
        	var removeName,addName,tips;
        	if(self.type =="false"){
        		removeName = self.CSS.S;
        		addName = self.CSS.F;
        		tips = self.text==null?self.CSS.text_F:self.text;
	        }else{
	            removeName = self.CSS.F;
        		addName = self.CSS.S;
        		tips = self.text==null?self.CSS.text_S:self.text;
	        }
	        self._setValue(removeName,addName,tips);
	        $(self.DOT+self.CSS.MSG).show();
	        self._location();
	        self._show_date_time();

        },
        _freshConfig:function(parameter){
        	var self = this;
        	self.type = msg.defaultConfig.type;
    		self.text= msg.defaultConfig.text;
	        self.follow= msg.defaultConfig.follow;
	        self.callFn= msg.defaultConfig.callFn;
        	if(parameter){
        		if(parameter.type){
	        		self.type = parameter.type;
	        	}
	        	if(parameter.text){
	        		self.text = parameter.text;
	        	}else{

	        		self.text = null;
	        	}
	        	if(parameter.follow){
	        		self.follow = parameter.follow;
	        	}
	        	if(parameter.callFn){
	        		self.callFn = parameter.callFn;
	        	}
        	}
        },
        _setValue:function(a,b,tips){
        	var self = this;
        	var MSG = $(self.DOT+self.CSS.MSG);
        	MSG.removeClass(a);
        	MSG.addClass(b);
        	MSG.find("dt").text(tips);
        },
        _location:function(){
        	var self = this;
        	var btn;
	        if(self.follow==null){
	            btn = $(".bottomBar")?$(".bottomBar .inputButton"):$(".editBlock").not(".search").find("tr:last .inputButton");
	        }else{
	            btn = $(self.follow);
	        }
	        var msgH = $(self.DOT+self.CSS.MSG).filter(":visible").outerHeight(true);
	        var left = btn.offset().left;
	        var top = btn.offset().top-msgH-15;
	        $(self.DOT+self.CSS.MSG).css({
	            "left":left,
	            "top":top
	        });
        },
        _show_date_time:function(){
        	var self = this;
        	if(self.mm>0){
	            self.t = window.setTimeout(function(){
	                self._show_date_time();
	            }, 1000); 
	        }else{
	            $(self.DOT+self.CSS.MSG).hide();
	            clearTimeout(self.t);
	            self.callFn();
	        }
	        $(self.DOT+self.CSS.TIME).find(">span").text(self.mm); 
	        self.mm--;
        }
    });
    tips.Msg = msg;
})(window);
/*===radio选择过滤隐藏显示===*/
function filter(){
	$(".filter").each(function(){
		var me = $(this),
		radio = me.find("input:radio");
		show();
		radio.click(function(){
			show();
		});
		function show(){
			checked = "filter_"+me.find(":radio:checked").val();
			me.find("input[type='hidden']").remove();
			me.append('<input type="hidden" id="chargeMode" name="chargeMode" value='+checked+' />');
			for(var i=0;i<radio.length;i++){
				$(".filter_"+radio.eq(i).val()).hide();
			} 
			$("."+checked).show();
			if(checked=="filter_plane" || checked=="filter_mileage")
				$("#planBeginDate").attr("onfocus","new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})");
			else if(checked=="filter_days" || checked=="filter_protocol")
				$("#planBeginDate").attr("onfocus","new WdatePicker({dateFmt:'yyyy-MM-dd'})");
		}
	});
}

var getOutterHeight = function(el){ //得到参数对象的高度值
	el = $(el);
    if (el) {
        return el.height() + 
			getIntval(el, 'paddingTop') +
			getIntval(el, 'paddingBottom') +
			getIntval(el, 'marginTop') +
			getIntval(el, 'marginBottom') +
			getIntval(el, 'border-top-width') +
			getIntval(el, 'border-bottom-width');
    } else {
        return 0;
    }
}
var getOutterWidth = function(el){ //得到参数对象的宽度值
	el = $(el);
    if (el) {
        return el.width() + 
			getIntval(el, 'paddingLeft') +
			getIntval(el, 'paddingRight') +
			getIntval(el, 'marginLeft') +
			getIntval(el, 'marginRight') +
			getIntval(el, 'border-left-width') +
			getIntval(el, 'border-right-width');
    } else {
        return 0;
    }
}
var getIntval = function(el, s){ //得到参数对象的各种边距边线值
    	var v = 0;
		el = $(el);
        if (el) {
			v = parseInt(el.css(s), 10);
			if (!$.isNumeric(v)) {
				v = 0;
			}
        }
		return v;
    };

/*===switch模拟选择按钮切换===*/
function switchs(){
	if($(".switch")){
		$(".switch").each(function(){
			var me = $(this);
			me.find("label").click(function(){
				me.find(".on").not($(this)).removeClass("on");
				$(this).addClass("on");
			})
		})
	}
}
function setMapH(){
	var iframeH = $("#rightFrame",window.top.document).height(),
        mapT = parseInt($(".map").css('marginTop'), 10),
        space = parseInt($(".space").css('paddingTop'), 10)+parseInt($(".space").css('paddingBottom'), 10),
        subtractH = 0,
        h = 0,
        subtract = $(".subtract");
    for(var i=0;i<subtract.length;i++){
    	subtractH += subtract.eq(i).outerHeight(true);
    }
    h = iframeH-subtractH-mapT-space-2;
    $(".map .fullScreen").text("全屏");
    if($("body").hasClass("fullScreen")){
        h = iframeH-2;
        $(".map .fullScreen").text("取消全屏");
    }
    $(".map").height(h);
}
$(function(){
	$(".carSelector").each(function (i){
		$(this).click(function(){
			art.dialog.data('plateNumber', this.value); 
			url='car_popup.action?carSelectorId='+this.id;
			if($(this).attr("synchDriverName")!=null)
				url=url+'&synchDriverName='+$(this).attr("synchDriverName");
			if($(this).attr("synchDriverId")!=null)
				url=url+'&synchDriverId='+$(this).attr("synchDriverId");
			art.dialog.open(url,{
				title: '车辆选择', 
				width: 300, 
				height: 530
			});
		});
	});
	
	$(".userSelector").each(function (i){
		$(this).click(function(){
			art.dialog.data('userName', this.value); 
			var url;
	        if($(this).attr("driverOnly")!=null)
	        	url="user_popup.action?driverOnly=true&userSelectorId="+this.id;
	        else
	        	url="user_popup.action?userSelectorId="+this.id;
	        art.dialog.open(url,{title: '员工选择', width: 300, height: 530});
		});
	});
	
	 
	 $("#schedulerName").click(function(){
         art.dialog.data('driverName', $("#schedulerName").val()); // 存储数据  
         art.dialog.open('user_popup.action',{
				title: '司机选择', 
				width: 300, 
				height: 530
			});
     });
	 
	 $("#customer_organization_name").click(function(){
         art.dialog.data('customer_organization_name', $("#customer_organization_name").val()); // 存储数据  
         art.dialog.open('customerOrganization_popup.action',{
				title: '单位选择', 
				width: 350, 
				height: 580
			});
     });
})