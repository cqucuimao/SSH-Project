var switcher=true;
function coverShow(){
	if(switcher){
		submiting.style.display='block';
		cover.style.display='block';
	}else
		coverSwitcherOn();
}

function coverHidden(){
	submiting.style.display='none';//效果不显示
	cover.style.display='none';
}
		
function coverSwitcherOn(){
	switcher=true;
}
		
function coverSwitcherOff(){
	switcher=false;
}


function registerClick(){
	var fun = function(old) {
        return function() {
        	coverShow();
        	if(old)
        		old.apply(this, arguments);
        };
    };

    var a = document.getElementsByTagName('a');
    for (var i = 0; i < a.length; i++) {
    	if(a[i].cover && a[i].cover=="off")
    		continue;
        var old = a[i].onclick;
        a[i].onclick = fun(old);
    }
    
    var input = document.getElementsByTagName('input');
    for (var i = 0; i < input.length; i++) {
    	if(input[i].cover && input[i].cover=="off")
    		continue;
    	if(input[i].getAttribute("type")=="submit" || input[i].getAttribute("type")=="image"){
    		var old = input[i].onclick;
    		input[i].onclick = fun(old);
    	}
    }
}

/*非模态对话框弹出窗口*/
function popUp(url,width,height,winname,left,top){
	var left = (left==''||left==null)?(screen.width - width)/2:left;
	var top = (top==''||top==null)?(screen.height - height)/2:top;
	var winnames = (winname=='')?'popUpWin':winname;
	window.open(url, winnames, 'toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,copyhistory=yes,width='+width+',height='+height+',left='+left+', top='+top+',screenX='+left+',screenY='+top+'');
}

/*模块对话框弹出窗口*/
function popUpModal(src, width, height, showScroll){ 
	return window.showModalDialog 
	(src,window,"location:no;status:no;help:no;dialogWidth:"+width+";dialogHeight:"+height+";scroll:"+showScroll+";"); 
	} 

$(document).ready(function(){
	//registerClick();
});