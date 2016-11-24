<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String contextPath=session.getServletContext().getContextPath();
    session.setAttribute("contextPath",contextPath);
%>
<!DOCTYPE html>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
<head>
    <title>开始答题-复兰科技</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    
    <script type="text/javascript" src="${contextPath}/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/plugins/flexpaper/flexpaper_flash.js"></script>
    <script type="text/javascript" src="${contextPath}/static/plugins/flexpaper/flexpaper.js"></script>
    <script type="text/javascript" src="${contextPath}/static/plugins/flexpaper/flexpaper_handlers.js"></script>
    <script type="text/javascript" src="${contextPath}/static/plugins/flexpaper/flexpaper_handlers_debug.js"></script>
    <script type="text/javascript" src="${contextPath}/static/plugins/flexpaper/flexpaper_flash_debug.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/vendor/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
    <script type="text/javascript" src="/static/js/swfobject/swfobject.js"></script>


<%--pdf 预览 控件--%>
    <script src="/static/js/exercise/plugins/util.js"></script>
    <script src="/static/js/exercise/plugins/api.js"></script>
    <script src="/static/js/exercise/plugins/metadata.js"></script>
    <script src="/static/js/exercise/plugins/canvas.js"></script>
    <script src="/static/js/exercise/plugins/webgl.js"></script>
    <script src="/static/js/exercise/plugins/pattern_helper.js"></script>
    <script src="/static/js/exercise/plugins/font_loader.js"></script>
    <script src="/static/js/exercise/plugins/annotation_helper.js"></script>
    <script>
        var page=1;
        function renderPage(i){
            PDFJS.workerSrc = '/static/js/exercise/plugins/worker_loader.js';
            var can=' <canvas id="the-canvas" />';
            $("#showPdf").empty();
            $("#showPdf").append(can);
            'use strict';
            PDFJS.getDocument('${pdfPath}').then(function(pdf) {
                if(i>pdf.numPages) {
                    alert("已翻到末页！")
                    i=pdf.numPages;
                }
                pdf.getPage(i).then(function(page) {
                    var scale = 1.1;
                    var viewport = page.getViewport(scale);
                    var canvas = document.getElementById('the-canvas');
                    var context = canvas.getContext('2d');
                    canvas.height = viewport.height;
                    canvas.width = viewport.width;
                    var renderContext = {
                        canvasContext: context,
                        viewport: viewport
                    };
                    page.render(renderContext);
                });
            });
        }
        function previousPage(){
            page=page-1;
            if(page<1) page=1;
            renderPage(page);
        }
        function nextPage(){
            page+=1;
            renderPage(page);
        }
        function previewSwf(){
            var fp = new FlexPaperViewer(
                    '/static/plugins/flexpaper/FlexPaperViewer',
                    'viewerPlaceHolder', { config : {
                        SwfFile : escape('${swfPath}'),
                        Scale : 0.6,
                        ZoomTransition : 'easeOut',
                        ZoomTime : 0.5,
                        ZoomInterval : 0.2,
                        FitPageOnLoad : true,
                        FitWidthOnLoad : false,
                        FullScreenAsMaxWindow : false,
                        ProgressiveLoading : false,
                        MinZoomSize : 0.2,
                        MaxZoomSize : 5,
                        SearchMatchAll : false,
                        InitViewMode : 'SinglePage',
                        ViewModeToolsVisible : true,
                        ZoomToolsVisible : true,
                        NavToolsVisible : true,
                        CursorToolsVisible : true,
                        SearchToolsVisible : true,
                        localeChain: 'en_US'
                    }});
        }
       $(document).ready(function () {
           //平台、设备和操作系统
           var system = {
               win: false,
               mac: false,
               xll: false
           };
           //检测平台
           var p = navigator.platform;
           system.win = p.indexOf("Win") == 0;
           system.mac = p.indexOf("Mac") == 0;
           system.x11 = (p == "X11") || (p.indexOf("Linux") == 0);
           //跳转语句
           if (system.win || system.mac || system.xll) {//转向后台登陆页面
        	   
        	   var swfExists=jQuery("#swfExists").val();
               if(swfExists==1)
            	   {
                     document.getElementById("configuration_img").style.display = "none";
                     previewSwf();
            	   }
               else
            	   {
            	   renderPage(1);
            	   }

           } else {
               renderPage(1);
           }
       });
    </script>
<%--==============================================--%>
    <link rel="stylesheet" type="text/css" href="/static/css/exercise/question.css">
