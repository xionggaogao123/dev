<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2015/8/19
  Time: 19:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>微课大赛</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link href="/static_new/css/microlesson/microlesson.css?v=2015041602" rel="stylesheet" />
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <link rel="stylesheet" type="text/css" href="/static/css/slideshow.css"/>
  <%--<link rel="stylesheet" type="text/css" href="/static/css/bootstrap-combined.school.css"/>--%>
  <link rel="stylesheet" type="text/css" href="/static/css/lesson/lesson-view.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/dialog.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/lesson/lesson-vote.css"/>
  <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
  <script type="text/javascript" src="/static/plugins/bowser.min.js"></script>
  <script type="text/javascript" src="/static/js/sharedpart.js"></script>
  <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
  <script type="text/javascript" src="/static/js/swfobject.js"></script>
  <script type="text/javascript" src="/static/js/recorder.js"></script>
  <script type="text/javascript" src="/static/js/main.js"></script>
  <script type="text/javascript" src="/static/js/experienceScore.js"></script>
  <script type="text/javascript" src="/static/js/lessons/lesson-view.js"></script>
  <script type="text/javascript" src="/static/js/lessons/ypCourseSheet.js"></script>
  <script type="text/javascript">
    var currentCommPage = 1;
    $(function() {
      $('.different').find('a').each(function(index, ele){
        var href = $(ele).attr('href');
        var fileKey = href.substring(href.lastIndexOf('/') + 1);
        var fileName = $(this).attr('fnm') + href.substring(href.lastIndexOf('.'));
        href = '/commonupload/doc/down.do?type=2&fileKey='+fileKey+'&fileName=' + fileName
        $(ele).attr('href', href)
      });
      $(".o1").click(function () {
        var url = $(this).attr("cid");
        var urlarg = url.split(".")[url.split(".").length-1];
        if (urlarg=="doc"||urlarg=="docx"||urlarg=="xls"||urlarg=="xlsx"||urlarg=="ppt"||urlarg=="pptx") {
          window.open("http://ow365.cn/?i=9666&furl="+url);
        } else {
          window.open(url);
        }

      });
//      for(var i=0 ; i<=100;i++){
//        var newOption  = document.createElement("option");
//        newOption.text=i;
//        newOption.value=i;
//        document.getElementById("socrelist").add(newOption);
//      }
    });

    function getUrl(strFileUrl) {
      //在线预览服务地址
      var strOfficeApps = "http://112.124.106.222/op/view.aspx";
      var strUrl = strOfficeApps + "/op/view.aspx?src=" + encodeURIComponent(strFileUrl);
      return strUrl;
    }
  </script>
