<%
    String contextPath = session.getServletContext().getContextPath();
    session.setAttribute("contextPath", contextPath);
%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>批改试卷-复兰科技</title>
    <link rel="stylesheet" type="text/css" href="/static/css/exercise/teacher_configuration.css">

    <link rel="stylesheet" href="/static/plugins/jslider/jslider.css" type="text/css">
    <link rel="stylesheet" href="/static/plugins/jslider/jslider.blue.css" type="text/css">
    <link rel="stylesheet" href="/static/plugins/jslider/jslider.plastic.css" type="text/css">
    <link rel="stylesheet" href="/static/plugins/jslider/jslider.round.css" type="text/css">
    <link rel="stylesheet" href="/static/plugins/jslider/jslider.round.plastic.css" type="text/css">
    <link rel="stylesheet" type="text/css" href="/static/css/teacherSheet.css"/>

    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/static/plugins/jslider/jshashtable-2.1_src.js"></script>
    <script type="text/javascript" src="/static/plugins/jslider/jquery.numberformatter-1.2.3.js"></script>
    <script type="text/javascript" src="/static/plugins/jslider/tmpl.js"></script>
    <script type="text/javascript" src="/static/plugins/jslider/jquery.dependClass-0.1.js"></script>
    <script type="text/javascript" src="/static/plugins/jslider/draggable-0.1.js"></script>
    <script type="text/javascript" src="/static/plugins/jslider/jquery.slider.js"></script>
    <%--<script type="text/javascript" src="/js/jquery-ui.min.js"></script>--%>

    <script type="text/javascript" src="/static/js/ypxxUtility.js"></script>
    <script type="text/javascript" src="/static/js/swfobject.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <style type="text/css">
        .check-homeworkbtn {
            color: white;
        }
    </style>
    <%--==============================================--%>
</head>
<body>
<div class="configuration_top">
    <div class="configuration_top_main">
       <span class="configuration_top_main_III">
           <span class="modify_II_I" style="color: #64B864;display: block;cursor: pointer;" onclick="go2href('/exam/studentlist.do?did=${did}')">
                  
                    返回
           </span>
       </span>
    </div>
</div>


<input type="button" onclick="bianhua()"/>

<div class="configuration_main">
    <div style="width: 770px;height: 60px;" id="configuration_img">
        <a href="javaScript:previousPage()"><img src="/img/te_first.jpg" style="margin-left: 100px;"></a>
        <a href="javaScript:nextPage()"><img src="/img/te_last.png" style="float: right;margin-right: 50px;"></a>
    </div>
    <div class="modify_2_left_I" id="btn">
        <span onclick="changeSwfUrl(1)" class="modify_2_left_II">试卷</span>
        <span onclick="changeSwfUrl(2)" class="modify_2_left_III">解析</span>
    </div>
    <div class="configuration_main_left" id="showPdf" style="float:left;">
        <a id="viewerPlaceHolder" style="width:790px;height:1000px;display:block"></a>

        <div class="configuration_main_left_I">

        </div>
    </div>
   
        <div class="configuration_main_rightt">
        <div class="configuration_main_rightt_I">
            <div>
            
             <c:forEach var="dto" items="${list}">
             
             <c:if test="${dto.isShow==1 }">
             
             
               <c:if test="${dto.ty!=5}">
	                <dl>
	                    <dt>
	                        <span>
	                            <em>题号${dto.titleId }</em>
	                            <em>题型：${dto.type }</em>
	                            <em>分数：${dto.score }分</em>
	                        </span>
	                    </dt>
	                    <dd>答案：${dto.answer }</dd>
	                </dl>
               </c:if>
               
               
               
                <c:if test="${dto.ty==5}">
	                               <dl>
                 <dl>
                    <dt>
                        <span>
                            <em>题号${dto.titleId }</em>
	                        <em>题型：${dto.type }</em>
                        </span>
                    </dt>
                    <dd>
                        <div class="configuration_dv">${dto.answer }</div>
                        
                        <div class="imageDiv">
	                        <c:forEach var="img" items="${dto.iamge}">
	                          <img src="${img.value}" width="50px" height="50px;" class="teacher-check" name="${dto.answerId }"  picId="${img.id}">
	                        </c:forEach>
                        </div>
                        
                        <div>
                            <em>得分：</em>
                            <input style="width:100px;" class="SliderSingle" id="slider_${dto.answerId }"  type="slider" name="price" value="0" totalScore=${dto.totalScore }>
                            <input id="score-${dto.answerId }" onblur="inputOnblur('${dto.answerId }')" class="modify_2_VV">
                        </div>
                    </dd>
                    
                 </dl>
                   
          
               </c:if>
               
               
               </c:if>
               
               
               
             </c:forEach>
            
              
            <dd>
                 <button onclick="saveScore()">保存</button>
             </dd>
               
            </div>
        </div>
    </div>

    <div id="browser-updater">

    </div>