</head>
<body>
<div class="task_title">
    <div class="task_title_main">
        <c:if test="${param.tty == 2}">
            <span class="task_title_main_II">提交截止日期</span>
            <span class="task_title_main_III">${date}</span>
            <span class="task_title_main_VI" onclick="answerFormTemp()">保存</span>
            <span class="task_title_main_II" style="display: none;">剩余时间</span>
            <span id="timmer" class="task_title_main_III" style="display: none">${time}:00</span>
        </c:if>
        <c:if test="${param.tty != 2}">
            <span class="task_title_main_II">剩余时间</span>
            <span id="timmer" class="task_title_main_III">${time}:00</span>
        </c:if>
        <c:if test="${dld == 1}">
            <a class="task_title_main_V" href="${pdfPath}" target="_blank">下载</a>
        </c:if>
        <c:choose>
            <c:when test="${missdeadline}">
                <span style="background-color: #758275" class="task_title_main_IIII">交卷</span>
            </c:when>
            <c:otherwise>
                <span onclick="answerForm()" class="task_title_main_IIII">交卷</span>
            </c:otherwise>
        </c:choose>

    </div>
</div>
<div class="task_center_main">
  <input type="hidden" id="swfExists" name="swfExists" value="${swfExists}"/>
    <div class="task_center_left">
        <div style="width: 770px;height: 60px;" id="configuration_img">
            <a href="javaScript:previousPage()"><img src="/img/te_first.jpg" style="margin-left: 100px;float: left"></a>
            <a href="javaScript:nextPage()"><img src="/img/te_last.png" style="float: right;margin-right: 50px;"></a>
        </div>
        <div class="task_center_left_I" id="showPdf">
            <a id="viewerPlaceHolder"  class="media" style="width:790px;height:1000px;display:block" href="${pdfPath}"></a>
        </div>
    </div>
    <form id="answerForm" action="/exercise/commitPaper.do" method="post">
    <div class="task_center_left_right">
        <div class="task_center_left_right_I">
            <div class="task_center_left_right_I_I">
                答题卡
            </div>
            <div class="task_center_left_right_I_II">
                <div class="task_center_left_right_I_III">
                    <c:forEach var="number" begin="1" end="${fn:length(itemList)}">
                        <span class="task_center_left_right_I_II_I" onclick="showDtk(${number})" id="BigNum${number}">${number}</span>
                    </c:forEach>
                </div>
            </div>
            <div class="task_center_left_right_III">
                <input type="hidden" name="wordExerciseId" value="${id}" >
                 <input type="hidden" name="lesson" id="lesson" value="${lesson}" >
                <input type="hidden" name="homework" id="homework" value="${homework}">

                <c:forEach items="${itemList}" var="quest" varStatus="i">
                        <div id="dtk${i.index+1}" class="dtk">
                            <div class="task_center_left_right_II_I">
                                        <span>本大题剩余时间</span>
                                        <span id="question${i.index+1}" style="display:none">
                                           
                                        </span>
                             </div>
                            <c:forEach items="${quest.list}" varStatus="k" var="qu">
                                        <div class="answer_center_left_right_II">

                                            <div class="answer_center_left_right_II_I">
                                                <span tihao-id="${qu.id}">${quest.titleId}-${qu.titleId}</span>
                                                <span tixing-id="${qu.itemType}">
                                                    <c:choose>
                                                        <c:when test="${qu.itemType==1}">选择题</c:when>
                                                        <c:when test="${qu.itemType==3}">判断题</c:when>
                                                        <c:when test="${qu.itemType==4}">填空题</c:when>
                                                        <c:when test="${qu.itemType==5}">主观题</c:when>
                                                    </c:choose>
                                                </span>
                                                <span>${qu.score}分</span>
                                            </div>

                                                <div class="answer_center_left_right_II_II">
                                                    <span style="display: block;">答案</span>
                                                    <c:if test="${qu.itemType==1}">

                                                    <div class="answer_center_left_right_II_III">
                                                        <c:forEach var="option" items="${qu.option}">
                                                            <input class="anser" type="checkbox"  value="${option}"
                                                                   <c:if test="${fn:contains(qu.userAnswer,option)}">checked</c:if>>
                                                            <label>${option}</label>
                                                        </c:forEach>
                                                    </div>


                                                    </c:if>
                                                    <c:if test="${qu.itemType==3}">
                                                        <input type="radio" value="1" name="panduan${qu.id}" class="anser" <c:if test="${fn:contains(qu.userAnswer,'1')}">checked</c:if>>对
                                                        <input type="radio" value="0" name="panduan${qu.id}" class="anser" <c:if test="${fn:contains(qu.userAnswer,'0')}">checked</c:if>>错
                                                    </c:if>

                                                    <c:if test="${qu.itemType==4}">
                                                        <input type="text" class="anser_1" value="${qu.userAnswer}" />
                                                    </c:if>

                                                    <c:if test="${qu.itemType==5}">
                                                        <textarea rows="" cols="" class="anser_1" style="height: 150px" >${qu.userAnswer}</textarea>
                                                        <%--=====================================================================================================图片上传--%>
                                                        <div class="task_center_left_right_IIII" style="position: relative;right: 0px;margin-top: auto" >

                                                            <label id="upload-blog-img" onclick="uploadPic('${qu.id}','${param.id}')" for="file${qu.id}" style="cursor: pointer; float: right; margin-right: 5px;">
                                                                <img src="/img/tp_tool_img.png" alt="" title="上传图片" style="display: inline; vertical-align: top; height: 18px; line-height: 18px;"/>
                                                                <span >添加图片</span>
                                                            </label>
                                                            <span id="${qu.id}" onclick="pain('${qu.id}', '${param.id}')" wordExerciseId="${id}" questId="${qu.id}" docId="${param.id}" style="z-index: 10000;">涂鸦答题</span>
                                                            <div style="width: 0;height: 0;">
                                                                <input  name="file${qu.id}" type="file"  style="display: none" id="file${qu.id}" accept="image/*" multiple="multiple"/>
                                                            </div>
                                                        </div>
                                                        <div  id="showPic${qu.id}" class="task_center_left_right_V">
                                                            <c:forEach var="img" items="${qu.imageList}">
                                                                <div class="task_center_left_right_V_II" id="pic${img.id}">
                                                                    <img class="task_center_left_right_V_I" src="http://7xiclj.com1.z0.glb.clouddn.com/${img.value}">
                                                                    <span class="task_center_left_right_V_III" onclick="removePic('${img.id}','${qu.id}',this)" style="position: absolute;top: 0px;background-color: #464646;width: 10px;height: 20px;display: inline-block;left: 70px;color: white;">x</span></div>
                                                            </c:forEach>
                                                        </div>
                                                    </c:if>
                                                </div>
                                        </div>
                                    </c:forEach>
                        </div>
                </c:forEach>
            </div>

        </div>
    </div>
    </form>
