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
        	var result=true;
        	if(old)
        		result=old.apply(this, arguments);
        	//coverHidden();
        	return result;
        };
    };

    var a = document.getElementsByTagName('a');
    for (var i = 0; i < a.length; i++) {
    	if(a[i].className && a[i].className.indexOf("coverOff")>=0)
    		continue;
        var old = a[i].onclick;
        a[i].onclick = fun(old);
    }
    
    var input = document.getElementsByTagName('input');
    for (var i = 0; i < input.length; i++) {
    	if(input[i].className && input[i].className.indexOf("coverOff")>=0)
    		continue;
    	if(input[i].getAttribute("type")=="submit" || input[i].getAttribute("type")=="image"){
    		var old = input[i].onclick;
    		input[i].onclick = fun(old);
    	}
    }
}

$(document).ready(function(){
	registerClick();
});