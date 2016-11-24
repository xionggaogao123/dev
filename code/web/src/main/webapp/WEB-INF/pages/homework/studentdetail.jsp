<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-8-17
  Time: 上午10:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>复兰科技-详情页面</title>
    <link rel="stylesheet" href="/static_new/css/homework.css">
    <link rel="stylesheet" type="text/css" href="/static/css/lesson/lesson-view.css"/>
    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static_new/css/schoolsecurity.css?v=2015041602" rel="stylesheet" />
    <link href="/static_new/js/modules/core/0.1.0/fancyBox/jquery.fancybox.css?v=2015041602" rel="stylesheet" type="text/css" media="screen"/>
    <link href="/static_new/css/friendcircle/homepage.css?v=1" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
    <%--<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>--%>
    <script type="text/javascript" src="/static/js/lessons/lesson-view.js"></script>
    <%--计算发表日期--%>
    <script src="/static/js/shareforflipped.js" type="text/javascript"></script>
    <%--录音--%>
    <%--<script type="text/javascript" src="/static/js/swfobject.js"></script>--%>
    <script type="text/javascript" src="/static/js/recorder.js"></script>
    <script type="text/javascript" src="/static/js/main.js"></script>
    <script src="/static/js/shareforflipped.js" type="text/javascript"></script>

    <script type="text/javascript" src="/static/plugins/bowser.min.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
    <script type="text/javascript" src="/static/js/swfobject.js"></script>
    <script type="text/javascript" src="/static/js/lessons/ypCourseSheet.js"></script>
    <script type="text/javascript" src="/static/plugins/cameraform/htdocs/webcam.js"></script>
    <script type="text/javascript">
        $(function() {
            $(".o1").click(function () {
                var url = $(this).attr("cid");
                var urlarg = url.split(".")[url.split(".").length-1];
                if (urlarg=="doc"||urlarg=="docx"||urlarg=="xls"||urlarg=="xlsx"||urlarg=="ppt"||urlarg=="pptx") {
                    window.open("http://ow365.cn/?i=9666&furl="+url);
                } else {
                    window.open(url);
                }

            });
            function getUrl(strFileUrl) {
                //在线预览服务地址
                var strOfficeApps = "http://112.124.106.222/op/view.aspx";
                var strUrl = strOfficeApps + "/op/view.aspx?src=" + encodeURIComponent(strFileUrl);
                return strUrl;
            }
        });
    </script>

    <style type="text/css">
        /*拍照*/


        .take-photo-container {
            width: 550px;
            position: fixed;
            top: 10%;
            left: 34%;
            padding: 8px 15px;
            background: #fff;
            display: none;
            z-index: 100;
        }

        .photo-title {
            font-size: 16px;
            overflow: hidden;
            float: left;
            color: #FF8400;
            font-weight: bold;
            margin-bottom: 8px;
        }

        .photo-close,
        .not-found-close {
            float: right;
            font-size: 19px;
            overflow: hidden;
            margin-bottom: 8px;
            cursor: pointer;
        }

        .take-photo-form {
            width: 100%;
            margin: 10px 0px;
        }

        .take-photo-btn {
            color: #fff;
            font-size: 16px;
            font-weight: bold;
            padding: 6px 25px;
            background-color: #71C469;
            border-radius: 2px;
            float: right;
            cursor: pointer;
            display: none;
        }

        .take-photo-again {
            color: #fff;
            font-size: 16px;
            font-weight: bold;
            padding: 6px 0px;
            background-color: #F53240;
            border-radius: 2px;
            float: right;
            cursor: pointer;
            text-align: center;
            width: 90px;
            display: none;
        }

        .take-photo-upload {
            color: #fff;
            font-size: 16px;
            font-weight: bold;
            padding: 6px 0px;
            background-color: #FF9000;
            border-radius: 2px;
            float: left;
            cursor: pointer;
            text-align: center;
            width: 90px;
            display: none;
        }

        .upload-photo-img {
            position: relative;
            width: 170px;
            height:123px;
            overflow: hidden;
            margin-right: 2px !important;
            margin-left: 2px;
            margin-bottom: 4px;
        }

        .upload-photo-container {
            display: inline-block;
        }

        .delete-photo {
            position: absolute;
            top: 2px;
            right: 2px;
            cursor: pointer;
        }

        .not-found-camera {
            width: 400px;
            background-color: #fff;
            padding: 5px;
            display: none;
            position: fixed;
            left: 38%;
            top: 150px;
            z-index: 100;
        }
    </style>