</div>
<div id="check-hw-container"  style="margin: auto;position: absolute"></div>
</body>

<script type="text/javascript">
   var dto={docId:"${id}",answerList:[]};
   var tty = ${param.tty};
   if(tty == 2){
       $('#timmer').html('10000:00');//节点作业，保证单次时间不会到
   }


    var bigQuestNum=0;
	$(function(){
		var k=setInterval(checkTime,1000);
		function checkTime(){
			//每个大题
			//试卷
			var examTimer = $('#timmer').html();
			if(examTimer == '0:00'){
				
				clearInterval(k);
				$('span[id^="question"]').each(function(){
					$obj = $(this);
					//如果未提交
					if($obj.prop('className').indexOf('commited') < 0){
						commitAnswer($obj);
					}
				});
				
                $.ajax({
    				url:'/exam/student/answer/submit.do?',
    				data: JSON.stringify(dto) ,
    				type:'post',
    				contentType: 'application/json',
                    success:function(data){
                        {
                        	var lesson=jQuery("#lesson").val();
                            var homework=jQuery("#homework").val();
                            if(homework) {
                                window.location.href = "/homework/student/detail.do?lessonId=" + lesson;
                            } else {
                                if (lesson) {
                                    window.location.href = "/lesson/view.do?lessonId=" + lesson;
                                } else {
                                    window.location.href = "/exam/student/index.do";
                                }
                            }
                           
                        }
    				}
    			});
                
			}
		};

	});

   //提交答题卡
   function commitAnswer($obj){
       var answers = '[';
       $obj.closest('.dtk').find('span[tihao-id]').each(function(){
           var answer ='';
           $this = $(this);
           //判断题型
           var tixing = $this.next('span').attr('tixing-id');
//				var $div = $this.next('span').next('div');
           var $div = $this.parent().next('div');
           if(tixing == 1){//选择题

               var answer="";
               $(':checked',$div).each(function(){
                   answer+=$(this).val()+",";
               });


               //answer = $(':checked',$div).val() != null ? $(':checked',$div).val(): null;
           }else if(tixing == 3){//判断题
               answer = $(':checked',$div).val() != null ? $(':checked',$div).val(): null;
           }else if(tixing == 4){//填空题
               answer = $(':text',$div).val() != null ? $(':text',$div).val(): null;
           }else if(tixing == 5){//主观题
               answer = $('textarea',$div).val() != null ? $('textarea',$div).val(): null;
           }
           answers += '{"questid":'+ $this.attr('tihao-id') +',"questtype":'+ tixing+',"answer":"'+ answer +'","wordexerciseid":${id},';
           answers+='"userid":${ui}},';

           var ans={titleId:$this.attr('tihao-id'),answer:answer};
           dto.answerList.push(ans);
       });
       answers=answers.substring(0,answers.length-1);
       answers += ']';
       var lessonId=window.location.href.split('=')[1];
   }




    var timeSlice=new Array(10);
    var isAutoCommit=true;;
    function uploadPic(questId, docId){
        $('#file'+questId).fileupload({
            url: '/exam/item/image/upload.do?titleId='+questId+'&docId='+docId,
            paramName: 'file',
            done: function (e, response) {
                var obj=response.result;
                
                if(obj.code=='200')
                	{
		                var   picDiv=' <div class="task_center_left_right_V_II"   id="pic'+obj.message.idStr+'" >'+
		                        '<img class="task_center_left_right_V_I" src="'+obj.message.value+'">'+
		                        '<span class="task_center_left_right_V_III" onclick="removePic(\''+obj.message.idStr+'\',\''+questId+'\',this)"  ' +
		                        'style="position: absolute;top: 0px;background-color: #464646;' +
		                        'width: 10px;height: 20px;display: inline-block;left: 70px;color: white;">x</span></div>';
		                $("#showPic"+questId).append(picDiv);
                	}
            },
            progressall:function(e, data){
            }
        });
    }
    function remainTime(timeId){
        var time= $.trim($("#"+timeId).text());
        var k= time.split(":");
        var minute=k[0];
        var second=k[1];
        if(minute==0 && second==0){
            if(isAutoCommit){
                alert("答题时间已到");
                answerForm();
            }
            return;
        }
        if(second==0){
            minute--;
            second=60;
        }
        second--;
        $("#"+timeId).text(minute+':'+second);
        var s=setTimeout("remainTime('"+timeId+"')",1000);
        if(timeId!="timmer"){
            timeSlice.push(s);
        }
    }
    remainTime("timmer");

    function removePic(id,titid,node){
        $.ajax({
            url: "/exam/item/image/remove.do",
            type: "post",
            dataType: "json",
            data: {
            	titleId:titid,
            	imageId:id
            },
            success: function (data) {
                if(data)
                $("#pic"+id).remove();
            }
        });
    }
    function answerForm(){
        var text=$("#answer").val();
        text= $.trim(text);
        var b=confirm("确定提交试卷吗？");
        if(b){
//            temp();
            $('#timmer').text('0:00');
            isAutoCommit=false;
        }


    }
   function answerFormTemp(){
       var b=confirm("确定保存试卷吗？");
       if(b){
           temp();
       }

   }

   function temp(){
       $('span[id^="question"]').each(function(){
           commitAnswer($(this));
       });
       $.ajax({
           url:'/exam/student/answer/temp.do?',
           data: JSON.stringify(dto) ,
           type:'post',
           async: false,
           contentType: 'application/json',
           success:function(data){
               {
                   dto.answerList = [];
                    alert('操作成功');
               }
           }
       });
   }
    function showDtk(k){
        //先暂停数组中的所有计时器  再启动指定计时器
        for(var x =0;x<timeSlice.length;x++){
            if(timeSlice[x]!=null ){
                clearTimeout(timeSlice[x]);
            }
        }
        for(var i=1;i<=${fn:length(questList)};i++){
            if(i==k){
                $("#dtk"+i).show();
                $("#BigNum"+i).attr("class","task_center_left_right_I_II_I active");
                remainTime("question"+k);
            }else {
                $("#dtk"+i).hide();
                $("#BigNum"+i).attr("class","task_center_left_right_I_II_I");
            }
        }
    }
    showDtk(1);

