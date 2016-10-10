<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cqu" uri="//WEB-INF/tlds/cqu.tld" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<style type="text/css">
		body, html,#allmap {width: 100%;height: 100%;margin:0;font-family:"微软雅黑";}
		#BaiduMap{height:500px;width:100%;}
</style>
<link rel="stylesheet" type="text/css" href="skins/main.css">
</head>
<body class="minW">
    <div class="space">
        <div class="title subtract">
            <h1>实时监控</h1>
            <p>&nbsp;&nbsp;</p>
            <div class="toolbar">
            <span class="cutline cars"><i class="icon-car Travel"></i>行驶</span>
            <span class="cutline cars"><i class="icon-car Stop"></i>静止</span>
            <span class="cutline cars"><i class="icon-car NoService"></i>无网络</span>
            <span class="cutline cars"><i class="icon-car Abnormal"></i>异常行驶</span>
            <span class="cutline cars"><i class="icon-car PullOut"></i>设备拔出</span>
            <span class="cutline alarm"><img id="alarmImageId" src="skins/images/noAlarm.png" width="16" height="19" />报警</span>
            </div>
        </div>
        <!-- 查询条件 -->
        <div class="editBlock search subtract">
            <s:form id="queryForm">
            <table>
                <tr>
                    <th>车牌号</th>
                    <td><cqu:carSelector name="car"/></td>
                    <th>驻车地点</th>
                    <td>
                    <s:select style="width:135px;" name="servicePoint.name" id="servicePointId" list="#servicePointList" listKey="id" listValue="name"/>
                    </td>
                    <td>
                    <input class="inputButton" type="button" id="queryBn" value="查询"/>
                    </td>
                    <td>
                    <input class="inputButton" type="button" id="monitorWindowId" value="打开监控屏"/>
                    </td>
                </tr>
            </table>
            </s:form>
        </div>
        <!-- 数据列表 -->
        <div class="dataGrid" id="dataList" style="display:none;">
            <div class="tableWrap fixW">
                <table>
                    <colgroup>
                        <col width="30"></col>
                        <col></col>
                        <col></col>
                        <col></col>
                        <col></col>
                        <col></col>
                        <col width="170"></col>
                    </colgroup>
                    <thead>
                        <tr>
                            <th class="alignCenter"><input type="checkbox" id="checkAll" /></th>
                            <th>车型</th>
                            <th>车牌号</th>
                            <th>司机</th>
                            <th>联系方式</th>
                            <th>驻车地点</th>
                        </tr>
                    </thead>
                    <tbody class="tableHover" id="htcList">
                    </tbody>
                </table>
            </div>
        </div>
        <div class="pageToolbar" id="pageBar" style="display:none;">
			    <span class="page" id="pageInfo"><span id="allRecords"></span>
			    	<span id="currentPageNum"></span>/<span id="totalPageNum"></span>&nbsp;&nbsp;
			    	<a href="javascript: gotoPage('first')"><img id="first" src="skins/images/page/page_first_b.gif" alt="first page" /></a>
			    	<a href="javascript: gotoPage('previous')"><img id="previous" src="skins/images/page/page_pre_b.gif" alt="previous page"/></a>
			    	<a href="javascript: gotoPage('next')"><img id="next" src="skins/images/page/page_next_a.gif" alt="next page"/></a>
			    	<a href="javascript: gotoPage('last')"><img id="last" src="skins/images/page/page_last_a.gif" alt="last page"/></a>
			    	转到： <select id="pageSkip" onchange="gotoPage(this.value)">
			    	</select>
			  	</span>
		</div>
		<!-- 被监控车辆正在执行订单时的弹出框 -->
        <div id="carOrderDetail" class="editBlock detailM" style="display:none;">
        <table id="orderDetail">
            <tbody>
                <tr>
                    <th width="70">订单号：</th>
                    <td></td>
                </tr>
                <tr>
                    <th width="70">客户单位：</th>
                    <td></td>
                </tr>
                <tr>
                    <th>联系人：</th>
                    <td></td>
                </tr>
                <tr>
                    <th>计费方式：</th>
                    <td></td>
                </tr>
                <tr>
                    <th>计划时间：</th>
                    <td></td>
                </tr>
                <tr>
                    <th>车型：</th>
                    <td></td>
                </tr>
                <tr>
                    <th>车牌：</th>
                    <td></td>
                </tr>
                <tr>
                    <th>司机：</th>
                    <td></td>
                </tr>
                <tr>
                    <th>始发地：</th>
                    <td></td>
                </tr>
                <tr>
                    <th>目的地：</th>
                    <td></td>
                </tr>
            </tbody>
            <tfoot>
                <tr>
                    <td>
                        <div class="bottomBar borderT alignCenter"><a id="trackUrlHaveOrder" href="fdafs"><input class="inputButton" type="button" value="查看轨迹" id="viewTrackBnHaveOrder"/></a></div>
                    </td>
                    <td>
                        <div class="bottomBar borderT alignCenter"><input class="inputButton" type="button" value="移除" id="removeCarHaveOrder"/></div>
               </td>
                </tr>
            </tfoot>
        </table>
        </div>
        <!-- 被监控车辆没有执行订单时的弹出框 -->
        <div id="carNoOrder" class="editBlock detailM" style="display:none;">
        <table id="noOrder">
           <tr><td colspan="2">车辆当前没有正在执行的订单</td></tr>
           <tr>
               <td>
                   <div class="bottomBar borderT alignCenter"><a id="trackUrlNoOrder" href="fdafs"><input class="inputButton" type="button" value="查看轨迹" id="viewTrackBnNoOrder"/></a></div>
               </td>
               <td>
                   <div class="bottomBar borderT alignCenter"><input class="inputButton" type="button" value="移除" id="removeCarNoOrder"/></div>
               </td>
           </tr>
        </table>
        </div>
        
        <!-- 警报信息弹出框 -->
        <div id="alarmMessageDetail" class="dataGrid" style="display:none;width:800px;height:400px;">
			<div  class="tableWrap">
				<table id="warningMessages">
					<thead>
						<tr>
						    <th><input type="checkbox" id="checkAllWarningId"/>全选</th>
							<th>车牌号</th>
							<th>司机</th>
							<th>联系方式</th>
							<th>报警时间</th>
							<th>报警原因</th>
						</tr>
					</thead>
					<tbody id="warningMessageList">
					     <tr><td colspan="6" align="center">&nbsp;&nbsp;</td></tr>
					     <tr><td colspan="6" style="text-align:center;">当前没有任何未处理的报警记录</td></tr>
					     <tr><td colspan="6" align="center">&nbsp;&nbsp;</td></tr>
					</tbody>
				</table>
			</div>
			<div id="processWarningId" style="display:none;" class="bottomBar borderT alignCenter"><input class="inputButton" type="button" value="处理" id="dealWarningsId"/></div>
		</div>
    
	    <!-- 总记录列表，Ajax请求获得的所有数据，这些数据需要隐藏-->
	    <table id="Searchresult" style="display:none;"></table>
	   <!--  <div id="BaiduMap" class="mt10 map">
        </div> -->
        
        <!-- 警报声音 -->
        <div><audio id="audioId" src="media/alarm.mp3" id="audio1" hidden="true" loop="true" /></div>
     </div>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
    <script type="text/javascript" src="js/artDialog4.1.7/jquery.artDialog.source.js"></script>
    <script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>	
	<script type="text/javascript" src="js/common.js"></script>	
	
    <script src="http://api.map.baidu.com/api?v=2.0&ak=XNcVScWmj4gRZeSvzIyWQ5TZ"></script>
    <script type="text/javascript">
    
    window.onbeforeunload = function()
    {
       return "如果离开本页面，或者刷新本页面，或者关闭本窗口，监控屏就无法正常工作。请确认要离开吗？";
    }
    
    $(function(){
         setMapH(); //设置地图外层容器的高度
         $(".fullScreen").click(function(){ 
              $(".search").toggle();
              $(".title").toggle();
              $("body").toggleClass("fullScreen");
              $(".map").toggleClass("mt10");
              $(".header", window.top.document).toggle();
              $(".leftWrap", window.top.document).toggle();
              window.parent.window.resizeBodyHeight();
              setMapH();
         });
    });
         
    //监控屏的百度地图对象
    var map;
    /* //百度地图
    var map = new BMap.Map("BaiduMap");
    var mapPoint = new BMap.Point(106.464617,29.570331);
    map.centerAndZoom(mapPoint, 13);
    map.enableScrollWheelZoom(true); */
    
    /* //百度地图的GPS坐标转Baidu坐标的API接口限制数
    var limitLen=98; */
    
    //报警声播放器
    var soundPlayer=document.getElementById('audioId');
    
    //双屏子窗口对象
    var sonWindow;
    var sonWindowFlag=false;
    
   /*  //地图左上角功能区
    var contentMark="<span class='actionBar'><a href='javascript:clearAllCars()'>清除所有车辆</a><em class='line'></em>"
                    +"<a href='javascript:recoverAbnormalCars()'>恢复异常车辆</a><em class='line'></em>"
                    +"<a href='#' class='fullScreen'>全屏</a>"+"<em class='line'></em><a href='javascript:doubleWindow()'>双屏</a></span>";  */
    
    //为打开监控屏按钮绑定监控屏弹出事件
    $("#monitorWindowId").bind("click",monitorWindow); 
                    
    //双屏按钮事件
    function monitorWindow(){
        //获取子窗口对象
        sonWindow=window.open('realtime_mapWindow.action', '车辆监控', 'top=0, left=0, toolbar=no, scrollbars=yes, location=no, resizable=yes, status=no');
        //双屏加载完毕，则为双屏标志置true
        /* sonWindow.onload = function() {
        	sonWindowFlag=true;
        	//获取监控屏的百度地图对象
        	map=sonWindow.map;
        	//sonWindow.flushTrack();
        } */
        
    }
    
    //为百度地图添加左上角标签                
    //$("#BaiduMap").append(contentMark); 
    
    //百度地图API中gps坐标转百度地图坐标的web服务请求格式,前缀,后缀有服务器端添加
    var head="baidu.action?coords=";
    
    //数据库中所有正常车辆，用于检查车辆是否出现报警
    var allNormalCars;
    
    //警告信息查询间隔时间  单位 毫秒
    var warningCheckInterval=15000;   //间隔时间15秒 
    
    //当前查询到的到车辆信息
    var carData;
    //当前查询得到的车辆数目
    var length;
    
    //需求要求每次查询并选中的结果会 添加(原有的不清除) 到地图 所以需要一个全局变量 记录所有查询到的车辆信息
    var allRecordedCars=new Array();
    //保存当前查询得到的车辆的所有id  用于全部选中 和 全部取消  
    var currentQueryCarIds;
    //保存分页前复选框状态
    var checkboxStatus;
    //获取当前页面的所有复选框对象(全选复选框除外),主要用于与子窗口的操作进行交互
    var checkboxes;
    //当前页面所有的checkbox 全选框也包括，用于 子窗口清除所有的车辆功能
    var allCheckboxes; 
    //单独的全选框
    var theCheckAllBox=$("#checkAll");
    
