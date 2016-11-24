<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>课程视频</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/slideshow.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap-combined.school.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/lesson/lesson-view.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/dialog.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/lesson/lesson-vote.css"/>
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bowser.min.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
    <script type="text/javascript" src="/static/js/swfobject.js"></script>
    <script type="text/javascript" src="/static/js/recorder.js"></script>
    <script type="text/javascript" src="/static/js/main.js"></script>
    <script type="text/javascript" src="/static/js/experienceScore.js"></script>
    <script type="text/javascript" src="/static/js/lessons/lesson-view.js"></script>
    <script type="text/javascript" src="/static/js/lessons/ypCourseSheet.js"></script>
    
    
    <script type="text/javascript" src="/static/plugins/flexpaper/flexpaper_flash.js"></script>
    <script type="text/javascript" src="/static/plugins/flexpaper/flexpaper_flash_debug.js"></script>
    
    
    
    <script type="text/javascript">
        var currentPageID = 1;
        var courseid = '${param.lessonId}';
        var currentCommPage = 1;
        var sequence = 0;
        //var CurrentUId = '${currentUser.id}';
        //var role = '${currentUser.role}';
        function PlayedMusic(ob, rid) {
            var player = '<embed src="/upload/voice/' + $(ob).attr('url') + '" belong="' + rid + '" width="0" height="0" autostart="true" />';
            $('embed[belong=' + rid + ']').remove();
            $('body').append(player);
        }


        function doPractise(wordexerciseId){
            window.location.href="/exam/view.do?id="+wordexerciseId+"&type=1&lesson=${param.lessonId}";
        }



        //查看结果
        function gores(wordexerciseId){
        	 window.location.href="/exam/view.do?id="+wordexerciseId+"&type=2";;
        }
    </script>
</head>
<body csid="${param.lessonId}">


