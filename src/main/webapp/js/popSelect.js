
function setBodyH(){
    $("html").css("height","100%");
    $("body").css({
        "height":"98%",
        "margin-bottom":0,
        "margin-top":0,
        "padding-top":'0px'
    });
}
function setDataGridH(){
    var t = setTimeout(function(){
        var bodyH = document.body.clientHeight;
        $("body").height(bodyH-10);
        var space = parseInt($(".space").css('paddingTop'), 10)+parseInt($(".space").css('paddingBottom'), 10);
        var titleH = $(".caption").outerHeight(true);
        var searchH =$(".search").outerHeight(true);
        var pageH = $(".pageToolbar").outerHeight(true);
        var bottombarH = $(".bottomBar").outerHeight(true);
        if(bodyH>302){
            var dataGridH = bodyH-space-titleH-searchH-pageH-bottombarH;
            $(".fixed-height").height(dataGridH);
            var tableW = $(".dataGrid>table").width()-16; //16为标准滚动条宽度
            $(".fixed-height").css("overflow-y","auto");
            var scrollbarH = $(".fixed-height")[0].scrollHeight-$(".fixed-height")[0].clientHeight;
            if(scrollbarH>0){
                $(".fixed-height>table").css({"width":tableW}); 
            }
        }else{
            $("body").css({"overflow-y":"auto","overflow-x":"hidden"});
            $(".fixed-height").css("overflow-y","auto");
            $(".TreeContainer").css("height","auto");
        } 
       
    },100);   
}
(function(scope){
    function PopSelect(){
        var self = this;
        self._init();
    };
    $.extend(PopSelect.prototype, {
        _init: function(){
            var self = this;
            self._defaultEvent();
        },
        _defaultEvent:function(){
            setBodyH();
            if($(".fixed-height").length>0){
                setDataGridH();
            }
        }
    });
    scope.PopSelect = PopSelect;
})(window);