</div>
<div id="footer">
    <div id="footer-w">
        <span style="float: left">版权所有：上海复兰信息科技有限公司          <a href="http://www.fulaan-tech.com" target="_blank">www.fulaan-tech.com</a>        沪ICP备14004857号</span>
        <span style="float: right"><a href="/aboutus/k6kt">关于我们</a>  |  <a href="/contactus/k6kt">联系我们</a>   |  <a
                href="/service/k6kt">服务条款 </a> |  <a href="/privacy/k6kt">隐私保护 </a> |  <a
                href='http://wpa.qq.com/msgrd?v=1&uin=2803728882&site=qq&menu=yes' target="_blank"><img
                src="/img/QQService.png"></a></span>
    </div>
</div>
<div id="check-hw-container"></div>
<div class="alert-bg"></div>

</body>


<script type="text/javascript" src="${contextPath}/static/plugins/flexpaper/flexpaper_flash.js"></script>
<script type="text/javascript" src="${contextPath}/static/plugins/flexpaper/flexpaper.js"></script>
<script type="text/javascript" src="${contextPath}/static/plugins/flexpaper/flexpaper_handlers.js"></script>
<script type="text/javascript" src="${contextPath}/static/plugins/flexpaper/flexpaper_handlers_debug.js"></script>
<script type="text/javascript" src="${contextPath}/static/plugins/flexpaper/flexpaper_flash_debug.js"></script>


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
    function go2href(url) {
        window.location.href = url;
    }
    var page = 1;
    var pdfUrl='${pdfPath}';
    function renderPage(i, url) {
    	

        PDFJS.workerSrc = '/static/js/exercise/plugins/worker_loader.js';
        var can = ' <canvas id="the-canvas" />';
        $("#showPdf").empty();
        $("#showPdf").append(can);
        'use strict';
        PDFJS.getDocument(url).then(function (pdf) {
            if (i > pdf.numPages) {
                alert("已翻到末页！")
                i = pdf.numPages;
            }
            pdf.getPage(i).then(function (page) {
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
    function previousPage() {
        page = page - 1;
        if (page < 1) page = 1;
        renderPage(page, pdfUrl);
    }
    function nextPage() {
        page += 1;
        renderPage(page, pdfUrl);
    }
    function previewSwf(url) {
        var fp = new FlexPaperViewer(
                '/static/plugins/flexpaper/FlexPaperViewer',
                'viewerPlaceHolder', {
                    config: {
                        SwfFile: escape(url),
                        Scale: 0.6,
                        ZoomTransition: 'easeOut',
                        ZoomTime: 0.5,
                        ZoomInterval: 0.2,
                        FitPageOnLoad: true,
                        FitWidthOnLoad: false,
                        FullScreenAsMaxWindow: false,
                        ProgressiveLoading: false,
                        MinZoomSize: 0.2,
                        MaxZoomSize: 5,
                        SearchMatchAll: false,
                        InitViewMode: 'SinglePage',

                        ViewModeToolsVisible: true,
                        ZoomToolsVisible: true,
                        NavToolsVisible: true,
                        CursorToolsVisible: true,
                        SearchToolsVisible: true,

                        localeChain: 'en_US'
                    }
                });
    }
    function isPcPlate() {
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
            return true;
        } else {
            return false;
        }
    }
    function changeSwfUrl(num) {
    	
    	
        if (num == 1) {
            //试卷url
            pdfUrl = '${pdfPath}';
            if (isPcPlate()) {
                previewSwf('${swfPath}');
            } else {
                renderPage(1,pdfUrl);
            }
        } else {
            //解析url
            pdfUrl = '${ansPdfPath}';
            if (isPcPlate()) {
                previewSwf('${ansPath}');
            } else {
                renderPage(1,pdfUrl);
            }

        }
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
            $("#configuration_img").hide();
            previewSwf('${swfPath}');
        } else {
            renderPage(1, '${pdfPath}');
        }
    });

    //判断浏览器版本
    if (navigator.appName == 'Microsoft Internet Explorer' && parseFloat(navigator.appVersion) < 5) {
        var updater = document.getElementById('browser-updater');
        updater.style.display = 'block';
        setTimeout(function () {
            updater.style.display = 'none';
        }, 3000);
    }

    var lesson ='${lesson}';
    var wordExerciseId = '${docId}';
    var questId = '${titleId}';
    var classId ='${classId}';
    var scoreArray = new Array();
    //next为1表示获取下一题 0表示获取当题 2表示取上一题
    function viewQuest(questId1, next) {
        //清空score Array
        scoreArray = new Array();
        $.ajax({
            url: "/exam/item/answer/stat.do",
            type: "post",
            dataType: "json",
            data: {
            	docId: wordExerciseId,
            	titleId: questId,
            	type: next,
            	lessonId:lesson,
                classId:classId
            },
            success: function (data) {
                if (data == null || $.trim(data) == '') {
                    alert("未查找到相关数据");
                    return;
                }
                if (!data.userAnswerList || data.userAnswerList.length== 0) {
                    handleQuestOnly(data);
                    return;
                }
                
                wordExerciseId=data.documentId;
                questId=data.id;
                
                var html = '<ul>';
             /*   html += '<li><div class="modify_2_I"><span onclick="prevQuest()" class="modify_2_II"><&nbsp;上一题</span>' +
                '<span class="modify_2_III">' + data.titleId + '</span> <span onclick="nextQuest()" class="modify_2_II">下一题&nbsp;></span></div></li>';
                html += '<li><div class="modify_2_V"> <div class="modify_2_V_I"><span>' + data.titleId + '</span>';*/
                var span = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
                if (data.type == 1) {
                    span += '<span>选择题</span>'
                } else if (data.type == 3) {
                    span += '<span>判断题</span>'
                } else if (data.type == 4) {
                    span += '<span>填空题</span>'
                } else if (data.type == 5) {
                    span += '<span>主观题</span>'
                }
                html += span;
                html += '&nbsp;&nbsp;&nbsp;&nbsp;<span id="zongfen" zongfen=' + data.score + '>' + data.score + '分</span></div>';
                if (data.type != 5)
                    html += '<div class="modify_2_V_V_I"><span>正确率：</span><span class="modify_2_V_V_II">' + data.rate + '</span> ';
                if (data.type != 5) {
                    if (data.type != 3) {
                        html += '<br/><span class="modify_2_V_V_III">正确答案:</span>' +
                        '<span class="modify_2_V_V_IIII">' + data.answer + '</span></div>';
                    } else {
                        var k = returnDuiCuo(data.answer);
                        html += '<br/><span class="modify_2_V_V_III">正确答案:</span>' +
                        '<span class="modify_2_V_V_IIII">' + k + '</span></div>';
                    }

                }
                html += '';
                //选择判断   显示选项细节 其他题型不显示
                if (data.type == 1) {
                    var select = makeSelection(data);
                    html += select;
                } else if (data.type == 3) {
                    html += '<div class="modify_2_V_VV_I" style="height: 20px;"><span class="modify_2_V_PD_II">对</span><span class="modify_2_V_PD_IIII"';
                    if (data.right == 0) {
                        html += 'style="width: 0px;"';
                    }
                    html += '></span><span class="modify_2_V_PD_VII">' + data.rightCount + '人</span><br></div>' +
                    '<div class="modify_2_V_VV_I"><span class="modify_2_V_PD_II">错' +
                    '</span><span class="modify_2_V_PD_IIII"';
                    if (data.wrong  == 0) {
                        html += 'style="width: 0px;"';
                    }
                    html += '></span><span class="modify_2_V_PD_VII">' + data.wrongCount + '人</span><br></div> ';
                }
                html += '</div> </li>';
//==================================================== 学生答案部分 =========================================================
                var saveFlag;
                for (var k = 0; k < data.userAnswerList.length; k++) {
                    var answer = data.userAnswerList[k];
                    html += '<li> <div class="modify_2_V_VVV_I"><div class="modify_2_V_VVV_II"><span class="modify_2_V_VVV_III">' + answer.name + '</span>' +
                    '</div> <div class="modify_2_V_VVV_V">';
                    var picflag = answer.imageList == null || answer.imageList == '' || answer.imageList == 'null';
                    if (picflag && (answer.answer == 'null' || answer.answer == null || $.trim(answer.answer) == '')) {
                        answer.answer = '未作答';
                    }
                    if (1 == answer.right) {//答案正确  显示绿色
                        if (data.type == 3) {
                            var x = returnDuiCuo(data.answer);
                            html += ' <span style="color: #008000">' + x + '</span>';
                        } else {
                            html += ' <span style="color: #008000">' + answer.answer + '</span>';
                        }
                    } else {//错误显示红色
                        if (answer.answer != null && answer.answer != 'null') {
                            if (data.type == 3) {
                                var z = returnDuiCuo(answer.answer);
                                html += '<span style="color: red;margin: 0 0 0 40px;display: inline-block;width: 11em;min-height: 20px;word-break: break-all;">' + z + '</span>';
                            } else {
                                html += '<span style="color: red;margin: 0 0 0 40px;display: inline-block;width: 11em;min-height: 20px;word-break: break-all;">' + answer.answer + '</span>';
                            }
                        }
                    }
                    if (data.type == 5) {
                        if (answer.imageList != null && answer.imageList != 'null') {
                            var picStr = answer.imageList;
                            html += '<br>';
                            html += '<div style="width:250px;min-heght:50px;;">'
                            for (var i = 0; i < picStr.length; i++) {
                                var pic = picStr[i].value
                                var answerId = picStr[i].idStr;
                                html += '<img  style="width: 50px;height: 50px;" class="teacher-check" name="' + answer.id + '"      picId="' + answerId + '"   src="' + pic + '">&nbsp;&nbsp;';
                            }
                            html += '</div>'
                            if (answer.score == null || answer.score == 'null') {
                                html += '<br><span>得分：</span><input class="SliderSingle" id="slider_' + answer.id + '"  type="slider" name="price" value="0"><input id="score-' + answer.id + '" onblur="inputOnblur(' + answer.id + ')" class="modify_2_V_ZG_VV_V">';
                                saveFlag = true;
                                scoreArray.push('score-' + answer.id);
                            } else {
                                html += '<br><span name="editSpan"  answerId="' + answer.id + '">得分：' + answer.score + '</span>';
                            }


                        } else {
                            if (answer.score == null || answer.score == 'null') {
                                html += '<br/> <span>得分：</span> <input class="SliderSingle" id="slider_' + answer.id + '"  type="slider" name="price" value="0"><input id="score-' + answer.id + '" onblur="inputOnblur(' + answer.id + ')"  class="modify_2_V_ZG_VV_V">';
                                saveFlag = true;
                                scoreArray.push('score-' + answer.id);
                            } else {
                                html += '<br/><span name="editSpan" answerId="' + answer.id + '" >得分：' + answer.score + '</span>';
                            }
                        }
                    }
                }
                html += '</div> </div></li>';
                if (data.type == 5) {
                    if (saveFlag) {
                        html += '<div class="modify_2_bottom_I"><span onclick="saveScore(this)" class="modify_2_bottom_II">保存</span></div>';
                    } else {
                        html += '<div class="modify_2_bottom_I"><span onclick="editScore(this)" class="modify_2_bottom_II">编辑</span></div>';
                    }
                }

                $(".configuration_main_right_I").empty();
                $(".configuration_main_right_I").append(html);
                jQuery(".SliderSingle").each(function () {
                    var id = jQuery(this).attr("id");
                    id = "score-" + id.replace("slider_", "");
                    jQuery(this).slider({
                        from: 0, to: data.score, step: 1, round: 1, skin: "round",
                        calculate: function (value) {
                            jQuery("#" + id).val(value);
                        }
                    });
                });

                $('.modify_2_V_VVV_V').each(function () {
                    ypxxutility.homepage.picswitch2(this, {
                        width: 600,
                        height: 400
                    });
                });
                //修改questId
                questId = data.id;
            }
        });
    }
    function editScore(node) {
        var spans = $("span[name='editSpan']");
        for (var i = 0; i < spans.length; i++) {
            var span = spans[i];
            var answerId = $(span).attr('answerId');
            var html = '<input class="SliderSingle" id="slider_' + answerId + '"  type="slider" name="price" value="0"><input id="score-' + answerId + '" onblur="inputOnblur(' + answerId + ')"  class="modify_2_V_ZG_VV_V">';

            $(span).text("得分：");
            $(span).after(html);
            scoreArray.push('score-' + answerId);
            var zongfen = $('#zongfen').attr('zongfen');
            jQuery(".SliderSingle").each(function () {
                var id = jQuery(this).attr("id");
                id = "score-" + id.replace("slider_", "");
                jQuery(this).slider({
                    from: 0, to: zongfen, step: 1, round: 1, skin: "round",
                    calculate: function (value) {
                        jQuery("#" + id).val(value);
                    }
                });
            });
        }
        $(node).replaceWith('<div class="modify_2_bottom_I"><span onclick="saveScore(this)" class="modify_2_bottom_II">保存</span></div>');
    }
    function returnDuiCuo(data) {
        if (data == 1) return '对';
        return '错';
    }
    
    
    function saveScore(node) {
        var str = '';
        for (var i = 0; i < scoreArray.length; i++) {
            var scoreId = scoreArray[i];
            var score = $("#" + scoreId).val();
            var answerId = scoreId.split('-')[1];
            if (score == null || $.trim(score) == '')score = 0;
            str += answerId + '-' + score + ',';
        }
        str = str.substring(0, str.length - 1);
        $.ajax({
            url: "/exam/item/update.do",
            type: "post",
            dataType: "json",
            data: {
                sc: str
            },
            success: function (data) {
               if(data.code!="200")
            	   {
            	   alert(data.message);
            	   }
               else
            	   {
            	   alert("批改已保存！");
            	   }
               
            }
        });
    }

    function prevQuest() {
        viewQuest(questId, 2);
    }
    function nextQuest() {
        viewQuest(questId, 1);
    }
    function handleQuestOnly(data) {
        var html = '<ul>';
        html += '<li><div class="modify_2_I"><span onclick="prevQuest()" class="modify_2_II"><&nbsp;上一题</span>' +
        '<span class="modify_2_III">' + data.titleId + '</span> <span onclick="nextQuest()" class="modify_2_II">下一题&nbsp;></span></div></li>';
        html += '<li><div class="modify_2_V"> <div class="modify_2_V_I"><span>' + data.titleId + '</span>';
        var span = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
        if (data.type == 1) {
            span += '<span>选择题</span>'
        } else if (data.type == 3) {
            span += '<span>判断题</span>'
        } else if (data.type == 4) {
            span += '<span>填空题</span>'
        } else if (data.type == 5) {
            span += '<span>主观题</span>'
        }
        html += span;
        html += '&nbsp;&nbsp;&nbsp;&nbsp;<span>' + data.score + '分</span></div>';
        html += '<div class="modify_2_V_V_I"><span>正确率：</span><span class="modify_2_V_V_II">0%</span>';
        if (data.type != 5) {
            var dc = data.answer;
            if (data.type == 3) {
                dc = returnDuiCuo(data.answer)
            }
            html += '<span class="modify_2_V_V_III">正确答案:</span>' +
            '<span class="modify_2_V_V_IIII">' + dc + '</span></div>';
        }
        html += '</div> </li>';
        $(".configuration_main_right_I").empty();
        $(".configuration_main_right_I").append(html);

        questId = data.id;
    }
    function makeSelection(data) {
    	
    	 var max=0;
    	 if(data.answerCounts)
    	 {
	    	  for(var j=0;j<data.answerCounts.length;j++)
	    	  {
	    		  try
	    		  {
	    		    var ac=data.answerCounts[j];
	    		    if(ac.count>max)
	    		    	max=ac.count;
	    		  }catch(x)
	    		  {
	    		  }
	    	  }
    	 }
        var array = new Array(20);
        array[0] = 'A';
        array[1] = 'B';
        array[2] = 'C';
        array[3] = 'D';
        array[4] = 'E';
        array[5] = 'F';
        array[6] = 'G';
        array[7] = 'H';
        array[8] = 'I';
        array[9] = 'J';
        var html = ''
        for (var i = 0; i < data.selectCount; i++) {
            var s = array[i];
            if (s == "A") {
                s = data.answerMap.A;
            } else if (s == "B") {
                s = data.answerMap.B;
            } else if (s == "C") {
                s = data.answerMap.C;
            } else if (s == "D") {
                s = data.answerMap.D;
            } else if (s == "E") {
                s = data.answerMap.E;
            } else if (s == "F") {
                s = data.answerMap.F;
            } else if (s == "G") {
                s = data.answerMap.G;
            } else if (s == "H") {
                s = data.answerMap.H;
            } else if (s == "I") {
                s = data.answerMap.I;
            } else if (s == "J") {
                s = data.answerMap.J;
            }
            if (s == null || s == "null") {
                s = 0;
            }
            
            var width=0;
            
            if(max>0)
            {
            	width=(s/max)*170;
            }
            
            html += '<div class="modify_2_V_VV_I" style="height: 20px;"><span class="modify_2_V_VV_II">' + array[i] +
            '</span><span class="modify_2_V_VV_III" ';
           // if (s == 0) 
            {
                html += 'style="width: '+width+'px;"';
            }
            
            
            
            
            
            
            html += '></span><span class="modify_2_V_VV_VII">' + s + '人</span>';
            if (i != data.titleId - 1) {
                html += '<br>';
            }
            html += '</div>';
        }
        return html;
        
    }
    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]);
        return null; //返回参数值
    }
    viewQuest(questId, 0);

    $(function () {
    	
    	 $('body').on('click', '.check-homeworkbtn', function () {
             var pUrl = $(this).prev().find('img').attr('src');
//             pUrl='7xilkg.com1.z0.glb.clouddn.com'+pUrl+'?imageView2/2/w/600';
             var answerId = $(this).prev().find('img').attr('name');
             var picId=$(this).prev().find('img').attr('picId');
             checkPrintPos();
             $('#check-hw-container').show();
             $('.alert-bg').show();
             $('#picswitch').remove();
             var picUrl = pUrl;
             var so = new FlashObject("/static/js/swfobject/pictureEditor.swf", "flashApp", "1000px", "700px", "8");
             so.addVariable("picUrl", picUrl);
            // so.addVariable("uploadUrl", "/exam/item/image/upload1.do?answerId=" + answerId+"&picId="+picId);
             so.addVariable("uploadUrl", "/exam/item/image/upload1/" + answerId+"/"+picId);
             so.addParam("wmode","transparent");
             so.write("check-hw-container");
             $('#check-hw-container').append('<div class="close-check-hw" style="color:white;top:65px;right:113px;"></div>');
         });
    	 
    	 
    	 
    	 
        $('body').on('click', '.close-check-hw', function () {
            $('#check-hw-container').empty().hide();
            $('.alert-bg').hide();
            $('#fullbg').hide();
            $("#btn").show();
        });

        window.onresize = function () {
            checkPrintPos();
        }

        function checkPrintPos() {
            var whigh = document.documentElement.clientHeight;
            var wwidth = document.documentElement.clientWidth;
            if (wwidth > 900) {
                
                $('#check-hw-container').css({
                    'top': (whigh - 700) / 2,
                    'left': (wwidth - 1000) / 2
                });
            }
        }
    });
    
    
    $(function () {
    	 jQuery(".SliderSingle").each(function () {
             var id = jQuery(this).attr("id");
             id = "score-" + id.replace("slider_", "");
             
             var t=jQuery(this).attr("totalScore");
             jQuery(this).slider({
                 from: 0, to: t, step: 1, round: 1, skin: "round",
                 calculate: function (value) {
                     jQuery("#" + id).val(value);
                 }
             });
         });
    	 
    	 
    	 
    	  $('.imageDiv').each(function () {
              ypxxutility.homepage.picswitch2(this, {
                  width: 600,
                  height: 400
              });
          });
    });
    
    
    
    
    //图片上传完成回调函数
    function uploadComplete() {
        $('#check-hw-container').empty().hide();
        $('.alert-bg').hide();
        $('#fullbg').hide();
        $("#btn").show();
        window.location.reload();
    }
    function inputOnblur(answerId) {
        var zongfen = $('#zongfen').attr('zongfen');
        var inputVal = $("#score-" + answerId).val();
        if (!checkRate(inputVal)) {
            alert("只能输入正整数");
            $("#score-" + answerId).val(0)
            return
        }
        inputVal = parseInt(inputVal)

        if (inputVal > zongfen) {

            alert("分值不能超过该题总分")
            $("#score-" + answerId).val(zongfen)
        }
    }
    function checkRate(input) {
        var re = /^[1-9]+[0-9]*]*$/;
        return re.test(input)

    }
    
    //56692a3863e7f4fcd28963ee-2,56692a1b63e7f4fcd28963e8-2,56692a6263e7f4fcd28963f9-2
    //score-${dto.answerId }
    function saveScore()
    {
    	var sc="";
    	
    	jQuery(".modify_2_VV").each(function(){
    		
    		
    		var id=jQuery(this).attr("id").replace("score-", "");
    		var s=jQuery(this).val();
    		if(!s)
    			{
    			 alert("请给出分数");
    			 return false;
    			}
    		
    		sc+=id+"-"+s+",";
    		
    	});
    	
    	
    	if(sc)
    	{
    		$.ajax({
                url: "/exam/item/update.do",
                type: "post",
                dataType: "json",
                data: {
                    sc: sc
                },
                success: function (data) {
                   if(data.code!="200")
                	   {
                	   alert(data.message);
                	   }
                   else
                	   {
                	   alert("批改已保存！");
                	   }
                   
                }
            });
    	}
    }
</script>

</html>