/*     //刷新的组别索引
    var flushGroupIndex=0;
    //刷新的分组的总数
    var flushGroupNum=3; */
    
    //设置每页显示记录数
    var num_entries;	  //记录总数
    var showCount;        //每页显示记录数
    var lastPage;         //总页数也即最后一页编号
    var currentPage;      //当前页
    
    //为车辆警报标识绑定事件
    $("#alarmImageId").bind("click",function(){
    	var alarmStatus=1;
    	if(alarmStatus==1){
    		var dialog = art.dialog({
	            title: "当前警报信息",
                lock: true,      //遮罩层效果
                drag: false,     //拖动效果
                content: document.getElementById("alarmMessageDetail")
            });
    		//为处理按钮绑定事件
    		$("#dealWarningsId").bind("click",function(){
    			//保存选中的警告信息的id
    			var warningIds=new Array();
    			var index=0;
    			//获取所有被选中的警告记录的id
    			$(".warningCheckItem").each(function(){
    				if($(this).is(":checked")){
                       warningIds[index++]=$(this).prop("id");
                    } 
    			});
    			//如果没有任何订单被选中，则提示
   	    	    if(index==0){
   	    		   alert("未选择任何警告记录");
   	    	    }else{
   	    	       var idStr="";
         	       for(var i=0;i<warningIds.length;i++){
         	    	   idStr+=warningIds[i]+",";
         	       }
         	       //去掉最后一个多余的","
         	       idStr=idStr.substring(0,idStr.length-1);
         	       $.get("alarm_dealWarnings.action?warningIds="+idStr+"&timestamp="+new Date().getTime(),function(result){
     	    		     if(result.status==1){
     	    		       //重新查询并填充警告记录
     	    		       warningsCheckAndFill();
     	    		       //重置监控车辆的行驶状态
     	    		       for(var i=0;i<warningIds.length;i++){
     	    		    	  allRecordedCars[warningIds[i]][2]=0;
     	    		       }
     	    		     }
     	           });
   	    	    }
    		});
    	}
    });
    
    function getAllCars(){
    	$.get("realtime_list.action",$("#queryForm").serializeArray(),function(carsJson){
       	 carData=carsJson;
      		 length=carData.cars.length; 
      		 //定义保存id的数组
      		 currentQueryCarIds=new Array(length);
      		 //如果得到的车辆数据不为空
      		 if(!((length==1)&&(carData.cars[0]==null))){
      		     //保存当前查询到车辆的所有ids 查找最大id作为数组的长度
      	   		 var maxId=0;
      	   		 for(var i=0;i<length;i++){
      	   		     currentQueryCarIds[i]=carData.cars[i].id;
      	   			 if(parseInt(carData.cars[i].id)>maxId)
      	   			     maxId=carData.cars[i].id;
      	   		 }
      	   		 //初始化查询得到的车辆记录的复选框状态时，需要比对allRecordedCars中的记录，如果当前车辆已经记录过，则它对应的复选框状态为记录中的状态
      	   		 //保存所有记录的复选框状态,初始化，全部为false
      	   		 checkboxStatus=new Array(maxId+1);
      	   		 for(var i=0;i<checkboxStatus.length;i++){
      	   			 if(allRecordedCars[i]===undefined){
      	   			    checkboxStatus[i]=false; 
      	   			 }else{
      	   				checkboxStatus[i]=allRecordedCars[i][1];
      	   			 }
      	   		 }
      	   		 //记录查询到的车辆的信息
      	   		 for(var i=0;i<length;i++){
      	   			 var carId=carData.cars[i].id;
      	   		     //只对未记录的车辆进行登记，并初始化状态，对已经存在的不做处理，时期保持原有状态
      	   		     if(allRecordedCars[carId]===undefined){
      	   		        allRecordedCars[carId]=new Array(3);
    	   		        //以id作为索引的车辆记录包含两个信息  车辆具体信息 和 是否被选中 用于监控
    	   		        allRecordedCars[carId][0]=carData.cars[i];
    	   		        allRecordedCars[carId][1]=false;
    	   		        //用于记录车辆当前处于 正常状态 0 还是 异常状态(异常状态包括  异常行驶 1  和 设备拔出 2) 默认初始化都处于正常状态
    	   		        allRecordedCars[carId][2]=0;
      	   		     }
      	   		 }
      	   		//清除所有复选框状态
      	   		$("[type=checkbox]").prop("checked",false);
      	   		 
      	   		//设置分页信息
      	   	     num_entries = length;	    //获取记录总数
      	   	     showCount = 10;             //每页显示记录数
      	   	     //计算分页数
      	   	     lastPage=Math.ceil(num_entries/showCount);  //Math.ceil向上取整
      	   	     //设置当前页
      	   	     currentPage=0;
      	   		 pageList();                //查询之后调用分页组件
      	   	     pageselectCallback(currentPage);  //分页
      	   	     $("#pageSkip").empty();
      	   	     //设置页码跳转框
      	   	     var selector=$("#pageSkip");     
      	         for(var i=0;i<lastPage;i++){ 
      	             selector.append('<option value="'+i+'">'+eval(i+1)+'</option>');     
      	         }
      	   		 
      	   	     //分页组件的当前页和总页数信息
    	         //如果总页数大于0，则置当前页为1，否则0
    	         $("#allRecords").html(num_entries+" 条结果 ");
    	         if(num_entries>0)
    	   		    $("#currentPageNum").html(currentPage+1);
    	         else
    	        	$("#currentPageNum").html(0); 
    	   		 $("#totalPageNum").html(lastPage);
      	   		 
      	   		 //显示数据表
      	   		 $("#dataList").show();
      	   		 //初始化分页栏样式
      	   	     initPageBarStyle()
      	   		 //显示分页栏
      	   		 $("#pageBar").show();
      		 }else{
      			 $("#htcList").empty();	
      		 }
      	});
    }
    getAllCars();
    //查询按钮的Ajax异步请求，集成Strusts框架进行参数传递
    $("#queryBn").click(function(){
    	getAllCars();
    });
   
   //全选按钮绑定单击事件响应
   $("#checkAll").bind("click", function(){
   	 if($("#checkAll").is(":checked")){
   		    //在页面上标记所有复选框的勾选状态(单独处理，因为与分页是相分离的)
     		$(".checkboxItems").prop("checked",true);
     		for(var i=0;i<checkboxStatus.length;i++){
   			    checkboxStatus[i]=true;
   		    }
     		//改变全局记录中 当前查询车辆的状态
     		for(var i=0;i<currentQueryCarIds.length;i++){
     			allRecordedCars[currentQueryCarIds[i]][1]=true;
     		}
     		
   	 }else{
   	    	//在页面上清除所有复选框的勾选状态(单独处理，因为与分页是相分离的)
    		$(".checkboxItems").prop("checked",false);
   		    //在checkboxStatus数组中清除所有复选框的勾选状态
  		    for(var i=0;i<checkboxStatus.length;i++){
  			   checkboxStatus[i]=false;
  		    }
  		    //改变全局记录中 当前查询车辆的状态
     		for(var i=0;i<currentQueryCarIds.length;i++){
     			allRecordedCars[currentQueryCarIds[i]][1]=false;
     		}
	      	//清除地图上的所有覆盖物,全部取消后，flushTrack将不再做任何处理，所以要手动清除标记物 
	  		map.clearOverlays();  
   	 }	   
   	 //判断双屏是否开启，如果开启，则对子窗口进行刷新
   	 if(sonWindowFlag){
   		 sonWindow.flushTrack();
   	 }
   });
   
   //分页组件
   function pageList(){	   
   	//不能直接使用append方法，需要对上次内容清空，否则查询结果会叠加
   	$("#Searchresult").empty();	
   	//遍历查询到的jsonData数据，将信息解析成页面需要显示的表单格式
   	$.each(carData.cars,function(index,car){
   		$("#Searchresult").append("<tr>"+"<td class='alignCenter'>"+"<input type='checkbox' name='checkItem' class='checkboxItems' id="
   				                  +car.id+">"+"</td>"+"<td>"+car.type+"</td>"+"<td>"+car.number+"</td>"+"<td>"+car.driver+"</td>"+"<td>"
   				                  +car.phone+"</td>"+"<td>"+car.location+"</td>"+"</tr>");  
   	});
   	
   	//为所有checkbox添加单击事件
   	//由于分页的存在  所以应该是对每个分页的列表添加click事件 (注意这段代码必须在查询操作之后执行)
    //这里出现一个bug，click事件和当前显示的那一页绑定了，别的页的checkbox状态消失了
	    $(".checkboxItems").click(function(){
		    //如果点击事件，使当前复选框被选中，那么改变相应复选框状态，并将对应的标识物添加到百度地图
		    if($(this).is(":checked")){
		       var id=parseInt($(this).prop("id"));
		       //选中当前复选框后，在checkboxStatus数组中改变状态
			   checkboxStatus[id]=true;
		       //将记录中的相应状态改变
			   allRecordedCars[id][1]=true;
		       if(sonWindowFlag){
		    	  //在地图上添加指定id的车辆图标及其相关操作
			      addCarOnMap(id);  
		       }
		       //遍历所有复选框，如果所有复选框被选中，则将全选按钮置为 选中状态,注意这里的id从1开始，id=0的车不存在
		       var allCheckedFlag=true;
		       for(var i=1;i<checkboxStatus.length;i++){
		    	   if(checkboxStatus[i]!==undefined){
		    		   if(!checkboxStatus[i])
		    			  allCheckedFlag=false;
		    	   }
		       }
		       if(allCheckedFlag){
		    	   $("#checkAll").prop("checked", true);
		       }
		    }else{
		    //如果点击事件是取消勾选状态,那么需要删除相应的标识物
		       //因为取消了一个，所以全选按钮的状态失效
			   $("#checkAll").prop("checked", false);
			   var id=parseInt($(this).prop("id"));
		       //取消当前失效状态后，在checkboxStatus数组中改变状态
		       checkboxStatus[id]=false;
		       //将记录中的相应状态改变
			   allRecordedCars[id][1]=false;
		       
		       var carPlateNumber=allRecordedCars[id][0].number;
		       //如果已经打开监控屏
		       if(sonWindowFlag){
		    	  //在地图上清除相应的车辆标识
	  	    	   var allOverlay = map.getOverlays();         	      		    	   
	  			   for (var i = 0; i < allOverlay.length; i++){
	  				      //这里可能发生异常的原因是 getOverlays对象得到的覆盖物的个数不定  不知道是什么原因  对于非车辆覆盖物 抛出的异常直接忽略
	  				      try{
	  				          if(allOverlay[i].getLabel().content == carPlateNumber){
	   					         map.removeOverlay(allOverlay[i]);
	   			              }
	  				      }catch(error){}
	  			   }
	  			   //取消一个后，遍历复选框所有状态，如果全部已经取消了的话，手动清除所有标识物
			       var flag=false;
			       for(var i=0;i<checkboxStatus.length;i++){
			    	   if(checkboxStatus[i]){
			    		   flag=true;
			    	   }
			       }
			       if(!flag){
		   	      	  //清除地图上的所有覆盖物
		   	  		  map.clearOverlays(); 
			       } 
		       }
		    }
        });
    }
   
    //分页回调函数
    function pageselectCallback(page_index){
    	//设置当前页编号
    	$("#currentPageNum").html(currentPage+1);
    	//设置跳转选择框的当前值
   	    $("#pageSkip").val(page_index); 
   	    //计算得到当前显示的页的记录(起始，终止)，从Searchresult中拷贝出相应记录，显示在htcList下
		var max_elem = Math.min((page_index+1) *showCount, num_entries);		
		$("#htcList").html("");		
		for(var i=page_index*showCount;i<max_elem;i++){
			var new_content = $("#Searchresult tr:eq("+i+")").clone(true,true); //注意这里clone函数默认不能复制事件机制，所以需要带参数true
			$("#htcList").append(new_content); 
		}
		
		//注意： 由于clone操作，所以复选框的真实对象个数 是  页面显示的个数的 2倍
		//获取当前页面的所有复选框对象(全选复选框除外),主要用于与子窗口的操作进行交互,子窗口中对车辆的移除会影响到当前复选框的状态
	    checkboxes=$(".checkboxItems");
		//获取所有的复选框对象 用于子窗口的 clearAllCars功能
	    allCheckboxes=$("input:checkbox");
		//根据显示不同分页前后保存的checkbox的状态来设置显示的checkbox状态
		 $("#htcList input").each(function () {
			if(checkboxStatus[parseInt($(this).prop("id"))]){
				$(this).prop("checked", true);
			}else{
				$(this).prop("checked", false);
			}
       }); 
		return false;
	}
    
   //每隔10秒刷新位置
   //window.setInterval(flushByGroup,monitorInterval); 
   //每隔5分钟刷新位置
   window.setInterval(warningsCheckAndFill,warningCheckInterval); 
   
   /*
     allRecordedCars[carId][2] 中记录了车辆当前的状态，0 正常        异常（1 异常行驶     2 设备拔出）
     (1) 当对数据库中所有处于正常状态的车辆进行 警告检查时 可以判断出 那辆车是  处于异常行驶 还是 设备 拔出  对allRecordedCars[carId][2]进行设置
     (2) 对allRecordedCars[carId][2]进行设置时  需要注意的是 对allRecordedCars[carId] 可能是undefined的，因为有可能该车并没有被纳入监控 这里需要具体处理
     (3) 当该车的警报信息处理以后，需要将处理的carId 对应的对allRecordedCars[carId]状态 进行重置  选中状态应置为 不变   行驶状态 置为 0
   */  
   
   //查询并填充警告记录数据 将数据填充到隐藏的div中  
   function warningsCheckAndFill(){
	    $.ajaxSettings.async=false; 
	    //查询当前是否有未处理的警告信息
		$.get("alarm_getUndealedMessages.action"+"?timestamp="+new Date().getTime(),function(resultData){
			  //清空之前填充的的警告记录
 		      $("#warningMessageList").empty();	
			  //如果当前存在未处理的警告信息
			  if(resultData.status==1){
				 //将警告信息填充到数据div中
				 $.each(resultData.messages,function(index,message){
				   		$("#warningMessageList").append("<tr>"+"<td class='alignCenter'>"+"<input type='checkbox' name='checkItem' class='warningCheckItem' id="
				   				                  +message.id+">"+"</td>"+"<td>"+message.plateNumber+"</td>"+"<td>"+message.driverName+"</td>"+"<td>"+message.phoneNumber+"</td>"+"<td>"
				   				                  +message.date+"</td>"+"<td>"+message.type+"</td>"+"</tr>");  
				   		
				   		var carId=message.carId;
				   		//如果该车辆未被纳入监控 初始化该车辆的状态
				   		if(allRecordedCars[carId]===undefined){
		 	   		       //以id作为索引的车辆记录包含两个信息  车辆具体信息 和 是否被选中 用于监控   
		 	   		       //???设备拔出后是否还会有 gps信息
                           allRecordedCars[carId]=new Array(3);
				   		}
				     	//获取车辆具体信息
                        $.get("realtime_getCarInfo.action?carId="+carId+"&timestamp="+new Date().getTime(),function(carInfo){
                     		 allRecordedCars[carId][0]=carInfo;
                        });
                        //监控异常车辆
 		 	   		    allRecordedCars[carId][1]=true;
 		 	   		    //用于记录车辆当前处于 正常状态 0 还是 异常状态(异常状态包括  异常行驶 1  和 设备拔出 2) 默认初始化都处于正常状态
 		 	   		    if("异常行驶"==message.type)
 		 	   		       allRecordedCars[carId][2]=1;
 		 	   		    else
 		 	   			   allRecordedCars[carId][2]=2;
				 });
				 //如果当前的刷新组是第一组  当时 警报发生在最后一组的车中，为了即刻显示异常车辆  需要在这里直接刷新
			   	 if(sonWindowFlag){
			   		sonWindow.flushTrack();
			   	 }
				 //使警报灯闪烁
				 $("#alarmImageId").attr("src","skins/images/alarm.gif");
				 //发出报警声
				 soundPlayer.play();
				 //显示处理按钮
			     $("#processWarningId").show();
			  }else{
				 //显示无警告记录状态
				 $("#warningMessageList").append("<tr><td colspan='6'>&nbsp;&nbsp;</td></tr>");
				 $("#warningMessageList").append("<tr><td style='text-align:center;' colspan='6'>所有任报警记录都已处理</td></tr>"); 
				 $("#warningMessageList").append("<tr><td colspan='6'>&nbsp;&nbsp;</td></tr>");
				 //使警报灯不闪烁
			     $("#alarmImageId").attr("src","skins/images/noAlarm.png");
			     //暂停报警声
				 soundPlayer.pause();
				 //隐藏处理按钮
			     $("#processWarningId").hide();
				 //使警告显示栏的 全选 复选框 按钮失效
				 $("#checkAllWarningId").prop("checked", false);
			  }
		});
		$.ajaxSettings.async=true; 
   }
   
   
   
   //在地图上添加指定id的一辆车
   function addCarOnMap(id){
	   
	   $.ajaxSettings.async=false; 
	   //直接将车辆标识添加到地图
       var addedCar;
       //根据车辆的sn号 查找 相应的gps坐标点
 	   var device_sn=allRecordedCars[id][0].sn;
 	   //显示定义二维数组，记录被勾选的车辆车牌号,gps经纬度,车辆真实id,gps设备当前的状态(status:1行驶,2离线),gps设备当前的速度(speed) 车辆当前的行驶状态
 	   addedCar=new Array(6);
 	   addedCar[0]=allRecordedCars[id][0].number;
 	   addedCar[2]=id;
 	   addedCar[5]=allRecordedCars[id][2];
 	   $.get("api.action?device.get.do?device_sn="+device_sn+"&timestamp="+new Date().getTime(),function(gpsData){
             addedCar[1]=gpsData.device.position.lng+","+gpsData.device.position.lat;
             addedCar[3]=gpsData.device.position.status;
             addedCar[4]=gpsData.device.position.speed;
             
             var reqUrl=head+addedCar[1];
             //向Baidu地图gps坐标转Baidu坐标接口发送请求，返回转换后的Baidu坐标
       	   $.getJSON(reqUrl,function(baiduData){
   	      	         var point;      //选中的记录的经纬度坐标点
  	  	             var marker;     //地图上的标识物
  	  	             //车辆的车牌
  	  	             var carPlateNumber=addedCar[0];
  	  	             var carId=addedCar[2];
  	  	             //自定义信息显示
   	      		     var opts={
 				                width : 10,     // 信息窗口宽度
 				                height: 10,     // 信息窗口高度
 				                title : "车辆信息" , // 信息窗口标题
 				                enableMessage:false//设置允许信息窗发送短息
 			             };
   	      		     point = new BMap.Point(parseFloat(baiduData.result[0].x),parseFloat(baiduData.result[0].y));
   	      		     //获取当前车辆的状态status和速度speed
   	      		     var carStatus=addedCar[3];
   	      		     var carSpeed=addedCar[4];
   	      		     //定义车辆状态的图片路径
   	      		     var imagePath;
   	      		     //如果车辆处于正常状态
   	      		     if(addedCar[5]==0){
   	      		        //判断车辆具体处于什么状态：无网络(status:2) 行驶(status:1,speed!=0) 停留(status:1,speed=0)
       	      		    if(carStatus==2){
       	      		       imagePath="images/car5.png";
       	      		    }else{
       	      		       if(carSpeed==0)
       	      		    	  imagePath="images/car1.png";
       	      		       else
       	      		    	  imagePath="images/car4.png"; 
       	      		    }
   	      		     }else if(addedCar[5]==1){
   	      		    	 //当前状态为异常行驶
   	      		    	 imagePath="images/car3.png";
   	      		     }else{
   	      		         //当前状态为设备拔出
   	      		    	 imagePath="images/car2.png";
   	      		     }
   	      		     
   	      		     /* //清除地图上当前车辆标识
 	      		     var allOverlay = map.getOverlays();         	      		    	   
    			     for (var i = 0; i < allOverlay.length; i++){
    				      //这里可能发生异常的原因是 getOverlays对象得到的覆盖物的个数不定  不知道是什么原因  对于非车辆覆盖物 抛出的异常直接忽略
    				      try{
    				          if(allOverlay[i].getLabel().content == carPlateNumber){
	    					         map.removeOverlay(allOverlay[i]);
	    			              }
    				      }catch(error){}
    			     } */
   	      		     
   	      		     //自定义车辆图标
   	      		     var myIcon = new BMap.Icon(imagePath, new BMap.Size(40,30));
   	      		     marker = new BMap.Marker(point,{icon:myIcon});// 创建标注
   	      		     //增加车牌信息  label的offset属性能够调整基于中心点的偏移位置
   	      		     var label = new BMap.Label(carPlateNumber,{offset:new BMap.Size(-16,-17)});
	      		     marker.setLabel(label);
   	      		    
	      		     map.addOverlay(marker);             // 将标注添加到地图中
	      		     
   	      		     //非常重要的一个问题
   	      		     //这里有一个关键问题  批量事件绑定环境下   响应函数需要的参数的变化问题   使用 闭包 将局部变量carId 和 carPlateNumber 作为参数传递到 事件响应函数中去
   	      		     marker.addEventListener('click',(function(myCarId,myCarPlateNumber){
   	      		    	 return function(){
       	      		    	  $.get("realtime_orderDetail.action?id="+myCarId+"&timestamp="+new Date().getTime(),function(orderJson){
       	      		    	        var data=orderJson;
       	      		    	        //如果存在正在执行的订单，则显示具体订单信息，否则进行提示 
       	      		    	        if(data.status==1){
       	      		    	           var dialog = art.dialog({
                                            height:400,
                                            width:400,
         		        	               title: "车辆详情信息",
                                            lock: true,      //遮罩层效果
                                            drag: false,     //拖动效果
                                            content: document.getElementById("carOrderDetail")
         			                   });
         	      		               //获得表格正文的行对象数组，并未每行的列赋值
         	      		    	       var tbTrs=$("#orderDetail").children("tbody").find("tr");
         	      		    	       for(var k=0;k<tbTrs.length;k++){  
         	      		    		       var tds=$(tbTrs[k]).find("td"); 
         	      		    		       $(tds[0]).html(data.order[k]);
         	      		    	       }
         	      		    	       //获得订单信息表格的查看轨迹按钮对象
        	      		    	           $("#viewTrackBnHaveOrder").bind("click",function(){
        	      		    	    	      //关闭订单信息框
        	      		    	    	      dialog.close();
        	      		    	    	      //跳转到轨迹播放页面
        	      		    	    	      $("#trackUrlHaveOrder").attr("href","replay_home.action?carId="+myCarId);
        	      		    	           });
         	      		    	                  	      		    	       
        	      		    	           $("#removeCarHaveOrder").bind("click",function(){
        	      		    	        	//将全选按钮值为false
           	      		    	        	  theCheckAllBox.prop("checked", false);
        	      		    	        	//在allRecordedCars中将相应车辆的选中状态值为false
        	      		    	    	          allRecordedCars[myCarId][1]=false;
        	      		    	    	          //如果该车辆在当前复选框中被选中，则改变复选框状态
        	      		    	    	          if(checkboxStatus[myCarId]==true){
        	      		    	    	        	  checkboxes.each(function(){
        	      		    	    	        		  if($(this).prop("id")==myCarId){
        	      		    	    	        			 $(this).prop("checked", false);
        	      		    	    	        		  }
        	      		    	    	        	  });
        	      		    	    	        	  checkboxStatus[myCarId]=false;
        	      		    	    	          }
    	      		    	                  //在地图上清除相应的车辆标识
 	      		    	    	          var allOverlay = map.getOverlays();
 	      		    			          for (var i = 0; i < allOverlay.length; i++){
 	      		    				          //这里可能发生异常的原因是 getOverlays对象得到的覆盖物的个数不定  不知道是什么原因  对于非车辆覆盖物 抛出的异常直接忽略
 	      		    				          try{
    	      		    				              if(allOverlay[i].getLabel().content == myCarPlateNumber){
     	      		    					         map.removeOverlay(allOverlay[i]);
     	      		    			              }
    	      		    				          }catch(error){}
 	      		    			          }
 	      		    			          //关闭订单信息框
 	      		    	    	          dialog.close();
    	      		    	               });            	      		    	
       	      		    	        }else{
       	      		    	           var dialog = art.dialog({
                                            height:200,
                                            width:300,
         		        	               title: "车辆详情信息",
                                            lock: true,      //遮罩层效果
                                            drag: false,     //拖动效果
                                            content: document.getElementById("carNoOrder")
         			                   });
       	      		    	           //获得订单信息表格的查看轨迹按钮对象
        	      		    	           $("#viewTrackBnNoOrder").bind("click",function(){
        	      		    	    	      //关闭订单信息框
        	      		    	    	      dialog.close();
        	      		    	    	      //跳转到轨迹播放页面
        	      		    	    	      $("#trackUrlNoOrder").attr("href","replay_home.action?carId="+myCarId);
        	      		    	           });
       	      		    	           
        	      		    	          $("#removeCarNoOrder").bind("click",function(){
        	      		    	        	//将全选按钮值为false
          	      		    	        	  theCheckAllBox.prop("checked", false);
       	      		    	    	             	      		    	    
        	      		    	        	//在allRecordedCars中将相应车辆的选中状态值为false
       	      		    	    	          allRecordedCars[myCarId][1]=false;
       	      		    	    	          //如果该车辆在当前复选框中被选中，则改变复选框状态
       	      		    	    	          if(checkboxStatus[myCarId]==true){
       	      		    	    	        	  checkboxes.each(function(){
       	      		    	    	        		  if($(this).prop("id")==myCarId){
       	      		    	    	        			 $(this).prop("checked", false);
       	      		    	    	        		  }
       	      		    	    	        	  });
       	      		    	    	        	  checkboxStatus[myCarId]=false;
       	      		    	    	          }
       	      		    	    	     //在地图上清除相应的车辆标识
       	      		    	    	     var allOverlay = map.getOverlays();         	      		    	   
       	      		    			     for (var i = 0; i < allOverlay.length; i++){
       	      		    				      //这里可能发生异常的原因是 getOverlays对象得到的覆盖物的个数不定  不知道是什么原因  对于非车辆覆盖物 抛出的异常直接忽略
       	      		    				      try{
       	      		    				          if(allOverlay[i].getLabel().content == myCarPlateNumber){
        	      		    					         map.removeOverlay(allOverlay[i]);
        	      		    			              }
       	      		    				      }catch(error){}
       	      		    			     }
       	      		    			     //关闭订单信息框
       	      		    	    	     dialog.close();
       	      		    	          });          	      		    	                    	      		    	         
       	      		    	        }     	      		    	        
       	      		    	    }); 
       	                  };	      		    	 
   	      		     })(carId,carPlateNumber)); 
               }); 
		   });
 	  $.ajaxSettings.async=true; 
   }
   
     	
  //每次查询操作都需要初始分页栏的样式
 	function initPageBarStyle(){
 		//如果总共只有1页 则所有跳转都置无效
			if(lastPage==1){
			   $("#first").attr("src","skins/images/page/page_first_b.gif"); 
			   $("#previous").attr("src","skins/images/page/page_pre_b.gif"); 
			   $("#next").attr("src","skins/images/page/page_next_b.gif"); 
			   $("#last").attr("src","skins/images/page/page_last_b.gif"); 
			}else{
			   $("#first").attr("src","skins/images/page/page_first_b.gif"); 
			   $("#previous").attr("src","skins/images/page/page_pre_b.gif"); 
			   $("#next").attr("src","skins/images/page/page_next_a.gif"); 
			   $("#last").attr("src","skins/images/page/page_last_a.gif"); 
			}
 	}
   	
    //转到指定的页码
	function gotoPage(pageIndex){
    	
		 if(pageIndex=='first'){
			currentPage=0;
			pageselectCallback(currentPage);
		 }
		 if(pageIndex=='last'){
			currentPage=lastPage-1;
			pageselectCallback(currentPage);
		 }
		 if(pageIndex=='previous'){
			if(currentPage>0)
				currentPage=currentPage-1;
			pageselectCallback(currentPage);
			
		 }
		 if(pageIndex=='next'){
			if(currentPage<lastPage-1)
			   currentPage=currentPage+1;
			pageselectCallback(currentPage);
		 }	 
		 if(!isNaN(pageIndex)){
			 currentPage=parseInt(pageIndex);
			 pageselectCallback(currentPage);
		 }
	
		 //如果分页总数为1，则值所有项为失效
		 if(lastPage==1){
			$("#first").attr("src","skins/images/page/page_first_b.gif"); 
 		    $("#previous").attr("src","skins/images/page/page_pre_b.gif"); 
 		    $("#next").attr("src","skins/images/page/page_next_b.gif"); 
 		    $("#last").attr("src","skins/images/page/page_last_b.gif"); 
		 }else{
			//如果总页数>1页
    	     //当前页为首页，则下一页和尾页置有效
    	     if(currentPage==0){
    		    $("#first").attr("src","skins/images/page/page_first_b.gif"); 
    		    $("#previous").attr("src","skins/images/page/page_pre_b.gif"); 
    		    $("#next").attr("src","skins/images/page/page_next_a.gif"); 
    		    $("#last").attr("src","skins/images/page/page_last_a.gif"); 
    		 }else if(currentPage==lastPage-1){
    			//如果当前页为尾页，则置上一页和首页有效
    		    $("#first").attr("src","skins/images/page/page_first_a.gif"); 
    		    $("#previous").attr("src","skins/images/page/page_pre_a.gif"); 
    			$("#next").attr("src","skins/images/page/page_next_b.gif"); 
    			$("#last").attr("src","skins/images/page/page_last_b.gif"); 
    	     }else{
    		    //如果当前页处于中间，则置所有跳转有效
    			$("#first").attr("src","skins/images/page/page_first_a.gif"); 
    			$("#previous").attr("src","skins/images/page/page_pre_a.gif"); 
    			$("#next").attr("src","skins/images/page/page_next_a.gif"); 
    			$("#last").attr("src","skins/images/page/page_last_a.gif"); 
    		 }
		 }
		 
	} 
   /*  //清除所有车辆
    function clearAllCars(){
    	//在页面上清除所有复选框的勾选状态(单独处理，因为与分页是相分离的)
		$("input:checkbox").prop("checked",false);
		//在checkboxStatus数组中清除所有复选框的勾选状态
		for(var i=0;i<checkboxStatus.length;i++){
			checkboxStatus[i]=false;
		}
      	//清除allRecordedCars中的车辆记录的选中状态
  		for(var i=0;i<allRecordedCars.length;i++){
  			if(allRecordedCars[i]!==undefined)
  			   allRecordedCars[i][1]=false;
  		}
  	    //清除地图上的所有覆盖物,这里需要手动清除，因为flushTrack由于条件限制，不会进行任何操作
  		map.clearOverlays();
    }
    //恢复异常车辆
    function recoverAbnormalCars(){
    	clearAllCars();	
    }  */
    </script>
</body>
</html>
