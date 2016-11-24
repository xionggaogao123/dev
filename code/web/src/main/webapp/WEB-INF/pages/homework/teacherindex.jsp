<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-8-4
  Time: 上午10:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>作业</title>
    <%--<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">--%>
    <link rel="stylesheet" href="/static_new/css/homework.css">
    <%--<link href="//netdna.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">--%>
    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static_new/css/schoolsecurity.css?v=2015041602" rel="stylesheet" />
    <link href="/static_new/css/friendcircle/homepage.css?v=1" type="text/css" rel="stylesheet">
    <%--计算发表日期--%>
    <script src="/static/js/shareforflipped.js" type="text/javascript"></script>
    <%--录音--%>

    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <script type="text/javascript" src="/static/js/swfobject.js"></script>
    <script type="text/javascript" src="/static/js/recorder.js"></script>
    <script type="text/javascript" src="/static/js/main.js"></script>



</head>
<body type="${param.type}">
<!--===========================startt头部==============================-->
<%@ include file="../common_new/head.jsp" %>
<!--===========================end头部==============================-->
<div class="homework-main">
    <!--===========================start左边导航==============================-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!--===========================end左边导航==============================-->
    <!--===========================start广告==============================-->
    <!--广告-->
    <c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>
    <!--广告-->
    <!--===========================end广告==============================-->
    <div class="homework-right">
        <div class="homework-right-top">
            <div class="homework-banner">
                <span>
                    <c:choose>
                        <c:when test="${param.type == 'keqian'}">
                            课前作业
                        </c:when>
                        <c:otherwise>
                            课后作业
                        </c:otherwise>
                    </c:choose>
                </span>
                <a href="#" class="he-ba-ri"></a>
            </div>
            <div class="homework-cls">
                <div class="cls-left">
                    <em>班级</em>|
                </div>
                <%--<div class="cls-center">--%>
                <div style="line-height:50px;">
                    <%--<em class="checkedClass classSubject" id="qbbj" classId="000000000000000000000000"--%>
                        <%--subjectId="000000000000000000000000">全部</em>--%>
                <%--</div>--%>
               </div>
                <div class="cls-right" id="classSubject" >
                    <%--<span>三年级一班(数学)</span>--%>
                </div>

                <%--<div class="cls-left">--%>
                    <%--<em>类型</em>|--%>
                <%--</div>--%>
                <div class="cls-right" hidden="">
                    <c:choose>
                        <c:when test="${param.type == 'keqian'}">
                            <span class="checkedType hwtype" value="0">课前</span>
                        </c:when>
                        <c:when test="${param.type == 'kehou'}">
                            <span class="checkedType hwtype" value="1">课后</span>
                        </c:when>
                        <c:otherwise>
                            <span class="checkedType hwtype" value="3">全部</span>
                        </c:otherwise>
                    </c:choose>
                    <%--<span class="checkedType hwtype" value="3">全部</span>--%>
                    <%--<span class="hwtype" value="0">课前</span>--%>
                    <%--<span class="hwtype" value="1">课后</span>--%>
                    <%--<span class="hwtype" value="2">其他</span>--%>
                </div>

                <div class="cls-left">
                    <em>内容</em>|
                </div>
                <div class="cls-right">
                    <span class="hwcon checkedType" value="0">全部</span>
                    <span class="hwcon" value="1">有视频</span>
                    <span class="hwcon" value="2">有附件</span>
                    <span class="hwcon" value="3">有练习</span>
                    <span class="hwcon" value="4">有语音</span>
                </div>

                <div class="cls-left">
                    <em>时间</em>|
                </div>
                <div class="cls-right">
                    <span class="hwterm checkedTerm" value="0">本学期作业</span>
                    <span class="hwterm" value="1">全部作业</span>
                </div>
            </div>
        </div>
        <div class="homework-right-info">
            <dl id="homeworkList">

                <%--作业列表--%>

            </dl>
            <dl id="nohomework" style="color:#FF0000;display: none;text-align:center">暂时没有作业</dl>
        </div>
        <%--分页--%>
        <div class="page-paginator">
            <span class="first-page">首页</span>
                        <span class="page-index">
                            <span class="active">1</span>
                            <%--<span>2</span>--%>
                            <%--<span>3</span>--%>
                            <%--<span>4</span>--%>
                            <%--<span>5</span>--%>
                            <i>···</i>
                        </span>
            <span class="last-page">尾页</span>

        </div>
        <%--分页结束--%>
    </div>


    <!--=============================批改作业=============================-->
    <div class="correct-main">
        <div class="correct-top">
            <dl>
                <dt>
                    <em>返回&gt;</em>
                </dt>
            </dl>
        </div>
    </div>