</head>
<body>
<!--===========================startt头部==============================-->
<%@ include file="../common_new/head.jsp" %>
<!--===========================end头部==============================-->
<div class="datail-main">
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
    <div class="detail-right">
        <div class="detail-right-top">
            <div class="detail-banner">
                <span>作业</span>
            </div>
            <dl>
                <div class="student-FH" id="FH">
                    返回
                </div>
                <dt>
                    <img src="" width="50" height="50" id="photo">
                <div>
                    <em>[作业]</em><i id="hwtitle"></i>


                <div class="reverse-LA" id="items">
                    <script id="itemsTmpl" type="text/template">
                        {{? it.pg==1}}
                        <span class="reverse-I"  value="{{=it.id}}" lessonId="{{=it.lessonId}}">
                    <strong>批改作业</strong>
                </span><!--批改作业-->
                        {{?}}
                        {{? it.exerciseId!="noExercise"}}
                        <span class="reverse-II"  value="{{=it.id}}" lessonId="{{=it.lessonId}}" exerciseId="{{=it.exerciseId}}">
                    <strong>批改进阶</strong>
                </span><!--批改进阶-->
                        {{?}}
                        <span class="reverse-III"  value="{{=it.id}}" lessonId="{{=it.lessonId}}" pg="{{=it.pg}}">
                    <strong>查看统计</strong>
                </span><!--查看统计-->
                        <span class="reverse-IV"  value="{{=it.id}}" lessonId="{{=it.lessonId}}">
                    <strong>编辑</strong>
                </span><!--编辑-->
                        <span class="reverse-V"  value="{{=it.id}}" lessonId="{{=it.lessonId}}">
                    <strong>删除</strong>
                </span><!--删除-->
                    </script>
                </div>

                <br>
                    <strong id="teaName"></strong><label id="date"></label>
                </div>
                <div class="detail-banner-bu">
                    <button id="dohomework" style="display:none">去做作业</button>
                    <button id="doexercise" style="display:none">去做微课进阶练习</button>
                    <button id="getAnswer" style="display:none">查看微课进阶练习结果</button>
                </div>
                </dt>
                <dd>
                    <p id="con"></p>
                <div id="teaVoice">
                    <%--<p><a onclick="playVoice();" url="data.voiceFile[0].value"><img src="/img/yuyin.png">播放</a></p>--%>
                </div>
                </dd>
            </dl>
        </div>

        <!--================================视频播放=================================-->
        <%--<div class="detail-video">--%>
        <div class="course-view-container">
        <div class="video-course-name"></div>
        <div id="videoplayer-div">
            <div id='player-div' class="video-player-container"></div>
            <div id='sewise-div' class="video-player-container">
                <script type="text/javascript" src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>
            </div>
            <img id="converting-img" src="/img/converting_big.png" class="video-player-container"/>
            <script type="text/javascript">
                SewisePlayer.setup({
                    server: "vod",
                    type: "m3u8",
                    skin: "vodFlowPlayer",
                    logo: "none",
                    lang: "zh_CN",
                    topbardisplay: 'enable',
                    videourl: ''
                });
            </script>

            <div class="video-thumb-list" id="video">
                <c:forEach items="${videoList}" var="video" varStatus="loop">
                <div>
                <div class="indicator"><i class="fa fa-play"></i></div>
                <div class="video-thumb" name="${video.type}" url="${video.value1}"
                video-length="${video.length}" video-id="${video.id}" state="${video.uploadState}"
                onclick="playVideo(${loop.index})">
                <img src="${empty video.value? '/img/K6KT/video-cover.png': video.value}"
                onerror="coverError(this);"/>

                <div class="video-name">${video.type}</div>
                    <div class="video-info">${video.value2}</div>
                </div>
                </div>
                </c:forEach>
            </div>
        </div>
            </div>
        <%--</div>--%>


        <div class="detail-info">
            <div class="detail-info-left">
                <a class="detail-cur wt" id="wt">我的作业</a>
                <a class="tl" id="tl">讨论作业</a>
                <hr>
                <!--=========================我的作业=========================-->

                <%--<div class="detail-bottom-I">--%>
                <div id="hwdetail">
                    <%--<div class="detail-bottom-I">--%>
                    <%--<img src="http://placehold.it/50x50" width="50px" height="50px">--%>
                    <%--<em>vivn</em><i>发表于一年前</i>--%>
                    <%--<p>kashfkashdkfhlakhdflkhak</p>--%>
                    <%--<div style="clear: both;"></div>--%>
                    <%--</div>--%>
                </div>

                <!--=========================讨论作业=========================-->
                <div id="commentList" class="detail-bottom">
                    <%--<div class="detail-bottom">--%>
                    <%--<img src="http://placehold.it/50x50" width="50px" height="50px">--%>
                    <%--<em>vivn</em><i>发表于一年前</i>--%>
                    <%--<p>kashfkashdkfhlakhdflkhak</p>--%>
                    <%--<div style="clear: both;"></div>--%>
                    <%--</div>--%>
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

                <%--<div class="detail-bottom-lt">--%>
                <%--<textarea placeholder="同学们，你需要首先在这里输入文字偶" id="textarea"></textarea>--%>
                <%--<span>语音</span>--%>
                <%--<input type="file" style="display:none" id="file" name="file"> <i id="uploadfile">添加附件</i>--%>

                <%--<strong>拍照</strong>--%>
                <%--<button id="submit">提交</button>--%>
                <%--<div id="filename"></div>--%>
                <%--</div>--%>


                <%--</div>--%>
                <%--======================================================================================================--%>
                <div id="reply_section1">
                <div id="reply_section" class="detail-bottom-I" style="display:none">
                    <div id="upload-photo-container"></div>
                    <div class="detail-bottom-lt">
        <textarea id="textarea" placeholder="同学们，在这里提交作业！"></textarea>

                        <div id="recordercontainer1" style="clear:both;margin:8px;">
                            <div id="recorder" class="">
                                <div class="area">
                                    <span class="a12" onclick="showflash('recordercontainer1')">
                                        <%--<img src="/img/mic.png" style="width: 19px; height: 19px;"/>--%>
                                        语音
                                    </span>

                                    <div style="padding-top: 10px;position: absolute;z-index: 50000;">
                                        <div class="sanjiao"
                                             style="width: 0;height: 0;border-left: 8px solid transparent;border-right: 8px solid transparent;border-bottom: 10px solid rgb(100,100,100);position:absolute;display: inline-block;top:0px;left:10px;display:none;"></div>
                                        <div id="myContent">


                                        </div>
                                    </div>
                                </div>
                                <%--<form id="uploadForm" name="uploadForm" action="audiosave.jsp?userId=${currentUser.id}&voicetype=0">--%>
                                <%--<input name="authenticity_token" value="xxxxx" type="hidden">--%>
                                <%--<input name="upload_file[parent_id]" value="1" type="hidden">--%>
                                <%--<input name="format" value="json" type="hidden">--%>
                                <%--</form>--%>
                            </div>
                        </div>


                        <div id="attacher">
                            <label for="file_attach" style="cursor: pointer;">
                                <img src="/img/fileattach.png"/>
                                添加附件
                            </label>
                            <img src="/img/loading4.gif" id="fileuploadLoading" style="display:none;"/>

                            <div style="width: 0; height: 0; overflow: visible">
                                <input id="file_attach" type="file" name="file" value="添加附件" size="1"
                                       style="width: 0; height: 0; opacity: 0">
                            </div>
                        </div>

                        <div class="take-photo"><strong>拍照</strong></div>
                        <%--<div class="teacher-exam-submit" style="margin-left: 170px;">--%>
                        <%--<p><a class="exam-submit" onclick="submitReply();"><img id="submitloading" src="/img/loading4.gif" style="display:none;vertical-align: middle;margin-right:5px;"/>提交</a></p>--%>
                        <%--</div>--%>
                        <button id="submit">提交</button>
                    </div>
                </div>
                </div>


                <div id="detail-bottom">
                <div class="detail-bottom">
                    <img src="${sessionValue.minAvatar}" width="50px" height="50px">
                    <textarea placeholder="请输入你想说的话" id="comment"></textarea>
                    <span>
                        <button id="tjpl">提交评论</button>
                    </span>
                </div>
                </div>
            </div>
            <div class="detail-info-right">
                <dl>
                    <c:if test="${ !empty coursewareList}">
                    <dt>
                        <em>课件列表</em>
                    </dt>
                    </c:if>
                    <dl id="docList">
                        <%--<dd>--%>
                        <%--<span> 11</span>--%>
                        <%--<button>下载</button>--%>
                        <%--</dd>--%>
                        <%--<dd>--%>
                        <%--<span> 11</span>--%>
                        <%--<a href="${cw.value}" target="_blank"><button>下载</button></a>--%>
                        <%--</dd>--%>

                        <%--<c:forEach items="${coursewareList}" var="cw">--%>
                            <%--<li>--%>
                                <%--<a href="${cw.value}" target="_blank">--%>
                                    <%--<img src="/img/kejian.jpg">--%>
                                <%--</a>--%>
                                <%--<div class="kjinfo">--%>
                                    <%--<span>${cw.type}</span>--%>
                                <%--</div>--%>
                                <%--<div class="kjdown">--%>
                                    <%--<a href="${cw.value}" target="_blank">下载</a>--%>
                                <%--</div>--%>
                            <%--</li>--%>
                        <%--</c:forEach>--%>

                        <c:forEach items="${coursewareList}" var="cw">
                            <%--<c:if test="${cw.value5==1}">--%>
                            <dd>
                                <img src="${cw.value3}">
                                <span>${cw.type}</span>
                                <c:choose>
                                    <c:when test="${cw.type1== 1}">
                                        <a class="o1" cid="${cw.value1}"><button>预览</button></a>
                                    </c:when>
                                </c:choose>

                                <a href="${cw.value1}" target="_blank"><button>下载</button></a>
                            </dd>
                            <%--</c:if>--%>
                        </c:forEach>
                    </dl>
                </dl>
            </div>
        </div>
    </div>