<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container">
    <div id="content_main">

        <%@ include file="../common_new/col-left.jsp" %>


        <div class="course-view-container">

            <c:if test="${roles:isEducation(sessionValue.userRole)}">
                <div id="nav_to_my" style="background:white;padding:10px 0;">
                    <div style="margin:0 auto;width:900px;">
                        <a href="/user">&nbsp;首页&nbsp;</a>>
                        <a id="browser" href="/browser">&nbsp;班级&nbsp;</a>>&nbsp;
                        <a id="classname" href="/teacher/class"></a>
                        <span>&nbsp;>&nbsp;</span>
                        <a id="teachername" href="/teacher/course"></a>&nbsp;>&nbsp;
                        <a id="coursename" href="#"></a>&nbsp;
                    </div>
                </div>
            </c:if>

            <div class="video-course-name"></div>



            <div class="examine_mian" >
			    <div class="examine_main_text" style="height:500px;overflow: visible;">
			        <a id="viewerPlaceHolder" style="width:645px;height:950px;display:inline" class="configuration_main_left_I"></a>
			    </div>
			</div>


  
            <%--<div style="height: 38px;border: 1px solid #d4d4d4;background: url(/images/all_bg.png) repeat-x;background-color: #ebebeb;cursor: pointer;position: relative;">
                <span style="width: 21px;height: 22px;position: absolute;top: 10px;right: 20px;cursor: pointer;background: url(/images/V_all.png) no-repeat;"></span>
            </div>--%>
            <div class="recorder-container right-width">
                <c:if test="${roles:isStudentOrParent(sessionValue.userRole)}">
                    <script>
                        setInterval(function () {
                            alert('已经学习30分钟了，该休息休息了!');
                        }, 1000 * 60 * 30);
                    </script>
                    <div id="voiceCourseId" voiceid="${param.lessonId}"/>
                    <div id="reply_section">
                        <div style="height:auto;margin-bottom:0;">
                            <div id="recordercontainer1">
                                <div id="recorder" class="">
                                    <div class="area" style="margin-left:15px;">
                                        <%--<b class="a12" onclick="showflash('recordercontainer1')"><img--%>
                                                <%--src="/img/mic.png"/>语音</b>--%>

                                        <div style="width: 660px;height: 50px;margin-top: -5px;margin-left: 40px;">

                                            <c:choose>
                                                <c:when test="${exerciseStat== 1 && isParent==0}">
                                                    <div style="cursor: pointer;color: #ffffff;font-family: 'Microsoft Yahei';
                                        font-size: 15px;background-color: #60BC2B;padding:5px 100px;position:relative;left:150px;top: 10px;
                                        display: inline-block;border-radius: 5px;" onclick="doPractise('${exerciseId}')">
                                                        去做课后练习
                                                        <%--<span>(${questCount}题)</span>--%>
                                                    </div>
                                                </c:when>

                                                <c:when test="${exerciseStat== 2}">
                                                    <div style="cursor: pointer;color: #ffffff;font-family: 'Microsoft Yahei';
                                        font-size: 15px;background-color: #60BC2B;padding:5px 100px;position:relative;left:150px;top: 10px;
                                        display: inline-block;border-radius: 5px;" onclick="gores('${exerciseId}')">
                                                        查看练习结果

                                                        <%--<span>(${questCount}题)</span>--%>
                                                    </div>
                                                </c:when>
                                            </c:choose>

                                        </div>


                                        <div style="padding-top: 10px;position: absolute;z-index: 50000;left: -30px;">
                                            <div class="sanjiao"
                                                 style="width: 0;height: 0;border-left: 8px solid transparent;border-right: 8px solid transparent;border-bottom: 10px solid rgb(100,100,100);position:absolute;display: inline-block;top:0px;left:30px;display:none;"></div>
                                            <div id="myContent">


                                            </div>
                                        </div>
                                    </div>
                                    <form id="uploadForm" name="uploadForm"
                                          action="audiosave.jsp?userId=${sessionValue.id}&outId=${param.lessonId}&type=0&commenttype=1&voicetype=0">
                                        <input name="authenticity_token" value="xxxxx" type="hidden">
                                        <input name="upload_file[parent_id]" value="1" type="hidden">
                                        <input name="format" value="json" type="hidden">
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
            <div class="right-width">
                <div class="fc_sheet_left">
                    <%--TODO 微课评比
                        Boolean isVote = (Boolean) session.getAttribute("isVote");
                        if (isVote) {
                            if ((Boolean) request.getAttribute("isJudge") || (Boolean) session.getAttribute("isVoteMaster")) {
                    %>
                    <div id="vote-section">
                        <div>
                            <h4>评分区</h4>

                            <hr style="background-color:#ff7e00;margin-top:10px;margin-bottom:0;">

                            <c:choose>
                                <c:when test="${isVoteMaster}">
                                    <div class="vote-section vote-result">
                                        <span>平均分</span><input type="text" name="voteScore" id="avg-score"
                                                               class="input-score"
                                                               readonly>分
                                    </div>
                                    <div class="vote-section vote-history">
                                        <jsp:include page="/vote/voteHistory.do">
                                            <jsp:param name="lessonId" value="${param.lessonId}"/>
                                        </jsp:include>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="vote-section new-vote">
                                        <span>课程评分</span><span><input placeholder="0 ~ 100" type="text" name="voteScore"
                                                                      id="vote-score"
                                                                      class="input-score" readonly
                                                                      onkeypress='return event.charCode >= 48 && event.charCode <= 57'
                                                                      maxlength="3">分</span>
                                        <button onclick="submitScore();" class="button-orange submit-score">提交</button>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <script type="text/javascript" src="/static/js/lessons/lesson-vote.js"></script>
                    <%
                            }
                        }
                    --%>
                    <%--<div id="answer_list_container">--%>
                        <%--<div id="answer_list" class="ypheader forIEFloatP">--%>
                            <%--<h4>答题列表</h4>--%>
                            <%--<hr style="background-color:#ff7e00;margin-top:10px;">--%>
                            <%--<ul>--%>
                            <%--</ul>--%>
                        <%--</div>--%>

                    <%--</div>--%>

                    <div id="discussion_container">
                        <div id="discussion" class="ypheader">
                            <h4>讨论区</h4>
                            <hr style="background-color:#ff7e00;margin-top:10px;margin-bottom:0;">
                            <img src="${sessionValue.minAvatar}"
                                 style="position:relative;top:70px;left:35px;width:38px;height:38px;">

                            <div style="margin:30px 0 50px 0;position:relative;left:92px;"><textarea id="commentText"
                                                                                                     style="height: 200px;"
                                                                                                     placeholder="请输入你想说的话"></textarea>

                                <div id="comm_toolbar">
                                    <div><input id="commentBtn" type="button" onclick="submitComment()" value="提交评论">
                                    </div>

                                </div>

                            </div>

                            <div id="discussion_list">


                            </div>
                        </div>
                    </div>
                </div>

                <div class="fc_sheet_right">
                    <div id="courseware_list">
                        <h4>备课列表</h4>
                        <ul>
                            <c:forEach items="${coursewareList}" var="cw">
                                <%--<c:if test="${cw.value5==1}">--%>
                                    <li>
                                        <a href="${cw.value}" target="_blank">
                                            <img src="/img/kejian.jpg">
                                        </a>
                                        <div class="kjinfo">
                                            <span>${cw.type}</span>
                                        </div>
                                        <div class="kjdown">
                                            <a href="${cw.value}" target="_blank">下载</a>
                                        </div>
                                    </li>
                                <%--</c:if>--%>
                            </c:forEach>
                        </ul>
                    </div>

                    <%--<div id="relatived_course_list">--%>
                        <%--<h4>相关课程</h4>--%>
                        <%--<ul>--%>

                        <%--</ul>--%>
                    <%--</div>--%>

                    <div id="viewed_video_students">
                        <h4>看过该视频的同学</h4>
                        <ul>

                        </ul>
                    </div>

                </div>

            </div>

        </div>

    </div>
</div>
<%@ include file="../common_new/foot.jsp" %>


<script type="text/javascript">
    var fp = new FlexPaperViewer(
            '/static/plugins/flexpaper/FlexPaperViewer',
            'viewerPlaceHolder', { config : {
                SwfFile : escape('${swf_path}'),
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
</script>
</body>
</html>