</div>


<div class="homework-bg" style="display: none">
</div>
<div class="homework-popup" style="display: none">
    <dl>
        <dt>
            <span class="popup-to-le">新建作业</span>
            <span class="popup-to-ri" style="margin-right: 40px">x</span>
        </dt>
        <dd>
            <em>标题：</em>
            <input type="text" class="popup-te" id="title">
        </dd>
        <dd>
            <em>班级：</em>

            <div id="classList" style="width: 400px;">
                <%--<input type="checkbox" name="ch"><i>三年级一班</i>--%>
                <%--<input type="checkbox" name="ch"><i>三年级3</i>--%>
                <%--<input type="checkbox" name="ch"><i>三年级</i>--%>
            </div>
        </dd>
        <dd>
            <em>类型：</em>
            <input type="radio" name="k" class="basic-ra" value="0"><span>课前</span>
            <input type="radio" name="k" class="basic-ra" value="1"><span>课后</span>
            <input type="radio" name="k" class="basic-ra" value="2"><span>其他</span>
        </dd>
        <dd>
            <em>内容：</em>
            <textarea id="con"> </textarea>
        </dd>
        <dd>
            <div id="submit_section" style="clear:both;margin-top:8px;margin-left:55px;">
                <div id="recordercontainer1" >
                    <div id="recorder" class="">
                        <div class="area">
                                        <span class="a12" >
                                            <img src="/img/mic.png" style="width: 19px; height: 19px;"/>
                                            语音
                                        </span>

                            <div style="padding-top: 10px;position: absolute;z-index: 50000;">
                                <div class="sanjiao"
                                     style="width: 0;height: 0;border-left: 8px solid transparent;border-right: 8px solid transparent;border-bottom: 10px solid rgb(100,100,100);position:absolute;display: inline-block;top:0px;left:10px;display:none;"></div>
                                <div id="myContent">
                                </div>
                            </div>
                        </div>
                        <form id="uploadForm" name="uploadForm"
                              action="audiosave.jsp?userId=${currentUser.id}&voicetype=0&isinform=1">
                            <input name="authenticity_token" value="xxxxx" type="hidden">
                            <input name="upload_file[parent_id]" value="1" type="hidden">
                            <input name="format" value="json" type="hidden">
                        </form>
                    </div>
                </div>
            </div>

            <button id="fb">
                下一步
            </button>
        </dd>
    </dl>
</div>


<div style="clear: both"></div>
<%@ include file="../common_new/foot.jsp" %>

<script id="classSubjectTmpl" type="text/template">
    {{ for(var i in it) { }}
    <span class="classSubject" classId="{{=it[i].classId}}" subjectId="{{=it[i].subjectId}}" className="{{=it[i].className}}"
          classType="{{=it[i].classType}}">{{=it[i].className}}{{?it[i].subjectName!=""}}({{=it[i].subjectName}}){{?}}
        {{? it[i].reminder>0 }}<i class="redpointt"></i>{{?}}
        <%--<i class="redpoint"></i>--%>
    </span>
    {{ } }}
</script>

<script id="classListTmpl" type="text/template">
    {{ for(var i in it) { }}
    <span>
        <input type="checkbox" name="ch" value="{{=it[i].classId}}"><i>{{=it[i].className}}{{?it[i].subjectName!=""}}({{=it[i].subjectName}}){{?}}</i>
    </span>
    {{ } }}
</script>