</div>

<!-- 拍照 -->

<div class="alert-bg" style="z-index: 99">  </div>
<div class="take-photo-container"><div class="photo-title">拍照答题</div>
<div class="photo-close">&times;</div>
<div class="take-photo-form">
<script language="JavaScript">document.write(webcam.get_html(550, 400));</script>
</div>
<div class="take-photo-btn">拍照</div>
<div class="take-photo-again">重新拍摄</div>
<div class="take-photo-upload">上传照片</div>
</div>
<div class="not-found-camera">
<div class="not-found-close">&times;</div>
<div class="not-found-content">抱歉，没有检测到摄像头</div>
</div>

<div id="bg" class="bg" style="display: none;"></div>


<div style="clear: both" id="end"></div>
<%@ include file="../common_new/foot.jsp" %>


<script id="teaVoiceTmpl" type="text/template">
    {{ for(var i in it) { }}
    <p><a onclick="playVoice('{{=it[i].value}}');" url="{{=it[i].value}}"><img src="/img/yuyin.png">播放</a></p>
    {{ } }}
</script>


<script id="hwdetailTmpl" type="text/template">
    {{ for(var i in it) { }}
    <div class="detail-bottom-I">
        <img src="{{=it[i].avatar}}" width="50px" height="50px">
        <em>{{=it[i].userName}}</em><i>发表于{{=getAdaptTimeFromNow(it[i].time)}}</i>
        <img class="delete deleteHW"  src="/img/dustbin.png" title="删除" userId="{{=it[i].userId}}" time="{{=it[i].time}}">
        {{?it[i].isview == 1}}
            <p>{{=it[i].content}}</p>
            {{? it[i].voiceFile&&it[i].voiceFile.length>0}}
            <p><a onclick="playVoice('{{=it[i].voiceFile[0].value}}');" url="{{=it[i].voiceFile[0].value}}"><img
                    src="/img/yuyin.png">播放</a></p>
            {{?}}

            {{? it[i].docFile&& it[i].docFile.length>0}}
                {{for (var j in it[i].docFile) { }}
                    {{var at = it[i].docFile[j];}}
                    {{var str = at.value;}}
                    {{var ext = str.substring(str.length-3,str.length);}}
                    {{var rule = /(jpg|gif|bmp|png)/;}}
                    {{? rule.test(ext) }}
                    <%--{{? checkImage(at.value)}}--%>
                    <a class="fancybox" href="{{=encodeURI(at.value)}}" data-fancybox-group="homework"><img class="homework-img" title="点击查看大图" src="{{=encodeURI(at.value)}}"></a>
                    {{??}}
                    <p><a href="/commondownload/file.do?url={{=encodeURI(at.value)}}&name={{=at.name}}"><img
                            src="/img/fileattach.png"/>附件{{=parseFloat(j)+1}}:{{=at.name}}</a></p>
                    {{?}}
                {{ } }}
            {{?}}
        {{??}}
        <p>该作业已提交</p>
        {{?}}
        <div style="clear: both;"></div>
    </div>
    {{?it[i].isview == 1}}
        <%--老师回复的内容--%>
        {{var hf = it[i].hf;}}
        {{ for(var i in hf) { }}
        <div class="detail-bottom-II">
            <img src="{{=hf[i].avatar}}" width="50px" height="50px">
            <em>{{=hf[i].userName}}</em><i>回复于{{=getAdaptTimeFromNow(hf[i].time)}}</i>

            <p>{{=hf[i].content}}</p>
            {{? hf[i].voiceFile&&hf[i].voiceFile.length>0}}
            <p><a onclick="playVoice('{{=hf[i].voiceFile[0].value}}');" url="{{=hf[i].voiceFile[0].value}}"><img
                    src="/img/yuyin.png">播放</a></p>
            {{?}}

            {{? hf[i].docFile&& hf[i].docFile.length>0}}
                {{for (var j in hf[i].docFile) { }}
                    {{var hfat = hf[i].docFile[j];}}
                    {{var hfstr = hfat.value;}}
                    {{var hfext = hfstr.substring(hfstr.length-3,hfstr.length);}}
                    {{var hfrule = /(jpg|gif|bmp|png)/;}}
                    {{? hfrule.test(hfext) }}
                    <%--{{? checkImage(at.value)}}--%>
                    <a class="fancybox" href="{{=encodeURI(hfat.value)}}" data-fancybox-group="homework"><img class="homework-img" title="点击查看大图" src="{{=encodeURI(hfat.value)}}"></a>
                    {{??}}
                    <p><a href="/commondownload/file.do?url={{=encodeURI(hfat.value)}}&name={{=hfat.name}}"><img
                            src="/img/fileattach.png"/>附件{{=parseFloat(j)+1}}:{{=hfat.name}}</a></p>
                    {{?}}
                {{ } }}
            {{?}}
            <div style="clear: both;"></div>
        </div>
        {{ } }}
        <%--老师回复结束--%>
    {{?}}
    {{ } }}
