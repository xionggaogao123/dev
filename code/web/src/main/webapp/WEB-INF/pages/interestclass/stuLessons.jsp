<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2015/10/13
  Time: 10:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>


<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
  <title>课时评分-复兰科技 K6KT-快乐课堂</title>
  <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
  <link rel="stylesheet" href="/static/css/k6kt.css"/>
  <link rel="stylesheet" href="/static/css/dialog.css"/>
  <link rel="stylesheet" href="/static/css/evaluation/each-lesson.css"/>
  <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
  <script type="text/javascript" src="/static/js/sharedpart.js"></script>
  <!-- Add fancyBox main JS and CSS files -->
  <script type="text/javascript" src="/static/plugins/fancyBox/jquery.fancybox.js?v=2.1.5"></script>
  <link rel="stylesheet" type="text/css" href="/static/plugins/fancyBox/jquery.fancybox.css?v=2.1.5" media="screen"/>
  <%--<script type="text/javascript" src="/static/js/evaluation/each-lesson.js"></script>--%>
  <script type="text/javascript">
    var currentPageID = 2;
    var classId = '${param.classId}';
    var type = '${param.type}';
  </script>
</head>
<body>

<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container">
  <div id="content_main">

    <%@ include file="../common_new/col-left.jsp" %>


    <div id="right-container">
      <!-- 内容 -->
      <div class="k6kt-body" style="width: 770px;position:relative;">

        <div class="lesson-score-list">
          <table class="table-k6kt">
            <tr>
              <th style="width: 100px;">课时</th>
              <th style="width: 100px;">课堂成果</th>
              <th >老师评语</th>
              <th style="width: 100px;">考勤</th>
              <th style="width: 100px;">课堂表现</th>
            </tr>
            <c:forEach items="${totalList}" var="lesson" varStatus="status">
              <tr class="score-line">
                <td>
                  <%--<input type="hidden" name="userid" value="${lessonScore.userid}">--%>
                  <%--<img class="stu-list-img avatar-min" src="${lessonScore.studentAvatar}"/>--%>
                  <%--课时${lesson.lessonindex}--%>
                    ${lesson.lessonName}
                </td>
                <td class="image-statement-td">
                  <c:if test="${lesson.pictureUrl != null && lesson.pictureUrl != ''}">
                  <a class="fancybox" href="${lesson.pictureUrl}">
                    <img src="${lesson.pictureUrl}"/>
                  </a>
                  </c:if>
                </td>
                <td style="display:inline-block;width: 230px;max-height:70px;overflow: auto;">${lesson.teacherComment}</td>
                <td>
                  <%--<input type="radio" name="1"><span>到</span>--%>
                  <%--<input type="radio" name="1"><span class="score-KK">缺勤</span>--%>
                  <c:if test="${lesson.attendance==1}">
                    <span>到</span>
                  </c:if>
                    <c:if test="${lesson.attendance==0}">
                      <span class="score-KK">缺勤</span>
                    </c:if>
                </td>
                <!--课堂表现-->
                <td class="stars-container">
                  <i class="fa fa-star-o fa-lg orange-color"></i>
                  <i class="fa fa-star-o fa-lg orange-color"></i>
                  <i class="fa fa-star-o fa-lg orange-color"></i>
                  <i class="fa fa-star-o fa-lg orange-color"></i>
                  <i class="fa fa-star-o fa-lg orange-color"></i>
                  <input type="text" name="stuscore" class="score"  hidden="true"
                         value="${lesson.stuscore != null? lesson.stuscore : 5}"
                         readonly/><span></span>
                  <c:if test="${lesson.id != null}">
                    <input type="hidden" name="id" value="${lesson.id}"/>
                    <input type="hidden" name="lessonindex" value="${lesson.lessonindex}"/>
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </table>
        </div>

        <div>
          <a href="/myclass/interstat/${param.classId}/${param.termType}" class="score-btn"><span
                  class="inline-button btn-orange fixed-size">返回</span></a>
        </div>


      </div>


    </div>

  </div>
  <div>
  </div>
  <!-- 页尾 -->
  <%@ include file="../common_new/foot.jsp" %>
</div>
<script type="text/javascript">
  $('.stars-container').each(function(index, element) {
    var $stars = $(element).children('i.fa');
    var score = $(element).children('input[name="stuscore"]').val();
    formatStars($stars, score);
  });

  function formatStars($stars, score) {
    var i = 0;
    while(i < score) {
      $($stars[i]).removeClass('fa-star-o');
      $($stars[i]).addClass('fa-star');
      i++;
    }
    while(i < $stars.length) {
      $($stars[i]).removeClass('fa-star');
      $($stars[i]).addClass('fa-star-o');
      i++;
    }
  }

  $('input[name="stuscore"]').each(function(){
    //alert($(this).val());
    var num = $(this).val();
    var desc;
    if(num == 5){
      desc = "优秀";
    } else if(num == 4){
      desc = "良好";
    } else if(num == 3){
      desc = "合格";
    } else {
      desc = "需努力";
    }
    $(this).siblings('span').text(desc);
  })
  $('.fancybox').fancybox();
</script>
</body>
</html>