<script id="homeworkListTmpl" type="text/template">
    {{ for(var i in it) { }}
    <dd>
        <%--<img src="http://placehold.it/50x50" width="50px" height="50px" alt="">--%>
        <img class="discuss-info-img" src="{{=it[i].userAvatar}}" width="50px" height="50px"/>

        <div id="detail" value="{{=it[i].id}}">
            <div class="detail-no" style="width:410px;margin:0px;padding:0px">
                <i class="hwtitle" value="{{=it[i].id}}" lessonId="{{=it[i].lessonId}}" style="" index="{{=i}}">
                    {{? it[i].type==0}}
                <em class="hw-ye">课前</em>
                {{?? it[i].type==1}}
                <em class="hw-red">课后</em>
                {{?? it[i].type==2}}
                <em class="hw-blue">其他</em>
                {{?}}
                {{=it[i].title}}
                    {{? it[i].reminder>0 }}<ii class="redpointt"></ii>{{?}}
                    <%--<ii class="redpoint"></ii>--%>
                
                {{? it[i].voiceNum != 0}}
                <i class="fa fa-microphone  cur-en YY">
                    <p class="hw-video">语音</p>
                </i>
                {{?}}
                {{? it[i].videoNum != 0}}
                <i class="fa fa-video-camera cur-en video">
                    <p class="hw-video">视频数</p>
                </i>
                <span>{{=it[i].videoNum}}</span><!--视频数-->
                {{?}}
                {{? it[i].fileNum != 0}}
                <i class="fa fa-files-o cur-en files">
                    <p class="hw-files">文档数</p>
                </i>
                <span>{{=it[i].fileNum}}</span><!--文档数-->
                {{?}}
                {{? it[i].exerciseNum != 0}}
                <i class="fa fa-file-o cur-en file">
                    <p class="hw-file">练习数</p>
                </i>
                <span>{{=it[i].exerciseNum}}</span><!--习题数-->
                {{?}}

                </i>
                {{? it[i].pg==1}}
                <span class="process" value="{{=it[i].id}}" lessonId="{{=it[i].lessonId}}" pg="{{=it[i].pg}}">{{=it[i].allStuNo - it[i].submitStuNo}}人未交，共{{=it[i].allStuNo}}人</span>
                {{??}}
                <span class="processc" value="{{=it[i].id}}" lessonId="{{=it[i].lessonId}}" pg="{{=it[i].pg}}">此作业无需提交</span>
                {{?}}
            </div>
            
                        <span>
                            <strong>{{=it[i].userName}}</strong> 于{{=it[i].time}}发表
                        </span>
            {{? it[i].submitCount != 0}}
            <br />
            <em class="homework-ee">最后回复于{{=getAdaptTimeFromNow(it[i].lastSubmitTime)}}</em><em class="homework-ee">已经回复{{=it[i].submitCount}}条</em>
            {{?}}
            <%--</div><div>--%>
            <!--<span class="homnework-info-tianji">0人提交/共11人</span>-->
                        <span class="homework-info-right">
                            {{? it[i].pg==1}}
                            <i class="edit" value="{{=it[i].id}}" lessonId="{{=it[i].lessonId}}" index="{{=i}}">
                                <p class="hw-edit">批改作业</p>
                            </i><!--批改作业-->
                             {{?}}
                                {{? it[i].exerciseId!="noExercise"}}
                            <i class="bar" value="{{=it[i].id}}" lessonId="{{=it[i].lessonId}}"
                               exerciseId="{{=it[i].exerciseId}}">
                                <p class="hw-bar">批改进阶</p>
                            </i><!--批改习题-->
                                {{?}}
                            <i class="trash" value="{{=it[i].id}}" lessonId="{{=it[i].lessonId}}" pg="{{=it[i].pg}}">
                                <p class="hw-trash">查看统计</p>
                            </i><!--查看统计-->
                             <i class="see" id="see" value="{{=it[i].id}}" lessonId="{{=it[i].lessonId}}">
                                 <p class="hw-see">编辑</p>
                             </i><!--编辑-->
                            <i class="del" value="{{=it[i].id}}" lessonId="{{=it[i].lessonId}}">
                                <p class="hw-del">删除</p>
                            </i><!--删除-->
                        </span>
        </div>
    </dd>
    {{ } }}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static_new/js/modules/homework/0.1.0/teacherindex.js', function (teaIndex) {
        teaIndex.init();
    });
</script>
</body>
</html>
