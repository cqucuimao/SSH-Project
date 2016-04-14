var frames = window.frames,
    IE = $.browser.msie,
    leftFrame = $('#leftFrame'),
    rightFrame = $('#rightFrame'), 
    header = $('#header'),
    resizeBodyHeight = function() { //计算并设置iframe高度值
        var viewHeight = document.documentElement.clientHeight||document.body.clientHeight,
        	h = viewHeight,
        	h2 = h;
        if(header.is(':visible')){
        	h2 = h2-getOutterHeight(header);
        }
        leftFrame.height(h);
        rightFrame.height(h2);
    },
    getOutterHeight = function(el){ //得到参数对象的高度值
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
    },
    getIntval = function(el, s){ //得到参数对象的各种边距边线值
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