</script>

<script id="commentListTmpl" type="text/template">
    {{ for(var i in it) { }}
    <div class="detail-bottom del">
        <img src="{{=it[i].avatar}}" width="50px" height="50px">
        <em>{{=it[i].name}}</em><i>发表于{{=getAdaptTimeFromNow(it[i].time)}}</i>
        <%--<i class="delete" style="display:none" >删除</i>--%>
        <img class="delete deleteCM" style="display:none" src="/img/dustbin.png" title="删除" userId="{{=it[i].ui}}" time="{{=it[i].time}}">
        <p>{{=it[i].comment}}</p>

        <div style="clear: both;"></div>
    </div>
    {{ } }}
</script>

<%--<script id="docListTmpl" type="text/template">--%>
    <%--{{ for(var i in it) { }}--%>
    <%--<dd>--%>
        <%--<span>{{=it[i].type}}</span>--%>
        <%--<a href="{{=it[i].value}}" target="_blank">--%>
            <%--<button>打开</button>--%>
        <%--</a>--%>
    <%--</dd>--%>
    <%--{{ } }}--%>
<%--</script>--%>

<%--<script id="videoTmpl" type="text/template">--%>
    <%--{{ for(var i in it) { }}--%>
    <%--<div>--%>
        <%--<div class="indicator"><i class="fa fa-play"></i></div>--%>
        <%--<div class="video-thumb" name="{{=it[i].type}}" url="{{=it[i].value1}}"--%>
             <%--video-length="{{=it[i].length}}" video-id="{{=it[i].id}}" state="{{=it[i].uploadState}}"--%>
             <%--onclick="playVideo(0)">--%>
            <%--{{? it[i].value}}--%>
            <%--<img src="{{=it[i].value}}" onerror="coverError(this);"/>--%>
            <%--{{??}}--%>
            <%--<img src='/img/K6KT/video-cover.png' onerror="coverError(this);"/>--%>
            <%--{{?}}--%>
            <%--<div class="video-name">{{=it[i].type}}</div>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%--{{ } }}--%>