</head>
<body>
<input hidden="hidden" value="${lessonid}" id="lessonid">
<input hidden="hidden" value="${matchid}" id="matchid">
<!--#head-->
<%@ include file="../common_new/head.jsp"%>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
  <!--.col-left-->
  <%@ include file="../common_new/col-left.jsp" %>
  <!--/.col-left-->

  <c:choose>
    <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
      <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
    </c:when>
    <c:otherwise>
      <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
    </c:otherwise>
  </c:choose>
  <!--.col-right-->
  <div class="col-right">

    <!--.tab-col右侧-->
    <div class="tab-col kecheng">
      <h3>${matchname}</h3>
      <p class="tab-LT">正在观看课程：${lessonname}</p>
  <c:if test="${isdelete==1}">
      <button id="dellesson">删除</button>
  </c:if>
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

        <div class="video-thumb-list">
          <c:forEach items="${videoList}" var="video" varStatus="loop">
            <div>
              <div class="indicator"><i class="fa fa-play"></i></div>
              <div class="video-thumb" name="${video.type}" url="${video.value1}"
                   video-length="${video.length}" video-id="${video.id}" state="${video.uploadState}"
                   onclick="playVideo(${loop.index})">
                <img src="${empty video.value? '/img/K6KT/video-cover.png': video.value}"
                     onerror="coverError(this);"/>

                <div class="video-name">${video.type}</div>
              </div>
            </div>
          </c:forEach>
        </div>
      </div>
      <div class="btm clearfix">
        <div class="lbdp" style="display: none;">
          <dl>
            <dd>
              <span>您的点评</span>
            </dd>
            <dl class="peoscore">
            </dl>
            <script id="fenshu" type="text/template">
              {{ for (var i = 0, l = it.scorelist.length; i < l; i++) { }}
              {{ var obj=it.scorelist[i];}}
              <dd>
                <span>{{=obj.name}}</span><em style="margin-left: 30px;">{{=obj.score}}分</em>
              </dd>
              {{ } }}
            </script>
            <dd>
              <p id="sccontent"></p>
            </dd>
          </dl>
        </div>
  <c:if test="${type==2}">
    <c:if test="${rate==2}">
        <div class="lbdf">
          <dl>
            <dd>
              <em>打分</em>
            </dd>
            <c:if test="${stype==0}">
            <dd>
              <select id="socrelist">
                <c:forEach var="num2" items="${scores}">
                  <option id="${num2}">${num2}</option>
                </c:forEach>
              </select>
            </dd>
            </c:if>
      <c:if test="${stype==1}">
            <c:forEach var="scoretype" items="${scoreTypeList}">
              <dd>
                <span>${scoretype.name}</span>
                <select id="socrelist2">
                  <c:forEach var="num" items="${scoretype.scorelist}">
                    <option id="${num}">${num}</option>
                  </c:forEach>
                </select>
              </dd>
            </c:forEach>
      </c:if>
            <dd>
              <em>点评</em>
            </dd>
            <dd>
              <textarea id="comment"></textarea>
            </dd>
            <dd>
              <button id="submitbut">提交点评</button>
            </dd>
          </dl>
        </div>
    </c:if>
    <c:if test="${rate==1}">
        <div class="lbdp">
          <dl>
            <dd>
              <span>您的点评</span>
            </dd>
            <c:forEach var="scoreinfo" items="${scorelist2}">
              <dd>
                <span>${scoreinfo.name}</span><em style="margin-left: 30px;">${scoreinfo.score}分</em>
              </dd>
            </c:forEach>
              <p>${comment}</p>
            </dd>
          </dl>
        </div>
    </c:if>
</c:if>
<c:if test="${type==3}">
        <div class="lbsw">
           已打分
        </div>
</c:if>
        <c:if test="${type==4}">
          <div class="lbsw">
            未打分
          </div>
        </c:if>
      <c:if test="${type==1}">
        <div class="lbtm">
          <p>打分结果</p>
          <div class="yello">
            <span>总分：${allscore}</span>
            <span>平均分：${avg}</span>
            <span>排名：${sort}</span>
          </div>
          <p>打分详情</p>
          <ul class="fenshu">
            <%--<script id="fenshu" type="text/template">--%>
              <%--{{~it.data:value:index}}--%>
              <c:forEach var="score" items="${scorelist}">
              <li>
                <span>${score.username}</span>
                <c:forEach var="scores" items="${score.scoreDTOList}">
                  <span>${scores.name}:${scores.score}分</span>
                </c:forEach>

                <p>${score.comment}</p>
              </li>
              </c:forEach>
              <%--{{~}}--%>
            <%--</script>--%>
          </ul>
        </div>
      </c:if>
        <div class="rbtm">
          <p>课件列表</p>
          <ul class="different t">
            <c:forEach items="${coursewareList}" var="cw">
              <%--<li>--%>
                <%--<a href="${cw.value}" target="_blank">--%>
                  <%--<img src="/img/kejian.jpg" class="imgup">--%>
                <%--</a>--%>
                <%--<div class="kjinfo">--%>
                  <%--<span>${cw.type}</span>--%>
                <%--</div>--%>
                <%--<div class="kjdown">--%>
                  <%--<a href="${cw.value}" target="_blank">下载</a>--%>
                <%--</div>--%>
              <%--</li>--%>

                <li class="o1"  style="background: url(${cw.value1}) no-repeat;" cid="${cw.value}">${cw.type}

                </li>
                <a href="${cw.value}" fnm="${cw.type}" target="_blank">
                  <span></span>
                </a>

            </c:forEach>
          </ul>
          <%--<p>查看习题</p>--%>
        </div>
      </div>
    </div>
    <!--/.tab-col右侧-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->




<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('lessondetail');
</script>
</body>
</html>
