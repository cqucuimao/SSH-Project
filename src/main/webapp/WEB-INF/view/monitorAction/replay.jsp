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
		
	    .cover {       
	            position: absolute; top: 0px; filter: alpha(opacity=60); background-color: #777;     
	            z-index: 9; left: 0px;     
	            opacity:0.5; -moz-opacity:0.5;     
	            display:table;overflow:hidden;
	        }     
</style>    
<link href="js/jquery-ui/jquery-ui.min.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="skins/main.css">
</head>
<body class="minW">
    <div class="space">
        <div class="title subtract">
            <h1>轨迹回放</h1>
            <p>&nbsp;&nbsp;</p>
        </div>
        <!-- 查询条件 -->
        <div class="editBlock search subtract">
        	<!-- 遮罩层 begin -->
	        <div id="mask" style="left:0px;top:0px;position:fixed;height:100%;width:100%;overflow:hidden;z-index:10;display:none">
		        <table style="WIDTH:100%; BORDER:0;CELLSPACING:0;CELLPADDING:0" >
		       		<tr height="360">
		        		<td>&nbsp;</td>
		        	</tr>
		        	<tr>
		        		<td width="30%"></td>
		        		<td style="text-align:center">
		        			<table style="width:100%; text-align:center; CELLSPACING:0;CELLPADDING:0" >
		        				<tr>
		        						<td style="text-align:center">
		        							<img style="text-align:center" src="skins/images/cover.png"/>
		        						</td>
			       				</tr>
			       			</table>
		        		</td>
		       			<td style="width:30%;"></td>
		        	</tr>
		      </table>
	       </div>
	        <div id="cover" style="background:#cdd0cf;filter:alpha(opacity=10);opacity:.7;left:0px;top:0px;position:fixed;height:100%;width:100%;overflow:hidden;z-index:9;display:none">
		        <table style="WIDTH:100%; height:100%; BORDER:0; CELLSPACING:0; CELLPADDING:0">
		        	<tr>
		        		<td align="center"></td>
		        	</tr>
		        </table>
	        </div>
	        <!-- 遮罩层 end -->
        <s:form id="queryForm">
            <table id="queryInfoTB">
                <tr>
                    <th>车牌号</th>
                    <td><cqu:carSelector name="car"/></td>
                    <th>起始时间</th>
					<td>
						<input class="Wdate half" style="width:165px;" type="text" id="beginTime" onfocus="new WdatePicker({dateFmt:'yyyy/MM/dd HH:mm:ss'})" />
						- 
						<input class="Wdate half" style="width:165px;" type="text" id="endTime" onfocus="new WdatePicker({dateFmt:'yyyy/MM/dd HH:mm:ss'})" />
                        <input class="inputButton" type="button" id="queryBn" value="查询" onclick=""/>
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
                        <col></col>
                    </colgroup>
                    <thead>
                        <tr>
                            <th class="alignCenter"><input type="checkbox" id="checkAll"/></th>
                            <!-- <th>行程段号</th> -->
                            <th>开始时间</th>
                            <th>结束时间</th>
                            <th>时间(min)</th>
                            <th>里程(KM)</th>
                            <th>播放时间(s)</th>
                        </tr>
                    </thead>
                    <tbody class="tableHover" id="htcList">
                    </tbody>
                </table>
            </div>
        </div>
        <div class="pageToolbar" id="pageBar" style="display:none;">
			    <span class="page" id="pageInfo"><span id="currentPageNum"></span>/<span id="totalPageNum"></span>&nbsp;&nbsp;
			    	<a href="javascript: gotoPage('first')"><img id="first" src="skins/images/page/page_first_b.gif" alt="first page" /></a>
			    	<a href="javascript: gotoPage('previous')"><img id="previous" src="skins/images/page/page_pre_b.gif" alt="previous page"/></a>
			    	<a href="javascript: gotoPage('next')"><img id="next" src="skins/images/page/page_next_a.gif" alt="next page"/></a>
			    	<a href="javascript: gotoPage('last')"><img id="last" src="skins/images/page/page_last_a.gif" alt="last page"/></a>
			    	转到： <select id="pageSkip" onchange="gotoPage(this.value)">
			    	</select>
			  	</span>
		</div>
	    <!-- 总记录列表，Ajax请求获得的所有数据，这些数据需要隐藏-->
	    <table id="Searchresult" style="display:none;">		
	    
	</table>
		
        
        <!-- 播放控制栏 -->
        <div class="playBar mt10 subtract" id="playBar" style="display:none;">
        	<div class="timeAndAddress" style="font-size:16px;margin-left:300px"></div>
        	<div>
	            <span class="speed switch"><label class="on" id="normalBn">正常</label><label id="fastBn">快速</label><label id="fasterBn">特快</label></span>
	            <span class="playBtn" id="play">Play</span>
	            <span class="playSlider" style="">
	                <span class="playTime time_start" id="playTimeStart">0:00</span>
	                <div class="slider" ></div>
	                <span class="playTime time_end" id="playTimeEnd">0:00</span>
	            </span>
            </div>
        </div>
        <div id="BaiduMap"></div>
    </div>
    <script src="http://api.map.baidu.com/api?v=2.0&ak=${baiduKey }"></script>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/LuShuUpdate.js"></script>
    <script type="text/javascript" src="js/jquery-ui/jquery-ui.min.js"></script>
    <script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
    <script src="js/artDialog4.1.7/artDialog.source.js?skin=blue"></script>
	<script src="js/artDialog4.1.7/plugins/iframeTools.source.js"></script>	
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript">
    
    	//显示遮罩层
    	function showMask(){   
    		$("#cover").css("height",$(document).height());     
            $("#cover").css("width",$(document).width());     
            $("#cover").show();   
        	$("#mask").show();     
	    }  
	    //隐藏遮罩层  
	    function hideMask(){     
	    	$("#cover").hide();   
	        $("#mask").hide();     
	    }  
    
        //从实时监控页面跳转过来后，需要填充时间
        //获取查询条件中车牌号的值
        var plateNumberValue=$("#queryInfoTB").find("tr").find("td").find("#carLabel").val();
        //如果车牌号不为空，说明是跳转过来的，为车牌号，开始时间，结束时间设置预定值
        if(plateNumberValue!=""){
        	//将当前值转化为xxxx-xx-xx xx:xx:xx日期格式
        	$("#beginTime").val(formatDate(new Date(),"yyyy/MM/dd HH:mm:ss").substring(0,10)+" 00:00:00");
        	$("#endTime").val(formatDate(new Date(),"yyyy/MM/dd HH:mm:ss"));
        }    
    
        //百度地图
        var map = new BMap.Map("BaiduMap");
        var mapPoint = new BMap.Point(106.464617,29.570331);
        map.centerAndZoom(mapPoint, 13);
        map.enableScrollWheelZoom(true);
        //百度地图API中gps坐标转百度地图坐标的web服务请求格式,前缀,后缀有服务器端添加
        var head="baidu.action?coords=";
        var head_1="baiduAddress.action?location=";
        //当前查询车辆的设备号
        var device_sn;
        //分段轨迹数据
        var trackPartsData;
        //轨迹段数，即相应车辆，当前时间段内的，所有轨迹的分段综述
        var length;
        //百度地图轨迹数据
        var tracksData;
        //地图上的画出来的轨迹路线
        var trackLines;
        //轨迹点序列对应的速度序列
        var tracksSpeed;
        //轨迹点对应的时间序列
        var trackTimes;
        //轨迹上点的详细地址
        var tracksLocation;
        
        //保存分页前复选框状态
        var checkboxStatus;
        
        //设置分页信息
        var num_entries;	  //记录总数
        var showCount;        //每页显示记录数
        var lastPage;         //总页数也即最后一页编号
        var currentPage;      //当前页
        
        //轨迹实际行驶时间和播放时间的比率
        var playRate=60;
        //存储轨迹列表,相应轨迹的播放时间以及轨迹的坐标点数，用于回显到播放条和计算播放条和轨迹点的映射
        var tracksPlayTimeAndPointLength=new Array();
        
        //路书播放
        var lushu=null;
        //选择的轨迹的点个数
        var selectedTracksPointsLength;
        
        //播放条的当前值
        var playCurrentVal=0;
        //播放条的最大值
        var playMaxVal=0;
        //播放刷新时间 ,用于设置 快速 和 特快 效果,正常速度为1000毫秒刷新1次，快速 为500毫秒刷新一次，特快 为 250毫秒刷新1次
        var refreshInternal=1000;
        //播放条的定时器
        var timer;
        //初始化播放栏
        initPlayBar();
        
        
        $("#queryBn").click(function(){
        	showMask();
        	//每次执行查询操作之后，地图重新清空
      		map.clearOverlays();   
        	//点击查询之后，清空列表
      		$("#htcList").html("");		
        	//点击查询之后播放条消失
        	$("#playBar").hide();
      		//查询相应车辆的设备编号即device_sn
      	    $.get("replay_list.action",$("#queryForm").serializeArray(),function(snsData){
      	    	  
      	    	  console.log("查询得到的设备数据信息");
      	    	  console.log(snsData);
      	    	  //获取返回信息的长度
      	    	  var snNum=snsData.sns.length;
      	    	  //如果snNum=0；表示该车辆没有安装gps设备
      	    	  if(snNum == 0){
           		   		hideMask();
      	    	  } 
      	    	  //如果设备device_sn信息不为空，取当前查询得到的sn数组的第一条记录(如果输入的是司机名称，出现多个同名司机，目前也只取第一个司机的车辆的sn号)
      	    	  if(!((snNum==1)&&(snsData.sns[0]==null))){
                     device_sn=snsData.sns[0].sn;
                   
                     console.log("设备编号");
                     console.log(device_sn);
                   
                     //获取开始和结束时间的时间戳
                     var beginTime=Date.parse(new Date($("#beginTime").val()));
                     var endTime=Date.parse(new Date($("#endTime").val()));
                     
                     console.log("开始时间时间戳");
                     console.log(beginTime);
                     console.log("结束时间时间戳");
                     console.log(endTime);
                     //查询相应device_sn号下，当前时间段内的所有轨迹段信息的请求url
                     var reqUrl="apiReplay.action?get.part.do?"+"device_sn="+device_sn+"&begin="+beginTime+"&end="+endTime;
                   
                     console.log("请求序列");
                     console.log(reqUrl);
                   
                     //通过服务器接口，查询轨迹列表
                     $.ajax({
                    	 type:"get",
                    	 dataType:'json',
                    	 url:reqUrl,
                    	 async:true,
                    	 error:function(){
                    		 alert("请求出错，请重试！");
                    	 },
                    	 success:function(json){
                    		 if(!(json.ret == 1)){
                      		   hideMask();
                  	    	   alert("请求出错，请重试！");
                  	       }
                  	       //由于获得的轨迹数据是反向序列，所以首先进行先将序列进行反向
                  	       trackPartsData=json;
                  	       //获取轨迹列表长度
                  	       length=trackPartsData.track.length;
                  	       if(length==0){
                      	       hideMask();
                  	    	   alert("该时间段内没有轨迹！");
                  	       }
                  	       hideMask();
                  	       //用于存储解析后的百度地图上要显示的轨迹数据
                  	       tracksData=new Array(length);
                  	       //用于存储解析后的百度地图上要显示的轨迹路径
                  	       trackLines=new Array(length);
                  	       //用于记录轨迹的点序列对应的速度序列（在轨迹回过程中，每个点都对应一个速度）
                  	       tracksSpeed=new Array(length);
                  	       trackTimes=new Array(length);
                  	       tracksLocation=new Array(length);
                  	       //为查询得到的轨迹列表进行倒序排列
                  	       var temp;
                  	       var mid=Math.floor(length/2);
                  	       for(var i=0;i<mid;i++){
                  		       temp=trackPartsData.track[i];
                  		       trackPartsData.track[i]=trackPartsData.track[length-i-1];
                  		       trackPartsData.track[length-i-1]=temp;
                  	       }
                  	       console.log("轨迹列表");
                  	       console.log(trackPartsData);
              		       //这里的复选框的下标与实时监控不同，实时监控的下标是相应车辆的id，而这里只是索引下标
                  	       checkboxStatus=new Array(length);
              		       for(var i=0;i<length;i++){
              			       checkboxStatus[i]=false;
              		       }
              		       //设置分页信息
                 	   	       num_entries = length;	  //获取记录总数，即轨迹数
                 	   	       showCount = 3;             //每页显示记录数
                 	   	       //计算分页数
                   	   	   lastPage=Math.ceil(num_entries/showCount);  //Math.ceil向上取整
                   	   	   
                   	   	   //初始化分页栏样式,realtime中此代码不再此位置,因为放在realtime相似位置时，无效，具体原因不明
                             initPageBarStyle();
                   	   	   
                 	   	       //设置当前页
                 	   	       currentPage=0;
                 	   		   pageList();         //查询之后调用分页组件
                 	   	       pageselectCallback(currentPage);  //分页
                 	   	       $("#pageSkip").empty();
                 	   	       //设置页码跳转框
                 	   	       var selector=$("#pageSkip");     
                 	           for(var i=0;i<lastPage;i++){ 
                 	               selector.append('<option value="'+i+'">'+eval(i+1)+'</option>');     
                 	           }
                 	           //分页组件的当前页和总页数信息
                 	           //如果总页数大于0，则置当前页为1，否则0
                 	           if(num_entries>0)
                 	   		      $("#currentPageNum").html(currentPage+1);
                 	           else
                 	        	  $("#currentPageNum").html(0); 
                 	   		   $("#totalPageNum").html(lastPage);
                    	 }
                     });
      		      }
      	    });
      		//显示数据列表
        	$("#dataList").show();
      		//显示分页组件
      		$("#pageBar").show();
      		
        });
        
        //全选按钮绑定单击事件响应
        $("#checkAll").bind("click", function(){
        	 
        	 //每次复选框改变之后，地图重新清空
      		 //map.clearOverlays();  
        	 if($("#checkAll").is(":checked")){
        		//在页面上标记所有复选框的勾选状态
          		$(".checkboxItems").prop("checked",true);
          		for(var i=0;i<checkboxStatus.length;i++){
        			checkboxStatus[i]=true;
        		}
          		
          		console.log("所有轨迹序列");
          		console.log(trackLines);
          		
          		//显示所有轨迹
          		for(var i=0;i<length;i++){
          		   //如果当前轨迹数据未曾获取，则获取轨迹数据，并画出轨迹
     			   if(trackLines[i]===undefined){
     				   drawTrack(i);
     			   }else{
     			   //若已有数据，则显示轨迹
     			       console.log(trackLines[i]);
     			       trackLines[i][0].show();
   		    	       trackLines[i][1].show();
   		    	       trackLines[i][2].show();
     			   }
          		}
          	    //复选框状态改变，则需要重新计算播放总时间  
   			    reSetPlayTimeEnd();
 	      	    //显示播放条
          	    $("#playBar").show();
        	 }else{
        		//在页面上清除所有复选框的勾选状态
         		$(".checkboxItems").prop("checked",false);
        		//在checkboxStatus数组中清除所有复选框的勾选状态
       		    for(var i=0;i<checkboxStatus.length;i++){
       			   checkboxStatus[i]=false;
       		    }
       		    //复选框状态改变，则需要重新计算播放总时间  
   			    reSetPlayTimeEnd();
       		    //隐藏所有轨迹
          		for(var i=0;i<length;i++){
          			 //如果轨迹已经存在，则隐藏轨迹
       		         if(!(trackLines[i]===undefined)){
       		    	      trackLines[i][0].hide();
       		    	      trackLines[i][1].hide();
       		    	      trackLines[i][2].hide();
       			     }
          		}
          	    //隐藏播放条
          	    $("#playBar").hide();
          	    //重新设置播放条的初始值
          	    $(".time_start").text("0:00");
        	 }	
        	 //重新选择轨迹后，播放条重置
 		     resetSlider();
        	 //由于重新选择轨迹之后，会导致传到路书中的行驶轨迹发生变化，所以需要从新生成路书对象，将原路书覆盖物清除，并将lushu对象置空
        	 if(lushu!=null){
        		lushu.stop();
        		lushu.remove();
            	lushu=null;
        	 }
        });
        
        //分页组件，将所有轨迹的列表组织成html元素，并隐藏，(调用分页结果显示函数时，每次只显示当前页面的记录)
        function pageList(){	   
        	//不能直接使用append方法，需要对上次内容清空，否则查询结果会叠加
        	$("#Searchresult").empty();	
        	//遍历查询到的jsonData数据，将信息解析成页面需要显示的表单格式
        	//trackPartsData.track待索引的数组，index索引下标，track索引下标对应的变量
        	$.each(trackPartsData.track,function(index,track){
        		var beginTime = new Date();
        		var endTime = new Date();
        		beginTime.setTime(track.states[1].receive);
        		endTime.setTime(track.states[0].receive);
        		//轨迹实际行驶时间
        		var realInterval=countRealInterval(track.states[1].receive, track.states[0].receive);
        		//轨迹播放时间
        		var playInterval=countPlayInterval(track.states[1].receive, track.states[0].receive,playRate);
        		//轨迹列表中的轨迹索引对应的播放时间，相应轨迹的gps坐标点数
        		tracksPlayTimeAndPointLength[index]=new Array(2);
        		tracksPlayTimeAndPointLength[index][0]=playInterval;
        		
        		//这里对轨迹进行过滤，每小时里程数小于2KM的，一律过滤掉（过滤的阀值后期可能更改）
        		var time = track.states[0].receive - track.states[1].receive;
        		if(track.distance/time < 2.0/3600000){
        			return true;
        		}
        		$("#Searchresult").append("<tr>"+"<td class='alignCenter'>"+"<input type='checkbox' name='checkItem' class='checkboxItems' id="+index+">"
        				                 +"<td>"+beginTime.toLocaleString()+"</td>"+"<td>"+endTime.toLocaleString()+"</td>"
        				                 +"<td>"+realInterval+"</td>"+"<td class='alignCenter'>"+track.distance+"</td>"+"<td>"+playInterval+"</td>");  
        	});
        	
        	//为除checAll之外的所有checkbox添加单击事件
    	    $(".checkboxItems").click(function(){
    	      //每次复选框状态改变之后，地图重新清空
         	  //map.clearOverlays();
    	      //在checkboxStatus数组中改变状态
    	      var index=$(this).prop("id");    	      
    		  //如果点击事件，使当前复选框被选中，那么改变相应复选框状态
    		  if($(this).is(":checked")){
    			 checkboxStatus[index]=true;
    			 //如果当前轨迹数据未曾获取，则获取轨迹数据，并画出轨迹
    			 if(trackLines[index]===undefined){
    				drawTrack(index);
    			 }else{
    			 //若已有数据，则显示轨迹以及该轨迹的起始点
    			    trackLines[index][0].show();
    			    trackLines[index][1].show();
    			    trackLines[index][2].show();
    			 }
    			 //复选框状态改变，则需要重新计算播放总时间  
    			 reSetPlayTimeEnd();
    	      	 //显示播放条
             	 $("#playBar").show();
    		    }else{
    		     //因为取消了一个，所以全选按钮的状态失效
     			 $("#checkAll").prop("checked", false);
    		     //在checkboxStatus数组中改变状态
    		     checkboxStatus[parseInt($(this).prop("id"))]=false;
    		     //如果轨迹已经存在，则隐藏轨迹
    		     if(!(trackLines[index]===undefined)){
    		    	  trackLines[index][0].hide();
    		    	  trackLines[index][1].hide();
    		    	  trackLines[index][2].hide();
    		    	  
    			 }
    		     //轨迹是否存在标志位
    		     var flag=false;
    		     //如果当前有轨迹被选中，则标志位置true
    		     for(var i=0;i<checkboxStatus.length;i++){
    		    	 if(checkboxStatus[i])
    		    		flag=true;
    		     }
    		     //复选框状态改变，则需要重新计算播放总时间  
    			 reSetPlayTimeEnd();
    		     //如果没有任何轨迹被选中
    		     if(!flag){
     		        //隐藏播放条
              	    $("#playBar").hide(); 
     		        //重新设置播放条的起始值
              	    $(".time_start").text("0:00");   
    		     }
    		    }
    		    //重新选择轨迹后，播放条重置
    		    resetSlider();
    		    //重新选择轨迹之后，会导致传到路书中的行驶轨迹发生变化，所以需要从新生成路书对象，将原路书覆盖物清除，并将lushu对象置空
    	        if(lushu!=null){
    	           lushu.stop();
    	           lushu.remove();
    	           lushu=null;
    	        }
             });
         }
        
         //分页回调函数，用于显示当前页面信息
         function pageselectCallback(page_index){
        	 //设置当前页编号
        	 $("#currentPageNum").html(currentPage+1);
        	 //设置跳转选择框的当前值
        	 $("#pageSkip").val(page_index); 
        	 //计算得到当前显示的页的记录(起始，终止)，从Searchresult中拷贝出相应记录，显示在htcList下
    		 var max_elem = Math.min((page_index+1) *showCount, num_entries);
        	 //清空htcList下的html内容
    		 $("#htcList").html("");		
    		 for(var i=page_index*showCount;i<max_elem;i++){
    			 //clone函数默认不能复制事件机制，所以需要带参数true,将单击事件的效果同时拷贝
    			 var new_content = $("#Searchresult tr:eq("+i+")").clone(true,true); 
    			 //将当前页的信息添加到htcList下
    			 $("#htcList").append(new_content); 
    		 }
    		 //根据显示不同分页前后保存的checkbox的状态来设置显示的checkbox状态
    		 $("#htcList input").each(function () {
    		    if(checkboxStatus[$(this).prop("id")]){
    			   $(this).prop("checked", true);
    			}else{
    			   $(this).prop("checked", false);
    			}
             }); 
    	 }
        
         //画出当前选中轨迹
         function drawTrack(index){
        	//将ajax请求置为同步请求，防止异步造成的轨迹乱序
       		//$.ajaxSettings.async=false;
        	showMask();
        	var BaiduPoints;    //存储百度point对象的数组，用于画出相应point序列组成的轨迹
        	var BaiduLocations;    //存储百度point对象的数组，用于轨迹回放时显示地点信息
        	var BaiduAddress;    
         	var begin=trackPartsData.track[index].states[1].receive;     //当前轨迹的开始时间
         	var end=trackPartsData.track[index].states[0].receive;       //当前轨迹的结束时间
         	console.log("=================查看一下trackPartsData里的数据===============");
         	console.log(trackPartsData);
         	//解析轨迹列表中的某一段轨迹的url
    		var reqUrl="apiReplay.action?get.track.do?device_sn="+device_sn+"&begin="+
    				   begin+"&end="+end+"&page_number=-1&page_size=-1";
    		//解析当前轨迹段的gps序列
    		 $.ajax({
                    	 type:"get",
                    	 dataType:'json',
                    	 url:reqUrl,
                    	 async:true,
                    	 error:function(){
                    		 hideMask();
                    		 alert("请求出错，请重试！");
                    	 },
                    	 success:function(gpsTrackData){
                   		  if(!(gpsTrackData.ret == 1)){
                     		   hideMask();
                 	    	   alert("请求出错，请重试！");
                   		  }
                   		  hideMask();
                    	  console.log("gpsTrackData 轨迹数据");
               			  console.log(gpsTrackData);
               			  //当前轨迹段的长度
               			  var trackPointsLength=gpsTrackData.track.length;
               			  //存储当前轨迹段的轨迹点数（每一段轨迹都由若干轨迹点组成）
               			  tracksPlayTimeAndPointLength[index][1]=trackPointsLength;
               			  tracksSpeed[index]=new Array(trackPointsLength);
               			  trackTimes[index] = new Array(trackPointsLength);
               			  //定义百度地图上的point数组
             		          BaiduPoints=new Array(trackPointsLength);
               			  //定义点在百度地图上的经纬度
               			  BaiduLocations = new Array(trackPointsLength);
               			  
               			  BaiduAddress = new Array(trackPointsLength);
               			  //百度地图的API坐标转换接口限制数
               	          var limitLen=98;
               	          //由于百度API的接口每次最多解析100个经纬度坐标，所以进行分段
               	          var parts=Math.floor(trackPointsLength/limitLen)+1;
               	          //最后一段的数据个数
               	          var lastPartNum=trackPointsLength%limitLen;
               	          
               	          //对每一段都进行坐标序列解析处理，每一段的gps坐标数据转换成百度API坐标转换请求格式
               	          for(var part=0;part<parts;part++){
               	        	  var gpsPositionStr="";
               	                //前几段都按固定值计算,最后一段另行处理
               	            	if(part!=parts-1){
               	            	   for(var i=0;i<limitLen;i++){
               	                	   if(i!=0)
               	               			  gpsPositionStr=gpsPositionStr+";";
               	               		   gpsPositionStr=gpsPositionStr+gpsTrackData.track[part*limitLen+i].lng+","+gpsTrackData.track[part*limitLen+i].lat;
               	               		   //获取轨迹上各个点的速度，并保留1位小数
              	                		   tracksSpeed[index][part*limitLen+i]=gpsTrackData.track[part*limitLen+i].speed.toFixed(1);
               	               		   //轨迹上各个点的实际时间
               	               		   trackTimes[index][part*limitLen+i] = gpsTrackData.track[part*limitLen+i].receive;
               	               	   }
               	            	}else{
               	            	   //最后一段的经纬度个数按剩余值进行计算
               	            	   for(var i=0;i<lastPartNum;i++){
               	                	   if(i!=0)
               	               			   gpsPositionStr=gpsPositionStr+";";
               	               		    gpsPositionStr=gpsPositionStr+gpsTrackData.track[part*limitLen+i].lng+","+gpsTrackData.track[part*limitLen+i].lat;
               	               		    //获取轨迹上各个点的速度，并保留1位小数
               	                		tracksSpeed[index][part*limitLen+i]=gpsTrackData.track[part*limitLen+i].speed.toFixed(1);
               	                		//轨迹上各个点的实际时间
                	               		   trackTimes[index][part*limitLen+i] = gpsTrackData.track[part*limitLen+i].receive;
               	               	   }
               	            	}
               	                //每一段轨迹的百度API坐标转换请求
               	           	    var reqUrl=head+gpsPositionStr;
               	           	    $.ajaxSettings.async=false;
               	           	    $.getJSON(reqUrl,function(baiduTrackData){
                    	    	         console.log("转换得到的百度地图轨迹序列");
                    	    	         console.log(baiduTrackData);
                    	    	         var pointsLength=baiduTrackData.result.length;
                    	    	         console.log("====当前轨迹段的长度===== "+pointsLength);
                	    		         for(var k=0;k<baiduTrackData.result.length;k++){
                	    		        	 BaiduLocations[part*limitLen+k] = baiduTrackData.result[k].y+","+baiduTrackData.result[k].x;
                	    			         BaiduPoints[part*limitLen+k]=new BMap.Point(baiduTrackData.result[k].x,baiduTrackData.result[k].y);
                	    		         }
                	    		        
                	                });
               	           	    
               	   		
               	          }
               	          //根据百度点序列数组，画出点序列轨迹，样式如下,蓝色
               	          var polyline = new BMap.Polyline(BaiduPoints, {strokeColor:"blue",strokeWeight:6,strokeOpacity:0.5});
               	          //在地图上添加轨迹路径
           	    	      map.addOverlay(polyline);
               	          //添加轨迹路径的起始点标志,注意，这里的点序列和路径真实起始点是相反的
               	          var endPoint=BaiduPoints[0];
               	          var startPoint=BaiduPoints[BaiduPoints.length-1];
                 	          var startIcon = new BMap.Icon("images/start.png", new BMap.Size(58,58));
                 	          var endIcon = new BMap.Icon("images/end.png", new BMap.Size(58,58));
                 	          var startMarker = new BMap.Marker(startPoint,{icon:startIcon});// 创建起点标注
                 	          var endMarker = new BMap.Marker(endPoint,{icon:endIcon});// 创建终点标注
           	      		  map.addOverlay(startMarker);             // 将标注添加到地图中
           	      		  map.addOverlay(endMarker);               // 将标注添加到地图中
               	          //保存轨迹路径对象，用于下一次同一轨迹的显示,同时保存起始点
               	          trackLines[index]=new Array(3);
           	              trackLines[index][0]=polyline;
           	      		  trackLines[index][1]=startMarker;
           	      		  trackLines[index][2]=endMarker;
           	              //将得到的百度点序列保存到对应的轨迹数据中，用于轨迹回放
               	          tracksData[index]=BaiduPoints;
           	              tracksLocation[index] = BaiduLocations;
                    	 }
    		 });
    		/* $.get(reqUrl,function(gpsTrackData){
    			  
    		 }); */
    		 //将ajax请求恢复为异步请求
       		 $.ajaxSettings.async=true;
         }
         
        //初始化播放栏
     	function initPlayBar() {
            // 初始化滑动条
            $('.slider').slider({
                value: 0,
                min: 0,
                max: playMaxVal,
                step: 1,
                slide: function(event, ui) {
                    
                },
                change: function(event, ui) {
                    $(".time_start").text(numToTimeFormat(ui.value));
                },
                stop: function(event, ui) {
                    //pauseSlider();
                }
            });
            // 初始化播放按钮
            $('#play').button({
                text: false,
                icons: { primary: "ui-icon-play" }
            }).click(function() {
                var val = $('.slider').slider('value'),
                    max = $('.slider').slider('option', 'max');
                if (val == max) {
                    resetSlider();
                    playSlider();
                }
                var options;
                //如果当前状态为播放，设置播放按钮为 可暂停，然后执行播放，否则 进行暂停设置
                if ($(this).text() === "Play") {
                    options = {
                                label: "Pause",
                                icons: { primary: "ui-icon-pause" }
                    };
                    //开始执行播放行为,播放行为在 定时器设置的第一个refreshInternal后执行
                    playSlider();
                }else{
                    options = {
                    label: "Play",
                    icons: { primary: "ui-icon-play" }
                    };
                    pauseSlider();
                }
                //设置播放按钮状态
                $(this).button("option", options);
            });
        }
        //播放设置
     	function playSlider() { 
     		//设置最大值
     		$('.slider').slider('option', 'max', playMaxVal);   
     		//播放事件
            clearInterval(timer);
            $(".time_start").addClass("active");
            timer = setInterval(playAction, refreshInternal);
        }
     	//播放过程每次刷新时的行为
     	function playAction(){
            var val= $('.slider').slider('value');    
            console.log("PPPPPPP查看播放进度PPPPPPPPP");
     		console.log("转换前="+val);
     	   //标记选择的轨迹的索引
            var index=0;
     	   //存储轨迹相应点对应的实际时间数组
     	   var allTracksTime=new Array();
     	   //存储轨迹点对应详细地址
     	   var allTracksLocation=new Array();
     	   //由于gps数据中存储数据和轨迹上点序列的相应数据是倒序的，所以处理时对每一段数据都要进行倒序处理
     	   console.log("length="+checkboxStatus.length);
            for(var i=0;i<checkboxStatus.length;i++){
         	   if(checkboxStatus[i]){
      			  for(var j=tracksData[i].length-1;j>=0;j--){
      			      allTracksTime[index] = trackTimes[i][j];
      			      allTracksLocation[index] = tracksLocation[i][j];
      			      index++;
      			  }
      		   }
            }
     		if(val==0){
     			//初始化lushu对象
                lushu=initLushu();
                //开始执行lushu的start方法,将车辆停留在起始点
                lushu.start();
     		}
            
            var max = $('.slider').slider('option', 'max');
            var newVal = val < max ? val + 1 : 0;
			
            
            //如果已经播放到最后，则停止滑动条
            if (newVal == 0) {
                pauseSlider();
                
            } else {
                //否则滑动到下一个点
                $('.slider').slider('value', newVal);
                var dateString = allTracksTime[siderValueToCarPos(newVal)-1];
                var addressString = allTracksLocation[siderValueToCarPos(newVal)-1];
                var date = new Date();
                date.setTime(dateString);
                
                var reqUrl_1 = head_1 + addressString;
                $.getJSON(reqUrl_1,function(baiduTrackAddress){
                	$(".timeAndAddress").text(date.toLocaleString()+" "+" "+baiduTrackAddress.result.formatted_address);
			    });
                
                lushu.moveNextPoint(siderValueToCarPos(newVal)-1);
            }
     	}
     	//暂停播放
     	function pauseSlider() {
            clearInterval(timer);
            $(".time_start").removeClass("active");
            var options = {
                label: "Play",
                icons: { primary: "ui-icon-play" }
            };
            $('#play').button("option", options);
        }
     	//重新设置播放条
     	function resetSlider() {
            clearInterval(timer);
            var options = {
                label: "Play",
                icons: { primary: "ui-icon-play" }
            };
            $('#play').button("option", options);
            $('.slider').slider('value', 0);
        }
        
     	//初始化lushu对象
     	function initLushu(){
     		//如果lushu未实例化，则需要先进行实例化，否则直接进行操作
            if(lushu==null){
        	   //标记选择的轨迹的索引
               var index=0;
        	   //存储所有轨迹数据的数组
               var allTracksData=new Array();
        	   //存储所有轨迹相应点的速度的数组
               var allTracksSpeed=new Array();
        	   //由于gps数据中存储轨迹数据和轨迹上点序列的相应速度是倒序的，所以处理时对每一段数据都要进行倒序处理
               for(var i=0;i<checkboxStatus.length;i++){
            	   if(checkboxStatus[i]){
         			  for(var j=tracksData[i].length-1;j>=0;j--){
         			      allTracksData[index]=tracksData[i][j];
         			      allTracksSpeed[index]=tracksSpeed[i][j];
         			      index++;
         			  }
         		   }
               }
        	   console.log("合成的轨迹数据");
        	   console.log(allTracksData);
        	   console.log("合成的速度列表");
        	   console.log(allTracksSpeed);
        	   //选中的轨迹包含的点数
        	   selectedTracksPointsLength=allTracksData.length;
        	     
        	   lushu=new BMapLib.LuShu(map,allTracksData,allTracksSpeed,{
                                          defaultContent:"",
                                          autoView:true,//是否开启自动视野调整，如果开启那么路书在运动过程中会根据视野自动调整
                                          icon:new BMap.Icon('http://developer.baidu.com/map/jsdemo/img/car.png', 
        				                       new BMap.Size(52,26),
        				                       {anchor:new BMap.Size(27, 13)}),
                                          speed:0,
                                          enableRotation:true,//是否设置车辆随着道路的走向进行旋转
                                          landmarkPois:[]});
        	}
     		return lushu;
     	}
        
        /**
        * 每次复选框状态改变之后，重新计算播放总时间
        */
        function reSetPlayTimeEnd(){
        	 //获取当前被选中轨迹的播放时间，转化为整数后，赋值给最大播放时间
			 //如果有多段轨迹被选中 那么计算多段轨迹的总时间
			 playMaxVal=0;
			 for(var i=0;i<checkboxStatus.length;i++){
				 if(checkboxStatus[i]){
					playMaxVal=playMaxVal+timeToNumFormat(playTimeFormat(tracksPlayTimeAndPointLength[i][0]));
				 }
			 }
			   
			 console.log("查看一下checkbox的状态和tracksPlayTimeAndPointLength的状态");
			 console.log(checkboxStatus);
			 console.log(tracksPlayTimeAndPointLength);
			   
			 //设置播放条的播放总时间
	      	 $("#playTimeEnd").html(numToTimeFormat(playMaxVal));
	      	 console.log("当前应该设置的播放总时间为 "+playMaxVal);
        }
        
        //为正常 快速 和 特快 按钮绑定事件
        $("#normalBn").bind("click",normal);
        $("#fastBn").bind("click",fast);
        $("#fasterBn").bind("click",faster);
     	
     	//使用正常速度播放
     	function normal(){
     		clearInterval(timer);
     		timer=setInterval(playAction, refreshInternal);
     	}
     	//使播放速度为原来的2倍
     	function fast(){
     		clearInterval(timer);
     		timer=setInterval(playAction, refreshInternal/2);
     		//alert("执行了快速播放");
     		//console.log("执行了快速播放");
     	}
     	//使播放速度为原来的4倍
     	function faster(){
     		clearInterval(timer);
     		timer=setInterval(playAction, refreshInternal/4);
     		//alert("执行了超快播放");
     		//console.log("执行了超快播放");
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
    		 coverHidden();
    	} 
     	
        /**
         * 计算两日期时间差
         */
        function countRealInterval(beginTime, endTime) {
                 var seconds=((endTime - beginTime) / 1000).toFixed(0);
                 var hour=Math.floor(seconds/3600);
                 var minute=Math.floor(seconds%3600/60);
                 var second=seconds%3600%60;
                 if(hour==0){
                    return minute+"分"+second+"秒";
                 }
                 if(hour==0&&minute==0){
                    return second+"秒";
                 }
                 return hour+"小时"+minute+"分"+second+"秒";
        }

        /**
         * 计算轨迹播放时间差
         * playRate 真实播放时间和轨迹播放时间的比值
         */
        function countPlayInterval(beginTime, endTime,playRate) {
                var realSeconds=((endTime - beginTime) / 1000).toFixed(0);
                var playSeconds=(realSeconds/playRate).toFixed(0);
                var hour=Math.floor(playSeconds/3600);
                var minute=Math.floor(playSeconds%3600/60);
                var second=playSeconds%3600%60;
                if(hour==0&&minute==0){
                    return second+"秒";
                 }
                if(hour==0){
                   return minute+"分"+second+"秒";
                }
                return hour+"小时"+minute+"分"+second+"秒";
        }
        /**
        * 将x分x秒字符串转换为时间格式x:x
        */
        function playTimeFormat(time){
        	   var minute;
     	       var second;
     	       var boolS=time.indexOf("秒");
     	       var boolM=time.indexOf("分");
     	       if(boolS>0&&boolM>0){
     		      minute=time.substring(0,boolM);
     		      second=time.substring(boolM+1,boolS);
     		      return minute+":"+second;
     	       }else{
     	    	  second=time.substring(0,boolS);
     	          return "0:"+second;
     	       }
        }
        /**
         * 将整数y转换为时间格式x:x
         */
        function numToTimeFormat(num){
        	var minute=Math.floor((num/60));
     	    var second=num%60;
     	    if(second>=10)
     	    	return minute+":"+second;
     	    else
     	    	return minute+":"+"0"+second;
         } 
        /**
         * 将时间格式x:x转化为整数y
         */
        function timeToNumFormat(time){
            var splitIndex=time.indexOf(":");
            var length=time.length;
        	var minute=time.substring(0,splitIndex);
     	    var second=time.substring(splitIndex+1,length);
     	    return parseInt(minute*60)+parseInt(second);
         } 
         /**
         * 播放条滑动的终止值映射到车辆的坐标值
         */
         function siderValueToCarPos(sliderIndex){
        	 var sliderMax=playMaxVal;
        	 var pointsMax=selectedTracksPointsLength;
        	 var mappingPos=Math.floor((pointsMax/sliderMax)*sliderIndex);
        	 console.log("播放秒数 "+sliderMax+"  坐标总点数  "+pointsMax);
        	 console.log("播放条坐标   "+sliderIndex);
        	 console.log("映射坐标   "+mappingPos);
        	 return mappingPos;
         }
         
         
         /**
         * 日期转换函数
         * formatDate(new Date(),"yyyy-MM-dd hh:mm:ss") ==>2006-07-02 08:09:04
         */      
         function formatDate(date,fmt) {         
        	    var o = {         
        	    "M+" : date.getMonth()+1, //月份         
        	    "d+" : date.getDate(), //日         
        	    "h+" : date.getHours()%12 == 0 ? 12 : date.getHours()%12, //小时         
        	    "H+" : date.getHours(), //小时         
        	    "m+" : date.getMinutes(), //分         
        	    "s+" : date.getSeconds(), //秒         
        	    };         
        	    if(/(y+)/.test(fmt)){         
        	        fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));         
        	    }          
        	    for(var k in o){         
        	        if(new RegExp("("+ k +")").test(fmt)){         
        	            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));         
        	        }         
        	    }         
        	    return fmt;         
         } 
 
    </script>
</body>
</html>
