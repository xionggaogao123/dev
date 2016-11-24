<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="for" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>

<html>
<head>
<title>配置试题--复兰科技</title>
<meta charset="utf-8">
<link rel="stylesheet" href="/static/css/dialog.css" type="text/css"/>

    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static/plugins/flexpaper/flexpaper_flash.js"></script>
    <script type="text/javascript" src="/static/plugins/flexpaper/flexpaper_flash_debug.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/vendor/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/friendcircle/0.1.0/rome.js"></script>
    <link rel="stylesheet" type="text/css" href="/static_new/css/friendcircle/rome.css?v=1"/>

<style type="text/css" media="screen">
    html, body  { height:100%; }
    body { margin:0; padding:0; overflow:auto; }
    #flashContent { display:none; }
</style>

<link rel="stylesheet" href="/static/css/exercise/teacher_configuration.css" type="text/css">


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
    function previewPdf(){
        PDFJS.workerSrc = '/static/js/exercise/plugins/worker_loader.js';
        var can=' <canvas id="the-canvas" />';
        $("#showPdf").empty();
        $("#showPdf").append(can);
        'use strict';
        PDFJS.getDocument('${pdfPath}').then(function(pdf) {
            pdf.getPage(1).then(function(page) {
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
        if (system.win || system.mac || system.xll) {
        	var swfExists=jQuery("#swfExists").val();
        	if(swfExists==1)
        	{
        		 jQuery("#configuration_img").hide();
                 previewSwf();
        	}
        	else
        	{
        		 previewPdf();
        	}
           
        } else {
            previewPdf();
        }
        var tty = ${tty};
        if(tty == 2){
            var date1 = '${date}';
            if(date1 == '1970-01-01'){
                rome(date, {initialValue: nextWeekDate(), time: false , dateValidator: rome.val.afterEq(dateInString()) });
            } else {
                rome(date, {initialValue: date1, time: false, dateValidator: rome.val.afterEq(dateInString()) });
            }

        }
        function nextWeekDate(){
            var date = new Date();
            date.setDate(date.getDate() + 7);
            var string = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
            return string;
        }
        function dateInString(){
            var date = new Date();
            var string = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
            return string;
        }
    });
</script>
<%--==============================================--%>




<script type="text/javascript">
$(function(){
    //注册text/template替换函数
    if (!String.prototype.format) {
        String.prototype.format = function() {
            var args = arguments;
            return this.replace(/{(\d+)}/g, function(match, number) {
                return typeof args[number] != 'undefined'
                        ? args[number]
                        : match
                        ;
            });
        };
    }

    //显示当前页面总分
    changeCurrentTotalScoreDisplay(countCurrentTotalScore());

    //初次载入页面时,选择第一页的题目进行显示
    $(".configuration_big_quest_detail").each(function(i){
        if(i == 0){
            $(this).show();
        }else{
            $(this).hide();
        }
    });

    //为每个大题题号绑定点击事件
    $(".configuration_main_right_VV_II").each(function(i){
        $(this).bind('click',function(){
            var bigQuestNum = $(this).attr("bigQuestNum");
            $(this).attr("class","configuration_main_right_VV_II active");
            $(this).siblings(".configuration_main_right_VV_II").attr("class","configuration_main_right_VV_II");
            $(".configuration_big_quest_detail").each(function(i){
                if(i == (bigQuestNum-1)){
                    $(this).show();
                }else{
                    $(this).hide();
                }
            });
        });
    });

    $(".configuration_main_right_VV_II").first().click();
    //为加做题时间,绑定事件.
    $("[type='time_plus']").click(function(){
        var $bigQuest_answerTime = $(this).prev(".configuration_main_right_VVV_III");
        var n = $bigQuest_answerTime.val();
        var num = parseInt(n) + 1;
        if(isNaN(num)){
            $bigQuest_answerTime.val(1);
        }else{
            $bigQuest_answerTime.val(num);
        }
    });

    //为减做题时间,绑定事件
    $("[type='time_minus']").click(function(){
        var $bigQuest_answerTime = $(this).prevAll(".configuration_main_right_VVV_III");
        var n = $bigQuest_answerTime.val();
        var num = parseInt(n) - 1;
        if(isNaN(num)){
            $bigQuest_answerTime.val('不限');
        }else if(num == 0){
            $bigQuest_answerTime.val("不限");
        }else{
            $bigQuest_answerTime.val(num);
        }
    });

    //为每一个向下的按钮绑定移动的事件
    $("body").on("click",".configuration_main_right_VVVV_VV",function(){
        var $beforeDiv = $(this).closest("li").parent("div");
        if($beforeDiv.next("div").length != 0){
            var $nextDiv = $beforeDiv.next();
            toggleQuestDiv($beforeDiv,$nextDiv);
            freshUpDownStatus();
        }else{
            return false;
        }
    });

    //为每一个向上的按钮绑定事件
    $("body").on("click",".configuration_main_right_VVVV_VIII",function(){
        var $nextDiv = $(this).closest("li").parent("div");
        if($nextDiv.prev("div").length != 0){
            var $beforeDiv = $nextDiv.prev();
            toggleQuestDiv($nextDiv,$beforeDiv);
            freshUpDownStatus();
        }else{
            return false;
        }
    });

    //为每个删除按钮绑定删除事件
    $(".configuration_main_right_VVVV_VVI").click(function(){
        var $delQuestDiv = $(this).closest("li").parent("div");
        var $delBigQuestDetail = $delQuestDiv.closest(".configuration_big_quest_detail");

        //如果此大题的小题数大于一
        if($delBigQuestDetail.children(".questDetail").length != 1){
            while($delQuestDiv.next().length != 0){
                var $nextQuestDiv = $delQuestDiv.next();
                toggleQuestDiv($delQuestDiv,$nextQuestDiv);
            }
           /* if($delQuestDiv.attr("questid") != ""){
                var questid = $delQuestDiv.attr("questid");

                $.ajax({
                    url: '/exercise/paperQuestDelId.do',
                    type: 'post',
                    data: {"questid" : questid},
                    success: function (result) {
                        alert("成功");
                    },
                    error: function(response) {
                        alert("失败");
                    }
                });
            }*/
            $delQuestDiv.remove();
            $delQuestDiv = null;
            freshUpDownStatus();
            //当此大题只剩下一道小题的时候
        }else{
            //得到要删去的大题的数字标签
            var delBigQuestNum = parseInt($delBigQuestDetail.attr("bigquestnum"));
            var $delBigQuestSpan = $(".configuration_main_right_VV_II[bigquestnum='"+delBigQuestNum+"']");


            //当此大题后面还有其他大题时
            if($delBigQuestDetail.nextAll().length != 0){
                //准备变量，记录需要被点击的选项
                var clickone;

                //遍历后面的每一道大题
                $delBigQuestDetail.nextAll().each(function(i){
                    var $next_big_quest_detail = $(this);
                    var old_bigquestnum = parseInt($next_big_quest_detail.attr("bigquestnum"));
                    var new_bigquestnum = old_bigquestnum - 1;

                    //修改下一道大题的bigquestnum
                    $next_big_quest_detail.attr("bigquestnum", new_bigquestnum);
                    //修改每道大题下面的小题目的bigquestnum和题号
                    $next_big_quest_detail.children(".questDetail").each(function(i){
                        var $quest_detail = $(this);
                        //questnum题号不用改变
                        var questnum = parseInt($quest_detail.attr("questnum"));
                        $quest_detail.attr("bigquestnum", new_bigquestnum);
                        //改变显示的题号
                        $changeDisplayQuestNum($quest_detail,new_bigquestnum,questnum);
                    })

                    //对大题小标签，进行修改
                    var $bigquestclickspan = $(".configuration_main_right_VV_II[bigquestnum='"+old_bigquestnum+"']")
                    $bigquestclickspan.attr("bigquestnum", new_bigquestnum);
                    $bigquestclickspan.html(new_bigquestnum);

                    //第一个后面的兄弟被顶上来点击
                    if(i == 0){
                        clickone = $bigquestclickspan;
                    }
                });

                //把这道题目的大题按钮删去
                $delBigQuestSpan.remove();
                $delBigQuestSpan = null;
                //把这道大题的题标和详情删除
                $delBigQuestDetail.remove();
                $delBigQuestDetail = null;
                freshUpDownStatus();
                clickone.click();

            }else{
                //当此大题后面没有其他大题时,可能，1.它的前面有大题，2.它是最后一道大题
                if($delBigQuestDetail.prev(".configuration_big_quest_detail").length != 0 ){
                    $delBigQuestSpan.prev(".configuration_main_right_VV_II").click();
                    //把这道题目的大题按钮删去
                    $delBigQuestSpan.remove();
                    $delBigQuestSpan = null;
                    //把这道大题的题标和详情删除
                    $delBigQuestDetail.remove();
                    $delBigQuestDetail = null;
                    freshUpDownStatus();
                }else{
                    alert("仅剩一道题目，无法再被删除！")
                }
            }
        }

        //显示当前页面总分
        changeCurrentTotalScoreDisplay(countCurrentTotalScore());
    });


    //为每个选择选项添加选择事件
    $("[type='choose']").click(function(){
        var $chooseli =  $(this).closest("li");
        //将原题型相关的各类选项删除
        $chooseli.nextAll("li").each(function(i){
            var $linext = $(this);
            $linext.remove();
            $linext = null;
        });

        var choose_li_html = $("#choose_li").html();
        //填充新的各类选项
        $chooseli.after(choose_li_html);
        $(this).siblings("span").attr("class","configuration_main_right_VVVVV_III");
        $(this).attr("class","configuration_main_right_VVVVV_III_active");

        //显示当前页面总分
        changeCurrentTotalScoreDisplay(countCurrentTotalScore());

        //为覆盖的input对象添加事件
        $("[usage='score']").change(function(){
            //显示当前页面总分
            changeCurrentTotalScoreDisplay(countCurrentTotalScore());
        });
    });

    //为每个判断题选项添加选择事件
    $("[type='judge']").click(function(){
        var $judgeli =  $(this).closest("li");
        //将原题型相关的各类选项删除
        $judgeli.nextAll("li").each(function(i){
            var $linext = $(this);
            $linext.remove();
            $linext = null;
        });

        var judge_li_html = $("#judge_li").html();
        //填充新的各类选项
        $judgeli.after(judge_li_html);
        $(this).siblings("span").attr("class","configuration_main_right_VVVVV_III");
        $(this).attr("class","configuration_main_right_VVVVV_III_active");


        $(this).attr("class","configuration_main_right_VVVVV_III_active");
        var $quest = $judgeli.closest(".questDetail");
        var questnum = $quest.attr("questnum");
        var bigquestnum = $quest.attr("bigquestnum");

        $quest.find(".configuration_main_right_VVVV_IIIII").attr("name",bigquestnum + "-" + questnum);

        //显示当前页面总分
        changeCurrentTotalScoreDisplay(countCurrentTotalScore());

        //为覆盖的input对象添加事件
        $("[usage='score']").change(function(){
            //显示当前页面总分
            changeCurrentTotalScoreDisplay(countCurrentTotalScore());
        });
    });

    //为每个填空题选项添加点击事件
    $("[type='fill']").click(function(){
        var $fillli =  $(this).closest("li");
        //将原题型相关的各类选项删除
        $fillli.nextAll("li").each(function(i){
            var $linext = $(this);
            $linext.remove();
            $linext = null;
        });

        var fill_li_html = $("#fill_li").html();
        //填充新的各类选项
        $fillli.after(fill_li_html);
        $(this).siblings("span").attr("class","configuration_main_right_VVVVV_III")
        $(this).attr("class","configuration_main_right_VVVVV_III_active");

        //显示当前页面总分
        changeCurrentTotalScoreDisplay(countCurrentTotalScore());

        //为覆盖的input对象添加事件
        $("[usage='score']").change(function(){
            //显示当前页面总分
            changeCurrentTotalScoreDisplay(countCurrentTotalScore());
        });
    });


    //为每个主观题选项添加点击事件
    $("[type='subjective']").click(function(){
        var $subjectiveli =  $(this).closest("li");
        //将原题型相关的各类选项删除
        $subjectiveli.nextAll("li").each(function(i){
            var $linext = $(this);
            $linext.remove();
            $linext = null;
        });

        var subjective_li_html = $("#subjective_li").html();
        //填充新的各类选项
        $subjectiveli.after(subjective_li_html);
        $(this).siblings("span").attr("class","configuration_main_right_VVVVV_III")
        $(this).attr("class","configuration_main_right_VVVVV_III_active");

        //显示当前页面总分
        changeCurrentTotalScoreDisplay(countCurrentTotalScore());

        //为覆盖的input对象添加事件
        $("[usage='score']").change(function(){
            //显示当前页面总分
            changeCurrentTotalScoreDisplay(countCurrentTotalScore());
        });
    });


    //为添加大题按钮添加点击事件
    $("#addBigQ").click(function(){
        //添加大题的题标
        var nextBigQuestNo = $("[type='bigQuestNo']").length + 1;
        var lastBigQuestNo = nextBigQuestNo - 2;
        var lastBigQuestNoSpan = $("span[type='bigQuestNo']")[lastBigQuestNo];
        var html = '<span class="configuration_main_right_VV_II" bigQuestNum="'+nextBigQuestNo+'" type="bigQuestNo">'+nextBigQuestNo+'</span>';
        $(lastBigQuestNoSpan).after(html);

        //添加大题的答题分钟数和默认的一道主观题
        //得到要填充的html
        var addBigQuestionTemp = $("#addBigQuestionTemp").html();
        var addBigQuestionHtml = addBigQuestionTemp.format(nextBigQuestNo);
        var lastBigQuestDetail = $("div .configuration_big_quest_detail")[$("div .configuration_big_quest_detail").length - 1];
        $(lastBigQuestDetail).after(addBigQuestionHtml);

        //将所有事件注册一遍
        submitAgain();
        freshUpDownStatus();
        $(".configuration_main_right_VV_II[bigQuestNum='"+nextBigQuestNo+"']").click();
    });

    //为添加小题绑定事件
    $("[usage='addQuest']").click(function(){
        //添加的小题目与当前显示的最后一道小题的题型要相同,那么克隆当前显示的最后一道小题
        //选定要克隆的最后一小题
        var $quests = $(".configuration_big_quest_detail:visible").find(".questDetail");
        var $quest2BeCloned = $($quests[$quests.length - 1]);
        var $quest2BeInserted = $quest2BeCloned.clone(true,true);
        var bigquestnum = parseInt($quest2BeCloned.attr("bigquestnum"));
        var questid = parseInt($quest2BeCloned.attr("questid"));
        var questnum = parseInt($quest2BeCloned.attr("questnum"));

        $quest2BeInserted.attr("questid","");
        $quest2BeInserted.attr("questnum",questnum + 1);
        $quest2BeInserted.find(".configuration_main_right_VVVV_III").html(bigquestnum + "-" + (questnum + 1));
        $quest2BeInserted.find(".configuration_main_right_VVVVV_III_active").click();

        $quest2BeCloned.after($quest2BeInserted);

        //为覆盖的input对象添加事件
        $("[usage='score']").change(function(){
            //显示当前页面总分
            changeCurrentTotalScoreDisplay(countCurrentTotalScore());
        });
        freshUpDownStatus();
    });

    //为添加选项绑定事件
    $("body").on("click","[usage='option_plus']",function(){
        var $lastOption = $(this).parent().prev().children("input").last();
        var lastOptionString = $lastOption.attr("option");
        var code = lastOptionString.charCodeAt(0);
        var newOptionString = String.fromCharCode(code + 1);

        var html = '<input class="configuration_main_right_VVVV_IIIII" type="checkbox" option="'+newOptionString+'" value="'+newOptionString+'"><lable>'+ newOptionString + '</lable>';

        $(this).parent().prev().append(html);


    });

    //为减去选项绑定事件
    $("body").on('click',"[usage='option_minus']",function(){
        var $lastOption = $(this).parent().prev().children("input").last();
        var $lastOptionLabel = $lastOption.next();

        if($(this).parent().prev().children("input").length != 1){
            $lastOption.remove();
            $lastOption = null;
            $lastOptionLabel.remove();
            $lastOptionLabel = null;
        }
    });

    //添加分值改变随之当前页面总分发生改变的事件
    $("[usage='score']").change(function(){
        //显示当前页面总分
        changeCurrentTotalScoreDisplay(countCurrentTotalScore());
    });

    //更改试卷的文件
    $('#word-upload').fileupload({
        url: '/exam/file/replace.do',
        paramName: 'file',
        formData: {wordexerciseId : $("input[name='wordexerciseId']").val(),type:1},
        add:fileTypeCheck,
        start: function(e) {
            MessageBox('试卷上传中...', 0, 'save');
        },
        done: function (e, response) {
        	var lesson=jQuery("#lesson").val();
        	if(lesson)
        	{
        		location.href="/exam/view.do?id="+$("input[name='wordexerciseId']").val()+"&lesson="+jQuery("#lesson").val();
        	}
        	else
        	{
        		location.href="/exam/view/"+$("input[name='wordexerciseId']").val()
        	}
        	
        }
    });

    //上传解析的文件
    $('#answer-word-upload').fileupload({
    	url: '/exam/file/replace.do',
        paramName: 'file',
        formData: {wordexerciseId : $("input[name='wordexerciseId']").val(),type:2},
        add:fileTypeCheck,
        start: function(e) {
            MessageBox('解析上传中...', 0, 'save');
        },
        done: function (e, response) {
            //成功后把上传文件改为查看文件
            var span = $("#answer-upload");
            span.remove();
            span = null;

            var html = '<span class="configuration_top_main_VII" onclick="checkAnswerWord()">查看解析</span>';
            $("#answer-word-upload").after(html);
            var prompt = $("#prompt-div");
            prompt.remove();
            prompt = null;

            $("#teacher_popup").show();
        }
    });



});

//检查格式
function fileTypeCheck(e, data) {
    var goUpload = true;
    var uploadFile = data.files[0];
    if (!(/\.(doc|docx)$/i).test(uploadFile.name)) {
        MessageBox('请选择doc或docx文件。', -1);
        goUpload = false;
    }
    if (goUpload == true) {
        data.submit();
    }
}

//把所有按钮的事件再注册一遍
function submitAgain(){
    //为每个大题题号绑定点击事件
    $(".configuration_main_right_VV_II").each(function(i){
        $(this).bind('click',function(){
            var bigQuestNum = $(this).attr("bigQuestNum");
            $(this).attr("class","configuration_main_right_VV_II active");
            $(this).siblings(".configuration_main_right_VV_II").attr("class","configuration_main_right_VV_II");
            $(".configuration_big_quest_detail").each(function(i){
                if(i == (bigQuestNum-1)){
                    $(this).show();
                }else{
                    $(this).hide();
                }
            });
        });
    });

    //为加做题时间,绑定事件.
    $("[type='time_plus']").click(function(){
        var $bigQuest_answerTime = $(this).prev(".configuration_main_right_VVV_III");
        var n = $bigQuest_answerTime.val();
        var num = parseInt(n) + 1;
        if(isNaN(num)){
            $bigQuest_answerTime.val(1);
        }else{
            $bigQuest_answerTime.val(num);
        }
    });

    //为减做题时间,绑定事件
    $("[type='time_minus']").click(function(){
        var $bigQuest_answerTime = $(this).prevAll(".configuration_main_right_VVV_III");
        var n = $bigQuest_answerTime.val();
        var num = parseInt(n) - 1;
        if(isNaN(num)){
            $bigQuest_answerTime.val('不限');
        }else if(num == 0){
            $bigQuest_answerTime.val("不限");
        }else{
            $bigQuest_answerTime.val(num);
        }
    });

    //为每一个向下的按钮绑定移动的事件
   /* $(".configuration_main_right_VVVV_VV").click(function(){
        var $beforeDiv = $(this).closest("li").parent("div");
        if($beforeDiv.next("div").length != 0){
            var $nextDiv = $beforeDiv.next();
            toggleQuestDiv($beforeDiv,$nextDiv);
            freshUpDownStatus();
        }else{
            return false;
        }
    });*/

    //为每一个向上的按钮绑定事件
    /*$(".configuration_main_right_VVVV_VIII").click(function(){
        var $nextDiv = $(this).closest("li").parent("div");
        if($nextDiv.prev("div").length != 0){
            var $beforeDiv = $nextDiv.prev();
            toggleQuestDiv($nextDiv,$beforeDiv);
            freshUpDownStatus();
        }else{
            return false;
        }
    });*/

    //为每个删除按钮绑定删除时间
    $(".configuration_main_right_VVVV_VVI").click(function(){
        var $delQuestDiv = $(this).closest("li").parent("div");
        var $delBigQuestDetail = $delQuestDiv.closest(".configuration_big_quest_detail");

        //如果此大题的小题数大于一
        if($delBigQuestDetail.children(".questDetail").length != 1){
            while($delQuestDiv.next().length != 0){
                var $nextQuestDiv = $delQuestDiv.next();
                toggleQuestDiv($delQuestDiv,$nextQuestDiv);
            }
            $delQuestDiv.remove();
            $delQuestDiv = null;
            freshUpDownStatus();
            //当此大题只剩下一道小题的时候
        }else{
            //得到要删去的大题的数字标签
            var delBigQuestNum = parseInt($delBigQuestDetail.attr("bigquestnum"));
            var $delBigQuestSpan = $(".configuration_main_right_VV_II[bigquestnum='"+delBigQuestNum+"']");


            //当此大题后面还有其他大题时
            if($delBigQuestDetail.nextAll().length != 0){
                //准备变量，记录需要被点击的选项
                var clickone;

                //遍历后面的每一道大题
                $delBigQuestDetail.nextAll().each(function(i){
                    var $next_big_quest_detail = $(this);
                    var old_bigquestnum = parseInt($next_big_quest_detail.attr("bigquestnum"));
                    var new_bigquestnum = old_bigquestnum - 1;

                    //修改下一道大题的bigquestnum
                    $next_big_quest_detail.attr("bigquestnum", new_bigquestnum);
                    //修改每道大题下面的小题目的bigquestnum和题号
                    $next_big_quest_detail.children(".questDetail").each(function(i){
                        var $quest_detail = $(this);
                        //questnum题号不用改变
                        var questnum = parseInt($quest_detail.attr("questnum"));
                        $quest_detail.attr("bigquestnum", new_bigquestnum);
                        //改变显示的题号
                        $changeDisplayQuestNum($quest_detail,new_bigquestnum,questnum);
                    })

                    //对大题小标签，进行修改
                    var $bigquestclickspan = $(".configuration_main_right_VV_II[bigquestnum='"+old_bigquestnum+"']");
                    $bigquestclickspan.attr("bigquestnum", new_bigquestnum);
                    $bigquestclickspan.html(new_bigquestnum);

                    //第一个后面的兄弟被顶上来点击
                    if(i == 0){
                        clickone = $bigquestclickspan;
                    }
                });

                //把这道题目的大题按钮删去
                $delBigQuestSpan.remove();
                $delBigQuestSpan = null;
                //把这道大题的题标和详情删除
                $delBigQuestDetail.remove();
                $delBigQuestDetail = null;
                freshUpDownStatus();
                clickone.click();

            }else{
                //当此大题后面没有其他大题时,可能，1.它的前面有大题，2.它是最后一道大题
                if($delBigQuestDetail.prev(".configuration_big_quest_detail").length != 0 ){
                    $delBigQuestSpan.prev(".configuration_main_right_VV_II").click();
                    //把这道题目的大题按钮删去
                    $delBigQuestSpan.remove();
                    $delBigQuestSpan = null;
                    //把这道大题的题标和详情删除
                    $delBigQuestDetail.remove();
                    $delBigQuestDetail = null;
                }else{
                    alert("仅剩一道题目，无法再被删除！")
                }
            }
        }
    });


    //为每个选择选项添加选择事件
    $("[type='choose']").click(function(){
        var $chooseli =  $(this).closest("li");
        //将原题型相关的各类选项删除
        $chooseli.nextAll("li").each(function(i){
            var $linext = $(this);
            $linext.remove();
            $linext = null;
        });

        var choose_li_html = $("#choose_li").html();
        //填充新的各类选项
        $chooseli.after(choose_li_html);
        $(this).siblings("span").attr("class","configuration_main_right_VVVVV_III")
        $(this).attr("class","configuration_main_right_VVVVV_III_active");

        //为覆盖的input对象添加事件
        $("[usage='score']").change(function(){
            //显示当前页面总分
            changeCurrentTotalScoreDisplay(countCurrentTotalScore());
        });

    });

    //为每个判断题选项添加选择事件
    $("[type='judge']").click(function(){
        var $judgeli =  $(this).closest("li");
        //将原题型相关的各类选项删除
        $judgeli.nextAll("li").each(function(i){
            var $linext = $(this);
            $linext.remove();
            $linext = null;
        });

        var judge_li_html = $("#judge_li").html();
        //填充新的各类选项
        $judgeli.after(judge_li_html);
        $(this).siblings("span").attr("class","configuration_main_right_VVVVV_III")
        $(this).attr("class","configuration_main_right_VVVVV_III_active");
        var $quest = $judgeli.closest(".questDetail");
        var questnum = $quest.attr("questnum");
        var bigquestnum = $quest.attr("bigquestnum");

        $quest.find(".configuration_main_right_VVVV_IIIII").attr("name",bigquestnum + "" + questnum);


        //为覆盖的input对象添加事件
        $("[usage='score']").change(function(){
            //显示当前页面总分
            changeCurrentTotalScoreDisplay(countCurrentTotalScore());
        });

    });

    //为每个填空题选项添加点击事件
    $("[type='fill']").click(function(){
        var $fillli =  $(this).closest("li");
        //将原题型相关的各类选项删除
        $fillli.nextAll("li").each(function(i){
            var $linext = $(this);
            $linext.remove();
            $linext = null;
        });

        var fill_li_html = $("#fill_li").html();
        //填充新的各类选项
        $fillli.after(fill_li_html);
        $(this).siblings("span").attr("class","configuration_main_right_VVVVV_III")
        $(this).attr("class","configuration_main_right_VVVVV_III_active");

        //为覆盖的input对象添加事件
        $("[usage='score']").change(function(){
            //显示当前页面总分
            changeCurrentTotalScoreDisplay(countCurrentTotalScore());
        });

    });


    //为每个主观题选项添加点击事件
    $("[type='subjective']").click(function(){
        var $subjectiveli =  $(this).closest("li");
        //将原题型相关的各类选项删除
        $subjectiveli.nextAll("li").each(function(i){
            var $linext = $(this);
            $linext.remove();
            $linext = null;
        });

        var subjective_li_html = $("#subjective_li").html();
        //填充新的各类选项
        $subjectiveli.after(subjective_li_html);
        $(this).siblings("span").attr("class","configuration_main_right_VVVVV_III")
        $(this).attr("class","configuration_main_right_VVVVV_III_active");

        //为覆盖的input对象添加事件
        $("[usage='score']").change(function(){
            //显示当前页面总分
            changeCurrentTotalScoreDisplay(countCurrentTotalScore());
        });
    });

    //添加分值改变随之当前页面总分发生改变的事件
    $("[usage='score']").change(function(){
        //显示当前页面总分
        changeCurrentTotalScoreDisplay(countCurrentTotalScore());
    });
}


//更换两个questDiv的位置，更换题号
function toggleQuestDiv(a,b){
    var beforeQuestNum = a.attr("questnum");
    var nextQuestNum = b.attr("questnum");

    //交换小题号
    a.attr("questnum", nextQuestNum);
    b.attr("questnum",beforeQuestNum);

    //交换显示的题号
    var bigQuestNum = a.attr("bigquestnum");

    $changeDisplayQuestNum(a,bigQuestNum,nextQuestNum);
    $changeDisplayQuestNum(b,bigQuestNum,beforeQuestNum);

    togglePostion(a,b);
}

//交换两个div的位置，不更换题号
function togglePostion(a,b){
    var a1 = $("<div id='a1'></div>").insertBefore(a);
    var b1 = $("<div id='a1'></div>").insertBefore(b);
    a.insertAfter(b1);
    b.insertAfter(a1);
    a1.remove();
    b1.remove();
    a1 = b1 = null;
}

//对传入的jquery对象的题号进行修改
function $changeDisplayQuestNum($quest, firstNum, nextNum){
    var $displayQuestNum = $quest.find(".configuration_main_right_VVVV_III");
    $displayQuestNum.html(firstNum + '-' + nextNum);
}

//打印jquery对象
function writeObj(obj){
    var description = "";
    for(var i in obj){
        var property=obj[i];
        description+=i+" = "+property+"\n";
    }
    alert(description);
}

//统计当前页面总分的js
function countCurrentTotalScore(){
    var currentTotalScore = 0;
    $("[usage='score']").each(function(i){
        var numPattern = /^\d+(\.\d+)?$/;
        if(numPattern.test($(this).val())){
            currentTotalScore += parseFloat($(this).val());
        }
    });
    return currentTotalScore;
}

//更改当前显示的当前页面总分
function changeCurrentTotalScoreDisplay(currentTotalScore){
    $("[usage='currentTotalScore']").html(currentTotalScore);
}

function completeAndSubmit(){

	var re= /^[+-]?[1-9]?[0-9]*\.[0-9]*$/;
	var re1 = /^[1-9]+[0-9]*]*$/;
	 
    var wordexerciseId = $("input[name='wordexerciseId']").val();
    var bigWordExerciseQuestInfos = [];
    var questErrorInfo = {
        errorInfo : "",
        errorBigquestnum : 0
    }

    if(!confirm('请您注意:发布后,若已有学生答题,则不能再修改配置!')){
        return false;
    }

    $(".configuration_big_quest_detail").each(function(i){

        var bigQuestId = $(this).attr("bigquestionid");
        var bigquestnum = $(this).attr("bigquestnum");
        var answerTime = $(this).find("input[usage='bigQuest_answerTime']").val();



        var wordExerciseQuestList = [];
        $(this).find(".questDetail").each(function(){
            //得到大题的id
            var questid = $(this).attr("questid");
            var questnum = $(this).attr("questnum");
            var questTypeId = 5;
            var questType = $(this).find(".configuration_main_right_VVVVV_III_active").html();
            var questScore = $(this).find("[usage='score']").val();

           
            if(typeof(questScore) == "undefined" || (!re.test(questScore) && !re1.test(questScore)) ){
                $(".configuration_main_right_VV_II[bigquestnum='"+bigquestnum+"']").click();
                questErrorInfo.errorInfo = "本大题中有题目的分值未被设定！";
                return false;
            }

            var optionNum = 0;
            var questAnswer = "";
            if(questType == "选择"){
            	questTypeId=1;
                $(this).find(".configuration_main_right_VVVV_IIIII:checked").each(function(){
                    questAnswer += $(this).val();
                });
                if(questAnswer.replace(/(^s*)|(s*$)/g, "").length ==0){
                    $(".configuration_main_right_VV_II[bigquestnum='"+bigquestnum+"']").click();
                    questErrorInfo.errorInfo = "本大题有选择题小题未设定答案！";
                    return false;
                }
                optionNum = $(this).find(":checkbox").length;
            }else if(questType == "判断"){
            	questTypeId=3;
                questAnswer += $(this).find("input:checked").val();
                if(questAnswer == "undefined"){
                    $(".configuration_main_right_VV_II[bigquestnum='"+bigquestnum+"']").click();
                    questErrorInfo.errorInfo = "本大题有判断题小题未设定答案！";
                    return false;
                }
            }else if(questType == "填空"){
            	questTypeId=4;
                questAnswer += $(this).find("input[usage='fill']").val();
                if(typeof(questAnswer) == "undefined" || (questAnswer.replace(/(^s*)|(s*$)/g, "").length ==0)){
                    $(".configuration_main_right_VV_II[bigquestnum='"+bigquestnum+"']").click();
                    questErrorInfo.errorInfo = "本大题有填空题小题未设定答案！";
                    return false;
                }
            }

            var wordExerciseQuest = {
                    "titleId" : questnum,
                    "itemType" : questTypeId,
                    "score" : parseFloat(questScore),
                    "rightAnswer" : questAnswer,
                    "selectCount" : optionNum
            };
            
            wordExerciseQuestList.push(wordExerciseQuest);
        });

        var timeData=0;
        
        if(re.test(answerTime))
        	{
        	timeData=parseFloat(timeData);
        	}

        var bigWordExerciseQuestInfo = {
        	"docId": wordexerciseId,
            "titleId": bigquestnum,
            "time": timeData,
            "list" : wordExerciseQuestList
        };
        bigWordExerciseQuestInfos.push(bigWordExerciseQuestInfo);
    });

    if(questErrorInfo.errorInfo != ""){
        alert(questErrorInfo.errorInfo);
        return false;
    }
    var tty = ${tty};

    var exerciseTime = $("input[name='exerciseTime']").val();
    var totalScore = $("span[usage='currentTotalScore']").html();
    var re = /^[1-9]+[0-9]*]*$/;
    if(tty !=2 && !re.test(exerciseTime)){
        alert("请输入考试时间！");
        return false;
    }

    
    var bigWordExerciseQuestInfo = {
            "docId": wordexerciseId,
            "titleId" : "",
            "time" : parseInt(exerciseTime),
            "lesson":jQuery("#lesson").val()
        };
    bigWordExerciseQuestInfos.push(bigWordExerciseQuestInfo);

    var date = "";
    if(tty == 2){
        date = $('#date').val();
    }
    var downLoad = $('#dld').is(':checked') ? 1 : 0;
    $.ajax({
        url: '/exam/conf.do?timeType=' + tty + '&date=' + date + '&downLoad=' + downLoad,
        type: 'post',
        data: JSON.stringify(bigWordExerciseQuestInfos) ,
        contentType: 'application/json',
        success: function (result) {
        	var lesson=jQuery("#lesson").val();
            var homework = jQuery("#homework").val();
            if(homework) {
                location.href="/homework/teacher/edit.do?lessonId="+lesson;
            }else if(lesson)
        	{
        		location.href="/lesson/edit.do?lessonId="+lesson;
        	}
        	else
        	{
        		window.location.href = "/exam/index.do";
        	}
        },
        error: function(response) {
            alert("失败");
        }
    });

}

//查看解析的文件
function checkAnswerWord(){
    var wordexerciseId = $("input[name='wordexerciseId']").val();
    window.open('/exam/parse/view/' + wordexerciseId);
}

function freshUpDownStatus(){
    $(".configuration_main_right_VVVV_VIII").each(function(){
        var $quest = $(this).closest(".questDetail");
        //是第一个
        if($quest.prev("div").length == 0){
            $(this).attr("class","configuration_main_right_VVVV_VIII disable");
        }else{
            $(this).attr("class","configuration_main_right_VVVV_VIII");
        }
    })

    $(".configuration_main_right_VVVV_VV").each(function(){
        var $quest = $(this).closest(".questDetail");
        //是第一个
        if($quest.next("div").length == 0){
            $(this).attr("class","configuration_main_right_VVVV_VV disable");
        }else{
            $(this).attr("class","configuration_main_right_VVVV_VV");
        }
    })
}

    function config_back(){
	    var lesson=jQuery("#lesson").val();
        var homework = jQuery("#homework").val();
        if(homework) {
            location.href="/homework/teacher/edit.do?lessonId="+lesson;
        }else if(lesson)
	   	{
	   		location.href="/lesson/edit.do?lessonId="+lesson;
	   	}
	   	else
	   	{
	   		location.href="/exam/index.do";
	   	}
    }

</script>


</head>
<body>
<div class="configuration_top">
    <input type="hidden" id="swfExists" name="swfExists" value="${swfExists}"/>
    <div class="configuration_top_main">
        <span class="configuration_top_main_back" onclick="config_back()">返回</span>

        <c:choose>
            <c:when test="${param.tty == 2}">
               <span class="configuration_top_main_III">
                 设置最后提交日期&nbsp;&nbsp;<input class="configuration_top_main_I" type="text" id="date" readonly>
                </span>
            </c:when>
            <c:otherwise>
                <span class="configuration_top_main_III">
                     设置考试时间&nbsp;&nbsp;<input class="configuration_top_main_I" type="text" name="exerciseTime" value="${time}">&nbsp;&nbsp;分钟
                 </span>
            </c:otherwise>
        </c:choose>
        <span class="configuration_top_main_III">
           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="dld" <c:if test="${dld == 1}">checked="ture" </c:if>> 进阶练习可下载
       </span>
        <div class="configuration_top_main_II">
            <%--<img src="/img/exercise/config_03.png">--%>
            <%--<span class="configuration_top_main_V">如何配置试卷</span>--%>

            <label id="upload-word" for="word-upload" style="cursor:pointer;">
                <span class="configuration_top_main_VI">更换文件</span>
            </label>

            <input type="file" name="word-upload" id="word-upload" accept="application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document" multiple="multiple" style="display: none">

            <c:choose>
                <c:when test="${empty swfPath}">
                    <label id="upload-answer-word" for="answer-word-upload" style="cursor: pointer;">
                        <span id="answer-upload" class="configuration_top_main_VII">上传解析</span>
                    </label>

                    <input type="file" name="answer-word-upload" id="answer-word-upload" accept="application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document" multiple="multiple" style="display: none">
                </c:when>
               
               <c:when test="${!empty ansPath}">
                    <span class="configuration_top_main_VII" onclick="checkAnswerWord()">查看解析</span>
               </c:when>
               
            </c:choose>




            <span class="configuration_top_main_VIII" onclick="completeAndSubmit()">完成并发布</span>
        </div>
    </div>
</div>
<div class="configuration_main">
    <div style="width:770px;height:60px;" id="configuration_img">
        <a href="javaScript:previousPage()"><img src="/img/te_first.jpg" style="margin-left: 100px;"></a>
        <a href="javaScript:nextPage()"><img src="/img/te_last.png" style="float: right;margin-right: 50px;"></a>
    </div>
    
<div class="configuration_main_left" id="showPdf" style="margin-top: 10px;float: left;height: 1020px!important">

    <a id="viewerPlaceHolder" style="width:645px;height:950px;display:inline" class="configuration_main_left_I" href="${pdfPath}"></a>
</div>

<div class="configuration_main_right">

<div class="configuration_main_right_I">

<ul>

<li>
    <div class="configuration_main_right_II">
        <span class="configuration_main_right_III">答题卡</span>
        <span class="configuration_main_right_IIII">当前总分：</span>
        <span class="configuration_main_right_V" usage="currentTotalScore">${totalScore}</span>
        <input type="hidden" value="${id}" name="wordexerciseId"/>
        <input type="hidden" value="${lesson}" name="lesson" id="lesson"/>
        <input type="hidden" value="${homework}" name="homework" id="homework"/>
    </div>
</li>

<li>
    <div class="configuration_main_right_VV_I">
        <div class="configuration_main_right_VV_V">
            <c:forEach var="i" begin="1" end="${fn:length(itemList)}" >
                <span class="configuration_main_right_VV_II" bigQuestNum="${i}" type="bigQuestNo">${i}</span>
            </c:forEach>

        </div>
        <span class="configuration_main_right_VV_III" id="addBigQ">添加大题</span>
    </div>
</li>

<c:forEach var="bigQuest" items="${itemList}" varStatus="loopStatus">

<div class="configuration_big_quest_detail"  bigQuestionId="${bigQuest.docId}" bigQuestNum="${bigQuest.titleId}">
<li>
    <div class="configuration_main_right_VVV_I">
        <div class="configuration_main_right_VVV_V">
            <span class="configuration_main_right_VVV_II">答题分钟（选填）</span>
            <input type="text" value="不限" class="configuration_main_right_VVV_III" usage="bigQuest_answerTime">

            <span class="configuration_main_right_VVV_VI" type="time_plus">+</span>

            <span class="configuration_main_right_VVV_VII" type="time_minus">-</span>
        </div>
    </div>
</li>

<c:forEach var="item" items="${bigQuest.list}" varStatus="status">

<c:choose>

    <c:when test="${item.itemType == 1}" >

        <div bigQuestNum="${bigQuest.titleId}" questId="${item.id}" questNum="${item.titleId}" class="questDetail">
            <li>
                <div class="configuration_main_right_VVVV_I">
                    <div class="configuration_main_right_VVVV_II" >
                        题号
                    </div>
                    <div class="configuration_main_right_VVVV_III">
                            ${bigQuest.titleId}-${item.titleId}
                    </div>
                    <div ${status.first ? 'class="configuration_main_right_VVVV_VIII disable"' : 'class="configuration_main_right_VVVV_VIII"'}></div>
                    <div ${status.last ? 'class="configuration_main_right_VVVV_VV disable"' : 'class="configuration_main_right_VVVV_VV"'}></div>
                    <div class="configuration_main_right_VVVV_VVI"></div>
                </div>
            </li>
            <li>
                <div class="configuration_main_right_VVVVV_I">
                    <div class="configuration_main_right_VVVVV_II">
                        题型
                    </div>
                    <span class="configuration_main_right_VVVVV_III_active" type="choose">选择</span>
                    <span class="configuration_main_right_VVVVV_III" type="judge">判断</span>
                    <span class="configuration_main_right_VVVVV_III" type="fill">填空</span>
                    <span class="configuration_main_right_VVVVV_III" type="subjective">主观</span>
                </div>
            </li>
            <li>
                <div class="configuration_main_right_VVVV_I">
                    <div class="configuration_main_right_VVVV_II">
                        分值
                    </div>
                    <input class="configuration_main_right_VVVV_IIII" type="text" value="${item.score}" usage="score">
                </div>
            </li>
            <li>
                <div class="configuration_main_right_VVVVVV_I">
                    <div class="configuration_main_right_VVVV_II">
                        答案
                    </div>
                    <div class="V">
                        <c:forEach var="option" items="${item.option}">
                            <input class="configuration_main_right_VVVV_IIIII" type="checkbox" option="${option}"  ${fn:contains(item.rightAnswer, option) ? 'checked="checked"':''} value="${option}"><label>${option}</label>
                        </c:forEach>
                    </div>
                    <div class="VV">
                        <div class="configuration_main_right_VVVV_IIIIII" usage="option_plus"> </div>
                        <div class="configuration_main_right_VVVV_VI" usage="option_minus"> </div>
                    </div>
                </div>
            </li>
        </div>

    </c:when>

    <c:when test="${item.itemType == 3}">
        <div bigQuestNum="${bigQuest.titleId}" questId="${item.id}" questNum="${item.titleId}" class="questDetail">
            <li>
                <div class="configuration_main_right_VVVV_I">
                    <div class="configuration_main_right_VVVV_II">
                        题号
                    </div>
                    <div class="configuration_main_right_VVVV_III">
                            ${bigQuest.titleId}-${item.titleId}
                    </div>
                    <div ${status.first ? 'class="configuration_main_right_VVVV_VIII disable"' : 'class="configuration_main_right_VVVV_VIII"'}></div>
                    <div ${status.last ? 'class="configuration_main_right_VVVV_VV disable"' : 'class="configuration_main_right_VVVV_VV"'}></div>
                    <div class="configuration_main_right_VVVV_VVI"></div>
                </div>
            </li>
            <li>
                <div class="configuration_main_right_VVVVV_I">
                    <div class="configuration_main_right_VVVVV_II">
                        题型
                    </div>
                    <span class="configuration_main_right_VVVVV_III" type="choose">选择</span>
                    <span class="configuration_main_right_VVVVV_III_active" type="judge">判断</span>
                    <span class="configuration_main_right_VVVVV_III" type="fill">填空</span>
                    <span class="configuration_main_right_VVVVV_III" type="subjective">主观</span>
                </div>
            </li>
            <li>
                <div class="configuration_main_right_VVVV_I">
                    <div class="configuration_main_right_VVVV_II">
                        分值
                    </div>
                    <input class="configuration_main_right_VVVV_IIII" type="text" value="${item.score}" usage="score">
                </div>
            </li>
            <li>
                <div class="configuration_main_right_VVVVVV_I">
                    <div class="configuration_main_right_VVVV_II">
                        答案
                    </div>
                    <div class="V">
                        <input class="configuration_main_right_VVVV_IIIII" type="radio" name="${bigQuest.titleId}-${item.titleId}" ${fn:contains(item.rightAnswer, '1') ? 'checked="checked"':''} value="1">对
                        <input class="configuration_main_right_VVVV_IIIII" type="radio" name="${bigQuest.titleId}-${item.titleId}"  ${fn:contains(item.rightAnswer, '0') ? 'checked="checked"':''} value="0">错

                    </div>
                </div>

            </li>
        </div>

    </c:when>

    <c:when test="${item.itemType == 4}">

        <div bigQuestNum="${bigQuest.titleId}" questId="${item.id}" questNum="${item.titleId}" class="questDetail">
            <li>
                <div class="configuration_main_right_VVVV_I">
                    <div class="configuration_main_right_VVVV_II">
                        题号
                    </div>
                    <div class="configuration_main_right_VVVV_III">
                            ${bigQuest.titleId}-${item.titleId}
                    </div>
                    <div class="VVVV">
                        <div ${status.first ? 'class="configuration_main_right_VVVV_VIII disable"' : 'class="configuration_main_right_VVVV_VIII"'}></div>
                        <div ${status.last ? 'class="configuration_main_right_VVVV_VV disable"' : 'class="configuration_main_right_VVVV_VV"'}></div>
                        <div class="configuration_main_right_VVVV_VVI"></div>
                    </div>
                </div>
            </li>
            <li>
                <div class="configuration_main_right_VVVVV_I">
                    <div class="configuration_main_right_VVVVV_II">
                        题型
                    </div>
                    <span class="configuration_main_right_VVVVV_III" type="choose">选择</span>
                    <span class="configuration_main_right_VVVVV_III" type="judge">判断</span>
                    <span class="configuration_main_right_VVVVV_III_active" type="fill">填空</span>
                    <span class="configuration_main_right_VVVVV_III" type="subjective">主观</span>
                </div>
            </li>
            <li>
                <div class="configuration_main_right_VVVV_I">
                    <div class="configuration_main_right_VVVV_II">
                        分值
                    </div>
                    <input class="configuration_main_right_VVVV_IIII" type="text" value="${item.score}" usage="score">
                </div>
            </li>
            <li>
                <div class="configuration_main_right_VVVVVV_I">
                    <div class="configuration_main_right_VVVV_II">
                        答案
                    </div>
                    <input class="configuration_main_right_VVVV_IIII" type="text" value="${item.rightAnswer}" usage="fill">
                </div>
            </li>
        </div>

    </c:when>


    <c:when test="${item.itemType == 5}">

        <div bigQuestNum="${bigQuest.titleId}" questId="${item.id}" questNum="${item.titleId}" class="questDetail">
            <li>
                <div class="configuration_main_right_VVVV_I">
                    <div class="configuration_main_right_VVVV_II">
                        题号
                    </div>
                    <div class="configuration_main_right_VVVV_III">
                            ${bigQuest.titleId}-${item.titleId}
                    </div>
                    <div class="VVVV">
                        <div ${status.first ? 'class="configuration_main_right_VVVV_VIII disable"' : 'class="configuration_main_right_VVVV_VIII"'}></div>
                        <div ${status.last ? 'class="configuration_main_right_VVVV_VV disable"' : 'class="configuration_main_right_VVVV_VV"'}></div>
                        <div class="configuration_main_right_VVVV_VVI"></div>
                    </div>
                </div>
            </li>
            <li>
                <div class="configuration_main_right_VVVVV_I">
                    <div class="configuration_main_right_VVVVV_II">
                        题型
                    </div>
                    <span class="configuration_main_right_VVVVV_III" type="choose">选择</span>
                    <span class="configuration_main_right_VVVVV_III" type="judge">判断</span>
                    <span class="configuration_main_right_VVVVV_III" type="fill">填空</span>
                    <span class="configuration_main_right_VVVVV_III_active" type="subjective">主观</span>
                </div>
            </li>
            <li>
                <div class="configuration_main_right_VVVV_I">
                    <div class="configuration_main_right_VVVV_II">
                        分值
                    </div>
                    <input class="configuration_main_right_VVVV_IIII" type="text" value="${item.score}" usage="score">
                </div>
            </li>
        </div>


    </c:when>
</c:choose>
</c:forEach>

</div>
</c:forEach>


</ul>
</div>
<div class="configuration_bottom">
    <span class="configuration_bottom_I" usage="addQuest">添加小题</span>
</div>



</div>






</div>

<!--=================================底部==================================-->
<!--<div id="footer">

    <div id="footer-w">
        <span style="float: left">版权所有：上海复兰信息科技有限公司          <a href="http://www.fulaan-tech.com" target="_blank">www.fulaan-tech.com</a>        沪ICP备14004857号</span>
        <span style="float: right"><a href="/aboutus/k6kt">关于我们</a>  |  <a href="/contactus/k6kt">联系我们</a>   |  <a href="/service/k6kt">服务条款 </a> |  <a href="/privacy/k6kt">隐私保护 </a> |  <a href='http://wpa.qq.com/msgrd?v=1&uin=2803728882&site=qq&menu=yes' target="_blank">
            <img src="/img/exercise/QQService.png"></a></span>
    </div>
</div>-->
<div style="background-color: #ffffff;height: 50px;width: 100%;clear:both"></div>
<div id="footer">
    <div id="footer-w">
        <span style="float: left">版权所有：上海复兰信息科技有限公司          <a href="http://www.fulaan-tech.com" target="_blank">www.fulaan-tech.com</a>        沪ICP备14004857号</span>
        <span style="float: right"><a href="/aboutus/k6kt">关于我们</a>  |  <a href="/contactus/k6kt">联系我们</a>   |  <a href="/service/k6kt">服务条款 </a> |  <a href="/privacy/k6kt">隐私保护 </a> |  <a href='http://wpa.qq.com/msgrd?v=1&uin=2803728882&site=qq&menu=yes' target="_blank"><img src="/img/QQService.png"></a></span>
    </div>
</div>
<div id="browser-updater">
    <div class="browser-updater-wrapper" style="display: none">
        <p>
            <span>亲，浏览器版本过低可能会影响您的使用体验，为了提升速度您可以：</span>
            <a class="browser-updater-chrome" href="/upload/resources/ChromeStandaloneSetup.exe">安装Chrome浏览器</a>
        </p>
        <a class="browser-updater-close" onclick="hideBrowserUpdater()"><i class="fa fa-close fa-2"></i></a>
    </div>
</div>

<!--===================================上传解析弹出框=====================================-->
<div class="teacher_popup_1" style="display: none" id="teacher_popup">
    <div class="teacher_popup_I">
        <span class="teacher_popup_II">提示</span>
    </div>
    <div class="teacher_popup_III">解析上传成功！</div>
    <div class="teacher_popup_IIII">
        <span class="teacher_popup_IIII_I" onclick="promptHidden()">完成</span>
        <span class="teacher_popup_IIII_II" onclick="checkAnswerWord()">查看解析</span>
    </div>
</div>

<script>
    //判断浏览器版本
    if (navigator.appName == 'Microsoft Internet Explorer' && parseFloat(navigator.appVersion) < 5) {
        var updater = document.getElementById('browser-updater');
        updater.style.display = 'block';
        setTimeout(function () {
            updater.style.display = 'none';
        }, 3000);
    }
</script>
</body>
<script type="text/javascript">
    var fp = new FlexPaperViewer(
            '/static/plugins/flexpaper/FlexPaperViewer',
            'viewerPlaceHolder', { config : {
                SwfFile : escape('${swfPath}'),
                Scale : 1,
                ZoomTransition : 'easeOut',
                ZoomTime : 0.5,
                ZoomInterval : 0.2,
                FitPageOnLoad : false,
                FitWidthOnLoad : true,
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

    function promptHidden(){
        $("#teacher_popup").hide();
    }
</script>
<script type="text/template" id="choose_li">
    <li>
        <div class="configuration_main_right_VVVV_I">
            <div class="configuration_main_right_VVVV_II">
                分值
            </div>
            <input class="configuration_main_right_VVVV_IIII" type="text" placeholder="点击输入分值" usage="score">
        </div>
    </li>
    <li>
        <div class="configuration_main_right_VVVVVV_I">
            <div class="configuration_main_right_VVVV_II">
                答案
            </div>
            <div class="V">
                <input class="configuration_main_right_VVVV_IIIII" type="checkbox" option="A" value="A"><lable>A</lable>
                <input class="configuration_main_right_VVVV_IIIII" type="checkbox" option="B" value="B"><lable>B</lable>
                <input class="configuration_main_right_VVVV_IIIII" type="checkbox" option="C" value="C"><lable>C</lable>
                <input class="configuration_main_right_VVVV_IIIII" type="checkbox" option="D" value="D"><lable>D</lable>
            </div>
            <div class="VV">
                <div class="configuration_main_right_VVVV_IIIIII" usage="option_plus"> </div>
                <div class="configuration_main_right_VVVV_VI" usage="option_minus"> </div>
            </div>
        </div>
    </li>
</script>

<script type="text/template" id="judge_li">
    <li>
        <div class="configuration_main_right_VVVV_I">
            <div class="configuration_main_right_VVVV_II">
                分值
            </div>
            <input class="configuration_main_right_VVVV_IIII" type="text" placeholder="点击输入分值" usage="score">
        </div>
    </li>
    <li>
        <div class="configuration_main_right_VVVVVV_I">
            <div class="configuration_main_right_VVVV_II">
                答案
            </div>
            <div class="V">
                <input class="configuration_main_right_VVVV_IIIII" type="radio" value="1">对
                <input class="configuration_main_right_VVVV_IIIII" type="radio" value="0">错

            </div>
        </div>

    </li>
</script>
<script type="text/template" id="fill_li">
    <li>
        <div class="configuration_main_right_VVVV_I">
            <div class="configuration_main_right_VVVV_II">
                分值
            </div>
            <input class="configuration_main_right_VVVV_IIII" type="text" placeholder="点击输入分值" usage="score">
        </div>
    </li>
    <li>
        <div class="configuration_main_right_VVVVVV_I">
            <div class="configuration_main_right_VVVV_II">
                答案
            </div>
            <input class="configuration_main_right_VVVV_IIII" type="text" placeholder="点击输入答案" usage="fill">
        </div>
    </li>
</script>
<script type="text/template" id="subjective_li">
    <li>
        <div class="configuration_main_right_VVVV_I">
            <div class="configuration_main_right_VVVV_II">
                分值
            </div>
            <input class="configuration_main_right_VVVV_IIII" type="text" placeholder="点击输入分值" usage="score">
        </div>
    </li>
</script>

<script type="text/template" id="addBigQuestionTemp">
    <div class="configuration_big_quest_detail"  bigQuestionId="" bigQuestNum="{0}">
        <li>
            <div class="configuration_main_right_VVV_I">
                <div class="configuration_main_right_VVV_V">
                    <span class="configuration_main_right_VVV_II">答题分钟（选填）</span>
                    <input type="text" value="不限" class="configuration_main_right_VVV_III" usage="bigQuest_answerTime">

                    <span class="configuration_main_right_VVV_VI" type="time_plus">+</span>

                    <span class="configuration_main_right_VVV_VII" type="time_minus">-</span>
                </div>
            </div>
        </li>
        <div bigQuestNum="{0}" questId="" questNum="1" class="questDetail">
            <li>
                <div class="configuration_main_right_VVVV_I">
                    <div class="configuration_main_right_VVVV_II">
                        题号
                    </div>
                    <div class="configuration_main_right_VVVV_III">
                        {0}-1
                    </div>
                    <div class="VVVV">
                        <div class="configuration_main_right_VVVV_VIII"></div>
                        <div class="configuration_main_right_VVVV_VV"></div>
                        <div class="configuration_main_right_VVVV_VVI"></div>
                    </div>
                </div>
            </li>
            <li>
                <div class="configuration_main_right_VVVVV_I">
                    <div class="configuration_main_right_VVVVV_II">
                        题型
                    </div>
                    <span class="configuration_main_right_VVVVV_III" type="choose">选择</span>
                    <span class="configuration_main_right_VVVVV_III" type="judge">判断</span>
                    <span class="configuration_main_right_VVVVV_III" type="fill">填空</span>
                    <span class="configuration_main_right_VVVVV_III_active" type="subjective">主观</span>
                </div>
            </li>
            <li>
                <div class="configuration_main_right_VVVV_I">
                    <div class="configuration_main_right_VVVV_II">
                        分值
                    </div>
                    <input class="configuration_main_right_VVVV_IIII" type="text" placeholder="点击输入分值" usage="score">
                </div>
            </li>
        </div>
    </div>
</script>

</html>