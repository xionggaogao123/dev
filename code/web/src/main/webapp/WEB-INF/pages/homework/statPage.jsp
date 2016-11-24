
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <link rel="stylesheet" type="text/css" href="/static/js/jquery-ui-1.11.1.custom/jquery-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/flippedStatistics.css" />
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <link rel="stylesheet" type="text/css" href="/static/js/timepicker.js"/>
    <%--<link rel="stylesheet" href="/skin/circle.skin/circle.player.css">--%>
    <script type="text/javascript" src="/static/js/jquery.jplayer.min.js"></script>
    <script type="text/javascript" src="/static/js/jquery.transform2d.js"></script>
    <script type="text/javascript" src="/static/js/jquery.grab.js"></script>
    <script type="text/javascript" src="/static/js/mod.csstransforms.min.js"></script>
    <script type="text/javascript" src="/static/js/circle.player.js"></script>

    <link rel="stylesheet" type="text/css" href="/static/css/exercise/teacher_configuration.css">
    <link rel="stylesheet" type="text/css" href="/static_new/css/reset.css" />
    <script type="text/javascript">
        var courseid="${lessonId}";
        
        var exerciseId="${exerciseId}";
        var timeType=1;


        function refreshPageByTime(t,st,et)
        {
            t= typeof t=='undefined'?0:t;
            st= typeof st=='undefined'?'':st;
            et= typeof et=='undefined'?'':et;
            timeType=t;
            $(".loading").show();
            getVideoInfo(t,st,et);
            if(exerciseId)
            {
                 getQuestions(t,st,et);
            }
            $(".loading").fadeOut(1000);
        }

        function getVideoInfo(t,st,et){
            $.ajax({
                url:"/lesson/videoview/list.do",
                type:"get",
                dataType:"json",
                async:false,
                data:{
                    lessonId:"${lessonId}",
                    classId:"${classId}",
                    timeType:t,
                    starttime:st,
                    endtime:et
                },
                success:function(data){
                    $('#fc_video_see_list').empty();
                    var html = '';
                    for(var i = 0; i < data.rows.length; i++){
                        var set = data.rows[i];
                        html += listHeader(set);
                        var stuList = set.studentrecord;
                        html += showCourseLogInfo(stuList);
                        html += '<div class="header_foot"></div>';
                    }
                    $('#fc_video_see_list').append(html);
                }
            });
        }

        function listHeader(set){

            var header =
                    '<div class="tatistics-head">'+
                    '<span class="vedioname">'+ set.videoName+'</span>'+
                    '<span class="vedioclick">点击<strong class="tatistics-num">'+ set.viewNumber +'</strong></span>'+
                    '<span class="vediofinish">看完<strong class="tatistics-num">'+ set.endViewNumber +'</strong></span>'+
                    '<span class="unview">未看<strong class="tatistics-num">'+ set.notViewNumber +'</strong></span>'+
                    '<span class="viewmore up" onclick="stuListShow(this)">收起观看统计</span>'+
                    '</div>'+
                    '<div class="header">'+
                    '<span class="I">学生名称</span>|'+
                    '<span class="II">班级</span>|'+
                    '<span class="III">观看时间</span>|'+
                    '<span class="IV">观看次数</span>|'+
                    '<span class="V">状态</span>|'+
                    '</div>';
            return header;
        }

        function showCourseLogInfo(data)
        {
            var html = '<ul style="border-bottom:none;">';
            if(data){
                for(var i=0;i<data.length;i++){
                    var cs=data[i];
                    if(cs.viewState==1){
                        viewstatus="已看完";
                    }
                    if(cs.viewState==0){
                        viewstatus="已点击";
                    }
                    if(cs.viewState==-1){
                        viewstatus="未看过";
                    }
                    if(cs.viewTime==null ||cs.viewTime == 0)
                    {
                        cs.viewTime="";
                    }
                    else{
                        cs.viewTime = new Date(cs.viewTime).toLocaleString();
                    }
                    var p='';
//                    if(cs.voiceInfo)
//                    {
//                        p='<a onclick="showPlayed(this);" url="/upload/voice/'+cs.voiceName+'"><span class="VI"><img src="/img/play1.png"/></span></a>';
//
                        /* p=
                         '<div id="cp_container_1'+ i +'" class="cp-container" onclick="playVoice(\'/upload/voice/'+cs.voiceName+'\','+ i +')">'+
                         '<div class="cp-buffer-holder">'+
                         '<div class="cp-buffer-1"></div>'+
                         '<div class="cp-buffer-2"></div>'+'</div>'+
                         '<div class="cp-progress-holder">'+
                         '<div class="cp-progress-1"></div>'+
                         '<div class="cp-progress-2"></div></div>'+
                         '<div class="cp-circle-control"></div>'+
                         '<ul class="cp-controls"><li style="padding:0;"><a class="cp-play" tabindex="1">play</a></li>'+
                         '<li style="padding:0;"><a class="cp-pause" style="display:none;" tabindex="1">pause</a></li></ul></div>'+
                         '<div id="jquery_jplayer_1'+ i +'" class="cp-jplayer"></div>';  */
                    //}
                    html+='<li class="content"><span class="I">'+ cs.userInfo.value +'</span><span class="II">'+ cs.classInfo.value +'</span><span class="III">'+ cs.viewTime +'</span>';
                    html+='<span class="IV" style="margin-left:20px;">'+ cs.viewCount +'</span><span class="V">'+ viewstatus +'</span>'+ p +'</li>';
                }
            }
            html += '</ul>';
            return html;
        }
        function playVoice(url,i){
            var host = window.location.host;
            new CirclePlayer("#jquery_jplayer_1"+i,
                    {
                        mp3: "http://"+host+url
                        //m4a:"http://www.jplayer.org/audio/m4a/Miaow-07-Bubble.m4a"
                    }, {
                        cssSelectorAncestor: "#cp_container_1"+i,
                        swfPath: "js",
                        //wmode: "window",
                        supplied: "mp3",
                        //keyEnabled: true
                    });
        }

        function showPlayed(ob)
        {
            var player='<embed src="'+ $(ob).attr('url') +'" belong="123" width="0" height="0" autostart="true" />';
            $('embed[belong=123]').remove();
            $('body').append(player);
        }




      
        
        
        function getQuestions(t,st,et){
            $.ajax({
                url:"/exam/answer/stat/list/data.do",
                type:"get",
                dataType:"json",
                async:false,
                data:{
                    'id':"${exerciseId}",
                    'lessonid':"${lessonId}",
                    'classId':"${classId}"
                },
                success:function(data){

                        showQuestions(data);
                }
            });
        }



        function showQuestions(data){
            var totalCount = data.totalCount;

            var html=
                    '<div class="ypheader">'+
                    '<h4>微课进阶练习统计<span class="FloatC" style="font-weight:lighter;font-size:14px;float:right;padding:0 8px;"></span></h4>'+
                    '</div>'+
                    '<ul>'+
                    '<li>'+
                    '<div style="height: 30px;line-height: 30px;;width: 900px;background-color: #F5F5F5;border-bottom:1px solid #DDDDDD;margin: 0 0 0 0px;">'+
                    '<span style="display: inline-block;color:#676767;width: 150px;text-align:center">题号</span>'+
                    '<span style="display: inline-block;color:#676767;width: 150px;text-align:center">正确率</span>'+
                    '<span style="display: inline-block;color:#676767;width: 200px;text-align:center">统计</span>'+
                    '<span style="display: inline-block;color:#676767;width: 200px;text-align:center">批改进度</span>'+
                    '</div>'+
                    '</li>'+
                    '</ul>';



            for(var i = 0; i < data.statList.length; i++) {
                var itemData = data.statList[i];

                var unCount = totalCount - itemData.answerCount;

                var itemtypestr = '';
                switch(itemData.type){
                    case 1:
                        itemtypestr = '选择题';
                        break;
                    case 3:
                        itemtypestr='判断题';
                        break;
                    case 4:
                        itemtypestr='填空题';
                        break;
                    case 5:
                        itemtypestr='主观题';
                        break;
                }
                html+='<li><div class="modify_main_III"><div class="modify_main_III_XZ_I">'+
                '<span class="modify_main_III_XZ_II">'+itemData.titleId+'</span><br>'+
                '<span>'+itemtypestr+'&nbsp;&nbsp;</span><span>'+itemData.score+'分</span>'+
                '</div>';

                html+= '<div class="modify_main_III_XZ_III">'+ itemData.rate;
                if(itemData.rate != ''){
                    html += '<span style="font-size: small">正确：'+itemData.rightCount+'人，错误：'+itemData.wrongCount+'人，未交：' + unCount + '人</span>';
                }
                      html += '</div>';

                if(itemData.type == 1 || itemData.type == 4){//选择题

                    html+='<div class="modify_main_III_XZ_IIII"><div>';
                    
                    
                    
                  if(itemData.answerCounts)
                	 {
                	  for(var j=0;j<itemData.answerCounts.length;j++)
                		  {
                		  var ac=itemData.answerCounts[j];
                		  try
                		  {
	                		  html+='<div style="position: relative;height: 30px;width:180px;overflow: hidden">'+
	                          '<span class="modify_main_III_XZ_V">'+ac.answer+
	                          '</span> <span class="modify_main_III_XZ_VI" style="width: '+
	                          (ac.count==0?0:(100*ac.count/itemData.answerCount))+
	                          'px;"></span><span class="modify_main_III_XZ_VII">'+
	                           ac.count+'人</span>'+
	                            '</div>';
	                		  
                		  }catch(x)
                		  {}
                		  }
                	 }
                    
                    
                    html+='</div> </div>';
                }
                else if(itemData.type == 3){//判断题
                    var num = itemData.answerMap["1"] + itemData.answerMap["0"];
                    if(num == 0){
                        num=1;
                    }
                    html+=
                    '<div class="modify_main_III_PD_I" ><div><span class="modify_main_III_PD_II">选对&nbsp;&nbsp;</span> <span class="modify_main_III_PD_III" style="width:'+
                    100*itemData.answerMap["1"]/num+
                    'px;"></span><span class="modify_main_III_XZ_VII">'+
                    itemData.answerMap["1"]+'人</span><br>'+
                    '<span class="modify_main_III_PD_II">选错&nbsp;&nbsp;</span> <span class="modify_main_III_PD_IIII" style="width:'+
                    100*itemData.answerMap["0"]/num+
                    'px;"></span><span class="modify_main_III_XZ_VII">'+
                    itemData.answerMap["0"]+'人</span><br></div></div>';

                }
                else if(itemData.type == 4){//填空题
                    
                       
                }
                else if(itemData.type == 5){//主观题和选择题一样
                	

                    html+='<div class="modify_main_III_XZ_IIII"><div>';
                    var num = 0;
                    for( answerMapKey in itemData.answerMap) {
                        num += itemData.answerMap[answerMapKey];
                    }
                    for( answerMapKey in itemData.answerMap) {
                        html+='<div style="position: relative;height: 30px;width:180px;overflow: hidden">'+
                        '<span class="modify_main_III_XZ_V">'+answerMapKey+
                        '</span> <span class="modify_main_III_XZ_VI" style="width: '+
                        100*itemData.answerMap[answerMapKey]/num +
                        'px;"></span><span class="modify_main_III_XZ_VII">'+
                        itemData.answerMap[answerMapKey]+'人</span>'+
                        '</div>';
                    }
                    html+='</div> </div>';
                }

                html+='<div class="modify_main_III_XZ_SU"><span>'+(itemData.type == 5?itemData.scoreCount:itemData.answerCount)
                +'</span><span>/</span><span>'+itemData.answerCount+
                '</span> </div> <div class="modify_main_III_XZ_CK">'+
                '<a href="/exam/item/answer.do?docId='+itemData.documentId+'&titleId='+itemData.id+'&lesson=${lessonId}'+'&classId=${classId}'+
                '" style="background-color:#60B760;padding:5px 20px;;position: relative;display: inline;color: #ffffff;border-radius: 3px;cursor: pointer"  target="_blank">';
                if(itemData.type == 5){
                    html+='进入批改';
                } else {
                    html+='查看';
                }
                html+='</a></div></div></li>';
            }


            $('#exercise_statistics').html(html);
        }
        function redirectTo(url) {
            location.href = url;
        }

        function openMarkPaperWindow(e, url) {
            window.open(url);
            //禁止事件冒泡
            if (!e)
                e = window.event;

            if (e.stopPropagation) {
                e.stopPropagation();
            }
            else {//IE 8-
                e.cancelBubble = true;
            }
        }

        $(function(){
            courseid=$.trim($("body").attr("csid"));
            refreshPageByTime(1);
            $(".ui_timepicker").datetimepicker({
                //showOn: "button",
                //buttonImage: "./css/images/icon_calendar.gif",
                //buttonImageOnly: true,
                showSecond: false,
                timeFormat: 'HH:mm',
                onSelect: function(){}
            });
            $(".ui_timepicker").change(function(){
                if($("input[name=timetype]:checked").val()==3)
                {
                    if($('#start-time').val().length>0 && $('#end-time').val().length>0)
                    {
                        refreshPageByTime(3,$('#start-time').val(),$('#end-time').val());
                    }
                }
            });
        });
        function stuListShow(obj){
            var stat = $(obj).prop('className');
            var $next = $(obj).parent().next('.header');
            if(stat.indexOf('up') > 0){
                $(obj).removeClass('up').addClass('down');
                $next.hide();
                $next.next('ul').hide();
                $next.next('ul').next('.header_foot').hide();
                $(obj).html('查看观看统计');
            }else{
                $(obj).removeClass('down').addClass('up');
                $next.show();
                $next.next('ul').show();
                $next.next('ul').next('.header_foot').show();
                $(obj).html('收起观看统计');
            }
        }
        function stuListShow1(obj){
            var stat = $(obj).prop('className');
            if(stat.indexOf('up') > 0){
                $(obj).removeClass('up').addClass('down');
                $('#hwtj1').hide();
                $(obj).html('查看作业统计');
            }else{
                $(obj).removeClass('down').addClass('up');
                $('#hwtj1').show();
                $(obj).html('收起作业统计');
            }
        }
    </script>

    <style>
        #fc_video_statistics{background:white;}
        .header_foot{height:20px;background:#ECF5FF;border-left:1px solid #D0D0D0;border-right:1px solid #D0D0D0;}
        .ypheader >div{/* padding:10px 5px; */border:1px solid #D0D0D0;}

        #fc_video_see_list span.I{width:200px;}
        #fc_video_see_list span.II{width:200px;}
        #fc_video_see_list span.III{width:150px;}
        #fc_video_see_list span.IV{width:100px;}
        #fc_video_see_list span.V{width:100px;}
        #fc_video_see_list span.VI{width:80px;}


        #fc_video_see_list .header span.V{width:100px;}
        #fc_video_see_list .header span.VI{width:60px;}

        #fc_ques_list ul li{cursor:pointer;}
        #fc_ques_list ul span.I{text-align:left;word-break:break-word;
            word-wrap:break-word;}
        .fc_statistics_list span.I{width:250px;padding-left:10px;}
        .fc_statistics_list span.II{width:150px;}
        .fc_statistics_list span.III{width:150px;}
        .fc_statistics_list span.IV{width:150px;}
        .fc_statistics_list span.V{width:150px;}

        .fc_statistics_list li span.I{width:252px;padding-left:10px;}
        .fc_statistics_list li span.II{width:158px;}
        .fc_statistics_list li span.III{width:159px;}
        .fc_statistics_list li span.IV{width:159px;}
        .fc_statistics_list li span.V{width:150px;}
    </style>
    <title>作业统计</title>
</head>
<body>

<%@ include file="../common_new/head.jsp" %>
<div class="ypheader center forIEFloatP" id="hwtj">
    <div>
        <h4>作业提交信息
            <%--<span class="FloatC" style="font-weight:lighter;font-size:14px;float:right;padding:0 8px;">--%>

            <%--</span>--%>
            <span class="viewmore" onclick="stuListShow1(this)" id="ck"></span>
            <button class="letter" onclick="stat.sendLetters()" style="float: right;margin-top: -6px; margin-right: 20px;">私信通知</button>
        </h4>
    </div>
</div>

<div class="fc_statistics_list ypheader center" id="hwtj1">
    <div class="header">
    <span class="I">学生名称</span>|
    <span class="II">班级</span>|
    <span class="III">提交次数</span>|
    <span class="IV">状态</span>|
    <%--<span class="V">状态</span>|--%>
    <%--<span class="VI">语音</span>--%>
    </div>
    <ul  id="submitInfo">
        <%--<li class="content">--%>
            <%--<span class="I">张三</span>--%>
            <%--<span class="II">二班</span>--%>
            <%--<span class="III">3</span>--%>
            <%--<span class="IV">已看完</span>--%>
        <%--</li>--%>
    </ul>
</div>
<%--=====================================--%>
<div id="fc_video_statistics" class="ypheader center forIEFloatP">
    <div>
        <h4>视频汇总信息
            <span class="FloatC" style="font-weight:lighter;font-size:14px;float:right;padding:0 8px;">
                <input type="radio" name="timetype" value="1" onclick="refreshPageByTime(1);" checked />全部时间 &nbsp;&nbsp;<input type="radio" name="timetype"  value="24小时" onclick="refreshPageByTime(2);"/>24小时&nbsp;&nbsp;
                <%--<input type="radio" name="timetype"  value="3" onclick="refreshPageByTime(3);"/>--%>
                <%--<input id="start-time" class="ui_timepicker" readonly="" type="text" placeholder="请选择开始时间" style="width: 100px;">-<input id="end-time" class="ui_timepicker" readonly="" type="text" placeholder="请选择结束时间" style="width: 100px;">--%>
            </span>
        </h4>
    </div>
</div>

<div id="fc_video_see_list" class="fc_statistics_list ypheader center">

</div>




<div id = "exercise_statistics" style="width: 900px;height: auto;border:1px solid #DDDDDD;overflow-x:hidden;position: relative;margin: 0 auto;background-color: #ffffff">



</div>


<%@ include file="../common_new/foot.jsp" %>

<script id="submitInfoTmpl" type="text/template">
    {{ for(var i in it) { }}
    <li class="content">
    <span class="I">{{=i}}</span>
    <span class="II">{{=it[i].className}}</span>
    <span class="III">{{=it[i].subNum}}</span>
    <span class="IV">{{? it[i].subNum==0}}未{{??}}已{{?}}提交</span>
    </li>
    {{ } }}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    var stat;
    seajs.use('/static_new/js/modules/homework/0.1.0/statPage.js', function (statPage) {
        stat = statPage;
        statPage.init();
    });
</script>
</body>
</html>