<%--</script>--%>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static_new/js/modules/homework/0.1.0/studentdetail.js', function (stuDetail) {
        stuDetail.init();
    });
</script>

<script type="text/javascript">
    function initwebcam(){
        webcam.set_quality(90);
        webcam.set_shutter_sound(true);
        webcam.set_hook('onComplete', 'uploadPhoto');


        $('.not-found-close').on('click', function() {
            $('.not-found-camera').hide();
            $('.alert-bg').hide();
        });

        $('.take-photo').on('click', function() {
            $('.alert-bg').show();
            $('.take-photo-container').show();
            $('.take-photo-btn').show();
        });

        $('.take-photo-btn').on('click', function() {
            webcam.freeze();
            $('.take-photo-btn').hide();
            $('.take-photo-again').show();
            $('.take-photo-upload').show();
        });
        $('.take-photo-again').on('click', function() {
            webcam.reset();
            resetPhoto();
        });

        $('.take-photo-upload').on('click', function() {
            webcam.dump();
            $('.photo-close').click();
        });

        $('.photo-close').on('click', function() {
            $('.alert-bg').hide();
            $('.take-photo-container').hide();
            resetPhoto();
        });

        $('#upload-photo-container').on('click', '.delete-photo', function() {
            $(this).parent().remove();
        });
    }

    function resetPhoto() {
        $('.take-photo-btn').show();
        $('.take-photo-again').hide();
        $('.take-photo-upload').hide();
    }

    function uploadPhoto(base64) {
        var target = $('#upload-photo-container');
        src = 'data:image/jpeg;base64,' + base64;
        $.ajax({
            url: '/commonupload/base64image.do',
            type: 'post',
            dataType: 'json',
            data: {
                base64ImgData: base64
            },
            success: function(data) {
                console.log(data);
                target.append('<div class="upload-photo-img"><img class="img-photo-take" src="' + src + '" realname="' + data.path + '" textname="'+data.name+'"><img class="delete-photo" src="/img/error-grey.png"></div>');
            },
            error: function() {
                console.log('convertImages error');
            }
        });
        webcam.reset();
    }
</script>
</body>
</html>