var $ele;
//涂鸦答题相关js
    function pain(questId, docId) {
        $ele = $('#' + questId);
//        var wordExerciseId = $("#flashdemo").attr("wordExerciseId");
        var uploadUrl = "/exam/item/scrawl/upload.do?titleId="+questId+","+docId;
        checkPrintPos();
        $('#check-hw-container').show();
        var picUrl = "/img/exercise/white.JPG";
        var so = new FlashObject("/static/js/swfobject/pictureEditor.swf", "flashApp", "1000px", "700px", "8");
        so.addVariable("picUrl", picUrl);
        so.addVariable("uploadUrl", uploadUrl);
        so.addParam("wmode", "transparent");
        so.write("check-hw-container");
        $('#check-hw-container').append('<div class="close-check-hw" onclick="closePain()" style="position:absolute;color:white;top: 20px;right: 65px;"></div>');
    }
    function closePain() {
        $('#check-hw-container').empty().hide();
    }
    function checkPrintPos() {
        var whigh = document.documentElement.clientHeight;
        var wwidth = document.documentElement.clientWidth;
        if (wwidth > 900) {
            $('#check-hw-container').css({
//                'position':'absolute',
                'top': (whigh - 700) / 2,
                'left': (wwidth - 1000)/2
            });
        }
    }
    //图片上传完成回调函数
    function uploadComplete() {
        alert("图片已保存！")
        $('#check-hw-container').empty().hide();
        $ele.replaceWith('<span  style="z-index: 10000;color: red;font-size: small">手写答案已提交!</span>');
//        window.location.reload();
    }


</script>



</html>
